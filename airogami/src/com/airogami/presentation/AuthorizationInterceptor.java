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

	@Override
	public void destroy() {

	}

	@Override
	public void init() {
	
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Map<String, Object> session = ActionContext.getContext().getSession();		
		if(session.containsKey("account")){
			return invocation.invoke();
		}
		else{
			HttpServletResponse response = ServletActionContext.getResponse();
			String json = JSONUtils.statusToJSONString(AirogamiError.Account_No_Signin_Status, AirogamiError.Account_No_Signin_Message);
			response.getWriter().print(json);
			return null;
		}
	}

}
