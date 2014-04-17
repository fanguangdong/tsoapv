package cn.ts987.oa.domain;

import java.io.Serializable;
import java.util.Set;

public class Role implements Serializable{
	private static final long serialVersionUID = 1L;

	private long id;
	
	private String name;
	
	private String description;
	
	private Set<User> users;
	
	private Set<Privilege> privileges;
	
	
	public boolean hasPrivilege(String uri) {
		if(uri == null) 
			return false;
		
		
		if(!uri.startsWith("/")) {
			uri = "/" + uri;
		}
		for(Privilege p : this.getPrivileges()) {
			if(uri.equals(p.getUrl())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return this.name;
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

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<User> getUsers() {
		return users;
	}


	public void setPrivileges(Set<Privilege> privileges) {
		this.privileges = privileges;
	}


	public Set<Privilege> getPrivileges() {
		return privileges;
	}

	
	
}
