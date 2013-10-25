package com.airogami.persistence.daos;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.airogami.common.constants.ChainMessageConstants;
import com.airogami.common.notification.NotifiedInfo;
import com.airogami.persistence.entities.ChainDAO;
import com.airogami.persistence.entities.EntityManagerHelper;

public class ChainAidedDao extends ChainDAO {
	private final String getNotifiedInfoJPQL = "select new com.airogami.common.notification.CMNotifiedInfo(chainMessage.id.accountId, accountStat.chainMsgCount + accountStat.msgCount) from ChainMessage chainMessage, AccountStat accountStat where accountStat.accountId = chainMessage.id.accountId and chainMessage.id.chainId = ?1 and chainMessage.status = ?2 and chainMessage.id.accountId <> ?3";
	
	//
	public List<NotifiedInfo> getNotifiedInfos(long chainId, int accountId) {
		EntityManagerHelper.log("getNotifiedInfosing with chainId = " + chainId, Level.INFO, null);
		try {
			TypedQuery<NotifiedInfo> query = EntityManagerHelper.getEntityManager().createQuery(
					getNotifiedInfoJPQL, NotifiedInfo.class);
			query.setParameter(1, chainId);
			query.setParameter(2, ChainMessageConstants.StatusReplied);
			query.setParameter(3, accountId);
			List<NotifiedInfo> notifiedInfos = query.getResultList();
			EntityManagerHelper
					.log("getNotifiedInfos successful", Level.INFO, null);
			return notifiedInfos;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getNotifiedInfos failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String increaseChainMessageCountJPQL = "update AccountStat accountStat set accountStat.chainMsgCount = accountStat.chainMsgCount + 1 where accountStat.account in (select chainMessage.account from ChainMessage chainMessage where chainMessage.id.chainId = ?1 and chainMessage.status = ?2 and chainMessage.id.accountId <> ?3)";
	
	//return increased lines
		public int increaseChainMessageCount(long chainId, int accountId) {
			EntityManagerHelper.log("increaseChainMessageCounting with chainId = " + chainId, Level.INFO, null);
			try {
				Query query = EntityManagerHelper.getEntityManager().createQuery(
						increaseChainMessageCountJPQL);
				query.setParameter(1, chainId);
				query.setParameter(2, ChainMessageConstants.StatusReplied);
				query.setParameter(3, accountId);
				int count = query.executeUpdate();
				EntityManagerHelper
						.log("increaseChainMessageCount successful", Level.INFO, null);
				return count;
			} catch (RuntimeException re) {
				EntityManagerHelper.log("increaseChainMessageCount failed", Level.SEVERE, re);
				throw re;
			}
		}
}
