package com.airogami.application;

import java.lang.reflect.InvocationTargetException;
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
import com.airogami.persistence.entities.Profile;
import com.airogami.persistence.entities.Report;
import com.airogami.persistence.entities.ReportId;

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
		Profile profile = account.getProfile();
		account.setAuthenticate(null);
		try {
			EntityManagerHelper.beginTransaction();
			authenticate = DaoUtils.authenticateDao.createAuthenticate(authenticate);
			//DaoUtils.authenticateDao.flush();
			if(authenticate == null){
				//duplicate email
				ae = new EmailExistsException();
			}
			else{
				account.setAccountId(authenticate.getAccountId());
				DaoUtils.accountDao.save(account);
				profile.setAccountId(authenticate.getAccountId());
				DaoUtils.profileDao.save(profile);
				accountStat.setAccountId(authenticate.getAccountId());
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
		account.setProfile(profile);
		account.setAuthenticate(authenticate);
		return account;
	}

	/*
	 * @see com.airogami.application.IAccountService#signin(java.lang.String[],
	 * int)
	 */
	@Override
	public Account signin(String[]args, int type) throws ApplicationException {
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
		if(account != null){
			account.getAuthenticate();
			account.getAccountStat();
			account.getProfile();
		}
		
		return account;
	}
	
	@Override
	public Profile editProfile(Map<String, Object> properties) throws ApplicationException{
		ApplicationException ae = null;
		Profile profile = null;
		try {
			Long accountId = (Long)properties.get("accountId");
			EntityManagerHelper.beginTransaction();
			profile = DaoUtils.profileDao.findById(accountId);
			if(profile != null){
				try{
			    	BeanUtils.populate(profile, properties);	
			    	DaoUtils.accountDao.flush();
			    	DaoUtils.profileDao.increaseUpdateCount(accountId, 1);
			    	DaoUtils.accountDao.increaseUpdateCount(accountId, 1);
				}catch (IllegalAccessException e) {
					throw new RuntimeException(e.getMessage());
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e.getMessage());
				}
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
		if(profile != null){
			profile.setUpdateCount(profile.getUpdateCount() + 1);
		}
		
		return profile;
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
	public Profile obtainProfile(long accountId, Long updateCount) throws ApplicationException{
		ApplicationException ae = null;
		Profile profile = null;
		try {
			EntityManagerHelper.beginTransaction();
			profile = DaoUtils.profileDao.obtainProfile(accountId, updateCount);								
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
		
		return profile;
	}
	
	public Report reportAccount(Report report) throws ApplicationException
	{
		ApplicationException ae = null;				
		try {
			report = DaoUtils.reportDao.createReport(report);								
		} catch (Throwable t) {		
			//t.printStackTrace();
            if(t.getCause() == null){
				ae = new ApplicationException();
			}
			else{
				ae = new ApplicationException(t.getCause().getMessage());
			}
		}		
		if (ae != null) {
			throw ae;
		} 		
		return report;
	}

}
