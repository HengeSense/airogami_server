package com.airogami.persistence.daos;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.airogami.common.constants.MessageConstants;
import com.airogami.common.constants.PlaneConstants;
import com.airogami.common.notification.MessageNotifiedInfo;
import com.airogami.common.notification.SilentNotifiedInfo;
import com.airogami.persistence.entities.EntityManagerHelper;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.MessageDAO;

public class MessageDao extends MessageDAO {

	// plane.updateCount = plane.updateCount + 1
	private final String verifyReplyByOwnerJPQL = "update Plane plane set plane.status = ?3, plane.updatedTime = ?4 where plane.planeId = ?1 and plane.accountByOwnerId.accountId = ?2 and plane.status = ?3 and plane.deletedByO = 0 and plane.deletedByT = 0";

	private final String verifyReplyByTargetJPQL = "update Plane plane set plane.status = ?3, plane.updatedTime = ?4 where plane.planeId = ?1 and plane.accountByTargetId.accountId = ?2 and plane.deletedByT = 0 and plane.deletedByO = 0";

	public boolean verifyReply(long planeId, int ownerId, boolean byOwner) {
		EntityManagerHelper.log("verifyReplying with planeId = " + planeId
				+ " and ownerId = " + ownerId, Level.INFO, null);
		try {
			String jpql = null;
			if (byOwner) {
				jpql = verifyReplyByOwnerJPQL;
			} else {
				jpql = verifyReplyByTargetJPQL;
			}
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					jpql);
			query.setParameter(1, planeId);
			query.setParameter(2, ownerId);
			query.setParameter(3, PlaneConstants.StatusReplied);
			query.setParameter(4, new Timestamp(System.currentTimeMillis()));
			int count = query.executeUpdate();
			EntityManagerHelper.log("verifyReply successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("verifyReply failed", Level.SEVERE, re);
			throw re;
		}
	}

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
	
	private final String oppositeAccountIdByOwnerJPQL = "select plane.accountByTargetId.accountId from Plane plane where plane.planeId = ?1 and plane.deletedByT = 0";

	private final String oppositeAccountIdByTargetJPQL = "select plane.accountByOwnerId.accountId from Plane plane where plane.planeId = ?1 and plane.deletedByO = 0";

	public SilentNotifiedInfo getSNotifiedInfo(long planeId, boolean byOwner) {
		EntityManagerHelper.log("getSNotifiedInfoing with planeId = " + planeId, Level.INFO, null);
		try {
			String jpql;
			if (byOwner) {
				jpql = oppositeAccountIdByOwnerJPQL;
			} else {
				jpql = oppositeAccountIdByTargetJPQL;
			}
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					jpql, SilentNotifiedInfo.class);
			query.setParameter(1, planeId);
			List<Integer> result = query.getResultList();
			SilentNotifiedInfo notifiedInfo = null;
			if(result.size() == 1){
				notifiedInfo = new SilentNotifiedInfo(result.get(0));
			}
			EntityManagerHelper
					.log("getSNotifiedInfo successful", Level.INFO, null);
			return notifiedInfo;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getSNotifiedInfo failed", Level.SEVERE, re);
			throw re;
		}
	}

	/*
	 * lock plane first
	 */

	private final String viewedMessageByOwnerJPQL = "update Plane plane set plane.lastMsgId = plane.lastMsgIdOfO, plane.lastMsgIdOfO = ?3 where plane.lastMsgIdOfO < ?3 and plane.accountByOwnerId.accountId = ?2 and plane.deletedByO = 0 and plane.deletedByT = 0  and plane.planeId = ?1 and plane.status = ?4";

	private final String viewedMessageByTargetJPQL = "update Plane plane set plane.lastMsgId = plane.lastMsgIdOfT, plane.lastMsgIdOfT = ?3 where plane.lastMsgIdOfT < ?3 and plane.accountByTargetId.accountId = ?2 and plane.deletedByO = 0 and plane.deletedByT = 0 and plane.planeId = ?1 and plane.status = ?4";

	private final String viewedMessageByOwnerCountJPQL = "update Message message set message.status = ?5 where message.status = ?4 and message.account.accountId <> ?2 and message.plane.planeId = ?1 and message.plane.status = ?6 and message.plane.accountByOwnerId.accountId = ?2 and message.messageId > message.plane.lastMsgId and message.messageId <= ?3";

	private final String viewedMessageByTargetCountJPQL = "update Message message set message.status = ?5 where message.status = ?4 and message.account.accountId <> ?2 and message.plane.planeId = ?1 and message.plane.status = ?6 and message.plane.accountByTargetId.accountId = ?2 and message.messageId > message.plane.lastMsgId and message.messageId <= ?3";

	private final String viewedMessageByOwnerQueryJPQL = "select plane.lastMsgIdOfO from Plane plane where plane.planeId = ?1 and plane.accountByOwnerId.accountId = ?2 and plane.deletedByO = 0 and plane.deletedByT = 0  and plane.status = ?3";

	private final String viewedMessageByTargetQueryJPQL = "select plane.lastMsgIdOfT from Plane plane where plane.planeId = ?1 and plane.accountByTargetId.accountId = ?2 and plane.deletedByO = 0 and plane.deletedByT = 0 and plane.status = ?3";

	//return succeed, lastMsgId, opposite accountId may verify whether lastMsgId exists
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
			Object[] results = new Object[3];
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
					//opposite
					if (byOwner) {
						jpql = oppositeAccountIdByOwnerJPQL;
					} else {
						jpql = oppositeAccountIdByTargetJPQL;
					}
					query = EntityManagerHelper.getEntityManager()
							.createQuery(jpql);
					query.setParameter(1, planeId);
					List<Integer> result = query.getResultList();
					if(result.size() > 0){
						results[2] = accountId = result.get(0);
						//
						DaoUtils.planeDao.updateInc(planeId, accountId, !byOwner);
					}
				}
			} else {
				lastMsgId = null;
				if (byOwner) {
					jpql = viewedMessageByOwnerQueryJPQL;
				} else {
					jpql = viewedMessageByTargetQueryJPQL;
				}
				query = EntityManagerHelper.getEntityManager()
						.createQuery(jpql);
				query.setParameter(1, planeId);
				query.setParameter(2, accountId);
				query.setParameter(3, PlaneConstants.StatusReplied);
				List<Long> result = query.getResultList();
				if (result.size() > 0) {
					lastMsgId = result.get(0);
				}
			}
			
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
		EntityManagerHelper.log("decreaseMessageCounting with planeId = "
				+ planeId + " and accountId = " + accountId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					decreaseMessageCountJPQL);
			query.setParameter(1, planeId);
			query.setParameter(2, accountId);
			query.setParameter(3, MessageConstants.MessageStatusUnread);
			int count = query.executeUpdate();
			EntityManagerHelper.log("decreaseMessageCount successful",
					Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("decreaseMessageCount failed",
					Level.SEVERE, re);
			throw re;
		}
	}

	// lock the plane first
	private final String clearPlaneJPQL = "update Plane plane set plane.clearMsgId = (select max(message.messageId) from Message message where message.plane = plane), plane.updateCount = plane.updateCount + 1 where plane.planeId = ?1 and (plane.accountByOwnerId.accountId = ?2 or plane.accountByTargetId.accountId = ?2) and plane.status = ?3 and plane.deletedByO = 0 and plane.deletedByT = 0";

	private final String clearPlaneOwnerSetJPQL = "update Message message set message.status = ?3 where message.status = ?2 and message.account <> message.plane.accountByOwnerId and message.plane.planeId = ?1 and message.messageId > message.plane.lastMsgIdOfO and message.messageId <= message.plane.clearMsgId";

	private final String clearPlaneOwnerCountJPQL = "update AccountStat accountStat set accountStat.msgCount = accountStat.msgCount + ?2 where accountStat.account = (select plane.accountByOwnerId from Plane plane where plane.planeId = ?1)";

	private final String clearPlaneTargetSetJPQL = "update Message message set message.status = ?3 where message.status = ?2 and message.account <> message.plane.accountByTargetId and message.plane.planeId = ?1 and message.messageId > message.plane.lastMsgIdOfT and message.messageId <= message.plane.clearMsgId";

	private final String clearPlaneTargetCountJPQL = "update AccountStat accountStat set accountStat.msgCount = accountStat.msgCount + ?2 where accountStat.account = (select plane.accountByTargetId from Plane plane where plane.planeId = ?1)";

	private final String clearPlaneUpdateJPQL = "update Plane plane set plane.lastMsgIdOfO = plane.clearMsgId, plane.lastMsgIdOfT = plane.clearMsgId, plane.ownerInc = (select accountSys.planeInc from AccountSys accountSys where accountSys.account = plane.accountByOwnerId), plane.targetInc = (select accountSys.planeInc from AccountSys accountSys where accountSys.account = plane.accountByTargetId) where plane.planeId = ?1";

	private final String clearPlaneQueryJPQL = "select plane.clearMsgId, plane.accountByOwnerId.accountId, plane.accountByTargetId.accountId from Plane plane where plane.planeId = ?1";

	// return null if not replied or deleted or not exist
	public Object[] clearPlane(long planeId, int accountId) {
		EntityManagerHelper.log("clearPlaneing with planeId = " + planeId,
				Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					clearPlaneJPQL);
			query.setParameter(1, planeId);
			query.setParameter(2, accountId);
			query.setParameter(3, PlaneConstants.StatusReplied);
			int count = query.executeUpdate();
			Object[] clearResult = null;
			if (count == 1) {
				// owner
				query = EntityManagerHelper.getEntityManager().createQuery(
						clearPlaneOwnerSetJPQL);
				query.setParameter(1, planeId);
				query.setParameter(2, MessageConstants.MessageStatusUnread);
				query.setParameter(3, MessageConstants.MessageStatusRead);
				count = query.executeUpdate();
				if (count > 0) {
					query = EntityManagerHelper.getEntityManager().createQuery(
							clearPlaneOwnerCountJPQL);
					query.setParameter(1, planeId);
					query.setParameter(2, -count);
					query.executeUpdate();
				}
				// target
				query = EntityManagerHelper.getEntityManager().createQuery(
						clearPlaneTargetSetJPQL);
				query.setParameter(1, planeId);
				query.setParameter(2, MessageConstants.MessageStatusUnread);
				query.setParameter(3, MessageConstants.MessageStatusRead);
				count = query.executeUpdate();
				if (count > 0) {
					query = EntityManagerHelper.getEntityManager().createQuery(
							clearPlaneTargetCountJPQL);
					query.setParameter(1, planeId);
					query.setParameter(2, -count);
					query.executeUpdate();
				}

				// update
				DaoUtils.accountSysDao.increaseBothPlaneInc(planeId, 1);
				query = EntityManagerHelper.getEntityManager().createQuery(
						clearPlaneUpdateJPQL);
				query.setParameter(1, planeId);
				query.executeUpdate();

				// query
				query = EntityManagerHelper.getEntityManager().createQuery(
						clearPlaneQueryJPQL);
				query.setParameter(1, planeId);
				List<Object[]> result = query.getResultList();
				if (result.size() > 0) {
					clearResult = result.get(0);
				}
			}
			EntityManagerHelper.log("clearPlane successful", Level.INFO, null);
			return clearResult;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("clearPlane failed", Level.SEVERE, re);
			throw re;
		}
	}

	private final String decreaseBothMessageCountJPQL = "update AccountStat accountStat set accountStat.msgCount = accountStat.msgCount - (select count(message) from Message message where message.status = ?2 and message.account.accountId <> accountStat.accountId and message.plane.planeId = ?1 and ((message.plane.accountByOwnerId.accountId = accountStat.accountId and message.messageId > message.plane.lastMsgIdOfO) or (message.plane.accountByTargetId.accountId = accountStat.accountId and message.messageId > message.plane.lastMsgIdOfT))) where accountStat.account = (select plane.accountByOwnerId from Plane plane where plane.planeId = ?1) or accountStat.account = (select plane.accountByTargetId from Plane plane where plane.planeId = ?1)";

	// decrease both
	public boolean decreaseMessageCount(long planeId) {
		EntityManagerHelper.log("decreaseMessageCounting with planeId = "
				+ planeId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					decreaseBothMessageCountJPQL);
			query.setParameter(1, planeId);
			query.setParameter(2, MessageConstants.MessageStatusUnread);
			int count = query.executeUpdate();
			EntityManagerHelper.log("decreaseMessageCount successful",
					Level.INFO, null);
			return count == 2;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("decreaseMessageCount failed",
					Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String getNotifiedInfoSQL = "select OWNER_ID as accountId, MSG_COUNT + CHAIN_MSG_COUNT as messagesCount, FULL_NAME as name from PLANE, PROFILE profile, ACCOUNT_STAT accountStat where PLANE_ID = ?1 and DELETED_BY_O = 0 and DELETED_BY_T = 0 and OWNER_ID <> ?2 and profile.ACCOUNT_ID = OWNER_ID and accountStat.ACCOUNT_ID = OWNER_ID union all select TARGET_ID as accountId, MSG_COUNT + CHAIN_MSG_COUNT as messagesCount, FULL_NAME as name from PLANE, PROFILE profile, ACCOUNT_STAT accountStat  where PLANE_ID = ?1 and DELETED_BY_T = 0 and DELETED_BY_O = 0  and TARGET_ID <> ?2 and profile.ACCOUNT_ID = TARGET_ID and accountStat.ACCOUNT_ID = TARGET_ID";

	public MessageNotifiedInfo getNotifiedInfo(long planeId, int accountId) {
		EntityManagerHelper.log("getNotifiedInfoing with planeId = " + planeId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createNativeQuery(
					getNotifiedInfoSQL, MessageNotifiedInfo.class);
			query.setParameter(1, planeId);
			query.setParameter(2, accountId);
			List<MessageNotifiedInfo> result = query.getResultList();
			MessageNotifiedInfo notifiedInfo = null;
			if(result.size() == 1){
				notifiedInfo = result.get(0);
			}
			EntityManagerHelper
					.log("getNotifiedInfo successful", Level.INFO, null);
			return notifiedInfo;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getNotifiedInfo failed", Level.SEVERE, re);
			throw re;
		}
	}
}
