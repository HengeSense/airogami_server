package com.airogami.application;

import com.airogami.application.exception.ApplicationException;
import com.airogami.application.exception.EmailExistsException;
import com.airogami.common.constants.AccountConstants;
import com.airogami.persitence.daos.DaoUtils;
import com.airogami.persitence.entities.Account;
import com.airogami.persitence.entities.AccountStat;
import com.airogami.persitence.entities.Authenticate;
import com.airogami.persitence.entities.EntityManagerHelper;

public class AccountService implements IAccountService {

	/*
	 * @see
	 * com.airogami.application.IAccountService#signup(com.airogami.persitence
	 * .entities.Account)
	 */
	public Account signup(Account account) throws ApplicationException {

		ApplicationException ae = null;
		AccountStat accountStat = new AccountStat();
		Authenticate authenticate = account.getAuthenticate();
		account.setAuthenticate(null);
		try {
			EntityManagerHelper.beginTransaction();
			authenticate = DaoUtils.authenticateDao.createAuthenticate(authenticate);
			DaoUtils.authenticateDao.flush();
			if(authenticate == null){
				//duplicate email
				ae = new EmailExistsException();
			}
			else{
				account.setAccountId(authenticate.getAccountId());
				DaoUtils.accountDao.save(account);
				DaoUtils.accountDao.flush();
				accountStat.setAccountId(account.getAccountId());
				DaoUtils.accountStatDao.save(accountStat);
			}
								
			EntityManagerHelper.commit();
		} catch (Throwable t) {		
			//t.printStackTrace();
			if(t.getCause() == null){
				ae = new ApplicationException();
			}
			else{
				ae = new ApplicationException(t.getCause().getMessage());
			}
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
		if (ae != null) {
			throw ae;
		} 
		
		account.setAccountStat(accountStat);
		
		return account;
	}

	/*
	 * @see com.airogami.application.IAccountService#signin(java.lang.String[],
	 * int)
	 */
	@Override
	public Account signin(String[] args, int type) throws ApplicationException {
		Account account = null;
		ApplicationException ae = null;
		try {
			EntityManagerHelper.beginTransaction();
			switch (type) {
			case AccountConstants.AuthenticateTypeScreenName:
				account = DaoUtils.authenticateDao.authenticateWithScreenName(args[0], args[1]);
				break;
			case AccountConstants.AuthenticateTypeEmail:
				account = DaoUtils.authenticateDao.authenticateWithEmail(args[0], args[1]);
				break;
			}			
			EntityManagerHelper.commit();
		} catch (Throwable t) {
			if(t.getCause() == null){
				ae = new ApplicationException();
			}
			else{
				ae = new ApplicationException(t.getCause().getMessage());
			}
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
		if (ae != null) {
			throw ae;
		}
		if(account == null)
		{
			throw new ApplicationException("Account not found");
		}
		//account.getAccountStat();
		return account;
	}
}
