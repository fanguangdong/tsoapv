package cn.ts987.oa.test;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ts987.oa.domain.User;

class PTestService<T> {
	@Resource
	protected SessionFactory sessionFactory= null; 
	
	@Transactional 
	public void save() {
		Session session = sessionFactory.getCurrentSession();
		//Session session = sessionFactory.openSession();
		
		User user = new User();
		user.setName("eeeee");
		session.save(user);
		//session.delete(user);
	}
}


@Service("testService")
public class TestService extends PTestService<User>{
	
	
}
