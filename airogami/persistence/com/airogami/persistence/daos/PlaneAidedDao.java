package com.airogami.persistence.daos;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.TypedQuery;

import com.airogami.common.constants.PlaneConstants;
import com.airogami.persistence.entities.EntityManagerHelper;
import com.airogami.persistence.entities.Plane;
import com.airogami.persistence.entities.PlaneDAO;

public class PlaneAidedDao extends PlaneDAO {
	
	private final String getPlaneJPQL = "select plane from Plane plane where plane.planeId = ?1 and ((plane.accountByOwnerId.accountId = ?2 and plane.status = ?3) or plane.accountByTargetId.accountId = ?2) and plane.deletedByO = 0 and plane.deletedByT = 0";

	public Plane getPlane(long planeId, int accountId) {
		EntityManagerHelper.log("getPlaneing with planeId = " + planeId + " and accountId = " + accountId, Level.INFO, null);
		try {
			TypedQuery<Plane> query = EntityManagerHelper.getEntityManager().createQuery(
					getPlaneJPQL, Plane.class);
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
	
	private final String planeExistsJPQL = "select count(plane) from Plane plane where plane.planeId = ?1 and ((plane.accountByOwnerId.accountId = ?2 and plane.status = ?3) or plane.accountByTargetId.accountId = ?2) and plane.deletedByO = 0 and plane.deletedByT = 0";

	public boolean planeExists(long planeId, int accountId) {
		EntityManagerHelper.log("planeExistsing with planeId = " + planeId + " and accountId = " + accountId, Level.INFO, null);
		try {
			TypedQuery<Long> query = EntityManagerHelper.getEntityManager().createQuery(
					planeExistsJPQL, Long.class);
			query.setParameter(1, planeId);
			query.setParameter(2, accountId);
			query.setParameter(3, PlaneConstants.StatusReplied);
			List<Long> result = query.getResultList();
			long count = 0;
			if(result.size() > 0){
				count = result.get(0);
			}
			EntityManagerHelper
					.log("planeExists successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("planeExists failed", Level.SEVERE, re);
			throw re;
		}
	}
}
