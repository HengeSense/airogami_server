package com.airogami.presentation.account;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ActionSupport;

public class SignoutAction extends ActionSupport implements ServletRequestAware{

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private Map<String, Object> dataMap;
	
	public SignoutAction() {
		dataMap = new TreeMap<String, Object>();
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public String execute() throws Exception{
		try{
			HttpSession session = request.getSession(false);
			try{
				if(session != null){
					session.invalidate();
				}
			}
			catch(IllegalStateException lse){
				lse.printStackTrace();
			}
			
			JSONUtils.putOKStatus(dataMap);
		}catch(Throwable t){
			//t.printStackTrace(System.out);
			JSONUtils.putStatus(dataMap, "Unexpected exception");
		}
		
		return SUCCESS;
	}
	
	public Map<String, Object> getDataMap() {   
        return dataMap;   
    }

}
