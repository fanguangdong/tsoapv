package cn.ts987.oa.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.zip.ZipInputStream;

import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
public class ProcessDefinitionAction extends BaseAction<Object>{
	
	private static final long serialVersionUID = 1L;

	private File upload;
	
	private String pdId;
	
	private String key;
	
	private InputStream inputStream;
	
	public String list() {
		Collection<ProcessDefinition> pdList = processDefinitionService.findAllLatestVersions();
		ActionContext.getContext().put("pdList", pdList);
		return "list";
	}
	
	public String addUI() {
		
		return "addUI";
	}
	
	public String add() throws Exception {
		System.out.println("ProcessDefinitionAction.add()");
		ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(upload));
		processDefinitionService.deployZip(zipInputStream);
		
		return "toList";
	}

	public String delete() throws Exception {
		key = new String(key.getBytes("iso8859-1"), "utf-8");
		processDefinitionService.deleteByKey(key);
		return "toList";
	}
	
	/**
	 * 获取流程图片 xxx.png
	 * @return
	 * @throws Exception 
	 */
	public String showProcessImage() throws Exception {
		// 进行第2次URL解码
		pdId = URLDecoder.decode(pdId, "utf-8");
		
		System.out.println(" ================================================= pdId: " + pdId);
		
		inputStream = processDefinitionService.getProcessImage(pdId);
		return "showProcessImage";
	}
	
	
	
	
	
	public void setUpload(File upload) {
		this.upload = upload;
	}

	public File getUpload() {
		return upload;
	}

	
	public void setPdId(String pdId) {
		this.pdId = pdId;
	}

	public String getPdId() {
		return pdId;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	
	
	
	
	
}
