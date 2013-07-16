package com.airogami.presentation.logic;

import com.airogami.application.ServiceUtils;
import com.airogami.application.exception.ApplicationException;
import com.airogami.application.exception.EmailExistsException;
import com.airogami.common.constants.AccountConstants;
import com.airogami.exception.AirogamiException;
import com.airogami.persitence.entities.Account;
import com.airogami.persitence.entities.Authenticate;

public class AccountManager {

	/*
	 * @param account:(Account) must be not null and have (fullName, sex, icon, longitude, altitude, country, province, city), account.authenticate (email, password)
	 * @return account, accountStat if successful
	 * @throws AirogamiException if failed 
	 */
	public Account signup(Account account)
			throws AirogamiException {	
		Authenticate authenticate = account.getAuthenticate();
		if (account == null || authenticate == null){
			throw new IllegalArgumentException("Illegal arguments in signup");
		}
        
		if ((account.getFullName() == null || account.getFullName().length() == 0)
				|| account.getSex() == null
			    || (account.getIcon() == null || account.getIcon().length() == 0)
				|| account.getLongitude() == null
				|| account.getLatitude() == null
				|| (account.getCountry() == null || account.getCountry().length() == 0)
				|| (account.getProvince() == null || account.getProvince().length() == 0)
				|| (account.getCity() == null || account.getCity().length() == 0)
				|| (authenticate.getEmail() == null || authenticate.getEmail().length() == 0)
				|| (authenticate.getPassword() == null || authenticate.getPassword().length() == 0)) {
			throw new IllegalArgumentException("Illegal arguments in signup");
		}
		if(account.getSex() > 1){
			account.setSex((short)1);
		}
		account.setAccountId(null);	
		account.setStatus((short)0);
		//no screen name		
		authenticate.setScreenName(null);
		account.setScreenName(null);
		//
		authenticate.setAccount(null);
		try {
			account = ServiceUtils.accountService.signup(account);
		} catch (ApplicationException re) {
			if(re instanceof EmailExistsException){
				throw new AirogamiException(
						AirogamiException.Account_Signup_Duplicate_Status,
						AirogamiException.Account_Signup_Duplicate_Message);
			}
			else{
				throw new AirogamiException(
					AirogamiException.Account_Signup_Failure_Status,
					AirogamiException.Account_Signup_Failure_Message);
			}
		}
		return account;
	}

	/*
	 * @param args:(String[]) must have two valid elements (screenName, password)
	 * @return account, accountStat if successful
	 * @throws AirogamiException if failed 
	 */
public Account signinWithScreenName(String screenName, String password)
			throws AirogamiException {
		if (screenName == null || screenName.length() == 0 || password == null || password.length() == 0){
			throw new IllegalArgumentException("Illegal arguments in signinWithScreenName");
		}
		Account account = null;
		try {
			account = ServiceUtils.accountService.signin(new String[]{screenName, password}, AccountConstants.AuthenticateTypeScreenName);
		} catch (Throwable re) {
			throw new AirogamiException(
					AirogamiException.Account_Signin_Failure_Status,
					AirogamiException.Account_Signin_Failure_Message);
		}
		return account;
	}
	
	/*
	 * @param args:(String[]) must have two valid elements (email, password)
	 * @return account, accountStat if successful
	 * @throws AirogamiException if failed 
	 */
	public Account signinWithEmail(String email, String password)
			throws AirogamiException {
		if (email == null || email.length() == 0 || password == null || password.length() == 0){
			throw new IllegalArgumentException("Illegal arguments in signinWithEmail");
		}
		Account account = null;
		try {
			account = ServiceUtils.accountService.signin(new String[]{email, password}, AccountConstants.AuthenticateTypeEmail);
		} catch (Throwable re) {
			throw new AirogamiException(
					AirogamiException.Account_Signin_Failure_Status,
					AirogamiException.Account_Signin_Failure_Message);
		}
		return account;
	}

}
