package com.airogami.presentation.logic;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.airogami.application.ServiceUtils;
import com.airogami.application.exception.ApplicationException;
import com.airogami.common.constants.AccountConstants;
import com.airogami.common.constants.PlaneConstants;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Category;
import com.airogami.persistence.entities.Chain;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.Plane;

public class PlaneManager {
	
	//private Timestamp baseTimestamp = Timestamp.valueOf("2013-01-01 00:00:00");
	
	/*
	 * @param category:(Category) must be not null, have name
	 * @return category if successful
	 * @throws AirogamiException if failed 
	 */
	public Category createCategory(Category category) throws AirogamiException{
		if (category == null || category.getName() == null || category.getName().length() == 0){
			throw new IllegalArgumentException("Illegal arguments in createCatrgory");
		}
		try {
			category = ServiceUtils.planeService.createCatrgory(category);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Plane_CreateCategory_Failure_Status,
					AirogamiException.Plane_CreateCategory_Failure_Message);
		}
		return category;
	}
	
	/*
	 * @param plane:(Plane) must be not null, have plane.message, plane.category
	 * @param ownerId:(long) must exist
	 * @return plane, plane.messages, plane.category if successful
	 * @throws AirogamiException if failed 
	 */ 
	public Plane sendPlane(Plane plane, long ownerId)throws AirogamiException {
		Category category = null;
		if (plane == null || plane.getMessages().size() == 0 ||(category = plane.getCategory()) == null){
			throw new IllegalArgumentException("Illegal arguments in sendPlane");
		}
        Message message = plane.getMessages().get(0);
        if(message.getType() == null
        		|| message.getContent() == null || message.getContent().length() == 0
        		|| category.getCategoryId() == null){
			throw new IllegalArgumentException("Illegal arguments in sendPlane");
		}
        plane.setMaxMatchCount(PlaneConstants.MaxMatchCount);
        plane.setMatchCount(0);
        plane.setStatus((short)PlaneConstants.StatusNew);
        plane.setLikes((short)0);
        if(plane.getSex() == null){
        	plane.setSex((short)AccountConstants.SexType_Unknown);
        }
        message.setPlane(plane);
		try {
			plane = ServiceUtils.planeService.sendPlane(plane, ownerId);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Plane_SendPlane_Failure_Status,
					AirogamiException.Plane_SendPlane_Failure_Message);
		}
		ServiceUtils.airogamiService.appendPlane(plane.getPlaneId());
		return plane;
	}
	
	/*
	 * @param planeId:(long) must exist
	 * @param accountId:(long) must be plane.accoutByOwnerId or plane.accountByTargetId
	 * @param message:(Message) must be not null, has type and content
	 * @return message if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Message replyPlane(long planeId,long accountId, Message message) throws AirogamiException
	{
		if(message == null || message.getType() == null
        		|| message.getContent() == null || message.getContent().length() == 0){
			throw new IllegalArgumentException("Illegal arguments in replyPlane");
		}
		try {
			message = ServiceUtils.planeService.replyPlane(planeId, accountId, message);
		} catch (ApplicationException re) {
			///re.printStackTrace();
			throw new AirogamiException(
					AirogamiException.Plane_ReplyPlane_Failure_Status,
					AirogamiException.Plane_ReplyPlane_Failure_Message);
		}
		return message;
	}
	
	/*
	 * pickup planes or chains
	 * @param accountId:(long)
	 * @return planes, chains if successful
	 * @throws AirogamiException if failed 
	 */
	public Map<String, Object> pickup(long accountId) throws AirogamiException{
		List<Plane> planes = Collections.emptyList();
		List<Chain> chains = Collections.emptyList();
		int[] counts = ManagerUtils.pickupCount();
		try {
			if(counts[0] > 0){
				planes = ServiceUtils.planeService.pickupPlane(accountId, counts[0]);
			}
			if(counts[1] > 0){
				chains = ServiceUtils.chainService.pickupChain(accountId, counts[1]);
			}
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Plane_PickupPlane_Failure_Status,
					AirogamiException.Plane_PickupPlane_Failure_Message);
		}
		Map<String, Object> result = new TreeMap<String, Object>();
		result.put("planes", planes);
		result.put("chains", chains);
		return result;
	}
	
	/*
	 * @param planeId:(long) must be not null
	 * @param accountId:(long) should = plane.accountByTarget.accountId
	 * @return canMatchAgain
	 * @throws ApplicationException if failed 
	 */ 
	public boolean throwPlane(long planeId,long accountId) throws AirogamiException{
		boolean canMatchAgain = false;
		try {
			canMatchAgain = ServiceUtils.planeService.throwPlane(planeId, accountId);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Plane_ThrowPlane_Failure_Status,
					AirogamiException.Plane_ThrowPlane_Failure_Message);
		}
		if(canMatchAgain){
			ServiceUtils.airogamiService.appendPlane(planeId);
		}
		return canMatchAgain;
	}
	
	/*
	 * @param planeId:(long) must be not null
	 * @param accountId:(long)
	 * @param byOwner:(boolean)
	 * @return message, message.plane oppositeAccountId
	 * @throws AirogamiException if failed 
	 */ 
	public Map<String, Object> likePlane(long planeId,long accountId, boolean byOwner) throws AirogamiException{
		try {
			return ServiceUtils.planeService.likePlane(planeId, accountId, byOwner);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Plane_LikePlane_Failure_Status,
					AirogamiException.Plane_LikePlane_Failure_Message);
		}
	}
	
	/*
	 * @param planeId:(long)
	 * @param accountId:(long)
	 * @param byOwner:(boolean)
	 * @return whether successful
	 * @throws AirogamiException if failed 
	 */ 
	public boolean deletePlane(long planeId,long accountId, boolean byOwner) throws AirogamiException
	{
		try {
			return ServiceUtils.planeService.deletePlane(planeId, accountId, byOwner);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Plane_DeletePlane_Failure_Status,
					AirogamiException.Plane_DeletePlane_Failure_Message);
		}
	}
	
	/*
	 * @param accountId:(long)
	 * @param startIdx:(int) (inclusive)
	 * @param start:(Timestamp) start datetime (exclusive), can be null
	 * @param end:(Timestamp) end datetime (exclusive), can be null
	 * @param limit:(int) must > 0, max(limit) = MaxPlaneLimit
	 * @param forward:(boolean)
	 * @return more, planes if successful(may have more than one plane if more = true)
	 * @throws AirogamiException if failed 
	 */ 
	public Map<String, Object> obtainPlanes(long accountId, int startIdx, Timestamp start, Timestamp end, int limit, boolean forward) throws AirogamiException{
		if(startIdx < 0){
			startIdx = 0;
		}
		Map<String, Object> result;
		try {
			result = ServiceUtils.planeService.obtainPlanes(accountId, startIdx, start, end, limit, forward);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Plane_ObtainPlanes_Failure_Status,
					AirogamiException.Plane_ObtainPlanes_Failure_Message);
		}
		return result;
	}
	
	/*
	 * @param accountId:(long)
	 * @param startIdx:(int) (inclusive)
	 * @param start:(Timestamp) start datetime (exclusive), can be null
	 * @param end:(Timestamp) end datetime (exclusive), can be null
	 * @param limit:(int) must > 0, max(limit) = MaxPlaneLimit
	 * @param forward:(boolean)
	 * @return more, planes if successful(may have more than one plane if more = true)
	 * @throws AirogamiException if failed 
	 */ 
	public Map<String, Object> receivePlanes(long accountId, int startIdx, Timestamp start, Timestamp end, int limit, boolean forward) throws AirogamiException{
		if(startIdx < 0){
			startIdx = 0;
		}
		Map<String, Object> result;
		try {
			result = ServiceUtils.planeService.receivePlanes(accountId, startIdx, start, end, limit, forward);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Plane_ReceivePlanes_Failure_Status,
					AirogamiException.Plane_ReceivePlanes_Failure_Message);
		}
		return result;
	}
	
	/*
	 * @param accountId:(long)
	 * @param planeId:(long)
	 * @param start:(String) start datetime (exclusive)
	 * @param limit:(int) max(limit) = MaxMessageLimit
	 * @return more, messages if successful
	 * @throws AirogamiException if failed 
	 */ 
	public Map<String, Object> obtainMessages(long accountId, long planeId, int startIdx, String start, int limit) throws AirogamiException{
		Timestamp timestamp = null;
		if(start != null && start.length() > 0){
			//may throw IllegalArgumentException
			timestamp = Timestamp.valueOf(start);			
		}
		if(startIdx < 0){
			startIdx = 0;
		}
		Map<String, Object> result;
		try {
			result = ServiceUtils.planeService.obtainMessages(accountId, planeId, startIdx, timestamp, limit, true);
			} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Plane_ObtainMessages_Failure_Status,
					AirogamiException.Plane_ObtainMessages_Failure_Message);
		}
		return result;
	}
	
	/*
	 * @param accountId:(long)
	 * @param planeId:(long)	 
	 * @param last:(String) can't be null or empty
	 * @param byOwner:(boolean)
	 * @return succeed
	 * @throws ApplicationException if failed 
	 */ 
	public boolean viewedMessages(long accountId, long planeId, String last, boolean byOwner) throws AirogamiException{
		if(last == null || last.length() == 0){
			throw new IllegalArgumentException("Illegal arguments in viewedMessages");
		}
		//may throw IllegalArgumentException
		Timestamp lastTimestamp = Timestamp.valueOf(last);
		long time = System.currentTimeMillis();
		if(lastTimestamp.getTime() > time){
			lastTimestamp.setTime(time);
		}	
		try {
			return ServiceUtils.planeService.viewedMessages(accountId, planeId, lastTimestamp, byOwner);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Plane_ViewedMessages_Failure_Status,
					AirogamiException.Plane_ViewedMessages_Failure_Message);
		}
	}

	
}
