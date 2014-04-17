package cn.ts987.oa.domain;

import java.io.Serializable;
import java.util.Set;

public class Privilege implements Serializable{
	private static final long serialVersionUID = 1L;

	private long id;
	
	private String name;
	
	private String url;
	
	private Set<Role> roles;
	
	private Privilege parent;
	
	private Set<Privilege> children;
	
	private String description;

	public Privilege() {
		
	}
	
	public Privilege(String name, String uri, Privilege parent) {
		this.name = name;
		this.url = uri;
		this.parent = parent;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setChildren(Set<Privilege> children) {
		this.children = children;
	}

	public Set<Privilege> getChildren() {
		return children;
	}

	public void setParent(Privilege parent) {
		this.parent = parent;
	}

	public Privilege getParent() {
		return parent;
	}
	
	
}
