package com.airogami.presentation.logic;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.airogami.application.ServiceUtils;
import com.airogami.application.exception.ApplicationException;
import com.airogami.common.constants.AccountConstants;
import com.airogami.common.constants.MessageConstants;
import com.airogami.common.constants.PlaneConstants;
import com.airogami.common.notification.MessageNotifiedInfo;
import com.airogami.common.notification.NotifiedInfo;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Category;
import com.airogami.persistence.entities.Chain;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.Plane;
import com.airogami.presentation.notification.LPMNotification;
import com.airogami.presentation.notification.Notification;
import com.airogami.presentation.notification.RPIMNotification;
import com.airogami.presentation.notification.RPMNotification;
import com.airogami.presentation.notification.SNotification;

public class PlaneManager {

	/*
	 * @param category:(Category) must be not null, have name
	 * 
	 * @return category if successful
	 * 
	 * @throws AirogamiException if failed
	 */
	public Category createCategory(Category category) throws AirogamiException {
		if (category == null || category.getName() == null
				|| category.getName().length() == 0) {
			throw new IllegalArgumentException(
					"Illegal arguments in createCatrgory");
		}
		try {
			category = ServiceUtils.planeService.createCategory(category);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		return category;
	}

	/*
	 * @param plane:(Plane) must be not null, have plane.message, plane.category
	 * 
	 * @param ownerId:(int) must exist
	 * 
	 * @return plane, accountStatLeft if successful otherwise error, accountStatLeft
	 * 
	 * @throws AirogamiException if failed
	 */
	public Map<String, Object> sendPlane(Plane plane, int ownerId)
			throws AirogamiException {
		Category category = null;
		if (plane == null || plane.getMessages().size() == 0
				|| (category = plane.getCategory()) == null) {
			throw new IllegalArgumentException("Illegal arguments in sendPlane");
		}
		Message message = plane.getMessages().get(0);
		if (message.getType() == null || message.getContent() == null
				|| message.getContent().length() == 0
				|| category.getCategoryId() == null) {
			throw new IllegalArgumentException("Illegal arguments in sendPlane");
		}
		plane.setMaxMatchCount(PlaneConstants.MaxMatchCount);
		plane.setMatchCount((short) 0);
		plane.setStatus((short) PlaneConstants.StatusNew);
		plane.setLikedByO((short) 0);
		plane.setLikedByT((short) 0);
		plane.setDeletedByO((short) 0);
		plane.setDeletedByT((short) 0);
		if (plane.getSex() == null) {
			plane.setSex((short) AccountConstants.SexType_Unknown);
		}
		message.setPlane(plane);
		message.setStatus(MessageConstants.MessageStatusRead);
		Map<String, Object> result = null;
		try {
			result = ServiceUtils.planeService.sendPlane(plane, ownerId);
		} catch (ApplicationException re) {
			// re.printStackTrace();
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
        plane = (Plane)result.get("plane"); 
		if (plane != null) {
			ServiceUtils.airogamiService.appendPlane(plane.getPlaneId());
		} 

		return result;
	}

	/*
	 * @param planeId:(long) must exist
	 * 
	 * @param accountId:(int) must be plane.accoutByOwnerId or
	 * plane.accountByTargetId
	 * 
	 * @param byOwner:(boolean)
	 * 
	 * @param message:(Message) must be not null, has type and content
	 * 
	 * @return message if successful otherwise error ( = "none")
	 * 
	 * @throws ApplicationException if failed
	 */
	public Map<String, Object> replyPlane(long planeId, int accountId, boolean byOwner,
			Message message) throws AirogamiException {
		Map<String, Object> result = null;
		if (message == null || message.getType() == null
				|| message.getContent() == null
				|| (message.getContent().length() == 0 && message.getType() == MessageConstants.MessageTypeText)) {
			throw new IllegalArgumentException(
					"Illegal arguments in replyPlane");
		}
		try {
			result = ServiceUtils.planeService.replyPlane(planeId, accountId, byOwner,
					message);
		} catch (ApplicationException re) {
			// /re.printStackTrace();
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		NotifiedInfo notifiedInfo = (NotifiedInfo) result.remove("notifiedInfo");
		if (notifiedInfo != null) {
			Notification notification = null;
			if(message.getType() == MessageConstants.MessageTypeImage){
				notification = new RPIMNotification(notifiedInfo);
			}
			else{
				notification = new RPMNotification(notifiedInfo);
			}
			ManagerUtils.notificationManager.addNotification(notification);
		}

		return result;
	}

	/*
	 * pickup planes or chains
	 * 
	 * @param accountId:(int)
	 * 
	 * @return planes, chains if successful, otherwise error, accountStatLeft
	 * 
	 * @throws AirogamiException if failed
	 */
	public Map<String, Object> pickup(int accountId) throws AirogamiException {
		try {
			return ServiceUtils.accountService.pickup(accountId);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
	}

	/*
	 * @param planeId:(long) must be not null
	 * 
	 * @param accountId:(int) should = plane.accountByTarget.accountId
	 * 
	 * @return canMatchedAgain if successful otherwise error or plane
	 * 
	 * @throws ApplicationException if failed
	 */
	public Map<String, Object> throwPlane(long planeId, int accountId)
			throws AirogamiException {
		Map<String, Object> result = null;
		try {
			result = ServiceUtils.planeService.throwPlane(planeId, accountId);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		Boolean canMatchAgain = (Boolean) result.remove("canMatchAgain");
		if (canMatchAgain != null && canMatchAgain) {
			ServiceUtils.airogamiService.appendPlane(planeId);
		}
		return result;
	}

	/*
	 * @param planeId:(long) must be not null
	 * 
	 * @param accountId:(int)
	 * 
	 * @param byOwner:(boolean)
	 * 
	 * @return message, message.plane oppositeAccountId
	 * 
	 * @throws AirogamiException if failed
	 */
	public Map<String, Object> likePlane(long planeId, int accountId,
			boolean byOwner) throws AirogamiException {
		Map<String, Object> result = null;
		try {
			result = ServiceUtils.planeService.likePlane(planeId, accountId,
					byOwner);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		NotifiedInfo notifiedInfo = (NotifiedInfo) result.remove("notifiedInfo");
		if (notifiedInfo != null) {
			Notification notification = new LPMNotification(notifiedInfo);
			ManagerUtils.notificationManager.addNotification(notification);
		}
		return result;
	}
	
	/*
	 * @param planeId:(long)
	 * @param accountId:(int)
	 * @return clearMsgId if successful otherwise error
	 * @throws AirogamiException if failed 
	 */ 
	
	public Map<String, Object> clearPlane(long planeId, int accountId) throws AirogamiException
	{
		Map<String, Object> result = null;
		try {
			result = ServiceUtils.planeService.clearPlane(planeId, accountId);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		NotifiedInfo notifiedInfo = (NotifiedInfo) result.remove("notifiedInfo");
		if (notifiedInfo != null) {
			Notification notification = new SNotification(notifiedInfo);
			ManagerUtils.notificationManager.addNotification(notification);
		}
		return result;
	}


	/*
	 * @param planeId:(long)
	 * 
	 * @param accountId:(int)
	 * 
	 * @param byOwner:(boolean)
	 * 
	 * @return whether successful
	 * 
	 * @throws AirogamiException if failed
	 */
	public Map<String, Object> deletePlane(long planeId, int accountId,
			boolean byOwner) throws AirogamiException {
		Map<String, Object> result = null;
		try {
			result = ServiceUtils.planeService.deletePlane(planeId, accountId,
					byOwner);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		NotifiedInfo notifiedInfo = (NotifiedInfo) result.remove("notifiedInfo");
		if (notifiedInfo != null) {
			Notification notification = new SNotification(notifiedInfo);
			ManagerUtils.notificationManager.addNotification(notification);
		}
		return result;
	}

	/*
	 * @param accountId:(int)
	 * 
	 * @param start:(Long) (exclusive)
	 * 
	 * @param end:(Long) (exclusive)
	 * 
	 * @param limit:(int) max(limit) = MaxPlaneLimit
	 * 
	 * @param forward:(boolean)
	 * 
	 * @return more, neoPlanes (may have more than one plane if more = true)
	 * 
	 * @throws AirogamiException if failed
	 */
	public Map<String, Object> getNeoPlanes(int accountId, Long start,
			Long end, int limit, boolean forward) throws AirogamiException {
		Map<String, Object> result;
		try {
			result = ServiceUtils.planeService.getNeoPlanes(accountId, start,
					end, limit, forward);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		return result;
	}

	/*
	 * @param accountId:(int)
	 * 
	 * @param planeIds:(List<Long>)
	 * 
	 * @param updated:(boolean)
	 * 
	 * @return planes
	 * 
	 * @throws AirogamiException if failed
	 */
	public Map<String, Object> getPlanes(int accountId, List<Long> planeIds, boolean updated)
			throws AirogamiException {
		if (planeIds == null) {
			throw new IllegalArgumentException(
					"Illegal arguments in getPlanes");
		}
		try {
			return ServiceUtils.planeService.getPlanes(accountId, planeIds, updated);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
	}

	/*
	 * @param accountId:(int)
	 * 
	 * @param start:(Long) (exclusive)
	 * 
	 * @param end:(Long) (exclusive)
	 * 
	 * @param limit:(int) max(limit) = MaxPlaneIdLimit
	 * 
	 * @param forward:(boolean)
	 * 
	 * @return more, planeIds (may have more than one plane if more = true)
	 * 
	 * @throws AirogamiException if failed
	 */
	public Map<String, Object> getOldPlanes(int accountId, Long start,
			Long end, int limit, boolean forward) throws AirogamiException {
		Map<String, Object> result;
		try {
			result = ServiceUtils.planeService.getOldPlanes(accountId, start,
					end, limit, forward);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		return result;
	}

	/*
	 * @param accountId:(int)
	 * 
	 * @param start:(Long) (exclusive)
	 * 
	 * @param end:(Long) (exclusive)
	 * 
	 * @param limit:(int) must > 0, max(limit) = MaxPlaneLimit
	 * 
	 * @param forward:(boolean)
	 * 
	 * @return more, planes if successful(may have more than one plane if more =
	 * true)
	 * 
	 * @throws AirogamiException if failed
	 */
	public Map<String, Object> obtainPlanes(int accountId, Long start,
			Long end, int limit, boolean forward) throws AirogamiException {
		Map<String, Object> result;
		try {
			result = ServiceUtils.planeService.obtainPlanes(accountId, start,
					end, limit, forward);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		return result;
	}

	/*
	 * @param accountId:(int)
	 * 
	 * @param start:(Long) (exclusive)
	 * 
	 * @param end:(Long) (exclusive)
	 * 
	 * @param limit:(int) must > 0, max(limit) = MaxPlaneLimit
	 * 
	 * @param forward:(boolean)
	 * 
	 * @return more, planeIds if successful (may have more than one planeIds if
	 * more = true)
	 * 
	 * @throws AirogamiException if failed
	 */
	public Map<String, Object> obtainPlaneIds(int accountId, Long start,
			Long end, int limit, boolean forward) throws AirogamiException {
		Map<String, Object> result;
		try {
			result = ServiceUtils.planeService.obtainPlaneIds(accountId, start,
					end, limit, forward);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		return result;
	}

	/*
	 * obtain all replied and undeleted planeIds for synchronization
	 * 
	 * @param accountId:(int)
	 * 
	 * @param startId:(long) (exclusive)
	 * 
	 * @param limit:(int) max(limit) = MaxPlaneIdLimit
	 * 
	 * @return more, planeIds if successful (may have more than one planeId if
	 * more = true)
	 * 
	 * @throws AirogamiException if failed
	 */
	public Map<String, Object> obtainPlaneIds(int accountId, long startId,
			int limit) throws AirogamiException {
		Map<String, Object> result;
		try {
			result = ServiceUtils.planeService.obtainPlaneIds(accountId,
					startId, limit);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		return result;
	}

	/*
	 * @param accountId:(int)
	 * 
	 * @param start:(Long) (exclusive)
	 * 
	 * @param end:(Long) (exclusive)
	 * 
	 * @param limit:(int) must > 0, max(limit) = MaxPlaneLimit
	 * 
	 * @param forward:(boolean)
	 * 
	 * @return more, planes if successful(may have more than one plane if more =
	 * true)
	 * 
	 * @throws AirogamiException if failed
	 */
	public Map<String, Object> receivePlanes(int accountId, Long start,
			Long end, int limit, boolean forward) throws AirogamiException {
		Map<String, Object> result;
		try {
			result = ServiceUtils.planeService.receivePlanes(accountId, start,
					end, limit, forward);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		return result;
	}

	/*
	 * @param accountId:(int)
	 * 
	 * @param start:(Long) (exclusive)
	 * 
	 * @param end:(Long) (exclusive)
	 * 
	 * @param limit:(int) must > 0, max(limit) = MaxPlaneLimit
	 * 
	 * @param forward:(boolean)
	 * 
	 * @return more, planeIds if successful (may have more than one planeId if
	 * more = true)
	 * 
	 * @throws AirogamiException if failed
	 */
	public Map<String, Object> receivePlaneIds(int accountId, Long start,
			Long end, int limit, boolean forward) throws AirogamiException {
		Map<String, Object> result;
		try {
			result = ServiceUtils.planeService.receivePlaneIds(accountId,
					start, end, limit, forward);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		return result;
	}

	/*
	 * get all received planeIds for synchronization
	 * 
	 * @param accountId:(int)
	 * 
	 * @param startId:(long) (exclusive)
	 * 
	 * @param limit:(int) max(limit) = MaxPlaneIdLimit
	 * 
	 * @return more, planeIds if successful (may have more than one planeId if
	 * more = true)
	 * 
	 * @throws AirogamiException if failed
	 */
	public Map<String, Object> receivePlaneIds(int accountId, long startId,
			int limit) throws AirogamiException {
		Map<String, Object> result;
		try {
			result = ServiceUtils.planeService.receivePlaneIds(accountId,
					startId, limit);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		return result;
	}

	/*
	 * @param accountId:(int)
	 * 
	 * @param planeId:(long)
	 * 
	 * @param startId:(Long) start messageId (can be null) (exclusive)
	 * 
	 * @param limit:(int) max(limit) = MaxMessageLimit
	 * 
	 * @return more, messages if successful
	 * 
	 * @throws AirogamiException if failed
	 */
	public Map<String, Object> obtainMessages(int accountId, long planeId,
			Long startId, int limit) throws AirogamiException {
		Map<String, Object> result;
		try {
			result = ServiceUtils.planeService.obtainMessages(accountId,
					planeId, startId, limit, true);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		return result;
	}

	/*
	 * @param accountId:(int)
	 * 
	 * @param planeId:(long)
	 * 
	 * @param lastMsgId:(long) (exclusive)
	 * 
	 * @param byOwner:(boolean)
	 * 
	 * @return succeed
	 * 
	 * @throws ApplicationException if failed
	 */
	public Map<String, Object> viewedMessages(int accountId, long planeId, long lastMsgId,
			boolean byOwner) throws AirogamiException {
		Map<String, Object> result = null;
		try {
			result = ServiceUtils.planeService.viewedMessages(accountId, planeId,
					lastMsgId, byOwner);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		NotifiedInfo notifiedInfo = (NotifiedInfo) result.remove("notifiedInfo");
		if (notifiedInfo != null) {
			Notification notification = new SNotification(notifiedInfo);
			ManagerUtils.notificationManager.addNotification(notification);
		}
		return result;
	}

}
