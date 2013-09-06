package com.airogami.persistence.daos;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;

import com.airogami.common.constants.ChainConstants;
import com.airogami.common.constants.ChainMessageConstants;
import com.airogami.persistence.entities.ChainMessage;
import com.airogami.persistence.entities.ChainMessageDAO;
import com.airogami.persistence.entities.EntityManagerHelper;

public class ChainMessageDao extends ChainMessageDAO {
	//CURRENT_TIMESTAMP
	private final String replyChainMessageJPQL = "update ChainMessage chainMessage set chainMessage.content = ?4, chainMessage.createdTime = ?7, chainMessage.lastViewedTime = ?7, chainMessage.status = ?6, chainMessage.type = ?5 where chainMessage.status = ?3 and chainMessage.chain.chainId = ?1 and chainMessage.account.accountId = ?2";
	private final String replyChainResetMatchCountJPQL = "update Chain chain set chain.matchCount = 0, chain.status = ?2, chain.updatedTime = ?3 where chain.chainId = ?1";

	// database time
	public boolean replyChainMessage(long accountId, long chainId, String content, int type){
		EntityManagerHelper.log("replyChainMessaging", Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					replyChainMessageJPQL);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			query.setParameter(1, chainId);
			query.setParameter(2, accountId);
			query.setParameter(3, ChainMessageConstants.StatusNew);
			query.setParameter(4, content);
			query.setParameter(5, type);
			query.setParameter(6, ChainMessageConstants.StatusReplied);
			query.setParameter(7, timestamp);
			int count = query.executeUpdate();
			if(count == 1){
				query = EntityManagerHelper.getEntityManager().createQuery(
						replyChainResetMatchCountJPQL);
				query.setParameter(1, chainId);
				query.setParameter(2, ChainConstants.StatusUnmatched);
				query.setParameter(3, timestamp);
				query.executeUpdate();
			}
			EntityManagerHelper
					.log("replyChainMessage successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("replyChainMessage failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	//no verify for whether replied here or deleted (verified in obtainChains)
	private final String obtainChainMessagesJPQL = "select chainMessage from ChainMessage chainMessage join fetch chainMessage.account join fetch chainMessage.account.profile , ChainMessage cm where  (?4 is null or chainMessage.createdTime > ?4)  and cm.chain.chainId = chainMessage.chain.chainId and cm.account.accountId = ?1 and cm.chain.chainId = ?2 and (chainMessage.account.accountId = ?1 or chainMessage.status >= ?3) order by chainMessage.createdTime asc";

	public List<ChainMessage> obtainChainMessages(long accountId, long chainId, Timestamp last, int limit){
		EntityManagerHelper.log("obtainChainMessagesing with accountId = " + accountId + "and chainId = " + chainId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					obtainChainMessagesJPQL);
			query.setParameter(1, accountId);
			query.setParameter(2, chainId);
			query.setParameter(3, ChainMessageConstants.StatusReplied);
			query.setParameter(4, last);
			if(limit > 0){
				query.setMaxResults(limit);
			}
			List<ChainMessage> chainMessages = query.getResultList();
			EntityManagerHelper
					.log("obtainChainMessages successful", Level.INFO, null);
			return chainMessages;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("obtainChainMessages failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	//verified for whether replied or deleted here
	private final String viewedChainMessageJPQL = "update ChainMessage chainMessage set chainMessage.lastViewedTime = ?3 where chainMessage.lastViewedTime < ?3 and chainMessage.chain.chainId = ?1 and chainMessage.account.accountId = ?2 and chainMessage.status = ?4";

	public boolean viewedChainMessage(long accountId, long chainId, Timestamp lastTimestamp){
		EntityManagerHelper.log("viewedChainMessaging with chainId = " + chainId, Level.INFO, null);		
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					viewedChainMessageJPQL);
			query.setParameter(1, chainId);
			query.setParameter(2, accountId);
			query.setParameter(3, lastTimestamp);
			query.setParameter(4, ChainMessageConstants.StatusReplied);
			int count = query.executeUpdate();
			EntityManagerHelper
					.log("viewedChainMessage successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("viewedChainMessage failed", Level.SEVERE, re);
			throw re;
		}
		
	}
}
