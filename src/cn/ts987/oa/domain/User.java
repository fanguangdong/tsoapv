package cn.ts987.oa.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import com.opensymphony.xwork2.ActionContext;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String name;
	private String description; 
	private String loginName;
	private Date hiredate;
	private String idCard;
	private String password;
	private String gender;
	private Department department;
	private String phoneNumber;
	private String email;
	private Set<Role> roles;
	
	
	public boolean isAdmin() {
		return "admin".equals(this.loginName);
	}
	/**
	 * 判断此User是否有uri的权限   
	 * @param uri
	 * @return
	 */
	public boolean hasPrivilege(String uri) {
		//admin是超级管理员，具有所有权限
		if("admin".equals(this.loginName)) {
			return true;
		}
		if(uri == null) 
			return false;
		
		int pos = uri.indexOf("?");
		if(pos != -1) {
			uri = uri.substring(0, pos);
		}
		int actionPos = uri.indexOf(".action");
		if(actionPos > 0) {
			uri = uri.substring(0, actionPos);
		}
		
		if(uri.endsWith("UI")) {
			uri = uri.substring(0, uri.indexOf("UI"));
		}
		
		// 如果本URL不需要控制，则登录用户就可以使用
		@SuppressWarnings("unchecked")
		Collection<String> allPrivilegeUrls = (Collection<String>) ActionContext.getContext().getApplication().get("allPrivilegeUrls");
		if (!allPrivilegeUrls.contains(uri)) 
			return true;
		
		for(Role p : roles) {
			if(p.hasPrivilege(uri)) {
				return true;
			}
		}
		return false;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setHiredate(Date hiredate) {
		this.hiredate = hiredate;
	}
	public Date getHiredate() {
		return hiredate;
	}

	
}
