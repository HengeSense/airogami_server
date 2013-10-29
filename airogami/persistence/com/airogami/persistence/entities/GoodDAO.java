package com.airogami.persistence.entities;

import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for Good
 * entities. Transaction control of the save(), update() and delete() operations
 * must be handled externally by senders of these methods or must be manually
 * added to each of these methods for data to be persisted to the JPA datastore.
 * 
 * @see com.airogami.persistence.entities.Good
 * @author MyEclipse Persistence Tools
 */

public class GoodDAO {
	// property constants
	public static final String FACEBOOK = "facebook";
	public static final String TWITTER = "twitter";

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved Good entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * GoodDAO.save(good);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param good
	 *            Good entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Good good) {
		EntityManagerHelper.log("saving Good instance", Level.INFO, null);
		try {
			getEntityManager().persist(good);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent Good entity. This operation must be performed within
	 * the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * GoodDAO.delete(good);
	 * EntityManagerHelper.commit();
	 * good = null;
	 * </pre>
	 * 
	 * @param good
	 *            Good entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Good good) {
		EntityManagerHelper.log("deleting Good instance", Level.INFO, null);
		try {
			good = getEntityManager().getReference(Good.class,
					good.getAccountId());
			getEntityManager().remove(good);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved Good entity and return it or a copy of it to
	 * the sender. A copy of the Good entity parameter is returned when the JPA
	 * persistence mechanism has not previously been tracking the updated
	 * entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * good = GoodDAO.update(good);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param good
	 *            Good entity to update
	 * @return Good the persisted Good entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Good update(Good good) {
		EntityManagerHelper.log("updating Good instance", Level.INFO, null);
		try {
			Good result = getEntityManager().merge(good);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Good findById(Integer accountId) {
		EntityManagerHelper.log("finding Good instance with id: " + accountId,
				Level.INFO, null);
		try {
			Good instance = getEntityManager().find(Good.class, accountId);
			EntityManagerHelper.log("find successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Good getReference(Integer accountId) {
		EntityManagerHelper.log("getReferencing Good instance with id: "
				+ accountId, Level.INFO, null);
		try {
			Good instance = getEntityManager().getReference(Good.class,
					accountId);
			EntityManagerHelper
					.log("getReference successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getReference failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void detach(Good good) {
		EntityManagerHelper.log("detaching Good instance", Level.INFO, null);
		try {
			getEntityManager().detach(good);
			EntityManagerHelper.log("detach successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("detach failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void refresh(Good good) {
		EntityManagerHelper.log("refreshing Good instance", Level.INFO, null);
		try {
			getEntityManager().refresh(good);
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

	public void remove(Good good) {
		EntityManagerHelper.log("removing Good instance", Level.INFO, null);
		try {
			getEntityManager().remove(good);
			EntityManagerHelper.log("remove successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("remove failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void flush() {
		EntityManagerHelper.log("flushing Good instance", Level.INFO, null);
		try {
			getEntityManager().flush();
			EntityManagerHelper.log("flush successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("flush failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void clear() {
		EntityManagerHelper.log("clearing Good instance", Level.INFO, null);
		try {
			getEntityManager().clear();
			EntityManagerHelper.log("clear successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("clear failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String removeByAccountIdJPQL = "delete from Good a where a.accountId in (?1)";

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

	/**
	 * Find all Good entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Good property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<Good> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<Good> findByProperty(String propertyName, final Object value,
			final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding Good instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from Good model where model."
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

	public List<Good> findByFacebook(Object facebook,
			int... rowStartIdxAndCount) {
		return findByProperty(FACEBOOK, facebook, rowStartIdxAndCount);
	}

	public List<Good> findByTwitter(Object twitter, int... rowStartIdxAndCount) {
		return findByProperty(TWITTER, twitter, rowStartIdxAndCount);
	}

	/**
	 * Find all Good entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<Good> all Good entities
	 */
	@SuppressWarnings("unchecked")
	public List<Good> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding all Good instances", Level.INFO, null);
		try {
			final String queryString = "select model from Good model";
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