package com.airogami.presentation;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.airogami.exception.AirogamiError;
import com.airogami.presentation.logic.ClientAgent;
import com.airogami.presentation.logic.User;
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
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession(false);
		User user = null;
		String result = null;
		//should save user and clientAgent simultaneously in session
		if(session != null && (user = (User)session.getAttribute("user")) != null){
			Object clientAgent = session.getAttribute("clientAgent");
			if(user.getClientAgent().equals(clientAgent)){
				request.setAttribute("user", user);
				result = invocation.invoke();
			}
			else{// signin elsewhere
				try{
					session.invalidate();
				}
				catch(IllegalStateException lse){
					lse.printStackTrace();
				}
				user = null;
			}
			
		}
		if(user == null){
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/json");
			String json = JSONUtils.statusToJSONString(AirogamiError.Account_No_Signin_Status, AirogamiError.Account_No_Signin_Message);
			response.getWriter().print(json);
		}
		return result;
	}

}
