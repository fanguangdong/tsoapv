package cn.ts987.oa.dao;

import java.util.Collection;

import org.springframework.stereotype.Service;

import cn.ts987.oa.domain.Form;
import cn.ts987.oa.domain.User;

@Service("formFlowDao")
public class FormFlowDao extends BaseDao<Form>{
	
	@SuppressWarnings("unchecked")
	public Collection<Form> findByUser(User user) {
		
		return (Collection<Form>)getSession().createQuery(
				"FROM Form f WHERE f.applicant.id = ?") //
				.setParameter(0, user.getId()).list();
	}
}
