package com.airogami.persistence.daos;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;

import com.airogami.common.constants.MessageConstants;
import com.airogami.common.constants.PlaneConstants;
import com.airogami.persistence.entities.EntityManagerHelper;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.MessageDAO;

public class MessageDao extends MessageDAO {
	private final String obtainMessagesForwardJPQL = "select message from Message message join fetch message.account where message.plane.planeId = ?2 and ((message.plane.accountByTargetId.accountId = ?1 and message.messageId > message.plane.lastMsgIdOfTarget and message.account = message.plane.accountByOwnerId) or (message.plane.accountByOwnerId.accountId = ?1 and message.messageId > message.plane.lastMsgIdOfOwner and (message.account = message.plane.accountByTargetId or message.plane.lastMsgIdOfOwner = 0))) and (?3 is null or message.messageId > ?3) order by message.messageId asc";

	private final String obtainMessagesBackwardJPQL = "select message from Message message join fetch message.account where message.plane.planeId = ?2 and ((message.plane.accountByTargetId.accountId = ?1 and message.messageId > message.plane.lastMsgIdOfTarget and message.account = message.plane.accountByOwnerId) or (message.plane.accountByOwnerId.accountId = ?1 and message.messageId > message.plane.lastMsgIdOfOwner and (message.account = message.plane.accountByTargetId or message.plane.lastMsgIdOfOwner = 0))) and (?3 is null or message.messageId < ?3) order by message.messageId desc";

	// not verify whether replied (verified in obtainPlanes)
	public List<Message> obtainMessages(long accountId, long planeId, Long startId, int limit, boolean forward){
		EntityManagerHelper.log("obtainMessagesing with accountId = " + accountId + " and planeId = " + planeId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = obtainMessagesForwardJPQL;
			}
			else{
				jpql = obtainMessagesBackwardJPQL;
			}
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					jpql);
			query.setParameter(1, accountId);
			query.setParameter(2, planeId);
			query.setParameter(3, startId);
			query.setMaxResults(limit);
			List<Message> messages = query.getResultList();
			EntityManagerHelper
					.log("obtainMessages successful", Level.INFO, null);
			return messages;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("obtainMessages failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String viewedMessageByOwnerJPQL = "update Message message set message.status = ?5 where message.status = ?4 and message.plane.planeId = ?1 and message.plane.status = ?6 and message.plane.accountByOwnerId.accountId = ?2 and message.messageId > message.plane.lastMsgIdOfOwner and message.messageId <= ?3";

	private final String viewedMessageByTargetJPQL = "update Message message set message.status = ?5 where message.status = ?4 and message.plane.planeId = ?1 and message.plane.status = ?6 and message.plane.accountByTargetId.accountId = ?2 and message.messageId > message.plane.lastMsgIdOfTarget and message.messageId <= ?3";
	
	private final String viewedMessageByOwnerUpdateJPQL = "update Plane plane set plane.lastMsgIdOfOwner = ?3 where plane.lastMsgIdOfOwner < ?3 and plane.accountByOwnerId.accountId = ?2 and plane.planeId = ?1 and plane.status = ?4";

	private final String viewedMessageByTargetUpdateJPQL = "update Plane plane set plane.lastMsgIdOfTarget = ?3 where plane.lastMsgIdOfTarget < ?3 and plane.accountByTargetId.accountId = ?2 and plane.planeId = ?1 and plane.status = ?4";

	//may verify whether lastMsgId exists
	public boolean viewedMessage(long accountId, long planeId, long lastMsgId, boolean byOwner){
		EntityManagerHelper.log("viewedMessaging", Level.INFO, null);		
		try {
			String jpql = null;
			if(byOwner){
				jpql = viewedMessageByOwnerJPQL;
			}
			else{
				jpql = viewedMessageByTargetJPQL;
			}
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					jpql);
			query.setParameter(1, planeId);
			query.setParameter(2, accountId);
			query.setParameter(3, lastMsgId);
			query.setParameter(4, MessageConstants.MessageStatusUnread);
			query.setParameter(5, MessageConstants.MessageStatusRead);
			query.setParameter(6, PlaneConstants.StatusReplied);
			//
			int count = query.executeUpdate();
			if(count > 0){
				DaoUtils.accountStatDao.increaseMsgCount(accountId, -count);
				//
				if(byOwner){
					jpql = viewedMessageByOwnerUpdateJPQL;
				}
				else{
					jpql = viewedMessageByTargetUpdateJPQL;
				}
				query = EntityManagerHelper.getEntityManager().createQuery(
						jpql);
				query.setParameter(1, planeId);
				query.setParameter(2, accountId);
				query.setParameter(3, lastMsgId);
				query.setParameter(4, PlaneConstants.StatusReplied);
				count = query.executeUpdate();

			}
			
			EntityManagerHelper
					.log("viewedMessage successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("viewedMessage failed", Level.SEVERE, re);
			throw re;
		}
		
	}
}
