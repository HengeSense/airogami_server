package com.airogami.presentation.account;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.AccountStat;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ModelDriven;

public class SigninAction extends AirogamiActionSupport implements
		ModelDriven<SigninVO> {

	private static final long serialVersionUID = 1L;
	private SigninVO signinVO;

	public SigninAction() {
		super();
		signinVO = new SigninVO();
	}

	// may need consider two simultaneous nonautomatic signins
	private void signin(int type) {
		boolean succeed = false;
		try {
			HttpSession session;
			User user = null;
			boolean shouldSignin = true;
			boolean automatic = signinVO.getAutomatic();
			Map<String, Object> result = new TreeMap<String, Object>();
			if (automatic) {
				session = request.getSession(false);
				if (session != null) {
					user = (User) session.getAttribute("user");
					if (user != null) {
						shouldSignin = false;
						//to assure follows, one user instance can't be changed to another instance
						if(signinVO.getSigninCount() != user.getSigninCount()){
							result.put("error", "elsewhere");
						}
						else{
							user.setClientAgent(signinVO.getClientAgent());
						}
					}
				}
			}
			
			if (shouldSignin) {
				Account account;
				if (type == 1) {
					account = ManagerUtils.accountManager.signinWithEmail(
							signinVO.getEmail(), signinVO.getPassword(),
							automatic);
				} else {
					account = ManagerUtils.accountManager.signinWithScreenName(
							signinVO.getScreenName(), signinVO.getPassword(),
							automatic);
				}
				if (account != null) {
					//
					AccountStat accountStat = account.getAccountStat();
					if (accountStat.getStatus() == 0) {
						if(automatic){
							if(signinVO.getSigninCount() != accountStat.getSigninCount()){
								result.put("error", "elsewhere");
								shouldSignin = false;
							}
							else{
								//account.setAccountStat(null);
							}
						}
						if(shouldSignin){
							session = request.getSession(true);
							user = ManagerUtils.userManager.updateUser(
									account.getAccountId(),
									signinVO.getClientAgent(), accountStat);
							session.setAttribute("user", user);
							session.setAttribute("signinCount",
									accountStat.getSigninCount());
							result.put("account", account);
						}
						
					} else {
						session = request.getSession(false);
						if (session != null) {
							session.invalidate();
						}
						result.put("error", "banned");
					}

				} else {
					result.put("error", "none");
				}
			}

			dataMap.put("result", result);
			succeed = true;
		} catch (AirogamiException e) {
			String localizedMessage = getText(e.getMessage(), e.getMessage());
			JSONUtils.putStatus(dataMap, e.getStatus(), localizedMessage);
		} catch (Throwable t) {
			t.printStackTrace(System.out);
			JSONUtils.putStatus(dataMap, "Unexpected exception");
		}
		// success
		if (succeed) {
			JSONUtils.putOKStatus(dataMap);
		}
	}

	public String emailSignin() throws Exception {
		signin(1);
		return SUCCESS;
	}

	public String screenNameSignin() throws Exception {
		signin(2);
		return SUCCESS;
	}

	public String execute() throws Exception {
		return emailSignin();
	}

	@Override
	public SigninVO getModel() {
		return signinVO;
	}

}
