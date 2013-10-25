package com.airogami.application;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityExistsException;

import org.apache.commons.beanutils.BeanUtils;

import com.airogami.application.exception.ApplicationException;
import com.airogami.application.exception.EmailExistsException;
import com.airogami.common.constants.AccountConstants;
import com.airogami.common.notification.ClientAgent;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.classes.AccountStatLeft;
import com.airogami.persistence.daos.DaoUtils;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.AccountStat;
import com.airogami.persistence.entities.AccountSys;
import com.airogami.persistence.entities.Agent;
import com.airogami.persistence.entities.Authenticate;
import com.airogami.persistence.entities.Chain;
import com.airogami.persistence.entities.EntityManagerHelper;
import com.airogami.persistence.entities.Hot;
import com.airogami.persistence.entities.Plane;
import com.airogami.persistence.entities.Profile;
import com.airogami.persistence.entities.Report;
import com.airogami.presentation.logic.ManagerUtils;

public class AccountService implements IAccountService {

	/*
	 * @see
	 * com.airogami.application.IAccountService#signup(com.airogami.persitence
	 * .entities.Account)
	 */
	public Account signup(Account account) throws ApplicationException {

		ApplicationException ae = null;
		AccountStat accountStat = new AccountStat();
		AccountSys accountSys = new AccountSys();
		Hot hot = new Hot();
		Authenticate authenticate = account.getAuthenticate();
		Profile profile = account.getProfile();
		account.setAuthenticate(null);
		Agent agent = account.getAgent();
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
				agent.setAccountId(authenticate.getAccountId());
				DaoUtils.agentDao.save(agent);
				accountStat.setAccountId(authenticate.getAccountId());
				DaoUtils.accountStatDao.save(accountStat);
				accountSys.setAccountId(authenticate.getAccountId());
				DaoUtils.accountSysDao.save(accountSys);
				hot.setAccountId(authenticate.getAccountId());
				DaoUtils.hotDao.save(hot);
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
		account.setHot(hot);
		account.setAgent(null);
		return account;
	}

	/*
	 * @see com.airogami.application.IAccountService#signin(java.lang.String[],
	 * int)
	 */
	@Override
	public Account signin(String[]args, int type, ClientAgent clientAgent, boolean automatic) throws ApplicationException {
		Account account = null;
		ApplicationException ae = null;
		try {
			EntityManagerHelper.beginTransaction();
			switch (type) {
			case AccountConstants.AuthenticateTypeEmail:
				account = DaoUtils.authenticateDao.authenticateWithEmail(args[0], args[1], automatic);
				break;
			case AccountConstants.AuthenticateTypeScreenName:
				account = DaoUtils.authenticateDao.authenticateWithScreenName(args[0], args[1], automatic);
				break;
			}		
			if(account != null ){
				if(clientAgent.equals(account.getAgent()) == false){
					BeanUtils.copyProperties(account.getAgent(), clientAgent);
				}
				if(automatic == false){
					DaoUtils.accountStatDao.increaseSigninCount(account.getAccountId(), 1);
				}
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
		if(account != null && automatic == false){
			AccountStat accountStat = account.getAccountStat();
			accountStat.setSigninCount(accountStat.getSigninCount() + 1);
		}
		
		return account;
	}
	
	/*
	 * @see com.airogami.application.IAccountService#signout(java.lang.String[],
	 * int)
	 */
	@Override
	public Integer signout(String[]args, int type) throws ApplicationException {
		Integer accountId = null;
		ApplicationException ae = null;
		try {
			EntityManagerHelper.beginTransaction();
			switch (type) {
			case AccountConstants.AuthenticateTypeEmail:
				accountId = DaoUtils.authenticateDao.signoutWithEmail(args[0], args[1]);
				break;
			case AccountConstants.AuthenticateTypeScreenName:
				accountId = DaoUtils.authenticateDao.signoutWithScreenName(args[0], args[1]);
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
		
		return accountId;
	}
	
	@Override
	public Profile editProfile(Map<String, Object> properties) throws ApplicationException{
		ApplicationException ae = null;
		Profile profile = null;
		try {
			Integer accountId = (Integer)properties.get("accountId");
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
	public boolean changePassword(int accountId, String oldPassword, String newPassword) throws ApplicationException{
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
	public boolean changeScreenName(int accountId, String screenName) throws ApplicationException{
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
	public Profile obtainProfile(int accountId, Integer updateCount) throws ApplicationException{
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
	
	@Override
	public Hot obtainHot(int accountId, Integer updateCount) throws ApplicationException{
		ApplicationException ae = null;
		Hot hot = null;
		try {
			EntityManagerHelper.beginTransaction();
			hot = DaoUtils.profileDao.obtainHot(accountId, updateCount);								
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
		
		return hot;
	}
	
	public Report reportAccount(Report report) throws ApplicationException{
		ApplicationException ae = null;				
		try {
			EntityManagerHelper.beginTransaction();
			report = DaoUtils.reportDao.createReport(report);	
			EntityManagerHelper.commit();
		} catch (Throwable t) {		
			//t.printStackTrace();
            if(t.getCause() == null){
				ae = new ApplicationException();
			}
			else{
				ae = new ApplicationException(t.getCause().getMessage());
			}
		}	
		finally {
			EntityManagerHelper.closeEntityManager();
		}
		if (ae != null) {
			throw ae;
		} 		
		return report;
	}

	@Override
	public int updateAccountStat() throws ApplicationException {
		int count = 0;	
		ApplicationException ae = null;
		try {
			EntityManagerHelper.beginTransaction();
			count = DaoUtils.accountStatDao.updatePickupAndSendCounts();	
			EntityManagerHelper.commit();
		} catch (Throwable t) {		
			//t.printStackTrace();
            if(t.getCause() == null){
				ae = new ApplicationException();
			}
			else{
				ae = new ApplicationException(t.getCause().getMessage());
			}
		}	
		finally {
			EntityManagerHelper.closeEntityManager();
		}
		if (ae != null) {
			throw ae;
		} 		
		return count;
	}

	@Override
	public Map<String, Object> pickup(int accountId) throws ApplicationException {
		List<Plane> planes = Collections.emptyList();
		List<Chain> chains = Collections.emptyList();
		int[] counts = ServiceUtils.pickupCount();
		//
		String error = null;
		AccountStatLeft accountStatLeft = null;
		ApplicationException ae = null;
		try {
			EntityManagerHelper.beginTransaction();
			if(DaoUtils.accountStatDao.verifyPickup(accountId)){
				if (counts[0] > 0) {
					planes = DaoUtils.planeDao.pickup(accountId, counts[0]);
				}
				if (counts[1] > 0) {
					chains = DaoUtils.chainDao.pickup(accountId, counts[1]);
				}
			}
			else{
				error = "limit";
			}
			accountStatLeft = DaoUtils.accountStatDao.getSendAndPickupLeftCounts(accountId);
			EntityManagerHelper.commit();
		} catch (Throwable t) {
			if(t.getCause() == null){
				ae = new ApplicationException();
			}
			else{
				ae = new ApplicationException(t.getCause().getMessage());
			}
		}
		finally {
			EntityManagerHelper.closeEntityManager();
		}
		if (ae != null) {
			throw ae;
		} 
		Map<String, Object> result = new TreeMap<String, Object>();
		if(error != null){
		    result.put("error", error);
		}
		else{
			result.put("planes", planes);
			result.put("chains", chains);
		}
		result.put("accountStatLeft", accountStatLeft);
		return result;
	}

}
