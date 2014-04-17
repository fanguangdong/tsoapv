package cn.ts987.oa.domain;

import java.util.Set;

public class Department {

	private long id;
	
	private String name;
	
	private String description;
	
	private Set<User> users; 
	
	private Set<Department> children;
	
	private Department parent;
	
	
	/*getter setter*/
	
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

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setChildren(Set<Department> children) {
		this.children = children;
	}

	public Set<Department> getChildren() {
		return children;
	}

	public void setParent(Department parent) {
		this.parent = parent;
	}

	public Department getParent() {
		return parent;
	}
	
	
}
