package com.airogami.presentation.account;

import javax.servlet.http.HttpSession;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Account;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ModelDriven;

public class SigninAction extends AirogamiActionSupport implements ModelDriven<SigninVO>{
	
	private static final long serialVersionUID = 1L;
	private SigninVO signinVO;
	
	public SigninAction() {
		super();
		signinVO = new SigninVO();
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
			if(account != null){
				HttpSession session = request.getSession(true);			
				User user = new User(account.getAccountId(), signinVO.getClientAgent());
				session.setAttribute("user", user);
			}			
			//dataMap.put("user", user);
			dataMap.put("result", account);
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
	
	@Override
	public SigninVO getModel() {
		return signinVO;
	}

}
