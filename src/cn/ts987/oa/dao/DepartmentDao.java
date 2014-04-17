package cn.ts987.oa.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.ts987.oa.domain.Department;

@Service("departmentDao")
@SuppressWarnings("unchecked")
public class DepartmentDao extends BaseDao<Department> implements IBaseDao<Department>{
	
	public List<Department> findRootList() {
		return getSession().createQuery("FROM Department d WHERE d.parent IS NULL").list();
		
	}
	
	public List<Department> findChildren(long parentId) {
		return getSession().createQuery("FROM Department d WHERE d.parent.id = ?")//.list();
			.setParameter(0, parentId).list();
	}

	public Department findParent(long id) {
		Department department = (Department) getSession().createQuery("FROM Department d WHERE d.id = ?").setParameter(0, id).uniqueResult();
		if(department.getParent() != null)
			return (Department) getSession().createQuery("FROM Department d WHERE d.id = ?").setParameter(0, department.getParent().getId()).uniqueResult();
		else
			return null;
	}
}
