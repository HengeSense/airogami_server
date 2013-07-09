package com.airogami.application;

import java.util.List;
import java.util.Map;

import com.airogami.application.exception.ApplicationException;
import com.airogami.persitence.entities.Message;
import com.airogami.persitence.entities.Plane;

public interface IPlaneService {
	
	public final int MaxPlaneLimit = 50;
	
	public final int MaxMessageLimit = 50;
	
	/*
	 * @param plane:(Plane) must be not null, have plane.message
	 * @param ownerId:(long) must exist
	 * @return plane, plane.message, plane.category if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Plane sendPlane(Plane plane, long ownerId) throws ApplicationException;
	
	/*
	 * @param planeId:(long) must exist
	 * @return plane, plane.message, plane.category, plane.accountByOwnerId if successful
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
	 * @param planeId:(long) must be not null
	 * @param accountId:(long)
	 * @return matchCount if successful
	 * @throws ApplicationException if failed 
	 */ 
	public int throwPlane(long planeId,long accountId) throws ApplicationException;
	
	/*
	 * @param planeId:(long) must be not null
	 * @param accountId:(long)
	 * @param byOwner:(boolean)
	 * @throws ApplicationException if failed 
	 */ 
	public void deletePlane(long planeId,long accountId, boolean byOwner) throws ApplicationException;
	
	/*
	 * @param accountId:(long)
	 * @param start:(String) start datetime (exclusive)
	 * @param limit:(int) max(limit) = MaxPlaneLimit
	 * @param forward:(boolean)
	 * @return more, planes if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainPlanes(long accountId, String start, int limit, boolean forward) throws ApplicationException;
	
	/*
	 * @param accountId:(long)
	 * @param planeId:(long)
	 * @param start:(String) start datetime (exclusive)
	 * @param limit:(int) max(limit) = MaxMessageLimit
	 * @param forward:(boolean)
	 * @return more, messages if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainMessages(long accountId, long planeId, String start, int limit, boolean forward) throws ApplicationException;
	
	
	/*
	 * @param accountId:(long)
	 * @param planeId:(long)	 
	 * @param last:(String)
	 * @param byOwner:(boolean)
	 * @throws ApplicationException if failed 
	 */ 
	public void viewedMessage(long accountId, long planeId, String last, boolean byOwner) throws ApplicationException;
}
