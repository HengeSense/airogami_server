package com.airogami.persitence.daos;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;

import com.airogami.common.constants.ChainMessageConstants;
import com.airogami.persitence.entities.ChainMessage;
import com.airogami.persitence.entities.ChainMessageDAO;
import com.airogami.persitence.entities.EntityManagerHelper;

public class ChainMessageDao extends ChainMessageDAO {
	private final String replyChainMessageJPQL = "update ChainMessage chainMessage set chainMessage.content = ?5, chainMessage.createdTime = ?8, chainMessage.status = ?7, chainMessage.type = ?6 where (chainMessage.status = ?3 or chainMessage.status = ?4) and chainMessage.chain.chainId = ?1 and chainMessage.account.accountId = ?2";

	public boolean replyChainMessage(long accountId, long chainId, String content, int type){
		EntityManagerHelper.log("replyChainMessaging", Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					replyChainMessageJPQL);
			query.setParameter(1, chainId);
			query.setParameter(2, accountId);
			query.setParameter(3, ChainMessageConstants.StatusRead);
			query.setParameter(4, ChainMessageConstants.StatusUnread);
			query.setParameter(5, content);
			query.setParameter(6, type);
			query.setParameter(7, ChainMessageConstants.StatusReplied);
			query.setParameter(8, new Timestamp(System.currentTimeMillis()));
			int count = query.executeUpdate();
			EntityManagerHelper
					.log("replyChainMessage successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("replyChainMessage failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String obtainChainMessagesJPQL = "select chainMessage from ChainMessage chainMessage, ChainMessage cm where cm.chain.chainId = chainMessage.chain.chainId and cm.account.accountId = ?1 and cm.chain.chainId = ?2 and (chainMessage.account.accountId = ?1 or chainMessage.status >= ?3) order by chainMessage.createdTime desc";

	public List<ChainMessage> obtainChainMessages(long accountId, long chainId){
		EntityManagerHelper.log("obtainChainMessagesing with accountId = " + accountId + "and chainId = " + chainId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					obtainChainMessagesJPQL);
			query.setParameter(1, accountId);
			query.setParameter(2, chainId);
			query.setParameter(3, ChainMessageConstants.StatusReplied);
			List<ChainMessage> chainMessages = query.getResultList();
			EntityManagerHelper
					.log("obtainChainMessages successful", Level.INFO, null);
			return chainMessages;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("obtainChainMessages failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String viewedChainMessageJPQL = "update ChainMessage chainMessage set chainMessage.lastViewedTime = ?3 where chainMessage.lastViewedTime < ?3 and chainMessage.chain.chainId = ?1 and chainMessage.account.accountId = ?2";

	public boolean viewedChainMessage(long accountId, long chainId, Timestamp lastTimestamp){
		EntityManagerHelper.log("viewedChainMessaging with chainId = " + chainId, Level.INFO, null);		
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					viewedChainMessageJPQL);
			query.setParameter(1, chainId);
			query.setParameter(2, accountId);
			query.setParameter(3, lastTimestamp);
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
