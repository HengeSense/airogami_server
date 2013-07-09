package com.airogami.persitence.daos;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;

import com.airogami.persitence.entities.EntityManagerHelper;
import com.airogami.persitence.entities.Message;
import com.airogami.persitence.entities.MessageDAO;

public class MessageDao extends MessageDAO {
	private final String obtainMessagesForwardJPQL = "select message from Message message where message.account.accountId = ?1 and message.plane.planeId = ?2 and (message.plane.accountByTargetId.accountId = ?1 and message.createdTime > message.plane.targetViewedTime or message.plane.accountByOwnerId.accountId = ?1 and message.createdTime > message.plane.ownerViewedTime) and message.createdTime > ?3 order by message.createdTime desc";

	private final String obtainMessagesBackwardJPQL = "select message from Message message where message.account.accountId = ?1 and message.plane.planeId = ?2 and (message.plane.accountByTargetId.accountId = ?1 and message.createdTime > message.plane.targetViewedTime or message.plane.accountByOwnerId.accountId = ?1 and message.createdTime > message.plane.ownerViewedTime) and message.createdTime < ?3 order by message.createdTime desc";

	public List<Message> obtainMessages(long accountId, long planeId, String start, int limit, boolean forward){
		EntityManagerHelper.log("obtainMessagesing with accountId = " + accountId + "and planeId = " + planeId, Level.INFO, null);
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
			query.setParameter(3, Timestamp.valueOf(start));
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
	
	private final String viewedMessageByOwnerJPQL = "update Plane plane set plane.ownerViewedTime = ?3 where plane.ownerViewedTime < ?3 and plane.accountByOwnerId.accountId = ?2 and plane.planeId = ?1";

	private final String viewedMessageByTargetJPQL = "update Plane plane set plane.targetViewedTime = ?3 where plane.targetViewedTime < ?3 and plane.accountByTargetId.accountId = ?2 and plane.planeId = ?1";

	public boolean viewedMessage(long accountId, long planeId, Timestamp lastTimestamp, boolean byOwner){
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
			query.setParameter(3, lastTimestamp);
			int count = query.executeUpdate();
			EntityManagerHelper
					.log("viewedMessage successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("viewedMessage failed", Level.SEVERE, re);
			throw re;
		}
		
	}
}
