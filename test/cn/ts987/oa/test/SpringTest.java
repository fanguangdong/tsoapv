package cn.ts987.oa.test;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest { 
	
	private ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	//@Test
	public void testBean() throws Exception {
		TestAction testAction = (TestAction)ac.getBean("testAction");
		System.out.println(testAction);
	}
	
	@Test
	public void testSessionFactory() throws Exception {
		
		SessionFactory sf = (SessionFactory)ac.getBean("sessionFactory");
		
		System.out.println(sf);
	}   
	
	@Test
	public void testTransaction() throws Exception {
		TestService ts = (TestService)ac.getBean("testService");
		ts.save(); 
	} 

	@Test
	public void testDao() throws Exception {
		
		  ////aaaaaaaaaaaaa
		
	}
	
	@Test
	public void testService() throws Exception {
		
	}
	
	@Test
	public void testAction() throws Exception {
		//DepartmentAction da = (DepartmentAction) ac.getBean("departmentAction");
	}
	
	
}
