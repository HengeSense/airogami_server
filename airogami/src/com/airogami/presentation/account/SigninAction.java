package com.airogami.presentation.account;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.persitence.entities.Account;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class SigninAction extends ActionSupport implements ServletRequestAware, ModelDriven<SigninVO>{
	
	private static final long serialVersionUID = 1L;
	private String method;
	private HttpServletRequest request;
	private SigninVO signinVO;
	private Map<String, Object> dataMap;
	
	public SigninAction() {
		dataMap = new HashMap<String, Object>();
		signinVO = new SigninVO();
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		this.method = ActionContext.getContext().getName();
	}
	
	public void validate(){	
		if(this.hasActionErrors()){
			JSONUtils.putStatus(dataMap, AirogamiError.Account_Signin_Input_Status, AirogamiError.Account_Signin_Input_Message);
			dataMap.put("actionErrors", this.getActionErrors());
		}
		else{
			super.validate();
			if(this.hasFieldErrors()){
				JSONUtils.putStatus(dataMap, AirogamiError.Account_Signin_Input_Status, AirogamiError.Account_Signin_Input_Message);
				dataMap.put("fieldErrors", this.getFieldErrors());
			}
		}
		
	}
	
	private void signin(int type){
		boolean succeed = false;
		try {
			Account account;
			if(type == 1){
				account =  ManagerUtils.accountManager.signinWithEmail(signinVO.getEmail(), signinVO.getPassword());
			}else{
				account =  ManagerUtils.accountManager.signinWithScreenName(signinVO.getScreenName(), signinVO.getPassword());
			}
			HttpSession session = request.getSession(true);
			session.setAttribute("account", account);
			session.setAttribute("clientAgent", signinVO.getClientAgent());
			dataMap.put("account", account);
			dataMap.put("clientAgent", signinVO.getClientAgent());
			succeed = true;
		} catch (AirogamiException e) {
			String localizedMessage = getText(e.getMessage(),e.getMessage());
			JSONUtils.putStatus(dataMap, e.getStatus(), localizedMessage);
		}
		catch(Throwable t){
			//t.printStackTrace(System.out);
			JSONUtils.putStatus(dataMap, "Unexpected exception");
		}
		//success
		if(succeed){
			JSONUtils.putOKStatus(dataMap);
		}	
	}
	
	
    public String emailSignin() throws Exception{ 
    	signin(1);
    	return SUCCESS;
	}
    
    public String screenNameSignin() throws Exception{
    	signin(2);
    	return SUCCESS;
    }
    
    public String execute() throws Exception{
		return emailSignin();
	}
    
    public String getMethod() {
		return method;
	}	
	
	public Map<String, Object> getDataMap() {   
        return dataMap;   
    }
	
	@Override
	public SigninVO getModel() {
		return signinVO;
	}

}
