package com.airogami.presentation.account;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Account;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class SigninAction extends AirogamiActionSupport implements ModelDriven<SigninVO>{
	
	private static final long serialVersionUID = 1L;
	private String method;
	private SigninVO signinVO;
	
	public SigninAction() {
		super();
		signinVO = new SigninVO();
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		super.setServletRequest(request);
		this.method = ActionContext.getContext().getName();
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
	
    @Override
	protected int getInputStatus() {
		return AirogamiError.Account_Signin_Input_Status;
	}

	@Override
	protected String getInputMessage() {
		return AirogamiError.Account_Signin_Input_Message;
	}
	
	@Override
	public SigninVO getModel() {
		return signinVO;
	}

}
