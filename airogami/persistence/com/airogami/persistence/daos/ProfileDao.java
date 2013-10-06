package com.airogami.persistence.daos;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.airogami.persistence.entities.EntityManagerHelper;
import com.airogami.persistence.entities.Profile;
import com.airogami.persistence.entities.ProfileDAO;

public class ProfileDao extends ProfileDAO {
	
	private final String obtainProfileJPQL = "select profile from Profile profile where profile.accountId = ?1 and (?2 is null or profile.updateCount > ?2)";

	public Profile obtainProfile(long accountId, Integer updateCount) {
		EntityManagerHelper.log("obtainProfileing", Level.INFO, null);
		try {
			TypedQuery<Profile> query = EntityManagerHelper.getEntityManager().createQuery(
					obtainProfileJPQL, Profile.class);
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
	
	private final String getFullNameJPQL = "select profile.fullName from Profile profile where profile.accountId = ?1";

	public String getFullName(long accountId) {
		EntityManagerHelper.log("getFullNameing", Level.INFO, null);
		try {
			TypedQuery<String> query = EntityManagerHelper.getEntityManager().createQuery(
					getFullNameJPQL, String.class);
			query.setParameter(1, accountId);
			List<String> result = query.getResultList();
			String fullName = null;
			if (result.size() > 0) {
				fullName = result.get(0);
			}
			EntityManagerHelper.log("getFullName successful", Level.INFO,
					null);
			return fullName;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getFullName failed", Level.SEVERE, re);
			throw re;
		}
	}
}
