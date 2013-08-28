package com.airogami.presentation.account;

import java.util.Map;
import java.util.TreeMap;

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
				account =  ManagerUtils.accountManager.signinWithEmail(signinVO.getEmail(), signinVO.getPassword(), signinVO.getClientAgent());
			}else{
				account =  ManagerUtils.accountManager.signinWithScreenName(signinVO.getScreenName(), signinVO.getPassword(), signinVO.getClientAgent());
			}
			Map<String, Object> result = new TreeMap<String, Object>();
			if(account != null){
				//test
				if(account.getAuthenticate() == null){
					System.err.println("Wrong signin");
				}
				int status = account.getProfile().getStatus();
				HttpSession session; 
				if(status == 0){
					session = request.getSession(true);			
					User user = new User(account.getAccountId(), signinVO.getClientAgent());
					session.setAttribute("user", user);
				}
				else{
					session = request.getSession();	
					if(session != null){
						session.invalidate();
					}
					//need localization
					result.put("error", "banned");
				}
				
			}			
			//dataMap.put("user", user);
			result.put("account", account);
			dataMap.put("result", result);
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
