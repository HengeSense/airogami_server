package com.airogami.presentation;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.airogami.exception.AirogamiError;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class AuthorizationInterceptor implements Interceptor {

	private static final long serialVersionUID = -6080707855369440485L;
	private Map<String, Object> session;
	private HttpServletResponse response;
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		// TODO Auto-generated method stub
		session = ActionContext.getContext().getSession();
		response = ServletActionContext.getResponse();
		if(session.containsKey("user")){
			return invocation.invoke();
		}
		else{
			String json = JSONUtils.statusToJSONString(AirogamiError.Account_No_Signin_Status, AirogamiError.Account_No_Signin_Message);
			response.getWriter().print(json);
			return null;
		}
	}

}
