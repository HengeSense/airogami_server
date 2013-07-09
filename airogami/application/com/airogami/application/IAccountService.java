package com.airogami.application;

import com.airogami.application.exception.ApplicationException;
import com.airogami.persitence.entities.Account;

public interface IAccountService {
	/*
	 * @param account:(Account) must be not null and have account.user (have email or phone)
	 * @return Account if successful
	 * @throws ApplicationException if failed 
	 */
	public Account signup(Account account) throws ApplicationException;
	/*
	 * @param args:(String[) must be not null, have password and (have email or phone)
	 * @return Account, account.accountStat if successful
	 * @throws ApplicationException if failed 
	 */
	public Account signin(String[]args, int type) throws ApplicationException;
}
