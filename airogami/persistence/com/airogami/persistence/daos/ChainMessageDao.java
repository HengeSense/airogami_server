package com.airogami.persistence.daos;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.airogami.common.constants.ChainConstants;
import com.airogami.common.constants.ChainMessageConstants;
import com.airogami.persistence.entities.ChainMessage;
import com.airogami.persistence.entities.ChainMessageDAO;
import com.airogami.persistence.entities.EntityManagerHelper;

public class ChainMessageDao extends ChainMessageDAO {
	//CURRENT_TIMESTAMP
	private final String replyChainMessageJPQL = "update ChainMessage chainMessage set chainMessage.content = ?4, chainMessage.createdTime = ?7, chainMessage.lastViewedTime = ?7, chainMessage.status = ?6, chainMessage.type = ?5 where chainMessage.status = ?3 and chainMessage.chain.chainId = ?1 and chainMessage.account.accountId = ?2";
	private final String replyChainResetMatchCountJPQL = "update Chain chain set chain.matchCount = 0, chain.status = ?2, chain.passCount = chain.passCount + 1, chain.updatedTime = ?3 where chain.chainId = ?1";

	// database time
	public boolean replyChainMessage(int accountId, long chainId, String content, int type){
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

	public List<ChainMessage> obtainChainMessages(int accountId, long chainId, Timestamp last, int limit){
		EntityManagerHelper.log("obtainChainMessagesing with accountId = " + accountId + " and chainId = " + chainId, Level.INFO, null);
		try {
			TypedQuery<ChainMessage> query = EntityManagerHelper.getEntityManager().createQuery(
					obtainChainMessagesJPQL, ChainMessage.class);
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
	private final String viewedChainMessageJPQL = "update ChainMessage chainMessage set chainMessage.lastTime = chainMessage.lastViewedTime, chainMessage.lastViewedTime = ?3 where chainMessage.lastViewedTime < ?3 and chainMessage.chain.chainId = ?1 and chainMessage.account.accountId = ?2 and chainMessage.status = ?4";

	//stupid jpql
	//private final String viewedChainMessageUpdateJPQL = "update AccountStat accountStat set accountStat.chainMsgCount = accountStat.chainMsgCount - (select count(chainMessage) from ChainMessage chainMessage, ChainMessage cm where chainMessage.createdTime > cm.lastTime and chainMessage.createdTime <= cm.lastViewedTime and chainMessage.account.accountId <> ?2 and cm.chain.chainId = ?1 and cm.account.accountId = ?2) where accountStat.accountId = ?2";

	private final String viewedChainMessageUpdateSQL = "update ACCOUNT_STAT set CHAIN_MSG_COUNT = CHAIN_MSG_COUNT - (select count(*) from CHAIN_MESSAGE a, CHAIN_MESSAGE cm where a.STATUS >= ?3 and a.CREATED_TIME <= cm.LAST_VIEWED_TIME and a.CREATED_TIME > cm.LAST_TIME and a.ACCOUNT_ID <> ?2 and cm.CHAIN_ID = ?1 and cm.ACCOUNT_ID = ?2) where ACCOUNT_ID = ?2";

	private final String viewedChainMessageQueryJPQL = "select chainMessage.lastViewedTime from ChainMessage chainMessage where chainMessage.chain.chainId = ?1 and chainMessage.account.accountId = ?2 and chainMessage.status = ?3";

	
	public Object[] viewedChainMessage(int accountId, long chainId, Timestamp lastViewedTime){
		EntityManagerHelper.log("viewedChainMessaging with chainId = " + chainId, Level.INFO, null);		
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					viewedChainMessageJPQL);
			query.setParameter(1, chainId);
			query.setParameter(2, accountId);
			query.setParameter(3, lastViewedTime);
			query.setParameter(4, ChainMessageConstants.StatusReplied);
			int count = query.executeUpdate();
			if(count > 0){
				query = EntityManagerHelper.getEntityManager().createNativeQuery(
						viewedChainMessageUpdateSQL);
				query.setParameter(1, chainId);
				query.setParameter(2, accountId);
				query.setParameter(3, ChainMessageConstants.StatusReplied);
				query.executeUpdate();
			}
			else{
				lastViewedTime = null;
				query = EntityManagerHelper.getEntityManager().createQuery(
						viewedChainMessageQueryJPQL, Timestamp.class);
				query.setParameter(1, chainId);
				query.setParameter(2, accountId);
				query.setParameter(3, ChainMessageConstants.StatusReplied);
				List<Timestamp> result = query.getResultList();
				if(result.size() > 0){
					lastViewedTime = result.get(0);
				}
			}
			Object[] results = new Object[]{count == 1, lastViewedTime};
			EntityManagerHelper
					.log("viewedChainMessage successful", Level.INFO, null);
			return results;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("viewedChainMessage failed", Level.SEVERE, re);
			throw re;
		}
		
	}
	
	// jpql stupid
	//private final String decreaseChainMessageCountJPQL = "update AccountStat accountStat set accountStat.chainMsgCount = accountStat.chainMsgCount - (select count(chainMessage) from ChainMessage chainMessage, ChainMessage cm where chainMessage.createdTime > cm.lastViewedTime and chainMessage.account.accountId <> ?2 and cm.chain.chainId = ?1 and cm.account.accountId = ?2) where accountStat.accountId = ?2";

	private final String decreaseChainMessageCountSQL = "update ACCOUNT_STAT set CHAIN_MSG_COUNT = CHAIN_MSG_COUNT - (select count(*) from CHAIN_MESSAGE a, CHAIN_MESSAGE cm where a.STATUS >= ?3 and a.CREATED_TIME > cm.LAST_VIEWED_TIME and a.ACCOUNT_ID <> ?2 and cm.CHAIN_ID = ?1 and cm.ACCOUNT_ID = ?2) where ACCOUNT_ID = ?2";

	public boolean decreaseChainMessageCount(long chainId, int accountId ){
		EntityManagerHelper.log("decreaseChainMessageCounting with chainId = " + chainId, Level.INFO, null);		
		try {
			Query query = EntityManagerHelper.getEntityManager().createNativeQuery(
					decreaseChainMessageCountSQL);
			query.setParameter(1, chainId);
			query.setParameter(2, accountId);
			query.setParameter(3, ChainMessageConstants.StatusReplied);
			int count = query.executeUpdate();
			EntityManagerHelper
					.log("decreaseChainMessageCount successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("decreaseChainMessageCount failed", Level.SEVERE, re);
			throw re;
		}
		
	}
	
private final String getChainMessageJPQL = "select chainMessage from ChainMessage chainMessage where chainMessage.chain.chainId = ?1 and chainMessage.account.accountId = ?2";
	
	public ChainMessage getChainMessage(int accountId, long chainId){
		EntityManagerHelper.log("getChainMessageing", Level.INFO, null);
		try {
			TypedQuery<ChainMessage> query = EntityManagerHelper.getEntityManager().createQuery(
					getChainMessageJPQL, ChainMessage.class);
			query.setParameter(1, chainId);
			query.setParameter(2, accountId);
			List<ChainMessage> chainMessages = query.getResultList();
			ChainMessage chainMessage = null;
			if(chainMessages.size() > 0){
				chainMessage = chainMessages.get(0);
			}
			EntityManagerHelper
					.log("getChainMessage successful", Level.INFO, null);
			return chainMessage;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getChainMessage failed", Level.SEVERE, re);
			throw re;
		}
	}
}
