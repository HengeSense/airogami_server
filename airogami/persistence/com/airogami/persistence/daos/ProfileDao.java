package com.airogami.persistence.daos;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;

import com.airogami.persistence.entities.EntityManagerHelper;
import com.airogami.persistence.entities.Profile;
import com.airogami.persistence.entities.ProfileDAO;

public class ProfileDao extends ProfileDAO {
	
	private final String obtainProfileJPQL = "select profile from Profile profile where profile.accountId = ?1 and (?2 is null or profile.updateCount > ?2)";

	public Profile obtainProfile(long accountId, Long updateCount) {
		EntityManagerHelper.log("obtainProfileing", Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					obtainProfileJPQL);
			query.setParameter(1, accountId);
			query.setParameter(2, updateCount);
			List<Profile> result = query.getResultList();
			Profile profile = null;
			if (result.size() > 0) {
				profile = result.get(0);
			}
			EntityManagerHelper.log("obtainProfile successful", Level.INFO,
					null);
			return profile;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("obtainProfile failed", Level.SEVERE, re);
			throw re;
		}
	}
}
