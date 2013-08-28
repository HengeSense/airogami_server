package com.airogami.persistence.entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * Profile entities. Transaction control of the save(), update() and delete()
 * operations must be handled externally by senders of these methods or must be
 * manually added to each of these methods for data to be persisted to the JPA
 * datastore.
 * 
 * @see com.airogami.persistence.entities.Profile
 * @author MyEclipse Persistence Tools
 */

public class ProfileDAO {
	// property constants
	public static final String FULL_NAME = "fullName";
	public static final String SCREEN_NAME = "screenName";
	public static final String SEX = "sex";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String STATUS = "status";
	public static final String CITY = "city";
	public static final String PROVINCE = "province";
	public static final String COUNTRY = "country";
	public static final String UPDATE_COUNT = "updateCount";
	public static final String LIKES_COUNT = "likesCount";
	public static final String SHOUT = "shout";
	public static final String LANGUAGE = "language";

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved Profile entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ProfileDAO.save(profile);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param profile
	 *            Profile entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Profile profile) {
		EntityManagerHelper.log("saving Profile instance", Level.INFO, null);
		try {
			getEntityManager().persist(profile);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent Profile entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ProfileDAO.delete(profile);
	 * EntityManagerHelper.commit();
	 * profile = null;
	 * </pre>
	 * 
	 * @param profile
	 *            Profile entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Profile profile) {
		EntityManagerHelper.log("deleting Profile instance", Level.INFO, null);
		try {
			profile = getEntityManager().getReference(Profile.class,
					profile.getAccountId());
			getEntityManager().remove(profile);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved Profile entity and return it or a copy of it
	 * to the sender. A copy of the Profile entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * profile = ProfileDAO.update(profile);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param profile
	 *            Profile entity to update
	 * @return Profile the persisted Profile entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Profile update(Profile profile) {
		EntityManagerHelper.log("updating Profile instance", Level.INFO, null);
		try {
			Profile result = getEntityManager().merge(profile);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Profile findById(Long accountId) {
		EntityManagerHelper.log("finding Profile instance with id: "
				+ accountId, Level.INFO, null);
		try {
			Profile instance = getEntityManager()
					.find(Profile.class, accountId);
			EntityManagerHelper.log("find successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Profile getReference(Long accountId) {
		EntityManagerHelper.log("getReferencing Profile instance with id: "
				+ accountId, Level.INFO, null);
		try {
			Profile instance = getEntityManager().getReference(Profile.class,
					accountId);
			EntityManagerHelper
					.log("getReference successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getReference failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void detach(Profile profile) {
		EntityManagerHelper.log("detaching Profile instance", Level.INFO, null);
		try {
			getEntityManager().detach(profile);
			EntityManagerHelper.log("detach successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("detach failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void refresh(Profile profile) {
		EntityManagerHelper
				.log("refreshing Profile instance", Level.INFO, null);
		try {
			getEntityManager().refresh(profile);
			EntityManagerHelper.log("refresh successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("refresh failed", Level.SEVERE, re);
			throw re;
		}
	}

	/*
	 * for persistent instance, remove directly
	 * 
	 * @see delete
	 */

	public void remove(Profile profile) {
		EntityManagerHelper.log("removing Profile instance", Level.INFO, null);
		try {
			getEntityManager().remove(profile);
			EntityManagerHelper.log("remove successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("remove failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void flush() {
		EntityManagerHelper.log("flushing Profile instance", Level.INFO, null);
		try {
			getEntityManager().flush();
			EntityManagerHelper.log("flush successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("flush failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void clear() {
		EntityManagerHelper.log("clearing Profile instance", Level.INFO, null);
		try {
			getEntityManager().clear();
			EntityManagerHelper.log("clear successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("clear failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String removeByAccountIdJPQL = "delete from Profile a where a.accountId in (?1)";

	public int removeByAccountId(Long accountId) {
		EntityManagerHelper.log("removeByAccountId", Level.INFO, null);
		int ret = 0;
		try {
			Query query = getEntityManager().createQuery(removeByAccountIdJPQL);
			query.setParameter(1, accountId);
			ret = query.executeUpdate();
			EntityManagerHelper.log("removeByAccountId successful", Level.INFO,
					null);
			return ret;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("removeByAccountId failed", Level.SEVERE,
					re);
			throw re;
		}
	}

	private static final String increaseUpdateCountJPQL = "update Profile a set a.updateCount = a.updateCount + :count where a.accountId in (:accountId)";

	public boolean increaseUpdateCount(java.lang.Long accountId, int count) {
		EntityManagerHelper.log("increaseLikesCount with accountId:"
				+ accountId, Level.INFO, null);
		try {
			Query query = getEntityManager().createQuery(
					increaseUpdateCountJPQL);
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

	private static final String increaseLikesCountJPQL = "update Profile a set a.likesCount = a.likesCount + :count where a.accountId in (:accountId)";

	public boolean increaseLikesCount(java.lang.Long accountId, int count) {
		EntityManagerHelper.log("increaseLikesCount with accountId:"
				+ accountId, Level.INFO, null);
		try {
			Query query = getEntityManager()
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

	/**
	 * Find all Profile entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Profile property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<Profile> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<Profile> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding Profile instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from Profile model where model."
					+ propertyName + "= :propertyValue";
			Query query = getEntityManager().createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find by property name failed",
					Level.SEVERE, re);
			throw re;
		}
	}

	public List<Profile> findByFullName(Object fullName,
			int... rowStartIdxAndCount) {
		return findByProperty(FULL_NAME, fullName, rowStartIdxAndCount);
	}

	public List<Profile> findByScreenName(Object screenName,
			int... rowStartIdxAndCount) {
		return findByProperty(SCREEN_NAME, screenName, rowStartIdxAndCount);
	}

	public List<Profile> findBySex(Object sex, int... rowStartIdxAndCount) {
		return findByProperty(SEX, sex, rowStartIdxAndCount);
	}

	public List<Profile> findByLongitude(Object longitude,
			int... rowStartIdxAndCount) {
		return findByProperty(LONGITUDE, longitude, rowStartIdxAndCount);
	}

	public List<Profile> findByLatitude(Object latitude,
			int... rowStartIdxAndCount) {
		return findByProperty(LATITUDE, latitude, rowStartIdxAndCount);
	}

	public List<Profile> findByStatus(Object status, int... rowStartIdxAndCount) {
		return findByProperty(STATUS, status, rowStartIdxAndCount);
	}

	public List<Profile> findByCity(Object city, int... rowStartIdxAndCount) {
		return findByProperty(CITY, city, rowStartIdxAndCount);
	}

	public List<Profile> findByProvince(Object province,
			int... rowStartIdxAndCount) {
		return findByProperty(PROVINCE, province, rowStartIdxAndCount);
	}

	public List<Profile> findByCountry(Object country,
			int... rowStartIdxAndCount) {
		return findByProperty(COUNTRY, country, rowStartIdxAndCount);
	}

	public List<Profile> findByUpdateCount(Object updateCount,
			int... rowStartIdxAndCount) {
		return findByProperty(UPDATE_COUNT, updateCount, rowStartIdxAndCount);
	}

	public List<Profile> findByLikesCount(Object likesCount,
			int... rowStartIdxAndCount) {
		return findByProperty(LIKES_COUNT, likesCount, rowStartIdxAndCount);
	}

	public List<Profile> findByShout(Object shout, int... rowStartIdxAndCount) {
		return findByProperty(SHOUT, shout, rowStartIdxAndCount);
	}

	public List<Profile> findByLanguage(Object language,
			int... rowStartIdxAndCount) {
		return findByProperty(LANGUAGE, language, rowStartIdxAndCount);
	}

	/**
	 * Find all Profile entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<Profile> all Profile entities
	 */
	@SuppressWarnings("unchecked")
	public List<Profile> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding all Profile instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from Profile model";
			Query query = getEntityManager().createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}