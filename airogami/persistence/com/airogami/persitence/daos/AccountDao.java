package com.airogami.persitence.daos;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import javax.persistence.Query;

import com.airogami.persitence.entities.Account;
import com.airogami.persitence.entities.AccountDAO;
import com.airogami.persitence.entities.EntityManagerHelper;

public class AccountDao extends AccountDAO {

	private Random random = new Random(System.currentTimeMillis());
	private final String[] randPlaneAccountJPQLs = {
			"select account.accountId from Account account where account.accountId >= (select ?1 * (1.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) - 1 from Account account where account.status = 0 and account <> ?2 and (0 = ?3 or account.sex = ?3)) and account.status = 0 and account <> ?2 and (0 = ?3 or account.sex = ?3)",
			"select account.accountId from Account account where account.accountId >= (select ?1 * (1.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) - 1 from Account account where account.status = 0 and account <> ?2 and (0 = ?4 or account.sex = ?4) and account.country = ?3) and account.status = 0 and account <> ?2 and (0 = ?4 or account.sex = ?4) and account.country = ?3",
			"select account.accountId from Account account where account.accountId >= (select ?1 * (1.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) - 1 from Account account where account.status = 0 and account <> ?2 and (0 = ?5 or account.sex = ?5) and account.country = ?3 and account.province = ?4) and account.status = 0 and account <> ?2 and (0 = ?5 or account.sex = ?5) and account.country = ?3 and account.province = ?4",
			"select account.accountId from Account account where account.accountId >= (select ?1 * (1.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) - 1 from Account account where account.status = 0 and account <> ?2 and (0 = ?6 or account.sex = ?6) and account.country = ?3 and account.province = ?4 and account.city = ?5) and account.status = 0 and account <> ?2 and (0 = ?6 or account.sex = ?6) and account.country = ?3 and account.province = ?4 and account.city = ?5"};

	// not equal (current) account
	public Long randPlaneAccount(Account account, Short sex, String ... conditions) {
		EntityManagerHelper.log("randPlaneAccounting", Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					randPlaneAccountJPQLs[conditions.length]);
			query.setParameter(1, random.nextDouble());
			query.setParameter(2, account);
			for(int i=0;i<conditions.length;++i){
				query.setParameter(i + 3, conditions[i]);
			}
			query.setParameter(3 + conditions.length, sex);
			query.setMaxResults(1);
			Long result = null;
			Iterator<Long> iter = query.getResultList().iterator();
			if(iter.hasNext()){
				result = iter.next();
			}
			EntityManagerHelper.log("randPlaneAccount successful",
					Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("randPlaneAccount failed", Level.SEVERE,
					re);
			throw re;
		}		
	}
	
	//consider whether the account is blocked
	private final String[] randChainAccountJPQLs = {
			"select account.accountId from Account account, Chain chain where chain.chainId = ?1 and (chain.sex = 0 or chain.sex = account.sex) and account.accountId >= (select ?2 * (1.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) - 1 from Account account, Chain chain where account.status = 0 and chain.chainId = ?1 and (chain.sex = 0 or chain.sex = account.sex) and  not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account = account)) and account.status = 0 and  not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account = account)",
			"select account.accountId from Account account, Chain chain where chain.chainId = ?1 and (chain.sex = 0 or chain.sex = account.sex) and account.accountId >= (select ?2 * (1.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) - 1 from Account account, Chain chain where account.status = 0 and chain.chainId = ?1 and (chain.sex = 0 or chain.sex = account.sex) and  not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account = account) and account.country = ?3) and account.status = 0 and  not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account = account) and account.country = ?3",
			"select account.accountId from Account account, Chain chain where chain.chainId = ?1 and (chain.sex = 0 or chain.sex = account.sex) and account.accountId >= (select ?2 * (1.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) - 1 from Account account, Chain chain where account.status = 0 and chain.chainId = ?1 and (chain.sex = 0 or chain.sex = account.sex) and  not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account = account) and account.country = ?3 and account.province = ?4) and account.status = 0 and  not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account = account) and account.country = ?3  and account.province = ?4",
			"select account.accountId from Account account, Chain chain where chain.chainId = ?1 and (chain.sex = 0 or chain.sex = account.sex) and account.accountId >= (select ?2 * (1.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) - 1 from Account account, Chain chain where account.status = 0 and chain.chainId = ?1 and (chain.sex = 0 or chain.sex = account.sex) and  not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account = account) and account.country = ?3 and account.province = ?4 and account.city = ?5) and account.status = 0 and  not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account = account) and account.country = ?3 and account.province = ?4 and account.city = ?5"
	};
	public Long randChainAccount(long chainId, String ... conditions) {
		EntityManagerHelper.log("randChainAccounting", Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					randChainAccountJPQLs[conditions.length]);
			query.setParameter(1, chainId);
			query.setParameter(2, random.nextDouble());
			
			for(int i=0;i<conditions.length;++i){
				query.setParameter(i + 3, conditions[i]);
			}
			query.setMaxResults(1);
			Long result = null;
			Iterator<Long> iter = query.getResultList().iterator();
			if(iter.hasNext()){
				result = iter.next();
			}
			EntityManagerHelper.log("randChainAccount successful",
					Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("randChainAccount failed", Level.SEVERE,
					re);
			throw re;
		}		
	}
	
	
	private final String changePasswordJPQL = "update Authenticate authenticate set authenticate.password = ?3 where authenticate.accountId = ?1 and authenticate.password = ?2";
	
	public boolean changePassword(long accountId, String oldPassword, String newPassword){
		EntityManagerHelper.log("changePasswording", Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					changePasswordJPQL);
			query.setParameter(1, accountId);
			query.setParameter(2, oldPassword);
			query.setParameter(3, newPassword);
			int count = query.executeUpdate();
			EntityManagerHelper.log("changePassword successful",
					Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("changePassword failed", Level.SEVERE,
					re);
			throw re;
		}
	}
	
    private final String changeScreenNameJPQL = "update Authenticate authenticate set authenticate.screenName = ?2 where authenticate.accountId = ?1";
	
    private final String changeScreenNameSetJPQL = "update Account account set account.screenName = ?2, account.updatedTime = ?3 where account.accountId = ?1";
	
	public boolean changeScreenName(long accountId, String screenName){
		EntityManagerHelper.log("changeScreenNameing", Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					changeScreenNameJPQL);
			query.setParameter(1, accountId);
			query.setParameter(2, screenName);
			int count = query.executeUpdate();
			if(count == 1){
				query = EntityManagerHelper.getEntityManager().createQuery(
						changeScreenNameSetJPQL);
				query.setParameter(1, accountId);
				query.setParameter(2, screenName);
				query.setParameter(3, new Timestamp(System.currentTimeMillis()));
				query.executeUpdate();
			}
			EntityManagerHelper.log("changeScreenName successful",
					Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("changeScreenName failed", Level.SEVERE,
					re);
			throw re;
		}
	}
	
    private final String obtainAccountJPQL = "select account from Account account where account.accountId = ?1 and account.updatedTime > ?2";
	
	public Account obtainAccount(long accountId, Timestamp last){
		EntityManagerHelper.log("obtainAccounting", Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					obtainAccountJPQL);
			query.setParameter(1, accountId);
			query.setParameter(2, last);
			List<Account> result = query.getResultList();
			Account account = null;
			if(result.size() > 0){
				account = result.get(0);
			}
			EntityManagerHelper.log("obtainAccount successful",
					Level.INFO, null);
			return account;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("obtainAccount failed", Level.SEVERE,
					re);
			throw re;
		}
	}

}
