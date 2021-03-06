package com.airogami.presentation;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.airogami.exception.AirogamiError;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public abstract class AirogamiActionSupport extends ActionSupport implements ServletRequestAware{

	private static final long serialVersionUID = 1L;
	protected HttpServletRequest request;
	protected Map<String, Object> dataMap;
	protected String method;
	
	public AirogamiActionSupport() {
		dataMap = new HashMap<String, Object>();
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		this.method = ActionContext.getContext().getName();
	}
	
	public void validate(){	
		if(this.hasActionErrors()){
			JSONUtils.putInputStatus(dataMap);
			dataMap.put("actionErrors", this.getActionErrors());
		}
		else{
			super.validate();
			if(this.hasFieldErrors()){
				JSONUtils.putInputStatus(dataMap);
				dataMap.put("fieldErrors", this.getFieldErrors());
			}
		}
	}
	
	public String getMethod() {
			return method;
	}
	
	public Map<String, Object> getDataMap() {   
        return dataMap;   
    }
}
