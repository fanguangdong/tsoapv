package cn.ts987.oa.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.ts987.oa.domain.Privilege;

@Service("privilegeDao")
public class PrivilegeDao extends BaseDao<Privilege>{
	
	@SuppressWarnings("unchecked")
	public List<Privilege> findTopPrivileges() {
		return this.getSession().createQuery("FROM Privilege p WHERE p.parent IS NULL").list();
	}
}
