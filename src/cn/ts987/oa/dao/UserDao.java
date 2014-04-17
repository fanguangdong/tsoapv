package cn.ts987.oa.dao;

import org.springframework.stereotype.Service;

import cn.ts987.oa.domain.User;

@Service
public class UserDao extends BaseDao<User>{
	public User findByLoginNameAndPassword(String loginName, String password) {
		return (User) getSession().createQuery("FROM User u WHERE u.loginName = ? AND u.password = ?")//
					.setParameter(0, loginName)//
					.setParameter(1, password)//
					.uniqueResult();
	}
}
