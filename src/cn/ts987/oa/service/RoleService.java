package cn.ts987.oa.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ts987.oa.domain.Role;

@Service
public class RoleService extends BaseService{
	
	@Transactional
	public List<Role> list() {
		List<Role> roleList = roleDao.findAll();
		return roleList;
	}
	
	@Transactional
	public void add(Role entity) {
		roleDao.save(entity);
	}
	
	@Transactional
	public void update(Role entity) {
		roleDao.update(entity);
	}
	
	@Transactional
	public void delete(long id) {
		roleDao.delete(id);
	}

	@Transactional
	public Role findById(long id) {
		
		return roleDao.findById(id);
	}
	
	@Transactional
	public List<Role> findAll() {
		return roleDao.findAll();
	}

	@Transactional
	public List<Role> findByIds(Long[] roleIds) {
		List<Long> roles = new ArrayList<Long>();
		Collections.addAll(roles, roleIds);
		if(roles.size() == 0) {
			return Collections.emptyList();
		}
		return roleDao.findByIds(roles);
	}
	
}
