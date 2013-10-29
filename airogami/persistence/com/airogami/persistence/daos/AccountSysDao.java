package com.airogami.persistence.daos;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.airogami.common.notification.MessageNotifiedInfo;
import com.airogami.persistence.entities.AccountSysDAO;
import com.airogami.persistence.entities.EntityManagerHelper;

public class AccountSysDao extends AccountSysDAO {

	private static final String increaseBothPlaneIncJPQL = "update AccountSys a set a.planeInc = a.planeInc + :count where a.account = (select plane.accountByOwnerId from Plane plane where plane.planeId = :planeId) or a.account = (select plane.accountByTargetId from Plane plane where plane.planeId = :planeId)";

	public boolean increaseBothPlaneInc(long planeId, int count) {
		EntityManagerHelper.log(
				"increaseBothPlaneInc with planeId " + planeId, Level.INFO,
				null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(increaseBothPlaneIncJPQL);
			query.setParameter("planeId", planeId);
			query.setParameter("count", count);
			boolean result = query.executeUpdate() == 2;
			EntityManagerHelper.log("increaseBothPlaneInc successful", Level.INFO,
					null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper
					.log("increaseBothPlaneInc failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String getMsgDataIncJPQL = "select accountSys.msgDataInc from AccountSys accountSys where accountSys.accountId = ?1";

	public Integer getMsgDataInc(int accountId) {
		EntityManagerHelper.log("getMsgDataIncing with accountId = " + accountId, Level.INFO, null);
		try {
			TypedQuery<Integer> query = EntityManagerHelper.getEntityManager().createQuery(
					getMsgDataIncJPQL, Integer.class);
			query.setParameter(1, accountId);
			List<Integer> result = query.getResultList();
			Integer msgDataInc = null;
			if(result.size() == 1){
				msgDataInc = result.get(0);
			}
			EntityManagerHelper
					.log("getMsgDataInc successful", Level.INFO, null);
			return msgDataInc;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getMsgDataInc failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String verifyMsgDataIncJPQL = "update AccountSys accountSys set accountSys.msgDataInc = accountSys.msgDataInc where accountSys.accountId = ?1 and accountSys.msgDataInc = ?2";

	public boolean verifyMsgDataInc(int accountId, int msgDataInc) {
		EntityManagerHelper.log("verifyMsgDataIncing with accountId = " + accountId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					verifyMsgDataIncJPQL);
			query.setParameter(1, accountId);
			query.setParameter(2, msgDataInc);
			int count = query.executeUpdate();
			EntityManagerHelper
					.log("verifyMsgDataInc successful", Level.INFO, null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("verifyMsgDataInc failed", Level.SEVERE, re);
			throw re;
		}
	}
}
