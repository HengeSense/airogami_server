package com.airogami.persitence.daos;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;

import com.airogami.common.constants.PlaneConstants;
import com.airogami.persitence.entities.Account;
import com.airogami.persitence.entities.EntityManagerHelper;
import com.airogami.persitence.entities.Message;
import com.airogami.persitence.entities.Plane;
import com.airogami.persitence.entities.PlaneDAO;

public class PlaneDao extends PlaneDAO {
	private final String verifyReplyJPQL = "update Plane plane set plane.status = ?3  where plane.planeId = ?1 and plane.accountByOwnerId is not null and plane.accountByTargetId is not null and ((plane.accountByOwnerId.accountId = ?2 and plane.status = ?3) or plane.accountByTargetId.accountId = ?2)";

	public boolean verifyReply(long planeId, long ownerId) {
		EntityManagerHelper.log("verifyReplying with planeId = " + planeId + " and ownerId = " + ownerId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					verifyReplyJPQL);
			query.setParameter(1, planeId);
			query.setParameter(2, ownerId);
			query.setParameter(3, PlaneConstants.StatusReplied);
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
	
	private final String obtainPlanesForwardJPQL = "select plane from Plane plane where ((plane.accountByOwnerId.accountId = ?1 and plane.status = ?2) or plane.accountByTargetId.accountId = ?1) and plane.createdTime > ?3 order by plane.createdTime desc";

	private final String obtainPlanesBackwardJPQL = "select plane from Plane plane where ((plane.accountByOwnerId.accountId = ?1 and plane.status = ?2) or plane.accountByTargetId.accountId = ?1) and plane.createdTime < ?3 order by plane.createdTime desc";

	public List<Plane> obtainPlanes(long accountId, String start, int limit, boolean forward){
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
			query.setParameter(3, Timestamp.valueOf(start));
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
	
	
}
