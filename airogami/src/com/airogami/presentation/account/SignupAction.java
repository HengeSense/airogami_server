package com.airogami.presentation.account;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.airogami.common.constants.AccountConstants;
import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.persitence.entities.Account;
import com.airogami.presentation.logic.AccountManager;
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
		// TODO Auto-generated constructor stub
		dataMap = new HashMap<String, Object>();
		user = new SignupVO();
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
		this.method = ActionContext.getContext().getName();
	}
	
	@Override
	public SignupVO getModel() {
		// TODO Auto-generated method stub
		return user;
	}
	
	public void validate(){			
		super.validate();
		if(this.hasFieldErrors()){
			JSONUtils.putStatus(dataMap, AirogamiError.Account_Signup_Input_Status, AirogamiError.Account_Signup_Input_Message);
			dataMap.put("fieldErrors", this.getFieldErrors());
		}
	}
	
	public String emailSignup() throws Exception{
		signup(user.getEmail(), AccountConstants.AuthenticateTypeEmail);
		return SUCCESS;
	}
	
	public String phoneSignup() throws Exception{
		signup(user.getPhone(), AccountConstants.AuthenticateTypePhone);
		return SUCCESS;
	}
	
    private void signup(String domain,int type){
    	Account account = new Account();
    	try {
			BeanUtils.copyProperties(account, user);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Unexpected exception");
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Unexpected exception");
		}
    	
    	boolean succeed = true;
		try {
			account = ManagerUtils.accountManager.signup(account);
			UploadUtils.uploadImage(account.getAccountId(), user.getFile(), user.getFileFileName(), user.getFileContentType());
			request.getSession(true).setAttribute("user", account);
			dataMap.put("user", account);
		} catch (AirogamiException e) {
			succeed = false;
			String localizedMessage = getText(e.getMessage(),e.getMessage());
			JSONUtils.putStatus(dataMap, e.getStatus(), localizedMessage);
		}
		//success
		if(succeed){
			JSONUtils.putOKStatus(dataMap);
		}	
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
