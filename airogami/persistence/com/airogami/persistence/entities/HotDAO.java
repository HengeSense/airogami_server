package com.airogami.persistence.entities;

import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for Hot
 * entities. Transaction control of the save(), update() and delete() operations
 * must be handled externally by senders of these methods or must be manually
 * added to each of these methods for data to be persisted to the JPA datastore.
 * 
 * @see com.airogami.persistence.entities.Hot
 * @author MyEclipse Persistence Tools
 */

public class HotDAO {
	// property constants
	public static final String LIKES_COUNT = "likesCount";
	public static final String UPDATE_COUNT = "updateCount";

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved Hot entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * HotDAO.save(hot);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param hot
	 *            Hot entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Hot hot) {
		EntityManagerHelper.log("saving Hot instance", Level.INFO, null);
		try {
			getEntityManager().persist(hot);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent Hot entity. This operation must be performed within
	 * the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * HotDAO.delete(hot);
	 * EntityManagerHelper.commit();
	 * hot = null;
	 * </pre>
	 * 
	 * @param hot
	 *            Hot entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Hot hot) {
		EntityManagerHelper.log("deleting Hot instance", Level.INFO, null);
		try {
			hot = getEntityManager()
					.getReference(Hot.class, hot.getAccountId());
			getEntityManager().remove(hot);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved Hot entity and return it or a copy of it to
	 * the sender. A copy of the Hot entity parameter is returned when the JPA
	 * persistence mechanism has not previously been tracking the updated
	 * entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * hot = HotDAO.update(hot);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param hot
	 *            Hot entity to update
	 * @return Hot the persisted Hot entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Hot update(Hot hot) {
		EntityManagerHelper.log("updating Hot instance", Level.INFO, null);
		try {
			Hot result = getEntityManager().merge(hot);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Hot findById(Integer accountId) {
		EntityManagerHelper.log("finding Hot instance with id: " + accountId,
				Level.INFO, null);
		try {
			Hot instance = getEntityManager().find(Hot.class, accountId);
			EntityManagerHelper.log("find successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Hot getReference(Integer accountId) {
		EntityManagerHelper.log("getReferencing Hot instance with id: "
				+ accountId, Level.INFO, null);
		try {
			Hot instance = getEntityManager()
					.getReference(Hot.class, accountId);
			EntityManagerHelper
					.log("getReference successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getReference failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void detach(Hot hot) {
		EntityManagerHelper.log("detaching Hot instance", Level.INFO, null);
		try {
			getEntityManager().detach(hot);
			EntityManagerHelper.log("detach successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("detach failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void refresh(Hot hot) {
		EntityManagerHelper.log("refreshing Hot instance", Level.INFO, null);
		try {
			getEntityManager().refresh(hot);
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

	public void remove(Hot hot) {
		EntityManagerHelper.log("removing Hot instance", Level.INFO, null);
		try {
			getEntityManager().remove(hot);
			EntityManagerHelper.log("remove successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("remove failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void flush() {
		EntityManagerHelper.log("flushing Hot instance", Level.INFO, null);
		try {
			getEntityManager().flush();
			EntityManagerHelper.log("flush successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("flush failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void clear() {
		EntityManagerHelper.log("clearing Hot instance", Level.INFO, null);
		try {
			getEntityManager().clear();
			EntityManagerHelper.log("clear successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("clear failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String removeByAccountIdJPQL = "delete from Hot a where a.accountId in (?1)";

	public int removeByAccountId(Integer accountId) {
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

	private static final String increaseLikesCountJPQL = "update Hot a set a.likesCount = a.likesCount + :count where a.accountId in (:accountId)";

	public boolean increaseLikesCount(java.lang.Integer accountId, int count) {
		EntityManagerHelper.log("increaseLikesCount with accountId: "
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

	private static final String increaseUpdateCountJPQL = "update Hot a set a.updateCount = a.updateCount + :count where a.accountId in (:accountId)";

	public boolean increaseUpdateCount(java.lang.Integer accountId, int count) {
		EntityManagerHelper.log("increaseUpdateCount with accountId: "
				+ accountId, Level.INFO, null);
		try {
			Query query = getEntityManager().createQuery(
					increaseUpdateCountJPQL);
			query.setParameter("accountId", accountId);
			query.setParameter("count", count);
			boolean result = query.executeUpdate() == 1;
			EntityManagerHelper.log("increaseUpdateCount successful",
					Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("increaseUpdateCount failed", Level.SEVERE,
					re);
			throw re;
		}
	}

	/**
	 * Find all Hot entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Hot property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<Hot> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<Hot> findByProperty(String propertyName, final Object value,
			final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding Hot instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from Hot model where model."
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

	public List<Hot> findByLikesCount(Object likesCount,
			int... rowStartIdxAndCount) {
		return findByProperty(LIKES_COUNT, likesCount, rowStartIdxAndCount);
	}

	public List<Hot> findByUpdateCount(Object updateCount,
			int... rowStartIdxAndCount) {
		return findByProperty(UPDATE_COUNT, updateCount, rowStartIdxAndCount);
	}

	/**
	 * Find all Hot entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<Hot> all Hot entities
	 */
	@SuppressWarnings("unchecked")
	public List<Hot> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding all Hot instances", Level.INFO, null);
		try {
			final String queryString = "select model from Hot model";
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