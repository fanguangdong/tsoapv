package cn.ts987.oa.service;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.ProcessDefinitionQueryProperty;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("processDefinitionService")
@Transactional
public class ProcessDefinitionService {

	@Resource
	private ProcessEngine processEngine;
	
	
	/**
	 * 查询所有最新版本的ProcessDefinition
	 * @return
	 */
	public Collection<ProcessDefinition> findAllLatestVersions() {
		//查询出所有的流程定义，按版本从小到大排序
		List<ProcessDefinition> allPdList = processEngine.getRepositoryService()//
			.createProcessDefinitionQuery()//
			.orderByProcessDefinitionVersion()//
			.asc()//
			.list(); 
		
		//过滤出所有最新版本
		Map<String, ProcessDefinition> pdMap = new HashMap<String, ProcessDefinition>();
		for(ProcessDefinition pd : allPdList) {
			pdMap.put(pd.getKey(), pd);
		}
		return pdMap.values();
		
	}
	
	/**
	 * 部署Zip流程定义
	 * @param zipInputStream
	 */
	public void deployZip(ZipInputStream zipInputStream) {
		processEngine.getRepositoryService()//
			.createDeployment()//
			.addZipInputStream(zipInputStream)//
			.deploy();
	}

	/**
	 * 获取流程图片
	 * @param pdId
	 * @return
	 * @throws Exception 
	 */
	public InputStream getProcessImage(String pdId) throws Exception {
		if(pdId == null)  
			throw new Exception("pdId为空");
		
		ProcessDefinition pd = processEngine.getRepositoryService()//
			.createProcessDefinitionQuery()//
			.processDefinitionId(pdId)//
			.singleResult();
		
		
		return processEngine.getRepositoryService()//
			.getResourceAsStream(pd.getDeploymentId(), pd.getDiagramResourceName());
	
	}

	/**
	 * 删除流程定义
	 * @param key
	 */
	public void deleteByKey(String key) {
		List<ProcessDefinition> pdList = processEngine.getRepositoryService()//
			.createProcessDefinitionQuery()//
			.processDefinitionKey(key)     //
			.list();
		
		for(ProcessDefinition pd : pdList) {
			processEngine.getRepositoryService()//
				.deleteDeployment(pd.getDeploymentId());
		}
	}
	
	
	
}
