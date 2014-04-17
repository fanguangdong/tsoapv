package cn.ts987.oa.service;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.springframework.transaction.annotation.Transactional;

import cn.ts987.oa.dao.IBaseDao;
import cn.ts987.oa.domain.ApproveInfo;
import cn.ts987.oa.domain.Department;
import cn.ts987.oa.domain.Form;
import cn.ts987.oa.domain.Privilege;
import cn.ts987.oa.domain.Role;
import cn.ts987.oa.domain.User;

import com.opensymphony.xwork2.ActionContext;

@Transactional
public abstract class BaseService {
	
	protected User getCurrentUser() {
		return (User) ActionContext.getContext().getSession().get("user");
	}
	
	@Resource(name="userDao")
	protected IBaseDao<User> userDao;
	
	@Resource(name="roleDao")
	protected IBaseDao<Role> roleDao;
	
	@Resource(name="departmentDao")
	protected IBaseDao<Department> departmentDao;	
	
	@Resource(name="privilegeDao")
	protected IBaseDao<Privilege> privilegeDao;
	
	@Resource(name="processEngine")
	protected ProcessEngine processEngine;
	
	@Resource(name="formFlowDao")
	protected IBaseDao<Form> formFlowDao;
	
	@Resource(name="approveInfoDao")
	protected IBaseDao<ApproveInfo> approveInfoDao;
}
