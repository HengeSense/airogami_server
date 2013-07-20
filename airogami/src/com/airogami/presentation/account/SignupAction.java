package com.airogami.presentation.account;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.airogami.common.constants.AccountConstants;
import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.persitence.entities.Account;
import com.airogami.persitence.entities.Authenticate;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.utilities.JSONUtils;
import com.airogami.presentation.utilities.UploadUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


public class SignupAction extends ActionSupport implements ServletRequestAware, ModelDriven<SignupVO> {

	private static final long serialVersionUID = -3591049023437005414L;

	private String method;
	private HttpServletRequest request;
	private Map<String, Object> dataMap;
	private SignupVO user;
	
	public SignupAction() {
		dataMap = new HashMap<String, Object>();
		user = new SignupVO();
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		this.method = ActionContext.getContext().getName();
	}
	
	@Override
	public SignupVO getModel() {
		return user;
	}
	
	public void validate(){	
		if(this.hasActionErrors()){
			JSONUtils.putStatus(dataMap, AirogamiError.Account_Signup_Input_Status, AirogamiError.Account_Signup_Input_Message);
			dataMap.put("actionErrors", this.getActionErrors());
		}
		else{
			super.validate();
			if(this.hasFieldErrors()){
				JSONUtils.putStatus(dataMap, AirogamiError.Account_Signup_Input_Status, AirogamiError.Account_Signup_Input_Message);
				dataMap.put("fieldErrors", this.getFieldErrors());
			}
		}
		
	}
	
	public String emailSignup() throws Exception{
		boolean succeed = false;
		try {
			Account account = new Account();
			Authenticate authenticate = new Authenticate();
			BeanUtils.copyProperties(account, user);
			BeanUtils.copyProperties(authenticate, user);
			account.setIcon("icon");
			account.setAuthenticate(authenticate);
			account = ManagerUtils.accountManager.signup(account);
			UploadUtils.uploadImage(account.getAccountId(), user.getFile(), user.getFileFileName(), user.getFileContentType());
			request.getSession(true).setAttribute("account", account);
			dataMap.put("account", account);
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
		return SUCCESS;
	}
    
    public String execute() throws Exception{
		return emailSignup();
	}
    
	public String getMethod() {
		return method;
	}	
	
	public Map<String, Object> getDataMap() {   
        return dataMap;   
    }
}
