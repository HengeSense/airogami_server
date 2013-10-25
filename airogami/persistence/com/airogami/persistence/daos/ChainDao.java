package com.airogami.persistence.daos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.airogami.common.constants.ChainConstants;
import com.airogami.common.constants.ChainMessageConstants;
import com.airogami.common.constants.MessageConstants;
import com.airogami.common.constants.PlaneConstants;
import com.airogami.common.notification.CMNotifiedInfo;
import com.airogami.common.notification.NotifiedInfo;
import com.airogami.persistence.classes.NeoChain;
import com.airogami.persistence.classes.OldChain;
import com.airogami.persistence.entities.AccountSys;
import com.airogami.persistence.entities.Chain;
import com.airogami.persistence.entities.ChainDAO;
import com.airogami.persistence.entities.ChainMessage;
import com.airogami.persistence.entities.ChainMessageId;
import com.airogami.persistence.entities.EntityManagerHelper;

public class ChainDao extends ChainAidedDao {
	
	private final String throwChainJPQL = "delete from ChainMessage chainMessage where chainMessage.account.accountId = ?2 and chainMessage.status = ?3 and chainMessage.chain.chainId = ?1";
	private final String throwChainSetJPQL = "update Chain chain set chain.status = ?2 where chain.chainId = ?1";

	public boolean throwChain(long chainId, int accountId){
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
	
    //private final String updateIncSQL = "update CHAIN  set UPDATE_INC = (select max_value from (select max(UPDATE_INC) + 1 as max_value from CHAIN) as tmp) WHERE CHAIN_ID = ?";

    private final String updateChainIncJPQL = "update AccountSys accountSys set accountSys.chainInc = accountSys.chainInc + 1 where accountSys.account in (select chainMessage.account from ChainMessage chainMessage where chainMessage.id.chainId = ?1 and chainMessage.status = ?2 and chainMessage.id.accountId <> ?3)";
    
    private final String updateIncJPQL = "update ChainMessage chainMessage set chainMessage.updateInc = (select accountSys.chainInc from AccountSys accountSys where accountSys.account = chainMessage.account) where chainMessage.id.chainId = ?1 and chainMessage.status = ?2 and chainMessage.id.accountId <> ?3";
    
    //not include accountId
	public void updateInc(long chainId, int accountId) {
		EntityManagerHelper.log("updateIncing with chainId = " + chainId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					updateChainIncJPQL);
			query.setParameter(1, chainId);
			query.setParameter(2, ChainMessageConstants.StatusReplied);
			query.setParameter(3, accountId);
			if(query.executeUpdate() > 0){
				query = EntityManagerHelper.getEntityManager().createQuery(
						updateIncJPQL);
				query.setParameter(1, chainId);
				query.setParameter(2, ChainMessageConstants.StatusReplied);
				query.setParameter(3, accountId);
				query.executeUpdate();
			}
			EntityManagerHelper
					.log("updateInc successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("updateInc failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	private final String getChainMatchCountJPQL = "select chain.matchCount from Chain chain where chain.chainId = ?1";

	public int getChainMatchCount(long chainId){
		EntityManagerHelper.log("getChainMatchCounting with chainId = " + chainId, Level.INFO, null);
		try {
			TypedQuery<Integer> query = EntityManagerHelper.getEntityManager().createQuery(
					getChainMatchCountJPQL, Integer.class);
			query.setParameter(1, chainId);
			int count = query.getSingleResult();
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
			TypedQuery<Long> query = EntityManagerHelper.getEntityManager().createQuery(
					canChainMatchJPQL, Long.class);
			query.setParameter(1, chainId);
			long count = query.getSingleResult();
			EntityManagerHelper
					.log("canChainMatch successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("canChainMatch failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String deleteChainJPQL = "update ChainMessage chainMessage set chainMessage.status = ?3 where chainMessage.chain.chainId = ?1 and chainMessage.status = ?4 and chainMessage.account.accountId = ?2";

	public boolean deleteChain(long chainId, int accountId){
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
	
	private final String obtainChainsForwardJPQL = "select chain from Chain chain join fetch chain.account, ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and (chain.account.accountId <> ?1 or chain.passCount > 0) and chainMessage.status = ?2 and (?3 is null or chain.updateInc > ?3) and (?4 is null or chain.updateInc < ?4) order by chain.updateInc asc";

	private final String obtainChainsBackwardJPQL = "select chain from Chain chain join fetch chain.account, ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and (chain.account.accountId <> ?1 or chain.passCount > 0) and chainMessage.status = ?2 and (?3 is null or chain.updateInc > ?3) and (?4 is null or chain.updateInc < ?4) order by chain.updateInc desc";

	public List<Chain> obtainChains(int accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("obtainChainsing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = obtainChainsForwardJPQL;
			}
			else{
				jpql = obtainChainsBackwardJPQL;
			}
			TypedQuery<Chain> query = EntityManagerHelper.getEntityManager().createQuery(
					jpql, Chain.class);
			query.setParameter(1, accountId);
			query.setParameter(2, ChainMessageConstants.StatusReplied);
			query.setParameter(3, start);
			query.setParameter(4, end);
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
	
	private final String getNeoChainsForwardJPQL = "select new com.airogami.persistence.classes.NeoChain(chain.chainId, chainMessage.updateInc, chain.updateCount, chain.updatedTime) from Chain chain, ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and (chain.account.accountId <> ?1 or chain.passCount > 0) and chainMessage.status < ?2 and (?3 is null or chainMessage.updateInc > ?3) and (?4 is null or chainMessage.updateInc < ?4) order by chainMessage.updateInc asc";

	private final String getNeoChainsBackwardJPQL = "select new com.airogami.persistence.classes.NeoChain(chain.chainId, chainMessage.updateInc, chain.updateCount, chain.updatedTime) from Chain chain, ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and (chain.account.accountId <> ?1 or chain.passCount > 0) and chainMessage.status < ?2 and (?3 is null or chainMessage.updateInc > ?3) and (?4 is null or chainMessage.updateInc < ?4) order by chainMessage.updateInc desc";

	public List<NeoChain> getNeoChains(int accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("getNeoChainsing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = getNeoChainsForwardJPQL;
			}
			else{
				jpql = getNeoChainsBackwardJPQL;
			}
			TypedQuery<NeoChain> query = EntityManagerHelper.getEntityManager().createQuery(
					jpql, NeoChain.class);
			query.setParameter(1, accountId);
			query.setParameter(2, ChainMessageConstants.StatusDeleted);
			query.setParameter(3, start);
			query.setParameter(4, end);
			query.setMaxResults(limit);
			List<NeoChain> chains = query.getResultList();
			EntityManagerHelper
					.log("getNeoChains successful", Level.INFO, null);
			return chains;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getNeoChains failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String getChainsJPQL = "select chain from Chain chain join fetch chain.account where chain.chainId in (?2) and exists (select chainMessage.id.accountId from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1)";

	private final String getUpdatedChainsJPQL = "select new com.airogami.persistence.classes.UpdatedChain(chain.chainId, chain.updateCount) from Chain chain where chain.chainId in (?2) and exists (select chainMessage.id.accountId from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1)";

	//return List<Chain> or List<UpdatedChain>
	public Object getChains(int accountId, List<Long> chainIds, boolean updated){
		EntityManagerHelper.log("getChainsing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(updated){
				jpql = getUpdatedChainsJPQL;
			}
			else{
				jpql = getChainsJPQL;
			}
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					jpql);
			query.setParameter(1, accountId);
			query.setParameter(2, chainIds);
			Object chains = query.getResultList();
			EntityManagerHelper
					.log("getChains successful", Level.INFO, null);
			return chains;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getChains failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String getOldChainsForwardJPQL = "select new com.airogami.persistence.classes.OldChain(chain.chainId, chainMessage.status, chainMessage.lastViewedTime) from Chain chain, ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and (chain.account.accountId <> ?1 or chain.passCount > 0) and chainMessage.status < ?2 and (?3 is null or chain.chainId > ?3) and (?4 is null or chain.chainId < ?4) order by chain.chainId asc";

	private final String getOldChainsBackwardJPQL = "select new com.airogami.persistence.classes.OldChain(chain.chainId, chainMessage.status, chainMessage.lastViewedTime) from Chain chain, ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and (chain.account.accountId <> ?1 or chain.passCount > 0) and chainMessage.status < ?2 and (?3 is null or chain.chainId > ?3) and (?4 is null or chain.chainId < ?4) order by chain.chainId desc";

	public List<OldChain> getOldChains(int accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("getOldChainsing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = getOldChainsForwardJPQL;
			}
			else{
				jpql = getOldChainsBackwardJPQL;
			}
			TypedQuery<OldChain> query = EntityManagerHelper.getEntityManager().createQuery(
					jpql, OldChain.class);
			query.setParameter(1, accountId);
			query.setParameter(2, ChainMessageConstants.StatusDeleted);
			query.setParameter(3, start);
			query.setParameter(4, end);
			query.setMaxResults(limit);
			List<OldChain> oldChains = query.getResultList();
			EntityManagerHelper
					.log("getOldChains successful", Level.INFO, null);
			return oldChains;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getOldChains failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String obtainChainIdsForwardJPQL = "select chain.chainId from Chain chain , ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and (chain.account.accountId <> ?1 or chain.passCount > 0) and chainMessage.status = ?2 and (?3 is null or chain.updateInc > ?3) and (?4 is null or chain.updateInc < ?4) order by chain.updateInc asc";

	private final String obtainChainIdsBackwardJPQL = "select chain.chainId from Chain chain , ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and (chain.account.accountId <> ?1 or chain.passCount > 0) and chainMessage.status = ?2 and (?3 is null or chain.updateInc > ?3) and (?4 is null or chain.updateInc < ?4) order by chain.updateInc desc";

	public List<Long> obtainChainIds(int accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("obtainChainIdsing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = obtainChainIdsForwardJPQL;
			}
			else{
				jpql = obtainChainIdsBackwardJPQL;
			}
			TypedQuery<Long> query = EntityManagerHelper.getEntityManager().createQuery(
					jpql, Long.class);
			query.setParameter(1, accountId);
			query.setParameter(2, ChainMessageConstants.StatusReplied);
			query.setParameter(3, start);
			query.setParameter(4, end);
			query.setMaxResults(limit);
			List<Long> chainIds = query.getResultList();
			EntityManagerHelper
					.log("obtainChainIds successful", Level.INFO, null);
			return chainIds;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("obtainChainIds failed", Level.SEVERE, re);
			throw re;
		}
	}	
	
	private final String obtainChainIdsJPQL = "select chain.chainId from Chain chain, ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and (chain.account.accountId <> ?1 or chain.passCount > 0) and chainMessage.status = ?3 and chain.chainId > ?2 order by chain.chainId asc";

	public List<Long> obtainChainIds(int accountId, long startId, int limit){
		EntityManagerHelper.log("obtainChainIdsing with accountId = " + accountId, Level.INFO, null);
		try {
			TypedQuery<Long> query = EntityManagerHelper.getEntityManager().createQuery(
					obtainChainIdsJPQL, Long.class);
			query.setParameter(1, accountId);
			query.setParameter(2, startId);
			query.setParameter(3, ChainMessageConstants.StatusReplied);
			query.setMaxResults(limit);
			List<Long> chainIds = query.getResultList();
			EntityManagerHelper
					.log("obtainChainIds successful", Level.INFO, null);
			return chainIds;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("obtainChainIds failed", Level.SEVERE, re);
			throw re;
		}
	}	
	
	private final String receiveChainsForwardJPQL = "select chain from Chain chain join fetch chain.account join fetch chain.account.profile, ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and chainMessage.status = ?2 and (?3 is null or chain.updateInc > ?3) and (?4 is null or chain.updateInc < ?4) order by chain.updateInc asc";

	private final String receiveChainsBackwardJPQL = "select chain from Chain chain join fetch chain.account join fetch chain.account.profile, ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and chainMessage.status = ?2 and (?3 is null or chain.updateInc > ?3) and (?4 is null or chain.updateInc < ?4) order by chain.updateInc desc";

	public List<Chain> receiveChains(int accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("receiveChainsing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = receiveChainsForwardJPQL;
			}
			else{
				jpql = receiveChainsBackwardJPQL;
			}
			TypedQuery<Chain> query = EntityManagerHelper.getEntityManager().createQuery(
					jpql, Chain.class);
			query.setParameter(1, accountId);
			query.setParameter(2, ChainMessageConstants.StatusNew);
			query.setParameter(3, start);
			query.setParameter(4, end);
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
	
	private final String receiveChainIdsForwardJPQL = "select chain.chainId from Chain chain, ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and chainMessage.status = ?2 and (?3 is null or chain.updateInc > ?3) and (?4 is null or chain.updateInc < ?4) order by chain.updateInc asc";

	private final String receiveChainIdsBackwardJPQL = "select chain.chainId from Chain chain, ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and chainMessage.status = ?2 and (?3 is null or chain.updateInc > ?3) and (?4 is null or chain.updateInc < ?4) order by chain.updateInc desc";

	public List<Long> receiveChainIds(int accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("receiveChainIdsing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = receiveChainIdsForwardJPQL;
			}
			else{
				jpql = receiveChainIdsBackwardJPQL;
			}
			TypedQuery<Long> query = EntityManagerHelper.getEntityManager().createQuery(
					jpql, Long.class);
			query.setParameter(1, accountId);
			query.setParameter(2, ChainMessageConstants.StatusNew);
			query.setParameter(3, start);
			query.setParameter(4, end);
			query.setMaxResults(limit);
			List<Long> chainIds = query.getResultList();
			EntityManagerHelper
					.log("receiveChainIds successful", Level.INFO, null);
			return chainIds;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("receiveChainIds failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String receiveChainIdsJPQL = "select chain.chainId from Chain chain, ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = ?1 and chainMessage.status = ?3 and chain.chainId > ?2 order by chain.chainId asc";

	public List<Long> receiveChainIds(int accountId, long startId, int limit){
		EntityManagerHelper.log("receiveChainIdsing with accountId = " + accountId, Level.INFO, null);
		try {
			TypedQuery<Long> query = EntityManagerHelper.getEntityManager().createQuery(
					receiveChainIdsJPQL, Long.class);
			query.setParameter(1, accountId);
			query.setParameter(2, startId);
			query.setParameter(3, ChainMessageConstants.StatusNew);
			query.setMaxResults(limit);
			List<Long> chainIds = query.getResultList();
			EntityManagerHelper
					.log("receiveChainIds successful", Level.INFO, null);
			return chainIds;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("receiveChainIds failed", Level.SEVERE, re);
			throw re;
		}
	}
		
	
	private final String pickupFindJPQL = "select chain.chainId from Chain chain, Profile profile where profile.accountId = ?1 and (chain.sex = 0 or chain.sex = profile.sex) and chain.status = ?2 and chain.passCount < chain.maxPassCount and chain.matchCount < chain.maxMatchCount and not exists (select chainMessage from ChainMessage chainMessage where chainMessage.id.chainId = chain.chainId and profile.accountId = chainMessage.id.accountId) and not exists (select chainHist from ChainHist chainHist where chainHist.id.chainId = chain.chainId and profile.accountId = chainHist.id.accountId) and (chain.city is null or chain.city = '' or chain.city = profile.city) and (chain.province is null or chain.province = '' or chain.province = profile.province) and (chain.country is null or chain.country = '' or chain.country = profile.country)  and (chain.birthdayLower is null or chain.birthdayLower <= profile.birthday) and (chain.birthdayUpper is null or chain.birthdayUpper >= profile.birthday) and (chain.language is null or chain.language = '' or profile.language = chain.language)";
	private final String matchJPQL = "update Chain chain set chain.status = ?2 where chain.chainId = ?1 and chain.status <> ?2 and chain.passCount < chain.maxPassCount and chain.matchCount < chain.maxMatchCount and not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain.chainId = chain.chainId and chainMessage.account.accountId = ?3) and not exists (select chainHist from ChainHist chainHist where chainHist.id.chainId = chain.chainId and ?3 = chainHist.id.accountId)";
	private final String pickupQueryJPQL = "select chain from Chain chain join fetch chain.account join fetch chain.account.profile where chain.chainId in (?1)";
		
	public List<Chain> pickup(int accountId, int count){
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
				AccountSys accountSys = EntityManagerHelper.getEntityManager().find(AccountSys.class, accountId, LockModeType.PESSIMISTIC_WRITE);
				if(accountSys != null){
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
							accountSys.setChainInc(accountSys.getChainInc() + 1);
							chainMessage.setUpdateInc(accountSys.getChainInc());
							chainMessage.setType(MessageConstants.MessageTypeText);
							chainMessage.setSource((short) PlaneConstants.SourcePickup);
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
	public boolean match(long chainId, int accountId) {
		EntityManagerHelper.log("matching chain with chainId = " + chainId + " and accountId = " + accountId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					matchJPQL);
			query.setParameter(1, chainId);
			query.setParameter(2, ChainConstants.StatusMatched);
			query.setParameter(3, accountId);
			int count = query.executeUpdate();
			if(count == 1){
				AccountSys accountSys = EntityManagerHelper.getEntityManager().find(AccountSys.class, accountId, LockModeType.PESSIMISTIC_WRITE);
                if(accountSys != null){
                	ChainMessage chainMessage = new ChainMessage();
    				chainMessage.setId(new ChainMessageId(chainId, accountId));
    				accountSys.setChainInc(accountSys.getChainInc() + 1);
    				chainMessage.setUpdateInc(accountSys.getChainInc());
    				chainMessage.setType(MessageConstants.MessageTypeText);
    				chainMessage.setSource((short) PlaneConstants.SourceReceive);
    				DaoUtils.chainMessageDao.save(chainMessage);
    				this.flush();
                }
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
