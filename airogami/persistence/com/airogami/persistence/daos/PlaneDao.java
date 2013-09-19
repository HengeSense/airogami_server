package com.airogami.persistence.daos;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.airogami.common.constants.PlaneConstants;
import com.airogami.persistence.entities.EntityManagerHelper;
import com.airogami.persistence.entities.NewPlane;
import com.airogami.persistence.entities.Plane;
import com.airogami.persistence.entities.PlaneDAO;

public class PlaneDao extends PlaneDAO {
	private final String planeExistsJPQL = "select count(plane.planeId) from Plane plane where plane.planeId = ?1 and ((plane.accountByOwnerId.accountId = ?2 and plane.status = ?3 and plane.deletedByOwner = 0) or (plane.accountByTargetId.accountId = ?2 and plane.deletedByTarget = 0))";

	public boolean planeExists(long planeId, long accountId) {
		EntityManagerHelper.log("planeExistsing with planeId = " + planeId + " and accountId = " + accountId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					planeExistsJPQL);
			query.setParameter(1, planeId);
			query.setParameter(2, accountId);
			query.setParameter(3, PlaneConstants.StatusReplied);
			//query.setParameter(4, new Timestamp(System.currentTimeMillis()));
			int count = query.executeUpdate();
			EntityManagerHelper
					.log("planeExists successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("planeExists failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String getPlaneJPQL = "select plane from Plane plane where plane.planeId = ?1 and ((plane.accountByOwnerId.accountId = ?2 and plane.status = ?3 and plane.deletedByOwner = 0) or (plane.accountByTargetId.accountId = ?2 and plane.deletedByTarget = 0))";

	public Plane getPlane(long planeId, long accountId) {
		EntityManagerHelper.log("getPlaneing with planeId = " + planeId + " and accountId = " + accountId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					getPlaneJPQL);
			query.setParameter(1, planeId);
			query.setParameter(2, accountId);
			query.setParameter(3, PlaneConstants.StatusReplied);
			//query.setParameter(4, new Timestamp(System.currentTimeMillis()));
			List<Plane> planes= query.getResultList();
			Plane plane = null;
			if(planes.size() > 0){
				plane = planes.get(0);
			}
			EntityManagerHelper
					.log("getPlane successful", Level.INFO, null);
			return plane;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getPlane failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	//CURRENT_TIMESTAMP
	private final String verifyReplyJPQL = "update Plane plane set plane.status = ?3, plane.updatedTime = ?4 where plane.planeId = ?1 and ((plane.accountByOwnerId.accountId = ?2 and plane.status = ?3 and plane.deletedByOwner = 0) or (plane.accountByTargetId.accountId = ?2 and plane.deletedByTarget = 0))";

	public boolean verifyReply(long planeId, long ownerId) {
		EntityManagerHelper.log("verifyReplying with planeId = " + planeId + " and ownerId = " + ownerId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					verifyReplyJPQL);
			query.setParameter(1, planeId);
			query.setParameter(2, ownerId);
			query.setParameter(3, PlaneConstants.StatusReplied);
			query.setParameter(4, new Timestamp(System.currentTimeMillis()));
			int count = query.executeUpdate();
			EntityManagerHelper
					.log("verifyReply successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("verifyReply failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String getNotifiedInfoSQL = "select OWNER_ID, FULL_NAME from PLANE, PROFILE  where PLANE_ID = ?1 and DELETED_BY_OWNER = 0 and OWNER_ID <> ?2 and ACCOUNT_ID = ?2 union select TARGET_ID, FULL_NAME from PLANE, PROFILE where PLANE_ID = ?1 and DELETED_BY_TARGET = 0 and TARGET_ID <> ?2 and ACCOUNT_ID = ?2";

	public Object[] getNotifiedInfo(long planeId, long accountId) {
		EntityManagerHelper.log("getAccountIdsing with planeId = " + planeId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createNativeQuery(
					getNotifiedInfoSQL);
			query.setParameter(1, planeId);
			query.setParameter(2, accountId);
			List<Object[]> result = query.getResultList();
			Object[] notifiedInfo = null;
			if(result.size() == 1){
				notifiedInfo = result.get(0);
			}
			EntityManagerHelper
					.log("getAccountIds successful", Level.INFO, null);
			return notifiedInfo;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getAccountIds failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	//private final String updateIncJPQL = "update Plane plane set (select max(plane.updateInc) + 1 from Plane plane) where plane.planeId = ?1";
	private final String updateIncSQL = "update PLANE  set UPDATE_INC = (select max_value from (select max(UPDATE_INC) + 1 as max_value from PLANE) as tmp) WHERE PLANE_ID = ?";

	
	public void updateInc(long planeId) {
		EntityManagerHelper.log("updateIncing with planeId = " + planeId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createNativeQuery(
					updateIncSQL);
			query.setParameter(1, planeId);
			query.executeUpdate();
			EntityManagerHelper
					.log("updateInc successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("updateInc failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	private final String throwPlaneJPQL = "update Plane plane set plane.accountByTargetId = null where plane.planeId = ?1 and plane.status = ?3 and plane.accountByTargetId.accountId = ?2";

	public boolean throwPlane(long planeId, long accountId){
		EntityManagerHelper.log("throwPlaneing with planeId = " + planeId + " and accountId = " + accountId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					throwPlaneJPQL);
			query.setParameter(1, planeId);
			query.setParameter(2, accountId);
			query.setParameter(3, PlaneConstants.StatusNew);
			int count = query.executeUpdate();
			EntityManagerHelper
					.log("throwPlane successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("throwPlane failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String getPlaneMatchCountJPQL = "select plane.matchCount from Plane plane where plane.planeId = ?1";

	public int getPlaneMatchCount(long planeId){
		EntityManagerHelper.log("getPlaneMatchCounting with planeId = " + planeId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					getPlaneMatchCountJPQL);
			query.setParameter(1, planeId);
			int count = (Integer)query.getSingleResult();
			EntityManagerHelper
					.log("getPlaneMatchCount successful", Level.INFO, null);
			return count;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getPlaneMatchCount failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String canPlaneMatchJPQL = "select count(plane.planeId) from Plane plane where plane.planeId = ?1 and plane.matchCount < plane.maxMatchCount";

	//plane can be matched again
	public boolean canPlaneMatch(long planeId){
		EntityManagerHelper.log("canPlaneMatching with planeId = " + planeId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					canPlaneMatchJPQL);
			query.setParameter(1, planeId);
			long count = (Long)query.getSingleResult();
			EntityManagerHelper
					.log("canPlaneMatch successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("canPlaneMatch failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String deletePlaneByOwnerJPQL = "update Plane plane set plane.deletedByOwner = 1 where plane.deletedByOwner = 0 and plane.planeId = ?1 and plane.status = ?3 and plane.accountByOwnerId.accountId = ?2";

	private final String deletePlaneByTargetJPQL = "update Plane plane set plane.deletedByTarget = 1 where plane.deletedByTarget = 0 and plane.planeId = ?1 and plane.status = ?3 and plane.accountByTargetId.accountId = ?2";

	public boolean deletePlane(long planeId, long accountId, boolean byOwner){
		EntityManagerHelper.log("deletePlaneing with planeId = " + planeId + " and accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(byOwner){
				jpql = deletePlaneByOwnerJPQL;
			}
			else{
				jpql = deletePlaneByTargetJPQL;
			}
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					jpql);
			query.setParameter(1, planeId);
			query.setParameter(2, accountId);
			query.setParameter(3, PlaneConstants.StatusReplied);
			int count = query.executeUpdate();
			if(count == 1){
				this.increaseUpdateCount(planeId, 1);
			}
			EntityManagerHelper
					.log("deletePlane successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("deletePlane failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String likePlaneByOwnerJPQL = "update Plane plane set plane.likedByOwner = 1 where plane.likedByOwner = 0 and plane.planeId = ?1 and plane.status <> ?3 and plane.accountByOwnerId.accountId = ?2";

	private final String likePlaneByTargetJPQL = "update Plane plane set plane.likedByTarget = 1 where plane.likedByTarget = 0 and plane.planeId = ?1  and plane.status <> ?3 and plane.accountByTargetId.accountId = ?2";
    //can't return if deleted
	private final String likePlaneQueryByOwnerJPQL = "select plane.accountByTargetId.accountId from Plane plane where plane.planeId = ?1 and plane.deletedByTarget = 0";

	private final String likePlaneQueryByTargetJPQL = "select plane.accountByOwnerId.accountId from Plane plane where plane.planeId = ?1 and plane.deletedByOwner = 0";

	//return the opposite accountId
	public Long likePlane(long planeId, Long accountId, boolean byOwner){
		EntityManagerHelper.log("likePlaneing with planeId = " + planeId + " and accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			
			if(byOwner){
				jpql = likePlaneByOwnerJPQL;
			}
			else{
				jpql = likePlaneByTargetJPQL;
			}
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					jpql);
			query.setParameter(1, planeId);
			query.setParameter(2, accountId);
			query.setParameter(3, PlaneConstants.StatusNew);
			//query.setParameter(4, new Timestamp(System.currentTimeMillis()));
			accountId = null;
			if(query.executeUpdate() == 1){
				this.increaseUpdateCount(planeId, 1);
				//query
				if(byOwner){
					jpql = likePlaneQueryByOwnerJPQL;
				}
				else{
					jpql = likePlaneQueryByTargetJPQL;
				}
				query = EntityManagerHelper.getEntityManager().createQuery(
						jpql);
				query.setParameter(1, planeId);
				accountId = (Long)query.getSingleResult();				
			}
			EntityManagerHelper
					.log("likePlane successful", Level.INFO, null);
			return accountId;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("likePlane failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String getNewPlanesForwardJPQL = "select new NewPlane(plane.planeId, plane.updateInc, plane.updateCount) from Plane plane where ((plane.accountByOwnerId.accountId = ?1 and plane.deletedByOwner = 0 and plane.status = ?2) or (plane.accountByTargetId.accountId = ?1 and plane.deletedByTarget = 0)) and (?3 is null or plane.updateInc > ?3) and (?4 is null or plane.updateInc < ?4) order by plane.updateInc asc";

	private final String getNewPlanesBackwardJPQL = "select new NewPlane(plane.planeId, plane.updateInc, plane.updateCount) from Plane plane where ((plane.accountByOwnerId.accountId = ?1 and plane.deletedByOwner = 0 and plane.status = ?2) or (plane.accountByTargetId.accountId = ?1 and plane.deletedByTarget = 0)) and (?3 is null or plane.updateInc > ?3) and (?4 is null or plane.updateInc < ?4) order by plane.updateInc desc";

	public List<NewPlane> getNewPlanes(long accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("getNewPlanesing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = getNewPlanesForwardJPQL; 
			}
			else{
				jpql = getNewPlanesBackwardJPQL;
			}
			TypedQuery<NewPlane> query = EntityManagerHelper.getEntityManager().createQuery(
					jpql, NewPlane.class);
			query.setParameter(1, accountId);
			query.setParameter(2, PlaneConstants.StatusReplied);
			query.setParameter(3, start);
			query.setParameter(4, end);
			query.setMaxResults(limit);
			List<NewPlane> newPlanes = query.getResultList();
			EntityManagerHelper
					.log("getNewPlanes successful", Level.INFO, null);
			return newPlanes;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getNewPlanes failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String getPlanesJPQL = "select plane from Plane plane join fetch plane.category join fetch plane.accountByTargetId join fetch plane.accountByOwnerId where (plane.accountByOwnerId.accountId = ?1 or plane.accountByTargetId.accountId = ?1) and plane.planeId in (?2) ";

	public List<Plane> getPlanes(long accountId, List<Long> planeIds){
		EntityManagerHelper.log("getPlanesing with accountId = " + accountId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					getPlanesJPQL);
			query.setParameter(1, accountId);
			query.setParameter(2, planeIds);
			List<Plane> planes = query.getResultList();
			EntityManagerHelper
					.log("getPlanes successful", Level.INFO, null);
			return planes;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getPlanes failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String getPlaneIdsForwardJPQL = "select plane.planeId from Plane plane where ((plane.accountByOwnerId.accountId = ?1 and plane.deletedByOwner = 0 and plane.status = ?2) or (plane.accountByTargetId.accountId = ?1 and plane.deletedByTarget = 0)) and (?3 is null or plane.planeId > ?3) and (?4 is null or plane.planeId < ?4) order by plane.planeId asc";

	private final String getPlaneIdsBackwardJPQL = "select plane.planeId from Plane plane where ((plane.accountByOwnerId.accountId = ?1 and plane.deletedByOwner = 0 and plane.status = ?2) or (plane.accountByTargetId.accountId = ?1 and plane.deletedByTarget = 0)) and (?3 is null or plane.planeId > ?3) and (?4 is null or plane.planeId < ?4) order by plane.planeId desc";

	public List<Long> getPlaneIds(long accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("getPlaneIdsing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = getPlaneIdsForwardJPQL;
			}
			else{
				jpql = getPlaneIdsBackwardJPQL;
			}
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					jpql);
			query.setParameter(1, accountId);
			query.setParameter(2, PlaneConstants.StatusReplied);
			query.setParameter(3, start);
			query.setParameter(4, end);
			query.setMaxResults(limit);
			List<Long> planeIds = query.getResultList();
			EntityManagerHelper
					.log("getPlaneIds successful", Level.INFO, null);
			return planeIds;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getPlaneIds failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String obtainPlanesForwardJPQL = "select plane from Plane plane join fetch plane.category join fetch plane.accountByTargetId join fetch plane.accountByOwnerId where ((plane.accountByOwnerId.accountId = ?1 and plane.deletedByOwner = 0) or (plane.accountByTargetId.accountId = ?1 and plane.deletedByTarget = 0)) and plane.status = ?2 and (?3 is null or plane.updateInc > ?3) and (?4 is null or plane.updateInc < ?4) order by plane.updateInc asc";

	private final String obtainPlanesBackwardJPQL = "select plane from Plane plane join fetch plane.category join fetch plane.accountByTargetId join fetch plane.accountByOwnerId where ((plane.accountByOwnerId.accountId = ?1 and plane.deletedByOwner = 0) or (plane.accountByTargetId.accountId = ?1 and plane.deletedByTarget = 0)) and plane.status = ?2 and (?3 is null or plane.updateInc > ?3) and (?4 is null or plane.updateInc < ?4) order by plane.updateInc desc";

	public List<Plane> obtainPlanes(long accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("obtainPlanesing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = obtainPlanesForwardJPQL; 
			}
			else{
				jpql = obtainPlanesBackwardJPQL;
			}
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					jpql);
			query.setParameter(1, accountId);
			query.setParameter(2, PlaneConstants.StatusReplied);
			query.setParameter(3, start);
			query.setParameter(4, end);
			query.setMaxResults(limit);
			List<Plane> planes = query.getResultList();
			EntityManagerHelper
					.log("obtainPlanes successful", Level.INFO, null);
			return planes;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("obtainPlanes failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String obtainPlaneIdsForwardJPQL = "select plane.planeId from Plane plane where ((plane.accountByOwnerId.accountId = ?1 and plane.deletedByOwner = 0) or (plane.accountByTargetId.accountId = ?1 and plane.deletedByTarget = 0)) and plane.status = ?2 and (?3 is null or plane.updateInc > ?3) and (?4 is null or plane.updateInc < ?4) order by plane.updateInc asc";

	private final String obtainPlaneIdsBackwardJPQL = "select plane.planeId from Plane plane where ((plane.accountByOwnerId.accountId = ?1 and plane.deletedByOwner = 0) or (plane.accountByTargetId.accountId = ?1 and plane.deletedByTarget = 0)) and plane.status = ?2 and (?3 is null or plane.updateInc > ?3) and (?4 is null or plane.updateInc < ?4) order by plane.updateInc desc";

	public List<Long> obtainPlaneIds(long accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("obtainPlaneIdsing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = obtainPlaneIdsForwardJPQL;
			}
			else{
				jpql = obtainPlaneIdsBackwardJPQL;
			}
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					jpql);
			query.setParameter(1, accountId);
			query.setParameter(2, PlaneConstants.StatusReplied);
			query.setParameter(3, start);
			query.setParameter(4, end);
			query.setMaxResults(limit);
			List<Long> planeIds = query.getResultList();
			EntityManagerHelper
					.log("obtainPlaneIds successful", Level.INFO, null);
			return planeIds;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("obtainPlaneIds failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String obtainPlaneIdsJPQL = "select plane.planeId from Plane plane where ((plane.accountByOwnerId.accountId = ?1 and plane.deletedByOwner = 0) or (plane.accountByTargetId.accountId = ?1 and plane.deletedByTarget = 0)) and plane.status = ?3 and plane.planeId > ?2 order by plane.planeId asc";

	public List<Long> obtainPlaneIds(long accountId, long planeId, int limit){
		EntityManagerHelper.log("obtainPlaneIdsing with accountId = " + accountId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					obtainPlaneIdsJPQL);
			query.setParameter(1, accountId);
			query.setParameter(2, planeId);
			query.setParameter(3, PlaneConstants.StatusReplied);
			query.setMaxResults(limit);
			List<Long> planeIds = query.getResultList();
			EntityManagerHelper
					.log("obtainPlaneIds successful", Level.INFO, null);
			return planeIds;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("obtainPlaneIds failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String receivePlanesForwardJPQL = "select plane from Plane plane join fetch plane.messages join fetch plane.category join fetch plane.accountByTargetId join fetch plane.accountByOwnerId join fetch plane.accountByOwnerId.profile where plane.status = ?2 and plane.accountByTargetId.accountId = ?1 and (?3 is null or plane.updateInc > ?3) and (?4 is null or plane.updateInc < ?4) order by plane.updateInc asc";

	private final String receivePlanesBackwardJPQL = "select plane from Plane plane join fetch plane.messages join fetch plane.category join fetch plane.accountByTargetId join fetch plane.accountByOwnerId join fetch plane.accountByOwnerId.profile where plane.status = ?2 and plane.accountByTargetId.accountId = ?1 and (?3 is null or plane.updateInc > ?3) and (?4 is null or plane.updateInc < ?4) order by plane.updateInc desc";

	public List<Plane> receivePlanes(long accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("receivePlanesing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = receivePlanesForwardJPQL;
			}
			else{
				jpql = receivePlanesBackwardJPQL;
			}
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					jpql);
			query.setParameter(1, accountId);
			query.setParameter(2, PlaneConstants.StatusNew);
			query.setParameter(3, start);
			query.setParameter(4, end);
			query.setMaxResults(limit);
			List<Plane> planes = query.getResultList();
			EntityManagerHelper
					.log("receivePlanes successful", Level.INFO, null);
			return planes;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("receivePlanes failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String receivePlaneIdsForwardJPQL = "select plane.planeId from Plane plane where plane.status = ?2 and plane.accountByTargetId.accountId = ?1 and (?3 is null or plane.updateInc > ?3) and (?4 is null or plane.updateInc < ?4) order by plane.updateInc asc";

	private final String receivePlaneIdsBackwardJPQL = "select plane.planeId from Plane plane where plane.status = ?2 and plane.accountByTargetId.accountId = ?1 and (?3 is null or plane.updateInc > ?3) and (?4 is null or plane.updateInc < ?4) order by plane.updateInc desc";

	public List<Long> receivePlaneIds(long accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("receivePlaneIdsing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = receivePlaneIdsForwardJPQL;
			}
			else{
				jpql = receivePlaneIdsBackwardJPQL;
			}
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					jpql);
			query.setParameter(1, accountId);
			query.setParameter(2, PlaneConstants.StatusNew);
			query.setParameter(3, start);
			query.setParameter(4, end);
			query.setMaxResults(limit);
			List<Long> planeIds = query.getResultList();
			EntityManagerHelper
					.log("receivePlaneIds successful", Level.INFO, null);
			return planeIds;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("receivePlaneIds failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String receivePlaneIdsJPQL = "select plane.planeId from Plane plane where plane.status = ?3 and plane.accountByTargetId.accountId = ?1 and plane.planeId > ?2 order by plane.planeId asc";

	public List<Long> receivePlaneIds(long accountId, long startId, int limit){
		EntityManagerHelper.log("receivePlaneIdsing with accountId = " + accountId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					receivePlaneIdsJPQL);
			query.setParameter(1, accountId);
			query.setParameter(2, startId);
			query.setParameter(3, PlaneConstants.StatusNew);
			query.setMaxResults(limit);
			List<Long> planeIds = query.getResultList();
			EntityManagerHelper
					.log("receivePlaneIds successful", Level.INFO, null);
			return planeIds;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("receivePlaneIds failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String pickupFindJPQL = "select plane.planeId from Plane plane, Profile profile where profile.accountId = ?1 and plane.status = ?2 and plane.accountByTargetId is null and plane.matchCount < plane.maxMatchCount and plane.accountByOwnerId.accountId <> ?1 and (plane.sex = 0 or plane.sex = profile.sex) and (plane.city is null or plane.city = '' or plane.city = profile.city) and (plane.province is null or plane.province = '' or plane.province = profile.province) and (plane.country is null or plane.country = '' or plane.country = profile.country)  and (plane.birthdayLower is null or plane.birthdayLower <= profile.birthday) and (plane.birthdayUpper is null or plane.birthdayUpper >= profile.birthday) and (plane.language is null or plane.language = '' or profile.language = plane.language) and  not exists (select planeHist from PlaneHist planeHist where planeHist.id.planeId = plane.planeId and profile.accountId = planeHist.id.accountId)";
	private final String matchJPQL = "update Plane plane set plane.accountByTargetId = ?1, plane.updatedTime = ?4 where plane.planeId in (?2) and plane.status = ?3 and plane.accountByTargetId is null and plane.matchCount < plane.maxMatchCount and not exists (select planeHist from PlaneHist planeHist where planeHist.id.planeId = plane.planeId and ?1 = planeHist.account)";
	private final String pickupQueryJPQL = "select plane from Plane plane join fetch plane.messages join fetch plane.category join fetch plane.accountByTargetId join fetch plane.accountByOwnerId join fetch plane.accountByOwnerId.profile where plane.accountByTargetId.accountId = ?1 and plane.planeId in (?2)";
	
	public List<Plane> pickup(long accountId, int count){
		EntityManagerHelper.log("pickuping plane with accountId = " + accountId + " and count = " + count, Level.INFO, null);
		try {
			List<Plane> planes = Collections.emptyList();
			//find
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					pickupFindJPQL);
			query.setParameter(1, accountId);
			query.setParameter(2, PlaneConstants.StatusNew);
			query.setMaxResults(count);
			List<Long> planeIds = query.getResultList();
			if(planeIds.size() > 0){
				//match
				query = EntityManagerHelper.getEntityManager().createQuery(
						matchJPQL);
				query.setParameter(1, DaoUtils.accountDao.getReference(accountId));
				query.setParameter(2, planeIds);
				query.setParameter(3, PlaneConstants.StatusNew);
				query.setParameter(4, new Timestamp(System.currentTimeMillis()));
				if(query.executeUpdate() > 0){
					//query
					query = EntityManagerHelper.getEntityManager().createQuery(
							pickupQueryJPQL);
					query.setParameter(1, accountId);
					query.setParameter(2, planeIds);
					planes = query.getResultList();
					Iterator<Plane> iter = planes.iterator();
					while(iter.hasNext()){
						Plane plane = iter.next();
						this.updateInc(plane.getPlaneId());
					}
				}
			}
			
			EntityManagerHelper
					.log("pickup plane successful", Level.INFO, null);
			return planes;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("pickup plane failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/*
	 * set targetId = accountId for a plane
	 * @return rematch
	 */
	public boolean match(long planeId, long accountId) {
		EntityManagerHelper.log("matching plane with planeId = " + planeId + " and accountId = " + accountId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					matchJPQL);
			query.setParameter(1, DaoUtils.accountDao.getReference(accountId));
			query.setParameter(2, planeId);
			query.setParameter(3, PlaneConstants.StatusNew);
			query.setParameter(4, new Timestamp(System.currentTimeMillis()));
			int count = query.executeUpdate();
			if(count == 1){
				this.updateInc(planeId);
			}
			EntityManagerHelper
					.log("match plane successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("match plane failed", Level.SEVERE, re);
			throw re;
		}
	} 
	
}
