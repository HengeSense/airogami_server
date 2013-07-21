package com.airogami.application;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Map;

import javax.persistence.EntityExistsException;

import org.apache.commons.beanutils.BeanUtils;

import com.airogami.application.exception.ApplicationException;
import com.airogami.application.exception.EmailExistsException;
import com.airogami.common.constants.AccountConstants;
import com.airogami.persistence.daos.DaoUtils;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.AccountStat;
import com.airogami.persistence.entities.Authenticate;
import com.airogami.persistence.entities.EntityManagerHelper;

public class AccountService implements IAccountService {

	/*
	 * @see
	 * com.airogami.application.IAccountService#signup(com.airogami.persitence
	 * .entities.Account)
	 */
	public long signup(Account account) throws ApplicationException {

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
		//account.setAccountStat(accountStat);
		return account.getAccountId();
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
	
	@Override
	public Account editAccount(Map<String, Object> properties) throws ApplicationException{
		ApplicationException ae = null;
		Account account = null;
		try {
			Long accountId = (Long)properties.get("accountId");
			EntityManagerHelper.beginTransaction();
			account = DaoUtils.accountDao.getReference(accountId);
			try{
		    	BeanUtils.populate(account, properties);		
			}catch (IllegalAccessException e) {
				throw new RuntimeException(e.getMessage());
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getMessage());
			}
			EntityManagerHelper.commit();
		} catch (Throwable t) {		
			t.printStackTrace();
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
		
		return account;
	}
	
	@Override
	public boolean changePassword(long accountId, String oldPassword, String newPassword) throws ApplicationException{
		ApplicationException ae = null;
		boolean succeed = false;
		try {
			EntityManagerHelper.beginTransaction();
			succeed = DaoUtils.accountDao.changePassword(accountId, oldPassword, newPassword);								
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
		
		return succeed;
	}
	
	@Override
	public boolean changeScreenName(long accountId, String screenName) throws ApplicationException{
		ApplicationException ae = null;
		boolean succeed = false;
		try {
			EntityManagerHelper.beginTransaction();
			succeed = DaoUtils.accountDao.changeScreenName(accountId, screenName);								
			EntityManagerHelper.commit();
		} catch (Throwable t) {		
			//t.printStackTrace();
			if(t instanceof EntityExistsException){
				succeed = false;
			}
			else if(t.getCause() == null){
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
		
		return succeed;
	}
	
	@Override
	public Account obtainAccount(long accountId, Timestamp last) throws ApplicationException{
		ApplicationException ae = null;
		Account account = null;
		try {
			EntityManagerHelper.beginTransaction();
			account = DaoUtils.accountDao.obtainAccount(accountId, last);								
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
		
		return account;
	}

}
