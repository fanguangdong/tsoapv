package cn.ts987.oa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ts987.oa.dao.UserDao;
import cn.ts987.oa.domain.User;

@Service("userService")
public class UserService extends BaseService{
	
	@Transactional
	public void jbpmTest() {
		System.err.println("processEngine is: " + processEngine);
	}
	
	@Transactional
	public List<User> list() {
		List<User> UserList = userDao.findAll();
		return UserList;
	}
	
	@Transactional
	public void add(User entity) {
		userDao.save(entity);
	}
	
	@Transactional
	public void update(User entity) {
		userDao.update(entity);
	}
	
	@Transactional
	public void delete(long id) {
		userDao.delete(id);
	}
 
	@Transactional
	public User findById(long id) {
		return userDao.findById(id);
	}

	@Transactional
	public List<User> findByIds(Long[] userIds) {
		//List<Long> users = new ArrayList<Long>();
		//Collections.addAll(users, userIds);
		return userDao.findByIds(userIds);
	}

	public User validateUser(String loginName, String password) {
		
		return ((UserDao)userDao).findByLoginNameAndPassword(loginName, password);
	}
	
}
