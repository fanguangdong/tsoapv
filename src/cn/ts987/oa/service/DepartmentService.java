package cn.ts987.oa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ts987.oa.dao.DepartmentDao;
import cn.ts987.oa.domain.Department;

@Service("departmentService")
@Transactional
public class DepartmentService extends BaseService{
	
	public List<Department> list() {
		List<Department> roleList = departmentDao.findAll();
		return roleList;
	}
	
	public void add(Department entity) {
		departmentDao.save(entity);
	}
	
	public void update(Department entity) {
		departmentDao.update(entity);
	}
	
	public void delete(long id) {
		departmentDao.delete(id);
	}

	public Department findById(long id) {
		
		return departmentDao.findById(id);
	}

	public List<Department> findAll() {
		
		return departmentDao.findAll();
	}

	public List<Department> findRootList() {
		return ((DepartmentDao)departmentDao).findRootList();
	}

	public List<Department> findChildren(long parentId) {
		return ((DepartmentDao)departmentDao).findChildren(parentId);
	}

	public Department getParent(long parentId) {
		return ((DepartmentDao)departmentDao).findParent(parentId);
	}
	
}
