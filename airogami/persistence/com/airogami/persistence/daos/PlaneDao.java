package com.airogami.persistence.daos;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.airogami.common.constants.PlaneConstants;
import com.airogami.common.notification.MessageNotifiedInfo;
import com.airogami.persistence.classes.NeoPlane;
import com.airogami.persistence.classes.OldPlane;
import com.airogami.persistence.entities.EntityManagerHelper;
import com.airogami.persistence.entities.Plane;
import com.airogami.persistence.entities.PlaneDAO;

public class PlaneDao extends PlaneAidedDao {
	
	private final String initLastMsgIdOfTJPQL = "update Plane plane set plane.lastMsgIdOfT = ?2, plane.neoMsgIdOfT = ?2 where plane.planeId = ?1";
	
	public void initLastMsgIdOfT(long planeId, long messageId) {
		EntityManagerHelper.log("initLastMsgIdOfTing with planeId = " + planeId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					initLastMsgIdOfTJPQL);
			query.setParameter(1, planeId);
			query.setParameter(2, messageId);
			query.executeUpdate();
			EntityManagerHelper
					.log("initLastMsgIdOfT successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("initLastMsgIdOfT failed", Level.SEVERE, re);
			throw re;
		}
	}	
	
	//private final String updateIncJPQL = "update Plane plane set (select max(plane.updateInc) + 1 from Plane plane) where plane.planeId = ?1";
	//private final String updateIncSQL = "update PLANE  set UPDATE_INC = (select max_value from (select max(UPDATE_INC) + 1 as max_value from PLANE) as tmp) WHERE PLANE_ID = ?";

	private final String updateIncByOwnerSQL = "update PLANE set OWNER_INC = (select PLANE_INC from ACCOUNT_SYS where ACCOUNT_ID = OWNER_ID) WHERE PLANE_ID = ?1";

	private final String updateIncByTargetSQL = "update PLANE set TARGET_INC = (select PLANE_INC from ACCOUNT_SYS where ACCOUNT_ID = TARGET_ID) WHERE PLANE_ID = ?1";

	
	public void updateInc(long planeId, int accountId, boolean byOwner) {
		EntityManagerHelper.log("updateIncing with planeId = " + planeId, Level.INFO, null);
		try {
			if(DaoUtils.accountSysDao.increasePlaneInc(accountId, 1)){
				String sql = null;
				if(byOwner){
					sql = updateIncByOwnerSQL;
				}
				else{
					sql = updateIncByTargetSQL;
				}
				Query query = EntityManagerHelper.getEntityManager().createNativeQuery(
						sql);
				query.setParameter(1, planeId);
				query.executeUpdate();
			}
			
			EntityManagerHelper
					.log("updateInc successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("updateInc failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String updateBothIncSQL = "update PLANE set OWNER_INC = (select PLANE_INC from ACCOUNT_SYS where ACCOUNT_ID = OWNER_ID), TARGET_INC = (select PLANE_INC from ACCOUNT_SYS where ACCOUNT_ID = TARGET_ID) WHERE PLANE_ID = ?1";
	
	public void updateBothInc(long planeId) {
		EntityManagerHelper.log("updateBothIncing with planeId = " + planeId, Level.INFO, null);
		try {
			if(DaoUtils.accountSysDao.increaseBothPlaneInc(planeId, 1)){
				Query query = EntityManagerHelper.getEntityManager().createNativeQuery(
						updateBothIncSQL);
				query.setParameter(1, planeId);
				query.executeUpdate();
			}
			
			EntityManagerHelper
					.log("updateBothInc successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("updateBothInc failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	//
	private final String throwPlaneJPQL = "update Plane plane set plane.accountByTargetId = null where plane.planeId = ?1 and plane.status = ?3 and plane.accountByTargetId.accountId = ?2";

	public boolean throwPlane(long planeId, int accountId){
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
			TypedQuery<Integer> query = EntityManagerHelper.getEntityManager().createQuery(
					getPlaneMatchCountJPQL, Integer.class);
			query.setParameter(1, planeId);
			int count = query.getSingleResult();
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
			TypedQuery<Long> query = EntityManagerHelper.getEntityManager().createQuery(
					canPlaneMatchJPQL, Long.class);
			query.setParameter(1, planeId);
			long count = query.getSingleResult();
			EntityManagerHelper
					.log("canPlaneMatch successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("canPlaneMatch failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String deletePlaneByOwnerJPQL = "update Plane plane set plane.deletedByO = 1, plane.updateCount = plane.updateCount + 1 where plane.deletedByO = 0 and plane.planeId = ?1 and plane.status = ?3 and plane.accountByOwnerId.accountId = ?2";

	private final String deletePlaneByTargetJPQL = "update Plane plane set plane.deletedByT = 1, plane.updateCount = plane.updateCount + 1 where plane.deletedByT = 0 and plane.planeId = ?1 and plane.status = ?3 and plane.accountByTargetId.accountId = ?2";

	public boolean deletePlane(long planeId, int accountId, boolean byOwner){
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
			//
			if(count == 1){
				updateBothInc(planeId);
			}
			EntityManagerHelper
					.log("deletePlane successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("deletePlane failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String likePlaneByOwnerJPQL = "update Plane plane set plane.likedByO = 1, plane.updateCount = plane.updateCount + 1 where plane.likedByO = 0 and plane.planeId = ?1 and plane.deletedByT = 0 and plane.deletedByO = 0 and plane.status <> ?3 and plane.accountByOwnerId.accountId = ?2";

	private final String likePlaneByTargetJPQL = "update Plane plane set plane.likedByT = 1, plane.updateCount = plane.updateCount + 1 where plane.likedByT = 0 and plane.planeId = ?1 and plane.deletedByT = 0 and plane.deletedByO = 0  and plane.status <> ?3 and plane.accountByTargetId.accountId = ?2";
    //can't return if deleted
	private final String likePlaneQueryByOwnerJPQL = "select plane.accountByTargetId.accountId from Plane plane where plane.planeId = ?1 and plane.deletedByT = 0";

	private final String likePlaneQueryByTargetJPQL = "select plane.accountByOwnerId.accountId from Plane plane where plane.planeId = ?1 and plane.deletedByO = 0";

	//return the opposite accountId
	public Integer likePlane(long planeId, Integer accountId, boolean byOwner){
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
				//this.increaseUpdateCount(planeId, 1);
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
				accountId = (Integer)query.getSingleResult();				
			}
			EntityManagerHelper
					.log("likePlane successful", Level.INFO, null);
			return accountId;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("likePlane failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	//private final String getNewPlanesForwardJPQL = "select new com.airogami.persistence.classes.NewPlane(plane.planeId, plane.updateInc, plane.updateCount) from Plane plane where ((plane.accountByOwnerId.accountId = ?1 and plane.deletedByO = 0 and plane.status = ?2) or (plane.accountByTargetId.accountId = ?1 and plane.deletedByT = 0)) and (?3 is null or plane.updateInc > ?3) and (?4 is null or plane.updateInc < ?4) order by plane.updateInc asc";

	//private final String getNewPlanesBackwardJPQL = "select new com.airogami.persistence.classes.NewPlane(plane.planeId, plane.updateInc, plane.updateCount) from Plane plane where ((plane.accountByOwnerId.accountId = ?1 and plane.deletedByO = 0 and plane.status = ?2) or (plane.accountByTargetId.accountId = ?1 and plane.deletedByT = 0)) and (?3 is null or plane.updateInc > ?3) and (?4 is null or plane.updateInc < ?4) order by plane.updateInc desc";

	// don't check double deletes
	private final String getNeoPlanesForwardSQL = "(select PLANE_ID as planeId, OWNER_INC as updateInc, UPDATE_COUNT as updateCount, CLEAR_MSG_ID as clearMsgId, LAST_MSG_ID_OF_T as lastMsgId, NEO_MSG_ID_OF_O as neoMsgId from PLANE where OWNER_ID = ?1 and DELETED_BY_O = 0 and STATUS = ?2 and (?3 is null or OWNER_INC > ?3) and (?4 is null or OWNER_INC < ?4) order by OWNER_INC asc limit ?5) union all (select PLANE_ID as planeId, TARGET_INC as updateInc, UPDATE_COUNT as updateCount, CLEAR_MSG_ID as clearMsgId, LAST_MSG_ID_OF_O as lastMsgId, NEO_MSG_ID_OF_T as neoMsgId from PLANE where TARGET_ID = ?1 and DELETED_BY_T = 0 and (?3 is null or TARGET_INC > ?3) and (?4 is null or TARGET_INC < ?4) order by TARGET_INC asc limit ?5) order by updateInc asc limit ?5";

	private final String getNeoPlanesBackwardSQL = "(select PLANE_ID as planeId, OWNER_INC as updateInc, UPDATE_COUNT as updateCount, CLEAR_MSG_ID as clearMsgId, LAST_MSG_ID_OF_T as lastMsgId, NEO_MSG_ID_OF_O as neoMsgId from PLANE where OWNER_ID = ?1 and DELETED_BY_O = 0 and STATUS = ?2 and (?3 is null or OWNER_INC > ?3) and (?4 is null or OWNER_INC < ?4) order by OWNER_INC desc limit ?5) union all (select PLANE_ID as planeId, TARGET_INC as updateInc, UPDATE_COUNT as updateCount, CLEAR_MSG_ID as clearMsgId, LAST_MSG_ID_OF_O as lastMsgId, NEO_MSG_ID_OF_T as neoMsgId from PLANE where TARGET_ID = ?1 and DELETED_BY_T = 0 and (?3 is null or TARGET_INC > ?3) and (?4 is null or TARGET_INC < ?4) order by TARGET_INC desc limit ?5) order by updateInc desc limit ?5";

	public List<NeoPlane> getNeoPlanes(int accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("getNeoPlanesing with accountId = " + accountId, Level.INFO, null);
		try {
			String sql = null;
			if(forward){
				sql = getNeoPlanesForwardSQL; 
			}
			else{
				sql = getNeoPlanesBackwardSQL;
			}
			Query query = EntityManagerHelper.getEntityManager().createNativeQuery(
					sql, NeoPlane.class);
			query.setParameter(1, accountId);
			query.setParameter(2, PlaneConstants.StatusReplied);
			query.setParameter(3, start);
			query.setParameter(4, end);
			query.setParameter(5, limit);
			//query.setMaxResults(limit);
			List<NeoPlane> neoPlanes = query.getResultList();
			EntityManagerHelper
					.log("getNeoPlanes successful", Level.INFO, null);
			return neoPlanes;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getNeoPlanes failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String getPlanesJPQL = "select plane from Plane plane join fetch plane.category join fetch plane.accountByTargetId join fetch plane.accountByOwnerId where (plane.accountByOwnerId.accountId = ?1 or plane.accountByTargetId.accountId = ?1) and plane.planeId in (?2) ";

	private final String getUpdatedPlanesJPQL = "select new com.airogami.persistence.classes.UpdatedPlane(plane.planeId, plane.status, plane.deletedByO, plane.deletedByT, plane.likedByO, plane.likedByT, plane.updateCount) from Plane plane where (plane.accountByOwnerId.accountId = ?1 or plane.accountByTargetId.accountId = ?1) and plane.planeId in (?2) ";

	//return List<Plane> or List<UpdatedPlane>
	public Object getPlanes(int accountId, List<Long> planeIds, boolean updated){
		EntityManagerHelper.log("getPlanesing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(updated){
				jpql = getUpdatedPlanesJPQL;
			}
			else{
				jpql = getPlanesJPQL;
			}
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					jpql);
			query.setParameter(1, accountId);
			query.setParameter(2, planeIds);
			Object planes = query.getResultList();
			EntityManagerHelper
					.log("getPlanes successful", Level.INFO, null);
			return planes;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getPlanes failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String getOldPlanesForwardJPQL = "select new com.airogami.persistence.classes.OldPlane(plane.planeId, plane.status, plane.lastMsgIdOfO, plane.lastMsgIdOfT) from Plane plane where ((plane.accountByOwnerId.accountId = ?1 and plane.status = ?2) or (plane.accountByTargetId.accountId = ?1)) and plane.deletedByT = 0 and plane.deletedByO = 0 and (?3 is null or plane.planeId > ?3) and (?4 is null or plane.planeId < ?4) order by plane.planeId asc";

	private final String getOldPlanesBackwardJPQL = "select new com.airogami.persistence.classes.OldPlane(plane.planeId, plane.status, plane.lastMsgIdOfO, plane.lastMsgIdOfT) from Plane plane where ((plane.accountByOwnerId.accountId = ?1 and plane.status = ?2) or (plane.accountByTargetId.accountId = ?1)) and plane.deletedByT = 0 and plane.deletedByO = 0 and (?3 is null or plane.planeId > ?3) and (?4 is null or plane.planeId < ?4) order by plane.planeId desc";

	public List<OldPlane> getOldPlanes(int accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("getOldPlanesing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = getOldPlanesForwardJPQL;
			}
			else{
				jpql = getOldPlanesBackwardJPQL;
			}
			TypedQuery<OldPlane> query = EntityManagerHelper.getEntityManager().createQuery(
					jpql, OldPlane.class);
			query.setParameter(1, accountId);
			query.setParameter(2, PlaneConstants.StatusReplied);
			query.setParameter(3, start);
			query.setParameter(4, end);
			query.setMaxResults(limit);
			List<OldPlane> oldPlanes = query.getResultList();
			EntityManagerHelper
					.log("getOldPlanes successful", Level.INFO, null);
			return oldPlanes;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getOldPlanes failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String obtainPlanesForwardJPQL = "select plane from Plane plane join fetch plane.category join fetch plane.accountByTargetId join fetch plane.accountByOwnerId where ((plane.accountByOwnerId.accountId = ?1 and plane.deletedByO = 0) or (plane.accountByTargetId.accountId = ?1 and plane.deletedByT = 0)) and plane.status = ?2 and (?3 is null or plane.updateInc > ?3) and (?4 is null or plane.updateInc < ?4) order by plane.updateInc asc";

	private final String obtainPlanesBackwardJPQL = "select plane from Plane plane join fetch plane.category join fetch plane.accountByTargetId join fetch plane.accountByOwnerId where ((plane.accountByOwnerId.accountId = ?1 and plane.deletedByO = 0) or (plane.accountByTargetId.accountId = ?1 and plane.deletedByT = 0)) and plane.status = ?2 and (?3 is null or plane.updateInc > ?3) and (?4 is null or plane.updateInc < ?4) order by plane.updateInc desc";

	public List<Plane> obtainPlanes(int accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("obtainPlanesing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = obtainPlanesForwardJPQL; 
			}
			else{
				jpql = obtainPlanesBackwardJPQL;
			}
			TypedQuery<Plane> query = EntityManagerHelper.getEntityManager().createQuery(
					jpql, Plane.class);
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
	
	private final String obtainPlaneIdsForwardJPQL = "select plane.planeId from Plane plane where ((plane.accountByOwnerId.accountId = ?1 and plane.deletedByO = 0) or (plane.accountByTargetId.accountId = ?1 and plane.deletedByT = 0)) and plane.status = ?2 and (?3 is null or plane.updateInc > ?3) and (?4 is null or plane.updateInc < ?4) order by plane.updateInc asc";

	private final String obtainPlaneIdsBackwardJPQL = "select plane.planeId from Plane plane where ((plane.accountByOwnerId.accountId = ?1 and plane.deletedByO = 0) or (plane.accountByTargetId.accountId = ?1 and plane.deletedByT = 0)) and plane.status = ?2 and (?3 is null or plane.updateInc > ?3) and (?4 is null or plane.updateInc < ?4) order by plane.updateInc desc";

	public List<Long> obtainPlaneIds(int accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("obtainPlaneIdsing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = obtainPlaneIdsForwardJPQL;
			}
			else{
				jpql = obtainPlaneIdsBackwardJPQL;
			}
			TypedQuery<Long> query = EntityManagerHelper.getEntityManager().createQuery(
					jpql, Long.class);
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
	
	private final String obtainPlaneIdsJPQL = "select plane.planeId from Plane plane where ((plane.accountByOwnerId.accountId = ?1 and plane.deletedByO = 0) or (plane.accountByTargetId.accountId = ?1 and plane.deletedByT = 0)) and plane.status = ?3 and plane.planeId > ?2 order by plane.planeId asc";

	public List<Long> obtainPlaneIds(int accountId, long planeId, int limit){
		EntityManagerHelper.log("obtainPlaneIdsing with accountId = " + accountId, Level.INFO, null);
		try {
			TypedQuery<Long> query = EntityManagerHelper.getEntityManager().createQuery(
					obtainPlaneIdsJPQL, Long.class);
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

	public List<Plane> receivePlanes(int accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("receivePlanesing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = receivePlanesForwardJPQL;
			}
			else{
				jpql = receivePlanesBackwardJPQL;
			}
			TypedQuery<Plane> query = EntityManagerHelper.getEntityManager().createQuery(
					jpql, Plane.class);
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

	public List<Long> receivePlaneIds(int accountId, Long start, Long end, int limit, boolean forward){
		EntityManagerHelper.log("receivePlaneIdsing with accountId = " + accountId, Level.INFO, null);
		try {
			String jpql = null;
			if(forward){
				jpql = receivePlaneIdsForwardJPQL;
			}
			else{
				jpql = receivePlaneIdsBackwardJPQL;
			}
			TypedQuery<Long> query = EntityManagerHelper.getEntityManager().createQuery(
					jpql, Long.class);
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

	public List<Long> receivePlaneIds(int accountId, long startId, int limit){
		EntityManagerHelper.log("receivePlaneIdsing with accountId = " + accountId, Level.INFO, null);
		try {
			TypedQuery<Long> query = EntityManagerHelper.getEntityManager().createQuery(
					receivePlaneIdsJPQL, Long.class);
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
	private final String matchJPQL = "update Plane plane set plane.accountByTargetId = ?1, plane.updatedTime = ?4, plane.source = ?5 where plane.planeId in (?2) and plane.status = ?3 and plane.accountByTargetId is null and plane.matchCount < plane.maxMatchCount and not exists (select planeHist from PlaneHist planeHist where planeHist.id.planeId = plane.planeId and ?1 = planeHist.account)";
	private final String pickupQueryJPQL = "select plane from Plane plane join fetch plane.messages join fetch plane.category join fetch plane.accountByTargetId join fetch plane.accountByOwnerId join fetch plane.accountByOwnerId.profile where plane.accountByTargetId.accountId = ?1 and plane.planeId in (?2)";
	
	public List<Plane> pickup(int accountId, int count){
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
				query.setParameter(5, PlaneConstants.SourcePickup);
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
						this.updateInc(plane.getPlaneId(), accountId, false);
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
	public boolean match(long planeId, int accountId) {
		EntityManagerHelper.log("matching plane with planeId = " + planeId + " and accountId = " + accountId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					matchJPQL);
			query.setParameter(1, DaoUtils.accountDao.getReference(accountId));
			query.setParameter(2, planeId);
			query.setParameter(3, PlaneConstants.StatusNew);
			query.setParameter(4, new Timestamp(System.currentTimeMillis()));
			query.setParameter(5, PlaneConstants.SourceReceive);
			int count = query.executeUpdate();
			if(count == 1){
				this.updateInc(planeId, accountId, false);
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
