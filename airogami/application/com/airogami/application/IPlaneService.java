package com.airogami.application;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.airogami.application.exception.ApplicationException;
import com.airogami.persitence.entities.Category;
import com.airogami.persitence.entities.Message;
import com.airogami.persitence.entities.Plane;

public interface IPlaneService {
	
	public final int MaxPlaneLimit = 50;
	
	public final int MaxMessageLimit = 50;
	
	/*
	 * @param category:(Category) must be not null, have name
	 * @return category if successful
	 * @throws ApplicationException if failed 
	 */
	public Category createCatrgory(Category category) throws ApplicationException;
	
	/*
	 * @param plane:(Plane) must be not null, have plane.message, plane.category
	 * @param ownerId:(long) must exist
	 * @return plane, plane.messages, plane.category if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Plane sendPlane(Plane plane, long ownerId) throws ApplicationException;
	
	/*
	 * @param planeId:(long) must exist
	 * @return plane, plane.messages, plane.category, plane.accountByOwnerId if successful or null if (not exist or already matched)
	 * @throws ApplicationException if failed 
	 */ 
	public Plane matchPlane(long planeId) throws ApplicationException;

	/*
	 * @param planeId:(long) must be not null
	 * @param ownerId:(long) must be plane.accoutByOwnerId or plane.accountByTargetId
	 * @param message:(Message)
	 * @return message if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Message replyPlane(long planeId,long ownerId, Message message) throws ApplicationException;	
	
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
	 * @return message, message.plane, oppositeAccountId
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> likePlane(long planeId,long accountId, boolean byOwner) throws ApplicationException;

	/*
	 * @param planeId:(long) must be not null
	 * @param accountId:(long)
	 * @return canMatchedAgain if successful
	 * @throws ApplicationException if failed 
	 */ 
	public boolean throwPlane(long planeId,long accountId) throws ApplicationException;
	
	/*
	 * @param planeId:(long) must be not null
	 * @param accountId:(long)
	 * @param byOwner:(boolean)
	 * @throws ApplicationException if failed 
	 */ 
	public void deletePlane(long planeId,long accountId, boolean byOwner) throws ApplicationException;
	
	/*
	 * get received planes
	 * @param accountId:(long)
	 * @param startIdx:(int) (inclusive)
	 * @param start:(TimeStamp) start datetime (inclusive) can be null
	 * @param end:(TimeStamp) end datetime (inclusive) can be null
	 * @param limit:(int) max(limit) = MaxPlaneLimit
	 * @param forward:(boolean)
	 * @return more, planes if successful (may have more than one plane if more = true)
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> receivePlanes(long accountId, int startIdx, Timestamp start, Timestamp end, int limit, boolean forward) throws ApplicationException;
	
	/*
	 * @param accountId:(long)
	 * @param startIdx:(int) (inclusive)
	 * @param start:(TimeStamp) start datetime (inclusive) can be null
	 * @param end:(TimeStamp) end datetime (inclusive) can be null
	 * @param limit:(int) max(limit) = MaxPlaneLimit
	 * @param forward:(boolean)
	 * @return more, planes if successful (may have more than one plane if more = true)
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainPlanes(long accountId, int startIdx, Timestamp start, Timestamp end, int limit, boolean forward) throws ApplicationException;

	
	/*
	 * @param accountId:(long)
	 * @param planeId:(long)
	 * @param start:(String) start datetime (inclusive)
	 * @param limit:(int) max(limit) = MaxMessageLimit
	 * @param forward:(boolean)
	 * @return more, messages if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainMessages(long accountId, long planeId, int startIdx, Timestamp start, int limit, boolean forward) throws ApplicationException;
	
	
	/*
	 * @param accountId:(long)
	 * @param planeId:(long)	 
	 * @param last:(String)
	 * @param byOwner:(boolean)
	 * @return succeed
	 * @throws ApplicationException if failed 
	 */ 
	public boolean viewedMessages(long accountId, long planeId, Timestamp last, boolean byOwner) throws ApplicationException;
}
