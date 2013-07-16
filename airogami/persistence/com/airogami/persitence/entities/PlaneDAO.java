package com.airogami.persitence.entities;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for Plane
 * entities. Transaction control of the save(), update() and delete() operations
 * must be handled externally by senders of these methods or must be manually
 * added to each of these methods for data to be persisted to the JPA datastore.
 * 
 * @see com.airogami.persitence.entities.Plane
 * @author MyEclipse Persistence Tools
 */

public class PlaneDAO {
	// property constants
	public static final String STATUS = "status";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String CITY = "city";
	public static final String PROVINCE = "province";
	public static final String COUNTRY = "country";
	public static final String SEX = "sex";
	public static final String MATCH_COUNT = "matchCount";
	public static final String MAX_MATCH_COUNT = "maxMatchCount";
	public static final String LIKES = "likes";

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved Plane entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * PlaneDAO.save(plane);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param plane
	 *            Plane entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Plane plane) {
		EntityManagerHelper.log("saving Plane instance", Level.INFO, null);
		try {
			getEntityManager().persist(plane);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent Plane entity. This operation must be performed within
	 * the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * PlaneDAO.delete(plane);
	 * EntityManagerHelper.commit();
	 * plane = null;
	 * </pre>
	 * 
	 * @param plane
	 *            Plane entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Plane plane) {
		EntityManagerHelper.log("deleting Plane instance", Level.INFO, null);
		try {
			plane = getEntityManager().getReference(Plane.class,
					plane.getPlaneId());
			getEntityManager().remove(plane);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved Plane entity and return it or a copy of it to
	 * the sender. A copy of the Plane entity parameter is returned when the JPA
	 * persistence mechanism has not previously been tracking the updated
	 * entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * plane = PlaneDAO.update(plane);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param plane
	 *            Plane entity to update
	 * @return Plane the persisted Plane entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Plane update(Plane plane) {
		EntityManagerHelper.log("updating Plane instance", Level.INFO, null);
		try {
			Plane result = getEntityManager().merge(plane);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Plane findById(Long planeId) {
		EntityManagerHelper.log("finding Plane instance with id: " + planeId,
				Level.INFO, null);
		try {
			Plane instance = getEntityManager().find(Plane.class, planeId);
			EntityManagerHelper.log("find successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Plane getReference(Long planeId) {
		EntityManagerHelper.log("getReferencing Plane instance with id: "
				+ planeId, Level.INFO, null);
		try {
			Plane instance = getEntityManager().getReference(Plane.class,
					planeId);
			EntityManagerHelper
					.log("getReference successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getReference failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void detach(Plane plane) {
		EntityManagerHelper.log("detaching Plane instance", Level.INFO, null);
		try {
			getEntityManager().detach(plane);
			EntityManagerHelper.log("detach successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("detach failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void refresh(Plane plane) {
		EntityManagerHelper.log("refreshing Plane instance", Level.INFO, null);
		try {
			getEntityManager().refresh(plane);
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

	public void remove(Plane plane) {
		EntityManagerHelper.log("removing Plane instance", Level.INFO, null);
		try {
			getEntityManager().remove(plane);
			EntityManagerHelper.log("remove successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("remove failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void flush() {
		EntityManagerHelper.log("flushing Plane instance", Level.INFO, null);
		try {
			getEntityManager().flush();
			EntityManagerHelper.log("flush successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("flush failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void clear() {
		EntityManagerHelper.log("clearing Plane instance", Level.INFO, null);
		try {
			getEntityManager().clear();
			EntityManagerHelper.log("clear successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("clear failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String removeByPlaneIdJPQL = "delete from Plane a where a.planeId in (?1)";

	public int removeByPlaneId(Long planeId) {
		EntityManagerHelper.log("removeByPlaneId", Level.INFO, null);
		int ret = 0;
		try {
			Query query = getEntityManager().createQuery(removeByPlaneIdJPQL);
			query.setParameter(1, planeId);
			ret = query.executeUpdate();
			EntityManagerHelper.log("removeByPlaneId successful", Level.INFO,
					null);
			return ret;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("removeByPlaneId failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String accountByTargetIdJPQL = "select a.accountByTargetId.accountId from Plane a where a.planeId = :planeId";

	public java.lang.Long getAccountByTargetId(java.lang.Long planeId) {
		EntityManagerHelper.log("getAccountByOwnerIdId with planeId" + planeId,
				Level.INFO, null);
		java.lang.Long accountId;
		try {
			Query query = getEntityManager().createQuery(accountByTargetIdJPQL);
			query.setParameter("planeId", planeId);
			accountId = (java.lang.Long) query.getSingleResult();
			EntityManagerHelper.log("getAccountByOwnerIdId successful",
					Level.INFO, null);
			return accountId;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getAccountByOwnerIdId failed",
					Level.SEVERE, re);
			throw re;
		}
	}

	private static final String categoryJPQL = "select a.category.categoryId from Plane a where a.planeId = :planeId";

	public java.lang.Short getCategory(java.lang.Long planeId) {
		EntityManagerHelper.log("getAccountByOwnerIdId with planeId" + planeId,
				Level.INFO, null);
		java.lang.Short categoryId;
		try {
			Query query = getEntityManager().createQuery(categoryJPQL);
			query.setParameter("planeId", planeId);
			categoryId = (java.lang.Short) query.getSingleResult();
			EntityManagerHelper.log("getAccountByOwnerIdId successful",
					Level.INFO, null);
			return categoryId;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getAccountByOwnerIdId failed",
					Level.SEVERE, re);
			throw re;
		}
	}

	private static final String accountByOwnerIdJPQL = "select a.accountByOwnerId.accountId from Plane a where a.planeId = :planeId";

	public java.lang.Long getAccountByOwnerId(java.lang.Long planeId) {
		EntityManagerHelper.log("getAccountByOwnerIdId with planeId" + planeId,
				Level.INFO, null);
		java.lang.Long accountId;
		try {
			Query query = getEntityManager().createQuery(accountByOwnerIdJPQL);
			query.setParameter("planeId", planeId);
			accountId = (java.lang.Long) query.getSingleResult();
			EntityManagerHelper.log("getAccountByOwnerIdId successful",
					Level.INFO, null);
			return accountId;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getAccountByOwnerIdId failed",
					Level.SEVERE, re);
			throw re;
		}
	}

	private static final String increaseMatchCountJPQL = "update Plane a set a.matchCount = a.matchCount + :count where a.planeId in (:planeId)";

	public void increaseMatchCount(java.lang.Long planeId, int count) {
		EntityManagerHelper.log(
				"increaseMaxMatchCount with planeId:" + planeId, Level.INFO,
				null);
		try {
			Query query = getEntityManager()
					.createQuery(increaseMatchCountJPQL);
			query.setParameter("planeId", planeId);
			query.setParameter("count", count);
			query.executeUpdate();
			EntityManagerHelper.log("increaseMaxMatchCount successful",
					Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("increaseMaxMatchCount failed",
					Level.SEVERE, re);
			throw re;
		}
	}

	private static final String increaseMaxMatchCountJPQL = "update Plane a set a.maxMatchCount = a.maxMatchCount + :count where a.planeId in (:planeId)";

	public void increaseMaxMatchCount(java.lang.Long planeId, int count) {
		EntityManagerHelper.log(
				"increaseMaxMatchCount with planeId:" + planeId, Level.INFO,
				null);
		try {
			Query query = getEntityManager().createQuery(
					increaseMaxMatchCountJPQL);
			query.setParameter("planeId", planeId);
			query.setParameter("count", count);
			query.executeUpdate();
			EntityManagerHelper.log("increaseMaxMatchCount successful",
					Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("increaseMaxMatchCount failed",
					Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all Plane entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Plane property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<Plane> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<Plane> findByProperty(String propertyName, final Object value,
			final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding Plane instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from Plane model where model."
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

	public List<Plane> findByStatus(Object status, int... rowStartIdxAndCount) {
		return findByProperty(STATUS, status, rowStartIdxAndCount);
	}

	public List<Plane> findByLongitude(Object longitude,
			int... rowStartIdxAndCount) {
		return findByProperty(LONGITUDE, longitude, rowStartIdxAndCount);
	}

	public List<Plane> findByLatitude(Object latitude,
			int... rowStartIdxAndCount) {
		return findByProperty(LATITUDE, latitude, rowStartIdxAndCount);
	}

	public List<Plane> findByCity(Object city, int... rowStartIdxAndCount) {
		return findByProperty(CITY, city, rowStartIdxAndCount);
	}

	public List<Plane> findByProvince(Object province,
			int... rowStartIdxAndCount) {
		return findByProperty(PROVINCE, province, rowStartIdxAndCount);
	}

	public List<Plane> findByCountry(Object country, int... rowStartIdxAndCount) {
		return findByProperty(COUNTRY, country, rowStartIdxAndCount);
	}

	public List<Plane> findBySex(Object sex, int... rowStartIdxAndCount) {
		return findByProperty(SEX, sex, rowStartIdxAndCount);
	}

	public List<Plane> findByMatchCount(Object matchCount,
			int... rowStartIdxAndCount) {
		return findByProperty(MATCH_COUNT, matchCount, rowStartIdxAndCount);
	}

	public List<Plane> findByMaxMatchCount(Object maxMatchCount,
			int... rowStartIdxAndCount) {
		return findByProperty(MAX_MATCH_COUNT, maxMatchCount,
				rowStartIdxAndCount);
	}

	public List<Plane> findByLikes(Object likes, int... rowStartIdxAndCount) {
		return findByProperty(LIKES, likes, rowStartIdxAndCount);
	}

	/**
	 * Find all Plane entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<Plane> all Plane entities
	 */
	@SuppressWarnings("unchecked")
	public List<Plane> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper
				.log("finding all Plane instances", Level.INFO, null);
		try {
			final String queryString = "select model from Plane model";
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