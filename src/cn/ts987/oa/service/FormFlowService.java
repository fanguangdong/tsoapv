package cn.ts987.oa.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.ts987.oa.dao.FormFlowDao;
import cn.ts987.oa.domain.ApproveInfo;
import cn.ts987.oa.domain.Form;
import cn.ts987.oa.domain.TaskView;
import cn.ts987.oa.domain.User;

@Service("formFlowService")

public class FormFlowService extends BaseService{

	public Form getById(Long id) {
		Form form = formFlowDao.findById(id);
		return form;
	}

	public void save(Form form) {
		formFlowDao.save(form);
	}

	public Collection<Form> findByUser(User user) {
		
		return ((FormFlowDao)formFlowDao).findByUser(user);
	}
	
	/**
	 * 保存表单，开始流转
	 */
	//@Transactional
	public void submit(Form form) { 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 1，设置属性并保存表单
		form.setTitle(form.getFormTemplate().getName() // 标题的格式为：{模板名称}_{申请人姓名}_{申请时间}
				+ "_" + form.getApplicant().getName() //
				+ "_" + sdf.format(form.getApplyTime()));
		form.setStatus(Form.STATUS_RUNNING); // 状态为正在审批中
		formFlowDao.save(form);
		
		//2、  开始流转  
		//a 启动流程实例，并设置流程变量
		String processDefinitionKey = form.getFormTemplate().getPdKey();
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("form", form);
		
		ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey, variables);
		
		//b   完成第一个任务“提交申请”
		String taskId = processEngine.getTaskService()//
			.createTaskQuery()//询出本流程实例中当前仅有一个任务（提交申请的任务）
			.processInstanceId(pi.getId())//
			.singleResult()//
			.getId();
		
		System.out.println("taskId: " + taskId);
		processEngine.getTaskService().complete(taskId);// 完成任务
		
	}
	
	/**
	 * 获取用户的任务列表
	 * @param currentUser
	 * @return
	 */
	public List<TaskView> getMyTaskList(User currentUser) {
		// 1，获取任务列表
		List<Task> taskList = processEngine.getTaskService().createTaskQuery().taskAssignee(currentUser.getLoginName()).list();

		// 2，获取与当前审批任务对应的待审批的表单，并放到要返回的结果集合中
		List<TaskView> taskViewList = new ArrayList<TaskView>();
		for (Task task : taskList) {
			Form form = (Form) processEngine.getTaskService().getVariable(task.getId(), "form");
			taskViewList.add(new TaskView(task, form)); 
			
		}
		return taskViewList;
	}

	/**
	 * 审批
	 * @param taskId
	 * @throws Exception 
	 */
	public void approve(String taskId, ApproveInfo approveInfo) throws Exception {
		//保存审批信息
		approveInfoDao.save(approveInfo);
		
		Task task = processEngine.getTaskService()//
			.createTaskQuery()                           //
			.taskId(taskId) //
			.singleResult(); 
        
		// 获取当前正执行的流程实例对象（如果查到的为null，表示已经执行完了）
		ProcessInstance pi = processEngine.getRuntimeService()//
			.createProcessInstanceQuery()//
			.processInstanceId(task.getExecutionId())//
			.singleResult();  //;
		
		// 维护表单状态
		Form form = approveInfo.getForm();
		if(form == null) {  //TODO 异常情况 
			throw new Exception("获取Form失败");
			//System.err.println("form 为空  FormFlowService.java L122");
			//deleteProcessInstance(task.getId()); 
			//return;
			//throw new Exception("获取Form失败");
		}
		
		if (!approveInfo.isApproval()) {  // 如果本次未同意，流程直接结束，表单状态为：未通过
			if (pi != null) {
				rejectAndtoEnd(taskId);  //没有通过，直接跳转到结束节点
			}
			form.setStatus(Form.STATUS_REJECTED);
			
		} else {
			// 如果本次同意，且本次是最后一个审批，就表示所有的环节都通过了，则流程正常结束，表单的状态为：已通过
			processEngine.getTaskService().complete(taskId);
			pi = processEngine.getRuntimeService()//
				.createProcessInstanceQuery()//
				.processInstanceId(task.getExecutionId())//
				.singleResult();  //;
			
			if (pi == null) { // 如果本流程实例已执行完，表示本次是最后一个审批
				form.setStatus(Form.STATUS_APPROVED); 
			}
			
		}
		System.out.println("========||||||||||||||||||form.approvel is: " + form.getStatus());
		//保存表单维护状态
		if(form != null)
			formFlowDao.save(form); 
		
	}
	
	
	
	//===============================Activiti5 封装API===============================================
	/**
	 * 获取Task 根据taskId
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	private Task getTaskById(String taskId) throws Exception {
		TaskEntity task = (TaskEntity) processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();  
        if (task == null) {  
            throw new Exception("任务实例未找到!");  
        }  
        return task;
	}
	
	
	/**
	 * 根据TaskId获取ProcessDefinition
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	private ProcessDefinitionEntity getProcessDefinitionEntityByTaskId(String taskId) throws Exception {
		// 取得流程定义  
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) processEngine.getRepositoryService())  
                .getDeployedProcessDefinition(getTaskById(taskId)  
                        .getProcessDefinitionId());  
        
        return processDefinition;
	}
	
	/** 
     * 根据任务ID和节点ID获取活动节点 <br> 
     *  
     * @param taskId 
     *            任务ID 
     * @param activityId 
     *            活动节点ID <br> 
     *            如果为null或""，则默认查询当前活动节点 <br> 
     *            如果为"end"，则查询结束节点 <br> 
     *  
     * @return 
     * @throws Exception 
     */  
    private ActivityImpl findActivitiImpl(String taskId, String activityId)  
            throws Exception {  
        // 取得流程定义  
        ProcessDefinitionEntity processDefinition = getProcessDefinitionEntityByTaskId(taskId);  
  
        // 获取当前活动节点ID  
        if (StringUtils.isEmpty(activityId)) {  
            activityId = getTaskById(taskId).getTaskDefinitionKey();  
        }  
  
        // 根据流程定义，获取该流程实例的结束节点  
        if (activityId.toUpperCase().equals("END")) {  
            for (ActivityImpl activityImpl : processDefinition.getActivities()) {  
                List<PvmTransition> pvmTransitionList = activityImpl  
                        .getOutgoingTransitions();  
                if (pvmTransitionList.isEmpty()) {  
                    return activityImpl;  
                }  
            }  
        }  
  
        // 根据节点ID，获取对应的活动节点  
        ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition)  
                .findActivity(activityId);  
  
        return activityImpl;  
    }  
    
    
    /** 
     * @param taskId 
     *            当前任务ID 
     * @param variables 
     *            流程变量 
     * @param activityId 
     *            流程转向执行任务节点ID<br> 
     *            此参数为空，默认为提交操作 
     * @throws Exception 
     */  
	@SuppressWarnings("unused")
	private void commitProcess(String taskId, Map<String, Object> variables,  
            String activityId) throws Exception {  
        if (variables == null) {  
            variables = new HashMap<String, Object>();  
        }  
        // 跳转节点为空，默认提交操作  
        if (StringUtils.isEmpty(activityId)) { 
        	processEngine.getTaskService()//
            	.complete(taskId, variables);  
        	
        } else {// 流程转向操作  
            turnTransition(taskId, activityId, variables);  
        }  
    }
    
    /** 
     * 流程转向操作 
     *  
     * @param taskId 
     *            当前任务ID 
     * @param activityId 
     *            目标节点任务ID 
     * @param variables 
     *            流程变量 
     * @throws Exception 
     */  
    private void turnTransition(String taskId, String activityId, Map<String, Object> variables) 
    				throws Exception {  
        // 当前节点  
        ActivityImpl currActivity = findActivitiImpl(taskId, null);  
        // 清空当前流向  
        List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);  
  
        // 创建新流向  
        TransitionImpl newTransition = currActivity.createOutgoingTransition();  
        // 目标节点  
        ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);  
        // 设置新流向的目标节点  
        newTransition.setDestination(pointActivity);  
  
        // 执行转向任务  
        processEngine.getTaskService().complete(taskId, variables);  
        // 删除目标节点新流入  
        pointActivity.getIncomingTransitions().remove(newTransition);  
  
        // 还原以前流向  
        restoreTransition(currActivity, oriPvmTransitionList);  
    }
    
    /** 
     * 清空指定活动节点流向 
     *  
     * @param activityImpl 
     *            活动节点 
     * @return 节点流向集合 
     */  
    private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {  
        // 存储当前节点所有流向临时变量  
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();  
        // 获取当前节点所有流向，存储到临时变量，然后清空  
        List<PvmTransition> pvmTransitionList = activityImpl  
                .getOutgoingTransitions();  
        for (PvmTransition pvmTransition : pvmTransitionList) {  
            oriPvmTransitionList.add(pvmTransition);  
        }  
        pvmTransitionList.clear();  
  
        return oriPvmTransitionList;  
    }
    
    /** 
     * 还原指定活动节点流向 
     *  
     * @param activityImpl 
     *            活动节点 
     * @param oriPvmTransitionList 
     *            原有节点流向集合 
     */  
    private void restoreTransition(ActivityImpl activityImpl,  
            List<PvmTransition> oriPvmTransitionList) {  
        // 清空现有流向  
        List<PvmTransition> pvmTransitionList = activityImpl  
                .getOutgoingTransitions();  
        pvmTransitionList.clear();  
        // 还原以前流向  
        for (PvmTransition pvmTransition : oriPvmTransitionList) {  
            pvmTransitionList.add(pvmTransition);  
        }  
    }  
    
    
    /**
     *  否决并跳转到结束节点
     * @param activityImpl taskId 
     */
    private void rejectAndtoEnd(String taskId) {    
        TaskEntity taskEntity = (TaskEntity) processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        
        System.out.println("taskEntity: " + taskEntity.getProcessDefinitionId()); 
        
        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) processEngine.getRepositoryService()).getDeployedProcessDefinition(taskEntity.getProcessDefinitionId());
        
         
        ExecutionEntity executionEntity = (ExecutionEntity) processEngine.getRuntimeService().createExecutionQuery().executionId(taskEntity.getExecutionId()).singleResult();//执行实例
        String activitiId = executionEntity.getActivityId();//当前实例的执行到哪个节点
        List<ActivityImpl> activitiList = def.getActivities();//获得当前任务的所有节点
        
        ActivityImpl activeActivity = findActivityImpl(activitiList, activitiId);
        ActivityImpl endActivity = findEndActivityImpls(activitiList).get(0);

        List<PvmTransition> pvmTransitionList = activeActivity.getOutgoingTransitions();//获取当前节点的所以出口（这个方法做的不好，应该返回List<TransitionImpl>才对的，这样就不用下面的强转换了，我想以后版本会改了这点）
        for (PvmTransition pvmTransition : pvmTransitionList) {
            TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;//强转为TransitionImpl
            transitionImpl.setDestination(endActivity);
        }
        
        processEngine.getTaskService().complete(taskId);  
    }
    
    //根据ActivitiId获取Acitiviti
    private ActivityImpl findActivityImpl(List<ActivityImpl> activitiList, String activitiId) {
        for (ActivityImpl activityImpl : activitiList) {
            String id = activityImpl.getId();
            if (id.equals(activitiId)) {
                return activityImpl;
            }
        }
        return null;
    }
    
    private List<ActivityImpl> findEndActivityImpls(List<ActivityImpl> activitiList) {
        List<ActivityImpl> activityImpls = new ArrayList<ActivityImpl>();
        for (ActivityImpl activityImpl : activitiList) {
            List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
            if (pvmTransitionList.isEmpty()) {
                activityImpls.add(activityImpl);
            }
        }
        return activityImpls;
    }

    /**
     * 删除Form
     * @param formId
     */
	public void delete(Long formId) {
		formFlowDao.delete(formId);
		
	}

	public void deleteProcessInstance(String taskId) {
		if(taskId == null)
			return;
		
		TaskEntity taskEntity = (TaskEntity) processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
	    //System.out.println("======== taskEntity: " + taskEntity.getProcessDefinitionId()); 
	    if(taskEntity != null)   
	        processEngine.getRuntimeService().deleteProcessInstance(taskEntity.getProcessInstanceId(), null); //删除流程
		
	}
    
}    

 







