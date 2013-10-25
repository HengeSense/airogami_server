package com.airogami.persistence.daos;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.airogami.persistence.entities.EntityManagerHelper;
import com.airogami.persistence.entities.Hot;
import com.airogami.persistence.entities.HotDAO;

public class HotDao extends HotDAO {

	private final String obtainHotJPQL = "select hot from Hot hot where hot.accountId = ?1 and (?2 is null or hot.updateCount > ?2)";

	public Hot obtainHot(long accountId, Integer updateCount) {
		EntityManagerHelper.log("obtainHoting", Level.INFO, null);
		try {
			TypedQuery<Hot> query = EntityManagerHelper.getEntityManager().createQuery(
					obtainHotJPQL, Hot.class);
			query.setParameter(1, accountId);
			query.setParameter(2, updateCount);
			List<Hot> result = query.getResultList();
			Hot hot = null;
			if (result.size() > 0) {
				hot = result.get(0);
			}
			EntityManagerHelper.log("obtainHot successful", Level.INFO,
					null);
			return hot;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("obtainHot failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private static final String increaseLikesCountJPQL = "update Hot a set a.likesCount = a.likesCount + :count, a.updateCount = a.updateCount + 1  where a.accountId in (:accountId)";

	public boolean increaseLikesCount(java.lang.Integer accountId, int count) {
		EntityManagerHelper.log("increaseLikesCount with accountId: "
				+ accountId, Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager()
					.createQuery(increaseLikesCountJPQL);
			query.setParameter("accountId", accountId);
			query.setParameter("count", count);
			boolean result = query.executeUpdate() == 1;
			EntityManagerHelper.log("increaseLikesCount successful",
					Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("increaseLikesCount failed", Level.SEVERE,
					re);
			throw re;
		}
	}
}
