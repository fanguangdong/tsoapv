package cn.ts987.oa.interceptor;

import cn.ts987.oa.domain.User;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class LoginInterceptor extends AbstractInterceptor{
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		//System.err.println("LoginInterceptor.intercept()");
		
		String namespace = invocation.getProxy().getNamespace();
		String actionName = invocation.getProxy().getActionName();
		String privUrl = namespace + actionName; // 对应的权限URL
		
		if(privUrl.startsWith("/user_login")) {
			return invocation.invoke();
		}
		
		User user = (User) invocation.getInvocationContext().getSession().get("user");
		
		if(user != null) {
			
			//System.err.println("LoginInterceptor.intercept() user不为空，放行");
			
			if(user.hasPrivilege(privUrl)) {
				return invocation.invoke();
			} else {
				return "illegalReq";
			}
			
		} else {
			//System.err.println("LoginInterceptor.intercept() user为空，返回到loginUI页面");
			return "loginUI";
		}
		
	}

}
