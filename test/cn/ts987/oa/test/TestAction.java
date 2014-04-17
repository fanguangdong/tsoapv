package cn.ts987.oa.test;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
public class TestAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;

	public String execute() throws Exception{
		System.out.println("----->TestAction.execute()");
		return "success";
	}
	
	
	
}
