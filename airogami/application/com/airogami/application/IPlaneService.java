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
	
	public final int MaxPlaneIdLimit = 1000;
	
	/*
	 * @param category:(Category) must be not null, have name
	 * @return category if successful
	 * @throws ApplicationException if failed 
	 */
	public Category createCatrgory(Category category) throws ApplicationException;
	
	/*
	 * @param plane:(Plane) must be not null, have plane.message, plane.category
	 * @param ownerId:(long) must exist
	 * @return plane, plane.messages, plane.category if successful or null if ownerId not exist
	 * @throws ApplicationException if failed 
	 */ 
	public Plane sendPlane(Plane plane, long ownerId) throws ApplicationException;
	
	/*
	 * @param planeId:(long) must exist
	 * @return succeed, rematch, accountId if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> matchPlane(long planeId) throws ApplicationException;

	/*
	 * @param planeId:(long) must be not null
	 * @param ownerId:(long) must be plane.accoutByOwnerId or plane.accountByTargetId
	 * @param message:(Message)
	 * @return message if successful or error if failed
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> replyPlane(long planeId,long ownerId, Message message) throws ApplicationException;	
	
	/*
	 * @param accountId:(long)
	 * @param count:(int) should > 0
	 * @return planes, planes.messages, planes.category, planes.accountByOwnerId if successful
	 * @throws ApplicationException if failed 
	 */
	public List<Plane> pickupPlane(long accountId, int count) throws ApplicationException;
	
	/*
	 * @param planeId:(long) must be not null
	 * @param accountId:(long)
	 * @param byOwner:(boolean)
	 * @return message, oppositeAccountId if successful otherwise error, plane
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> likePlane(long planeId,long accountId, boolean byOwner) throws ApplicationException;

	/*
	 * @param planeId:(long) must be not null
	 * @param accountId:(long)
	 * @return canMatchedAgain if successful otherwise error or plane
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> throwPlane(long planeId,long accountId) throws ApplicationException;
	
	/*
	 * @param planeId:(long) must be not null
	 * @param accountId:(long)
	 * @param byOwner:(boolean)
	 * @return succeed if successful otherwise error or plane
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> deletePlane(long planeId,long accountId, boolean byOwner) throws ApplicationException;
	
	/*
	 * get received planes
	 * @param accountId:(long)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxPlaneLimit
	 * @param forward:(boolean)
	 * @return more, planes (planes.messages, planes.category, planes.accountByOwnerId) if successful (may have more than one plane if more = true)
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> receivePlanes(long accountId, Long start, Long end, int limit, boolean forward) throws ApplicationException;
	
	/*
	 * get all received planeIds for synchronization
	 * @param accountId:(long)
	 * @param startId:(long) (exclusive)
	 * @param limit:(int) max(limit) = MaxPlaneIdLimit
	 * @return more, planeIds if successful (may have more than one planeId if more = true)
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> receivePlaneIds(long accountId, long startId, int limit) throws ApplicationException;
	
	/*
	 * get received planes
	 * @param accountId:(long)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxPlaneLimit
	 * @param forward:(boolean)
	 * @return more, planeIds if successful (may have more than one planeId if more = true)
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> receivePlaneIds(long accountId, Long start, Long end, int limit, boolean forward) throws ApplicationException;
	
	
	/*
	 * obtain all replied and undeleted planeIds for synchronization
	 * @param accountId:(long)
	 * @param startId:(long) (exclusive)
	 * @param limit:(int) max(limit) = MaxPlaneIdLimit
	 * @return more, planeIds if successful (may have more than one planeId if more = true)
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainPlaneIds(long accountId, long startId, int limit) throws ApplicationException;

	/*
	 * @param accountId:(long)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxPlaneLimit
	 * @param forward:(boolean)
	 * @return more, planeIds if successful (may have more than one planeId if more = true)
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainPlaneIds(long accountId, Long start, Long end, int limit, boolean forward) throws ApplicationException;

	
	/*
	 * @param accountId:(long)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxPlaneLimit
	 * @param forward:(boolean)
	 * @return more, planes(planes.messages, planes.category, planes.accountByTargetId, planes.accountByOwnerId) if successful (may have more than one plane if more = true)
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainPlanes(long accountId, Long start, Long end, int limit, boolean forward) throws ApplicationException;

	
	/*
	 * @param accountId:(long)
	 * @param planeId:(long)
	 * @param startId:(Long) start messageId (can be null)
	 * @param limit:(int) max(limit) = MaxMessageLimit
	 * @param forward:(boolean)
	 * @return more, messages if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainMessages(long accountId, long planeId, Long startId,
			int limit, boolean forward) throws ApplicationException;
	
	
	/*
	 * @param accountId:(long)
	 * @param planeId:(long)	 
	 * @param lastMsgId:(long) (exclusive)
	 * @param byOwner:(boolean)
	 * @return succeed
	 * @throws ApplicationException if failed 
	 */ 
	public boolean viewedMessages(long accountId, long planeId, long lastMsgId, boolean byOwner) throws ApplicationException;
}
