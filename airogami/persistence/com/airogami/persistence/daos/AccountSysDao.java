package com.airogami.persistence.daos;

import java.util.logging.Level;

import javax.persistence.Query;

import com.airogami.persistence.entities.AccountSysDAO;
import com.airogami.persistence.entities.EntityManagerHelper;

public class AccountSysDao extends AccountSysDAO {

	private static final String increasePlaneIncJPQL = "update AccountSys a set a.planeInc = a.planeInc + :count where a.account = (select plane.accountByOwnerId from Plane plane where plane.planeId = :planeId) or a.account = (select plane.accountByTargetId from Plane plane where plane.planeId = :planeId)";

	public boolean increasePlaneInc(long planeId, int count) {
		EntityManagerHelper.log(
				"increasePlaneInc with planeId " + planeId, Level.INFO,
				null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(increasePlaneIncJPQL);
			query.setParameter("planeId", planeId);
			query.setParameter("count", count);
			boolean result = query.executeUpdate() == 2;
			EntityManagerHelper.log("increasePlaneInc successful", Level.INFO,
					null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper
					.log("increasePlaneInc failed", Level.SEVERE, re);
			throw re;
		}
	}
}
