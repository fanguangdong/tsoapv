package cn.ts987.oa.action;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;

import cn.ts987.oa.domain.User;
import cn.ts987.oa.service.DepartmentService;
import cn.ts987.oa.service.FormFlowService;
import cn.ts987.oa.service.FormTemplateService;
import cn.ts987.oa.service.PrivilegeService;
import cn.ts987.oa.service.ProcessDefinitionService;
import cn.ts987.oa.service.RoleService;
import cn.ts987.oa.service.UserService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public BaseAction() {
		
		Type[] actualGenericTypes = ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments();
		
			//model = (T) actualGenericTypes[0].getClass().newInstance();
			Class<T> clazz = (Class<T>)actualGenericTypes[0];
			
			try {
				model = (T) Class.forName(clazz.getName()).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	}
	
	protected T model = null;
	
	@Override
	public T getModel() {
		return model;
	}
	
	@Resource(name="departmentService")
	protected DepartmentService departmentService; 
	
	@Resource
	protected RoleService roleService;
	
	@Resource
	protected UserService userService;
	
	@Resource
	protected PrivilegeService privilegeService;

	@Resource
	protected ProcessDefinitionService processDefinitionService;
	
	@Resource
	protected FormTemplateService formTemplateService;
	
	@Resource
	protected FormFlowService formFlowService;
	
	private static String servletPath = null;
	
	/**
	 * 获得当前用户
	 * @return
	 */
	protected User getCurrentUser() {
		return (User) ActionContext.getContext().getSession().get("user");
	}
	
	/**
	 * 获取项目路径
	 * @return
	 */
	protected String getServletContextPath() {
		if(servletPath == null)
			servletPath = ServletActionContext.getServletContext().getRealPath("");
		
		return servletPath;
	}
	
	/**
	 * 保存上传的文件，使用UUID做为文件名，并返回文件存储的全路径
	 * 
	 * @param upload
	 * @return
	 */
	protected String saveUploadFile(File upload) {
		String basePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF/upload_files/"); // 返回结果最后没有'/'
		SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");
		String subPath = sdf.format(new Date());
		// 如果文件夹不存在，就创建
		File dir = new File(basePath + subPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String path = basePath + subPath + UUID.randomUUID().toString(); // 使用UUID做为文件名，以解决重名的问题
		File destFile = new File(path);
		// 移动到目的地，return true if and only if the renaming succeeded; false otherwise
		// 如果目标文件存在就会失败返回false.
		// 如果目标文件所在的文件夹不存在，就会失败返回false.
		upload.renameTo(destFile); 
		
		String relativeFilePath = path.substring(path.indexOf("WEB-INF"), path.length());
		return relativeFilePath;
	}
}
