package com.airogami.presentation.account;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Authenticate;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.utilities.JSONUtils;
import com.airogami.presentation.utilities.UploadUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;


public class CopyOfSignupAction extends AirogamiActionSupport implements ModelDriven<SignupVO> {

	private static final long serialVersionUID = -3591049023437005414L;

	private String method;
	private SignupVO user;
	
	public CopyOfSignupAction() {
		super();
		user = new SignupVO();
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		super.setServletRequest(request);
		this.method = ActionContext.getContext().getName();
	}
	
	@Override
	public SignupVO getModel() {
		return user;
	}
	
	public String emailSignup() throws Exception{
		boolean succeed = false;
		try {
			Account account = new Account();
			Authenticate authenticate = new Authenticate();
			BeanUtils.copyProperties(account, user);
			BeanUtils.copyProperties(authenticate, user);
			//account.setIcon("account/icon.jpg");
			account.setAuthenticate(authenticate);
			Object accountId = ManagerUtils.accountManager.signup(account);
			//UploadUtils.uploadImage(accountId, user.getFile(), user.getFileFileName(), user.getFileContentType());

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
}
