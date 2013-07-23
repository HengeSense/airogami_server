package com.airogami.persistence.daos;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;

import com.airogami.common.constants.PlaneConstants;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.EntityManagerHelper;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.Plane;
import com.airogami.persistence.entities.PlaneDAO;

public class PlaneDao extends PlaneDAO {
	private final String verifyReplyJPQL = "update Plane plane set plane.status = ?3, plane.updatedTime = ?4 where plane.planeId = ?1 and ((plane.accountByOwnerId.accountId = ?2 and plane.status = ?3) or plane.accountByTargetId.accountId = ?2)";

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
	
	private final String deletePlaneByOwnerJPQL = "update Plane plane set plane.accountByOwnerId = null where plane.planeId = ?1 and plane.status = ?3 and plane.accountByOwnerId.accountId = ?2";

	private final String deletePlaneByTargetJPQL = "update Plane plane set plane.accountByTargetId = null where plane.planeId = ?1 and plane.status = ?3 and plane.accountByTargetId.accountId = ?2";

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
			EntityManagerHelper
					.log("deletePlane successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("deletePlane failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String likePlaneByOwnerJPQL = "update Plane plane set plane.likes = (plane.likes + 1), plane.updatedTime = ?4 where MOD(plane.likes, 16) = 0 and plane.planeId = ?1 and plane.status <> ?3 and plane.accountByOwnerId.accountId = ?2";

	private final String likePlaneByTargetJPQL = "update Plane plane set plane.likes = (plane.likes + 16), plane.updatedTime = ?4 where plane.likes < 16 and plane.planeId = ?1  and plane.status <> ?3 and plane.accountByTargetId.accountId = ?2";

	private final String likePlaneQueryByOwnerJPQL = "select plane.accountByTargetId.accountId from Plane plane where plane.planeId = ?1";

	private final String likePlaneQueryByTargetJPQL = "select plane.accountByOwnerId.accountId from Plane plane where plane.planeId = ?1";

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
			query.setParameter(4, new Timestamp(System.currentTimeMillis()));
			accountId = null;
			if(query.executeUpdate() == 1){
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
	
	private final String obtainPlanesForwardJPQL = "select plane from Plane plane join fetch plane.messages join fetch plane.category join fetch plane.accountByTargetId join fetch plane.accountByOwnerId where (plane.accountByOwnerId.accountId = ?1 or plane.accountByTargetId.accountId = ?1) and plane.status = ?2 and (?3 is null or plane.updatedTime >= ?3) and (?4 is null or plane.updatedTime <= ?4) order by plane.updatedTime asc";

	private final String obtainPlanesBackwardJPQL = "select plane from Plane plane join fetch plane.messages join fetch plane.category join fetch plane.accountByTargetId join fetch plane.accountByOwnerId where (plane.accountByOwnerId.accountId = ?1 or plane.accountByTargetId.accountId = ?1) and plane.status = ?2 and (?3 is null or plane.updatedTime >= ?3) and (?4 is null or plane.updatedTime <= ?4) order by plane.updatedTime desc";

	public List<Plane> obtainPlanes(long accountId, int startIdx, Timestamp start, Timestamp end, int limit, boolean forward){
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
			if(startIdx > 0){
				query.setFirstResult(startIdx);
			}
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
	
	private final String receivePlanesForwardJPQL = "select plane from Plane plane join fetch plane.messages join fetch plane.category join fetch plane.accountByTargetId where plane.status = ?2 and plane.accountByTargetId.accountId = ?1 and (?3 is null or plane.updatedTime >= ?3) and (?4 is null or plane.updatedTime <= ?4) order by plane.updatedTime asc";

	private final String receivePlanesBackwardJPQL = "select plane from Plane plane join fetch plane.messages join fetch plane.category join fetch plane.accountByTargetId where plane.status = ?2 and plane.accountByTargetId.accountId = ?1 and (?3 is null or plane.updatedTime >= ?3) and (?4 is null or plane.updatedTime <= ?4) order by plane.updatedTime desc";

	public List<Plane> receivePlanes(long accountId, int startIdx, Timestamp start, Timestamp end, int limit, boolean forward){
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
			if(startIdx > 0){
				query.setFirstResult(startIdx);
			}
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
	
	private final String pickupFindJPQL = "select plane.planeId from Plane plane, Account account where account.accountId = ?1 and plane.status = ?2 and plane.accountByTargetId is null and plane.matchCount < plane.maxMatchCount and plane.accountByOwnerId.accountId <> ?1 and (plane.sex = 0 or plane.sex = account.sex) and (plane.city is null or plane.city = '' or plane.city = account.city) and (plane.province is null or plane.province = '' or plane.province = account.province) and (plane.country is null or plane.country = '' or plane.country = account.country)";
	private final String matchJPQL = "update Plane plane set plane.accountByTargetId = ?1 where plane.planeId in (?2) and plane.status = ?3 and plane.accountByTargetId is null and plane.matchCount < plane.maxMatchCount";
	private final String pickupQueryJPQL = "select plane from Plane plane join fetch plane.messages join fetch plane.category join fetch plane.accountByOwnerId where plane.accountByTargetId.accountId = ?1 and plane.planeId in (?2)";
	
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
				if(query.executeUpdate() > 0){
					//query
					query = EntityManagerHelper.getEntityManager().createQuery(
							pickupQueryJPQL);
					query.setParameter(1, accountId);
					query.setParameter(2, planeIds);
					planes = query.getResultList();
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
			int count = query.executeUpdate();
			EntityManagerHelper
					.log("match plane successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("match plane failed", Level.SEVERE, re);
			throw re;
		}
	} 
	
}
