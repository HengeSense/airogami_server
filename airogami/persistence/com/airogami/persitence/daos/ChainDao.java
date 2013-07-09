package com.airogami.persitence.daos;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;

import com.airogami.application.exception.ApplicationException;
import com.airogami.common.constants.ChainMessageConstants;
import com.airogami.persitence.entities.ChainDAO;
import com.airogami.persitence.entities.EntityManagerHelper;
import com.airogami.persitence.entities.Chain;

public class ChainDao extends ChainDAO {
	private final String throwChainJPQL = "delete from ChainMessage chainMessage where chainMessage.account.accountId = ?2 and (chainMessage.status = ?3 or chainMessage.status = ?4) and chainMessage.chain.chainId = ?1";

	public boolean throwChain(long chainId, long accountId){
		EntityManagerHelper.log("throwChaining with chainId = " + chainId + " and accountId = " + accountId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					throwChainJPQL);
			query.setParameter(1, chainId);
			query.setParameter(2, accountId);
			query.setParameter(3, ChainMessageConstants.StatusRead);
			query.setParameter(4, ChainMessageConstants.StatusUnread);
			int count = query.executeUpdate();
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
	
	private final String obtainChainsForwardJPQL = "select chain from Chain chain , ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and (chain.account.accountId <> ?1 or chain.account.accountId = ?1 and chain.passCount > 0) and chainMessage.status <> ?2 and chain.createdTime > ?3 order by chain.createdTime desc";

	private final String obtainChainsBackwardJPQL = "select chain from Chain chain , ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and (chain.account.accountId <> ?1 or chain.account.accountId = ?1 and chain.passCount > 0) and chainMessage.status <> ?2 and chain.createdTime < ?3 order by chain.createdTime desc";

	public List<Chain> obtainChains(long accountId, String start, int limit, boolean forward){
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
			query.setParameter(2, ChainMessageConstants.StatusDeleted);
			query.setParameter(3, Timestamp.valueOf(start));
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
	
}
