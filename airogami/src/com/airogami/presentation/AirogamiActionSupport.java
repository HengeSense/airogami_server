package com.airogami.presentation;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ActionSupport;

public abstract class AirogamiActionSupport extends ActionSupport implements ServletRequestAware{

	private static final long serialVersionUID = 1L;
	protected HttpServletRequest request;
	protected Map<String, Object> dataMap;
	
	public AirogamiActionSupport() {
		dataMap = new HashMap<String, Object>();
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	abstract protected int getInputStatus();
	
	abstract protected String getInputMessage();
	
	public void validate(){	
		if(this.hasActionErrors()){
			JSONUtils.putStatus(dataMap, getInputStatus(), getInputMessage());
			dataMap.put("actionErrors", this.getActionErrors());
		}
		else{
			super.validate();
			if(this.hasFieldErrors()){
				JSONUtils.putStatus(dataMap,  getInputStatus(), getInputMessage());
				dataMap.put("fieldErrors", this.getFieldErrors());
			}
		}
		
	}
	
	public Map<String, Object> getDataMap() {   
        return dataMap;   
    }
}
