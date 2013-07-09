package com.airogami.presentation.logic;

import com.airogami.application.ServiceUtils;
import com.airogami.application.exception.ApplicationException;
import com.airogami.exception.AirogamiException;
import com.airogami.persitence.entities.Account;

public class AccountManager {

	/*
	 * @param account:(Account) must be not null and have (country, province, city), account.authenticate (email or phone)
	 * @return Account if successful
	 * @throws AirogamiException if failed 
	 */
	public Account signup(Account account)
			throws AirogamiException {	
		
		if (account == null || account.getAuthenticate() == null){
			throw new IllegalArgumentException("Illegal arguments in signup");
		}
		String email = account.getAuthenticate().getEmail();
		String password = account.getAuthenticate().getPassword();
		String country = account.getCountry();
		String province = account.getProvince();
		String city = account.getCity();

		if ((email == null || email.length() == 0)
				|| (password == null || password.length() == 0)
				|| (country == null || country.length() == 0)
				|| (province == null || province.length() == 0)
				|| (city == null || city.length() == 0)) {
			throw new IllegalArgumentException("Illegal arguments in signup");
		}
		
		account.setAccountId(null);	
		try {
			account = ServiceUtils.accountService.signup(account);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Account_Signup_Failure_Status,
					AirogamiException.Account_Signup_Failure_Message);
		}
		return account;
	}

	/*
	 * @param args:(String[]) must have two valid elements
	 * @param type:(int) must be a valid AuthenticateType
	 * @return Account if successful
	 * @throws AirogamiException if failed 
	 */
	public Account signin(String[] args, int type)
			throws AirogamiException {
		if (args == null || args.length != 2){
			throw new IllegalArgumentException("Illegal arguments in authenticate");
		}
		Account account = null;
		try {
			account = ServiceUtils.accountService.signin(args, type);
		} catch (Throwable re) {
			throw new AirogamiException(
					AirogamiException.Account_Signin_Failure_Status,
					AirogamiException.Account_Signin_Failure_Message);
		}
		return account;
	}

}
