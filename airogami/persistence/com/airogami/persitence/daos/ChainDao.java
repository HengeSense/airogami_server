package com.airogami.persitence.daos;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;

import com.airogami.application.exception.ApplicationException;
import com.airogami.common.constants.ChainConstants;
import com.airogami.common.constants.ChainMessageConstants;
import com.airogami.common.constants.MessageConstants;
import com.airogami.common.constants.PlaneConstants;
import com.airogami.persitence.entities.ChainDAO;
import com.airogami.persitence.entities.ChainMessage;
import com.airogami.persitence.entities.ChainMessageId;
import com.airogami.persitence.entities.EntityManagerHelper;
import com.airogami.persitence.entities.Chain;
import com.airogami.persitence.entities.Plane;

public class ChainDao extends ChainDAO {
	private final String throwChainJPQL = "delete from ChainMessage chainMessage where chainMessage.account.accountId = ?2 and chainMessage.status = ?3 and chainMessage.chain.chainId = ?1";
	private final String throwChainSetJPQL = "update Chain chain set chain.status = ?2 where chain.chainId = ?1";

	public boolean throwChain(long chainId, long accountId){
		EntityManagerHelper.log("throwChaining with chainId = " + chainId + " and accountId = " + accountId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					throwChainJPQL);
			query.setParameter(1, chainId);
			query.setParameter(2, accountId);
			query.setParameter(3, ChainMessageConstants.StatusNew);
			int count = query.executeUpdate();
			if(count == 1){
				query = EntityManagerHelper.getEntityManager().createQuery(
						throwChainSetJPQL);
				query.setParameter(1, chainId);
				query.setParameter(2, ChainConstants.StatusUnmatched);
				query.executeUpdate();
			}
			EntityManagerHelper
					.log("throwChain successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("throwChain failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String getChainMatchCountJPQL = "select chain.matchCount from Chain chain where chain.chainId = ?1";

	public int getChainMatchCount(long chainId){
		EntityManagerHelper.log("getChainMatchCounting with chainId = " + chainId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					getChainMatchCountJPQL);
			query.setParameter(1, chainId);
			int count = (Integer)query.getSingleResult();
			EntityManagerHelper
					.log("getChainMatchCount successful", Level.INFO, null);
			return count;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getChainMatchCount failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String canChainMatchJPQL = "select count(chain.chainId) from Chain chain where chain.chainId = ?1 and chain.matchCount < chain.maxMatchCount and chain.passCount < chain.maxPassCount";

	//plane can be matched again
	public boolean canChainMatch(long chainId){
		EntityManagerHelper.log("canChainMatching with planeId = " + chainId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					canChainMatchJPQL);
			query.setParameter(1, chainId);
			long count = (Long)query.getSingleResult();
			EntityManagerHelper
					.log("canChainMatch successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("canChainMatch failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String deleteChainJPQL = "update ChainMessage chainMessage set chainMessage.status = ?3 where chainMessage.chain.chainId = ?1 and chainMessage.status = ?4 and chainMessage.account.accountId = ?2";

	public boolean deleteChain(long chainId, long accountId){
		EntityManagerHelper.log("deleteChaining with chainId = " + chainId + " and accountId = " + accountId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					deleteChainJPQL);
			query.setParameter(1, chainId);
			query.setParameter(2, accountId);
			query.setParameter(3, ChainMessageConstants.StatusDeleted);
			query.setParameter(4, ChainMessageConstants.StatusReplied);
			int count = query.executeUpdate();
			EntityManagerHelper
					.log("deleteChain successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("deleteChain failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String obtainChainsForwardJPQL = "select chain from Chain chain , ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and (chain.account.accountId <> ?1 or chain.passCount > 0) and chainMessage.status = ?2 and ((?3 is null or chain.updatedTime >= ?3) and (?4 is null or chain.updatedTime <= ?4)) order by chain.updatedTime asc";

	private final String obtainChainsBackwardJPQL = "select chain from Chain chain , ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and (chain.account.accountId <> ?1 or chain.passCount > 0) and chainMessage.status = ?2 and ((?3 is null or chain.updatedTime >= ?3) and (?4 is null or chain.updatedTime <= ?4)) order by chain.updatedTime desc";

	public List<Chain> obtainChains(long accountId, int startIdx, Timestamp start, Timestamp end, int limit, boolean forward){
		EntityManagerHelper.log("obtainChainsing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = obtainChainsForwardJPQL;
			}
			else{
				jpql = obtainChainsBackwardJPQL;
			}
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					jpql);
			query.setParameter(1, accountId);
			query.setParameter(2, ChainMessageConstants.StatusReplied);
			query.setParameter(3, start);
			query.setParameter(4, end);
			if(startIdx > 0){
				query.setFirstResult(startIdx);
			}
			query.setMaxResults(limit);
			List<Chain> chains = query.getResultList();
			EntityManagerHelper
					.log("obtainChains successful", Level.INFO, null);
			return chains;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("obtainChains failed", Level.SEVERE, re);
			throw re;
		}
	}	
	
	private final String receiveChainsForwardJPQL = "select chain from Chain chain , ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and chainMessage.status = ?2 and ((?3 is null or chain.updatedTime >= ?3) and (?4 is null or chain.updatedTime <= ?4)) order by chain.updatedTime asc";

	private final String receiveChainsBackwardJPQL = "select chain from Chain chain , ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and chainMessage.status = ?2 and ((?3 is null or chain.updatedTime >= ?3) and (?4 is null or chain.updatedTime <= ?4)) order by chain.updatedTime desc";

	public List<Chain> receiveChains(long accountId, int startIdx, Timestamp start, Timestamp end, int limit, boolean forward){
		EntityManagerHelper.log("receiveChainsing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = receiveChainsForwardJPQL;
			}
			else{
				jpql = receiveChainsBackwardJPQL;
			}
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					jpql);
			query.setParameter(1, accountId);
			query.setParameter(2, ChainMessageConstants.StatusNew);
			query.setParameter(3, start);
			query.setParameter(4, end);
			if(startIdx > 0){
				query.setFirstResult(startIdx);
			}
			query.setMaxResults(limit);
			List<Chain> chains = query.getResultList();
			EntityManagerHelper
					.log("receiveChains successful", Level.INFO, null);
			return chains;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("receiveChains failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String pickupFindJPQL = "select chain.chainId from Chain chain, Account account where account.accountId = ?1 and (chain.sex = 0 or chain.sex = account.sex) and chain.status = ?2 and chain.passCount < chain.maxPassCount and chain.matchCount < chain.maxMatchCount and not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain.chainId = chain.chainId and account.accountId = chainMessage.account.accountId) and (chain.city is null or chain.city = '' or chain.city = account.city) and (chain.province is null or chain.province = '' or chain.province = account.province) and (chain.country is null or chain.country = '' or chain.country = account.country)";
	private final String matchJPQL = "update Chain chain set chain.status = ?2 where chain.chainId = ?1 and chain.status <> ?2 and chain.passCount < chain.maxPassCount and chain.matchCount < chain.maxMatchCount and not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain.chainId = chain.chainId and chainMessage.account.accountId = ?3)";
	private final String pickupQueryJPQL = "select chain from Chain chain join fetch chain.chainMessages join fetch chain.chainMessages.account where chain.chainId in (?1)";
		
	public List<Chain> pickup(long accountId, int count){
		EntityManagerHelper.log("pickuping chain with accountId = " + accountId + " and count = " + count, Level.INFO, null);
		try {
			List<Chain> chains = Collections.emptyList();
			//find
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					pickupFindJPQL);
			query.setParameter(1, accountId);
			query.setParameter(2, ChainConstants.StatusUnmatched);
			query.setMaxResults(count);
			List<Long> chainIds = query.getResultList();
			if(chainIds.size() > 0){
				//match
				Iterator<Long> iter = chainIds.iterator();
				List<Long> matchedChainIds = new ArrayList<Long>(chainIds.size());
				while(iter.hasNext()){
					Long chainId = iter.next();
					query = EntityManagerHelper.getEntityManager().createQuery(
							matchJPQL);
					query.setParameter(1, chainId);
					query.setParameter(2, ChainConstants.StatusMatched);
					query.setParameter(3, accountId);
					if(query.executeUpdate() == 1){
						ChainMessage chainMessage = new ChainMessage();
						chainMessage.setId(new ChainMessageId(chainId, accountId));
						chainMessage.setType(MessageConstants.MessageTypeText);
						//chainMessage.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
						DaoUtils.chainMessageDao.save(chainMessage);
						matchedChainIds.add(chainId);
					}
				}		
				
				if(matchedChainIds.size() > 0){
					this.flush();
					//query
					query = EntityManagerHelper.getEntityManager().createQuery(
							pickupQueryJPQL);
					query.setParameter(1, matchedChainIds);
					chains = query.getResultList();
				}
			}
			
			EntityManagerHelper
					.log("pickup chain successful", Level.INFO, null);
			return chains;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("pickup chain failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	// set chain.status = matched when chain.status != matched
	public boolean match(long chainId, long accountId) {
		EntityManagerHelper.log("matching chain with chainId = " + chainId + " and accountId = " + accountId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					matchJPQL);
			query.setParameter(1, chainId);
			query.setParameter(2, ChainConstants.StatusMatched);
			query.setParameter(3, accountId);
			int count = query.executeUpdate();
			if(count == 1){
				ChainMessage chainMessage = new ChainMessage();
				chainMessage.setId(new ChainMessageId(chainId, accountId));
				chainMessage.setType(MessageConstants.MessageTypeText);
				DaoUtils.chainMessageDao.save(chainMessage);
				this.flush();
			}
			
			EntityManagerHelper
					.log("match chain successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("match chain failed", Level.SEVERE, re);
			throw re;
		}
	} 
	
}
