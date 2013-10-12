package com.airogami.persistence.daos;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.airogami.common.constants.MessageConstants;
import com.airogami.common.constants.PlaneConstants;
import com.airogami.persistence.entities.EntityManagerHelper;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.MessageDAO;

public class MessageDao extends MessageDAO {
	private final String obtainMessagesForwardJPQL = "select message from Message message join fetch message.account where message.plane.planeId = ?2 and ((message.plane.accountByTargetId.accountId = ?1 and (message.messageId > message.plane.lastMsgIdOfT or message.plane.status = ?4) and message.account = message.plane.accountByOwnerId) or (message.plane.accountByOwnerId.accountId = ?1 and message.messageId > message.plane.lastMsgIdOfO and (message.account = message.plane.accountByTargetId or message.plane.lastMsgIdOfO = 0))) and (?3 is null or message.messageId > ?3) order by message.messageId asc";

	private final String obtainMessagesBackwardJPQL = "select message from Message message join fetch message.account where message.plane.planeId = ?2 and ((message.plane.accountByTargetId.accountId = ?1 and (message.messageId > message.plane.lastMsgIdOfT or message.plane.status = ?4) and message.account = message.plane.accountByOwnerId) or (message.plane.accountByOwnerId.accountId = ?1 and message.messageId > message.plane.lastMsgIdOfO and (message.account = message.plane.accountByTargetId or message.plane.lastMsgIdOfO = 0))) and (?3 is null or message.messageId < ?3) order by message.messageId desc";

	// not verify whether replied (verified in obtainPlanes)
	public List<Message> obtainMessages(int accountId, long planeId,
			Long startId, int limit, boolean forward) {
		EntityManagerHelper.log("obtainMessagesing with accountId = "
				+ accountId + " and planeId = " + planeId, Level.INFO, null);
		try {
			String jpql = null;
			if (forward) {
				jpql = obtainMessagesForwardJPQL;
			} else {
				jpql = obtainMessagesBackwardJPQL;
			}
			TypedQuery<Message> query = EntityManagerHelper.getEntityManager()
					.createQuery(jpql, Message.class);
			query.setParameter(1, accountId);
			query.setParameter(2, planeId);
			query.setParameter(3, startId);
			query.setParameter(4, PlaneConstants.StatusNew);
			query.setMaxResults(limit);
			List<Message> messages = query.getResultList();
			EntityManagerHelper.log("obtainMessages successful", Level.INFO,
					null);
			return messages;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("obtainMessages failed", Level.SEVERE, re);
			throw re;
		}
	}

	/*
	 * lock plane first
	 */

	private final String viewedMessageByOwnerJPQL = "update Plane plane set plane.lastMsgId = plane.lastMsgIdOfO, plane.lastMsgIdOfO = ?3 where plane.lastMsgIdOfO < ?3 and plane.accountByOwnerId.accountId = ?2 and plane.deletedByO = 0 and plane.planeId = ?1 and plane.status = ?4";

	private final String viewedMessageByTargetJPQL = "update Plane plane set plane.lastMsgId = plane.lastMsgIdOfT, plane.lastMsgIdOfT = ?3 where plane.lastMsgIdOfT < ?3 and plane.accountByTargetId.accountId = ?2 and plane.deletedByT = 0 and plane.planeId = ?1 and plane.status = ?4";

	private final String viewedMessageByOwnerCountJPQL = "update Message message set message.status = ?5 where message.status = ?4 and message.account.accountId <> ?2 and message.plane.planeId = ?1 and message.plane.status = ?6 and message.plane.accountByOwnerId.accountId = ?2 and message.messageId > message.plane.lastMsgId and message.messageId <= ?3";

	private final String viewedMessageByTargetCountJPQL = "update Message message set message.status = ?5 where message.status = ?4 and message.account.accountId <> ?2 and message.plane.planeId = ?1 and message.plane.status = ?6 and message.plane.accountByTargetId.accountId = ?2 and message.messageId > message.plane.lastMsgId and message.messageId <= ?3";

	private final String viewedMessageByOwnerQueryJPQL = "select plane.lastMsgIdOfO from Plane plane where plane.planeId = ?1 and plane.accountByOwnerId.accountId = ?2 and plane.deletedByO = 0 and plane.status = ?3";

	private final String viewedMessageByTargetQueryJPQL = "select plane.lastMsgIdOfT from Plane plane where plane.planeId = ?1 and plane.accountByTargetId.accountId = ?2 and plane.deletedByT = 0 and plane.status = ?3";

	// may verify whether lastMsgId exists
	public Object[] viewedMessage(int accountId, long planeId, Long lastMsgId,
			boolean byOwner) {
		EntityManagerHelper.log("viewedMessaging", Level.INFO, null);
		try {
			String jpql = null;
			if (byOwner) {
				jpql = viewedMessageByOwnerJPQL;
			} else {
				jpql = viewedMessageByTargetJPQL;
			}
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					jpql);
			query.setParameter(1, planeId);
			query.setParameter(2, accountId);
			query.setParameter(3, lastMsgId);
			query.setParameter(4, PlaneConstants.StatusReplied);
			//
			int count = query.executeUpdate();
			if (count > 0) {
				if (byOwner) {
					jpql = viewedMessageByOwnerCountJPQL;
				} else {
					jpql = viewedMessageByTargetCountJPQL;
				}
				query = EntityManagerHelper.getEntityManager()
						.createQuery(jpql);
				query.setParameter(1, planeId);
				query.setParameter(2, accountId);
				query.setParameter(3, lastMsgId);
				query.setParameter(4, MessageConstants.MessageStatusUnread);
				query.setParameter(5, MessageConstants.MessageStatusRead);
				query.setParameter(6, PlaneConstants.StatusReplied);
				int cnt = query.executeUpdate();
				if (cnt > 0) {
					DaoUtils.accountStatDao.increaseMsgCount(accountId, -cnt);
				}
			}
			else{
				lastMsgId = null;
				if (byOwner) {
					jpql = viewedMessageByOwnerQueryJPQL;
				} else {
					jpql = viewedMessageByTargetQueryJPQL;
				}
				query = EntityManagerHelper.getEntityManager().createQuery(
						jpql);
				query.setParameter(1, planeId);
				query.setParameter(2, accountId);
				query.setParameter(3, PlaneConstants.StatusReplied);
				List<Long> result = query.getResultList();
				if(result.size() > 0){
					lastMsgId = result.get(0);
				}
			}
            Object[] results = new Object[2];
            results[0] = count == 1;
            results[1] = lastMsgId;
			EntityManagerHelper.log("viewedMessage successful", Level.INFO,
					null);
			return results;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("viewedMessage failed", Level.SEVERE, re);
			throw re;
		}

	}

	private final String decreaseMessageCountJPQL = "update AccountStat accountStat set accountStat.msgCount = accountStat.msgCount - (select count(message) from Message message where message.status = ?3 and message.account.accountId <> ?2 and message.plane.planeId = ?1 and ((message.plane.accountByOwnerId.accountId = ?2 and message.messageId > message.plane.lastMsgIdOfO) or (message.plane.accountByTargetId.accountId = ?2 and message.messageId > message.plane.lastMsgIdOfT))) where accountStat.accountId = ?2";

	public boolean decreaseMessageCount(long planeId, int accountId) {
		EntityManagerHelper.log("decreaseMessageCounting with planeId = " + planeId
				+ " and accountId = " + accountId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					decreaseMessageCountJPQL);
			query.setParameter(1, planeId);
			query.setParameter(2, accountId);
			query.setParameter(3, MessageConstants.MessageStatusUnread);
			int count = query.executeUpdate();
			EntityManagerHelper.log("decreaseMessageCount successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("decreaseMessageCount failed", Level.SEVERE, re);
			throw re;
		}
	}
}
