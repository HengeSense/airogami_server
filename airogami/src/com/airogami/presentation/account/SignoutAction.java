package com.airogami.presentation.account;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class SignoutAction extends ActionSupport implements ServletRequestAware, ModelDriven<SignoutVO>{

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private Map<String, Object> dataMap;
	private SignoutVO signoutVO;
	
	public SignoutAction() {
		dataMap = new TreeMap<String, Object>();
		signoutVO = new SignoutVO();
	}
	
	public void validate(){	
		super.validate();
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
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public String execute() throws Exception{
		boolean succeed = false;
		try{
			boolean succeeded = false;
			User user = ManagerUtils.userManager.getUser(signoutVO.getAccountId());
			if(user != null && signoutVO.getSigninCount() == user.getSigninCount()
					&& signoutVO.getClientAgent().equals(user.getClientAgent())){
				ManagerUtils.userManager.removeUser(user.getAccountId());
				succeeded = true;
			}
			//
			HttpSession session = request.getSession(false);
			if(session != null){
				try{
				    session.invalidate();
				}
				catch(IllegalStateException lse){
					lse.printStackTrace();
				}
			}
			//
			Map<String, Object> result = new TreeMap<String, Object>();
			result.put("succeed", succeeded);
			dataMap.put("result", result);
			succeed = true;
		}catch(Throwable t){
			//t.printStackTrace(System.out);
			JSONUtils.putStatus(dataMap, "Unexpected exception");
		}
		//success
		if(succeed){
			JSONUtils.putOKStatus(dataMap);
		}		
		return SUCCESS;
	}
	
	public Map<String, Object> getDataMap() {   
        return dataMap;   
    }

	@Override
	public SignoutVO getModel() {
		return signoutVO;
	}

}
