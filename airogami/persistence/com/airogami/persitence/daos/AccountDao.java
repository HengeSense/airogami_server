package com.airogami.persitence.daos;

import java.util.logging.Level;

import javax.persistence.Query;

import com.airogami.persitence.entities.Account;
import com.airogami.persitence.entities.AccountDAO;
import com.airogami.persitence.entities.EntityManagerHelper;

public class AccountDao extends AccountDAO {

	private final String[] randPlaneAccountJPQLs = {
			"select account.accountId from Account account where account.accountId >= (select ?1 * (0.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) from Account account where account.status = 0 and account <> ?2) and account.status = 0 and account <> ?2",
			"select account.accountId from Account account where account.accountId >= (select ?1 * (0.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) from Account account where account.status = 0 and account <> ?2 and account.country = ?3) and account.status = 0 and account <> ?2 and account.country = ?3",
			"select account.accountId from Account account where account.accountId >= (select ?1 * (0.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) from Account account where account.status = 0 and account <> ?2 and account.country = ?3 and account.province = ?4) and account.status = 0 and account <> ?2 and account.country = ?3 and account.province = ?4",
			"select account.accountId from Account account where account.accountId >= (select ?1 * (0.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) from Account account where account.status = 0 and account <> ?2 and account.country = ?3 and account.province = ?4 and account.city = ?5) and account.status = 0 and account <> ?2 and account.country = ?3 and account.province = ?4 and account.city = ?5"};

	public long randPlaneAccount(Account account, String ... conditions) {
		EntityManagerHelper.log("randPlaneAccounting", Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					randPlaneAccountJPQLs[conditions.length]);
			query.setParameter(1, Math.random());
			query.setParameter(2, account);
			for(int i=0;i<conditions.length;++i){
				query.setParameter(i + 3, conditions[i]);
			}
			query.setMaxResults(1);
			long accountId = (Long) query.getSingleResult();
			EntityManagerHelper.log("randPlaneAccount successful",
					Level.INFO, null);
			return accountId;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("randPlaneAccount failed", Level.SEVERE,
					re);
			throw re;
		}		
	}
	
	private final String[] randChainAccountJPQLs = {
			"select account.accountId from Account account where account.accountId >= (select ?2 * (0.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) from Account account where account.status = 0 and account.accountId not in (select chainMessage.account.accountId from ChainMessage chainMessage where chainMessage.chain.chainId = ?1)) and account.status = 0 and account.accountId not in (select chainMessage.account.accountId from ChainMessage chainMessage where chainMessage.chain.chainId = ?1)",
			"select account.accountId from Account account where account.accountId >= (select ?1 * (0.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) from Account account where account.status = 0 and account <> ?2 and account.country = ?3) and account.status = 0 and account <> ?2 and account.country = ?3",
			"select account.accountId from Account account where account.accountId >= (select ?1 * (0.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) from Account account where account.status = 0 and account <> ?2 and account.country = ?3 and account.province = ?4) and account.status = 0 and account <> ?2 and account.country = ?3 and account.province = ?4",
			"select account.accountId from Account account where account.accountId >= (select ?1 * (0.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) from Account account where account.status = 0 and account <> ?2 and account.country = ?3 and account.province = ?4 and account.city = ?5) and account.status = 0 and account <> ?2 and account.country = ?3 and account.province = ?4 and account.city = ?5"};

	public long randChainAccount(long chainId, String ... conditions) {
		EntityManagerHelper.log("randPlaneAccounting", Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					randChainAccountJPQLs[conditions.length]);
			query.setParameter(1, chainId);
			query.setParameter(2, Math.random());
			
			for(int i=0;i<conditions.length;++i){
				query.setParameter(i + 3, conditions[i]);
			}
			query.setMaxResults(1);
			long accountId = (Long) query.getSingleResult();
			EntityManagerHelper.log("randPlaneAccount successful",
					Level.INFO, null);
			return accountId;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("randPlaneAccount failed", Level.SEVERE,
					re);
			throw re;
		}		
	}

}
