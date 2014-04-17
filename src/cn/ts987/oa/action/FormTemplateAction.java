package cn.ts987.oa.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Collection;

import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.ts987.oa.domain.FormTemplate;

import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
public class FormTemplateAction extends BaseAction<FormTemplate>{
	private static final long serialVersionUID = 1L;
	
	private File upload;  //上传的模板 
	
	private InputStream inputStream;
	
	private Long id;
	
	public String list() {
		Collection<FormTemplate> formTemplateList = formTemplateService.list();
		ActionContext.getContext().put("formTemplateList", formTemplateList);
		
		return "list";
	}
	
	public String addUI() {
		Collection<ProcessDefinition> processDefinitionList = processDefinitionService.findAllLatestVersions();
		
		ActionContext.getContext().put("processDefinitionList", processDefinitionList);
		return "saveUI";
	}
	 
	public String add() {
		String relativeFilePath = saveUploadFile(upload);
		model.setPath(relativeFilePath);  
		formTemplateService.add(model); 
		return "toList";
	}
	
	public String updateUI() throws Exception {
		id = model.getId();
		FormTemplate ft = formTemplateService.getById(id);   
		if(ft == null) {
			throw new Exception("获取FormTemplate错误");
		}
		model.setName(ft.getName());
		Collection<ProcessDefinition> processDefinitionList = processDefinitionService.findAllLatestVersions();
		
		ActionContext.getContext().put("processDefinitionList", processDefinitionList);
		return "saveUI";
	}
	
	public String update() {
		
		return "toList";
	}
	
	public String delete() {
		
		FormTemplate ft = formTemplateService.getById(model.getId()); 
		
		if(ft != null) {
			String filePath = super.getServletContextPath() + File.separatorChar + ft.getPath();
			
			File f = new File(filePath);  
			if(f.exists()) {
				f.delete();
			}
			
			formTemplateService.delete(model.getId());
		}
		return "toList";
	}

	public String download() throws Exception {
		Long id = model.getId();
		FormTemplate ft = formTemplateService.getById(id);
		
		if(ft != null) {
			String filePath = getServletContextPath() + File.separatorChar + ft.getPath();
			
			inputStream = new FileInputStream(filePath);
			String fileName = URLEncoder.encode(ft.getName(), "utf-8"); // 解决下载的默认文件名中的中文是乱码的问题
			ActionContext.getContext().put("fileName", fileName);
			
		}
		return "download";
	}
	
	
	/* Getters and Setters */
	
	public void setUpload(File upload) {
		this.upload = upload;
	}

	public File getUpload() {
		return upload;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	
}
