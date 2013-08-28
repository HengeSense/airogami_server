package com.airogami.presentation.account;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Authenticate;
import com.airogami.persistence.entities.Profile;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;


public class SignupAction extends AirogamiActionSupport implements ModelDriven<SignupVO> {

	private static final long serialVersionUID = -3591049023437005414L;

	private String method;
	private SignupVO signupVO;
	
	public SignupAction() {
		super();
		signupVO = new SignupVO();
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		super.setServletRequest(request);
		this.method = ActionContext.getContext().getName();
	}
	
	@Override
	public SignupVO getModel() {
		return signupVO;
	}
	
	public String emailSignup() throws Exception{
		boolean succeed = false;
		try {
			Account account = new Account();
			Authenticate authenticate = new Authenticate();
			Profile profile = new Profile();
			PropertyUtils.copyProperties(profile, signupVO);
			PropertyUtils.copyProperties(authenticate, signupVO);
			account.setAuthenticate(authenticate);
			account.setProfile(profile);
			account = ManagerUtils.accountManager.signup(account);
			Map<String, Object> result = null;
			if(account != null){
				result = new TreeMap<String, Object>();
				result.put("tokens", ManagerUtils.dataManager.accountIcon(account.getAccountId())); 
				result.put("account", account);
				//session
				HttpSession session = request.getSession(true);			
				User user = new User(account.getAccountId(), signupVO.getClientAgent());
				session.setAttribute("user", user);
			}
			dataMap.put("result", result);
			succeed = true;
		} catch (AirogamiException e) {			
			String localizedMessage = getText(e.getMessage(),e.getMessage());
			JSONUtils.putStatus(dataMap, e.getStatus(), localizedMessage);
		}
		catch(Throwable t){
			t.printStackTrace();
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
