package cn.ts987.oa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ts987.oa.dao.PrivilegeDao;
import cn.ts987.oa.domain.Privilege;

@Service("privilegeService")
@Transactional
public class PrivilegeService extends BaseService {

	public List<Privilege> findAll() {
		
		return privilegeDao.findAll();
	}

	public List<Privilege> findTopPrivileges() {
		
		return ((PrivilegeDao)privilegeDao).findTopPrivileges();
	}

	public List<Privilege> findByIds(List<Long> privilegeIds) {
		
		return privilegeDao.findByIds(privilegeIds);
	}
	
	public List<String> findAllPrivilegeUri() {
		List<String> uris = new ArrayList<String>();
		for(Privilege p : findAll()) {
			if(p.getUrl() != null)
				uris.add(p.getUrl());
		}
		return uris;
	}
}
