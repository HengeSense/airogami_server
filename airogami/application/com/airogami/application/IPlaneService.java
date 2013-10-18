package com.airogami.application;

import java.util.List;
import java.util.Map;

import com.airogami.application.exception.ApplicationException;
import com.airogami.persistence.entities.Category;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.Plane;

public interface IPlaneService {
	
	public final int MaxPlaneLimit = 50;
	
	public final int MaxMessageLimit = 50;
	
	public final int MaxOldPlanesLimit = 1000;
	
	/*
	 * @param category:(Category) must be not null, have name
	 * @return category if successful
	 * @throws ApplicationException if failed 
	 */
	public Category createCatrgory(Category category) throws ApplicationException;
	
	/*
	 * @param plane:(Plane) must be not null, have plane.message, plane.category
	 * @param ownerId:(int) must exist
	 * @return plane, accountStatLeft if successful otherwise error, accountStatLeft 
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> sendPlane(Plane plane, int ownerId) throws ApplicationException;
	
	/*
	 * @param planeId:(long) must exist
	 * @return succeed, rematch, accountId if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> matchPlane(long planeId) throws ApplicationException;

	/*
	 * @param planeId:(long) must be not null
	 * @param ownerId:(int) must be plane.accoutByOwnerId or plane.accountByTargetId
	 * @param byOwner:(boolean)
	 * @param message:(Message)
	 * @return message if successful or error if failed
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> replyPlane(long planeId,int ownerId, boolean byOwner,  Message message) throws ApplicationException;	
	
	/*
	 * @param accountId:(int)
	 * @param count:(int) should > 0
	 * @return planes if successful
	 * @throws ApplicationException if failed 
	 */
	public List<Plane> pickupPlane(int accountId, int count) throws ApplicationException;
	
	/*
	 * @param planeId:(long) must be not null
	 * @param accountId:(int)
	 * @param byOwner:(boolean)
	 * @return message, oppositeAccountId if successful otherwise error, plane
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> likePlane(long planeId,int accountId, boolean byOwner) throws ApplicationException;

	/*
	 * @param planeId:(long) must be not null
	 * @param accountId:(int)
	 * @return canMatchedAgain if successful otherwise error or plane
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> throwPlane(long planeId,int accountId) throws ApplicationException;
	
	/*
	 * @param planeId:(long)
	 * @param accountId:(int)
	 * @return clearMsgId, notifiedInfo if successful otherwise error
	 * @throws ApplicationException if failed 
	 */ 
	
	public Map<String, Object> clearPlane(long planeId, int accountId) throws ApplicationException;
	
	/*
	 * @param planeId:(long) must be not null
	 * @param accountId:(int)
	 * @param byOwner:(boolean)
	 * @return succeed if successful otherwise error or plane
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> deletePlane(long planeId,int accountId, boolean byOwner) throws ApplicationException;
	
	/*
	 * get received planes
	 * @param accountId:(int)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxPlaneLimit
	 * @param forward:(boolean)
	 * @return more, planes (planes.messages, planes.category, planes.accountByOwnerId) if successful (may have more than one plane if more = true)
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> receivePlanes(int accountId, Long start, Long end, int limit, boolean forward) throws ApplicationException;
	
	/*
	 * get all received planeIds for synchronization
	 * @param accountId:(int)
	 * @param startId:(long) (exclusive)
	 * @param limit:(int) max(limit) = MaxPlaneIdLimit
	 * @return more, planeIds if successful (may have more than one planeId if more = true)
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> receivePlaneIds(int accountId, long startId, int limit) throws ApplicationException;
	
	/*
	 * get received planes
	 * @param accountId:(int)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxPlaneLimit
	 * @param forward:(boolean)
	 * @return more, planeIds if successful (may have more than one planeId if more = true)
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> receivePlaneIds(int accountId, Long start, Long end, int limit, boolean forward) throws ApplicationException;
	
	
	/*
	 * obtain all replied and undeleted planeIds for synchronization
	 * @param accountId:(int)
	 * @param startId:(long) (exclusive)
	 * @param limit:(int) max(limit) = MaxPlaneIdLimit
	 * @return more, planeIds if successful (may have more than one planeId if more = true)
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainPlaneIds(int accountId, long startId, int limit) throws ApplicationException;

	/*
	 * @param accountId:(int)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxPlaneLimit
	 * @param forward:(boolean)
	 * @return more, planeIds if successful (may have more than one planeId if more = true)
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainPlaneIds(int accountId, Long start, Long end, int limit, boolean forward) throws ApplicationException;

	
	/*
	 * @param accountId:(int)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxPlaneLimit
	 * @param forward:(boolean)
	 * @return more, planes(planes.messages, planes.category, planes.accountByTargetId, planes.accountByOwnerId) if successful (may have more than one plane if more = true)
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainPlanes(int accountId, Long start, Long end, int limit, boolean forward) throws ApplicationException;

	/*
	 * @param accountId:(int)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxPlaneLimit
	 * @param forward:(boolean)
	 * @return more, newPlanes (may have more than one plane if more = true)
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> getNewPlanes(int accountId, Long start, Long end, int limit, boolean forward) throws ApplicationException;

	
	/*
	 * @param accountId:(int)
	 * @param planeIds:(List<Long>) 
	 * @return planes
	 * @throws ApplicationException if failed 
	 */ 
	public List<Plane> getPlanes(int accountId, List<Long> planeIds) throws ApplicationException;

	
	/*
	 * @param accountId:(int)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxPlaneIdLimit
	 * @param forward:(boolean)
	 * @return more, oldPlanes (may have more than one plane if more = true)
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> getOldPlanes(int accountId, Long start, Long end, int limit, boolean forward) throws ApplicationException;

	
	/*
	 * @param accountId:(int)
	 * @param planeId:(long)
	 * @param startId:(Long) start messageId (can be null)
	 * @param limit:(int) max(limit) = MaxMessageLimit
	 * @param forward:(boolean)
	 * @return more, messages if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainMessages(int accountId, long planeId, Long startId,
			int limit, boolean forward) throws ApplicationException;
	
	
	/*
	 * @param accountId:(int)
	 * @param planeId:(long)	 
	 * @param lastMsgId:(long) (exclusive)
	 * @param byOwner:(boolean)
	 * @return succeed, lastMsgId
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> viewedMessages(int accountId, long planeId, long lastMsgId, boolean byOwner) throws ApplicationException;
}
