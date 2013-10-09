package com.airogami.persistence.daos;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.airogami.persistence.classes.AccountStatLeft;
import com.airogami.persistence.entities.AccountStatDAO;
import com.airogami.persistence.entities.EntityManagerHelper;

public class AccountStatDao extends AccountStatDAO {
	
	private final int updatePickupAndSendCountsTimeout = 5 * 60 * 1000; // 30 min
	
	private final String updatePickupAndSendCountsJPQL = "update AccountStat accountStat set accountStat.pickupLeftCount = accountStat.pickupCount, accountStat.sendLeftCount = accountStat.sendCount where accountStat.status = 0";

	public int updatePickupAndSendCounts() {
		EntityManagerHelper.log("updatePickupAndSendCountsing", Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					updatePickupAndSendCountsJPQL);
			query.setHint("javax.persistence.query.timeout", updatePickupAndSendCountsTimeout);
			//query.setParameter(1, accountId);
			int count = query.executeUpdate();
			EntityManagerHelper.log("updatePickupAndSendCounts successful", Level.INFO,
					null);
			return count;
		} catch (RuntimeException re) {
			EntityManagerHelper
					.log("updatePickupAndSendCounts failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String verifySendJPQL = "update AccountStat accountStat set accountStat.sendLeftCount = accountStat.sendLeftCount - 1 where accountStat.sendLeftCount > 0 and accountStat.accountId = ?1";

	public boolean verifySend(int accountId) {
		EntityManagerHelper.log("verifySending", Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					verifySendJPQL);
			query.setParameter(1, accountId);
			int count = query.executeUpdate();
			EntityManagerHelper.log("verifySend successful", Level.INFO,
					null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper
					.log("verifySend failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String verifyPickupJPQL = "update AccountStat accountStat set accountStat.pickupLeftCount = accountStat.pickupLeftCount - 1 where accountStat.pickupLeftCount > 0 and accountStat.accountId = ?1";

	public boolean verifyPickup(int accountId) {
		EntityManagerHelper.log("verifyPickuping", Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					verifyPickupJPQL);
			query.setParameter(1, accountId);
			int count = query.executeUpdate();
			EntityManagerHelper.log("verifyPickup successful", Level.INFO,
					null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper
					.log("verifyPickup failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private final String getSendAndPickupLeftCountsJPQL = "select new com.airogami.persistence.classes.AccountStatLeft(accountStat.sendLeftCount, accountStat.pickupLeftCount) from AccountStat accountStat where accountStat.accountId = ?1";

	public AccountStatLeft getSendAndPickupLeftCounts(int accountId) {
		EntityManagerHelper.log("getSendAndPickupLeftCountsing", Level.INFO, null);
		try {
			TypedQuery<AccountStatLeft> query = EntityManagerHelper.getEntityManager().createQuery(
					getSendAndPickupLeftCountsJPQL, AccountStatLeft.class);
			query.setParameter(1, accountId);
			List<AccountStatLeft> result = query.getResultList();
			AccountStatLeft counts = null;
			if(result.size() == 1){
				counts = result.get(0);
			}
			EntityManagerHelper.log("getSendAndPickupLeftCounts successful", Level.INFO,
					null);
			return counts;
		} catch (RuntimeException re) {
			EntityManagerHelper
					.log("getSendAndPickupLeftCounts failed", Level.SEVERE, re);
			throw re;
		}
	}
}
