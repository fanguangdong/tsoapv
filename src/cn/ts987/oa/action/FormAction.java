package cn.ts987.oa.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;

import cn.ts987.oa.domain.ApproveInfo;
import cn.ts987.oa.domain.Form;
import cn.ts987.oa.domain.FormTemplate;
import cn.ts987.oa.domain.TaskView;
import cn.ts987.oa.domain.User;

import com.opensymphony.xwork2.ActionContext;

@Controller
public class FormAction extends BaseAction<Form> {
	private static final long serialVersionUID = 1L;
	
	private Long formTemplateId;
	
	private File upload;
	
	private Long formId;
	
	private String taskId;
	
	private boolean approval;
	
	private String comments;
	
	private InputStream inputStream;  //下载申请文档
	
	public String formTemplateList() {
		Collection<FormTemplate> formTemplateList = formTemplateService.list();
		ActionContext.getContext().put("formTemplateList", formTemplateList);
		return "formTemplateList";
	}
	
	public String submitUI() {
		return "submitUI";
	}
	
	/**
	 * 提交申请
	 * @return
	 */
	public String submit() {
		FormTemplate ft = formTemplateService.getById(formTemplateId);
		
		String path = saveUploadFile(upload);
		Form form = new Form();
		
		form.setApplicant(getCurrentUser());
		form.setApplyTime(new Date());
		form.setFormTemplate(ft);
		form.setStatus(Form.STATUS_REJECTED);
		form.setPath(path);
		form.setTitle(ft.getName());
		
		formFlowService.submit(form);
		
		return "formTemplateList";
	}
	
	/**
	 * 我的申请列表
	 * @return
	 */
	public String myApplicationList() {
		Collection<FormTemplate> formTemplateList = formTemplateService.list();
		ActionContext.getContext().put("formTemplateList", formTemplateList);
		
		User user = getCurrentUser();
		Collection<Form> recordList = formFlowService.findByUser(user);
		
		ActionContext.getContext().put("recordList", recordList);
		return "myApplicationList";
	}
	
	/**
	 * 删除Form
	 * @return
	 */
	public String delete() {
		formFlowService.delete(formId);
		formFlowService.deleteProcessInstance(taskId);
		
		Collection<FormTemplate> formTemplateList = formTemplateService.list();
		ActionContext.getContext().put("formTemplateList", formTemplateList);
		User user = getCurrentUser();
		Collection<Form> recordList = formFlowService.findByUser(user); 
		ActionContext.getContext().put("recordList", recordList);
		return "myApplicationList";
	}
	
	/**
	 * 当前用户的待办任务列表
	 * @param formTemplateId
	 */
	public String myTaskList() {
		List<TaskView> taskViewList = formFlowService.getMyTaskList(getCurrentUser());
		ActionContext.getContext().put("taskViewList", taskViewList);
		return "myTaskList";
	}
	
	/**
	 * 下载申请文档
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String downloadApplication() throws FileNotFoundException, UnsupportedEncodingException{
		Form form = formFlowService.getById(formId);
		
		if(form != null) {
			String filePath = getServletContextPath() + File.separatorChar + form.getPath();
			
			inputStream = new FileInputStream(filePath);
			String fileName = URLEncoder.encode(form.getTitle(), "utf-8"); // 解决下载的默认文件名中的中文是乱码的问题
			ActionContext.getContext().put("fileName", fileName);
			
		}
		//formFlowService.downloadApplication();
		return "downloadApplication";
	}
	
	/**
	 * 审批界面
	 * @return
	 */
	public String approveUI() {
		return "approveUI";
	}
	
	
	/**
	 * 审批处理
	 * @return
	 * @throws Exception 
	 */
	public String approve() throws Exception {
		if(taskId == null) 
			throw new Exception("taskId 为空");
		
		Form form = formFlowService.getById(formId);
		
		// 生成一个ApproveInfo对象，表示本次的审批信息
		ApproveInfo ai = new ApproveInfo();
		ai.setApproval(approval); 
		ai.setApprover(getCurrentUser());
		ai.setApproveTime(new Date());
		ai.setComment(comments);
		ai.setForm(form);
		
		formFlowService.approve(taskId, ai);
		
		//跳转到TaskList页面
		List<TaskView> taskViewList = formFlowService.getMyTaskList(getCurrentUser());
		ActionContext.getContext().put("taskViewList", taskViewList);
		return "myTaskList"; 
	}
	
	/**
	 * 查看表单流转记录
	 * @return
	 */
	public String approvedHistory() { 
		Form form = formFlowService.getById(formId);
		Collection<ApproveInfo> approveInfos = form.getApproveInfos();
		ActionContext.getContext().put("approveInfos", approveInfos);
		return "approvedHistory";
	}
	
	
	
	public void setFormTemplateId(Long formTemplateId) {
		this.formTemplateId = formTemplateId;
	}

	public Long getFormTemplateId() {
		return formTemplateId;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public File getUpload() {
		return upload;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public Long getFormId() {
		return formId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setApproval(boolean approval) {
		this.approval = approval;
	}

	public boolean isApproval() {
		return approval;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getComments() {
		return comments;
	}
}
