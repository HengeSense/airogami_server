package com.airogami.persistence.entities;

import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * AccountSys entities. Transaction control of the save(), update() and delete()
 * operations must be handled externally by senders of these methods or must be
 * manually added to each of these methods for data to be persisted to the JPA
 * datastore.
 * 
 * @see com.airogami.persistence.entities.AccountSys
 * @author MyEclipse Persistence Tools
 */

public class AccountSysDAO {
	// property constants
	public static final String CHAIN_INC = "chainInc";
	public static final String PLANE_INC = "planeInc";

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved AccountSys entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * AccountSysDAO.save(accountSys);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param accountSys
	 *            AccountSys entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AccountSys accountSys) {
		EntityManagerHelper.log("saving AccountSys instance", Level.INFO, null);
		try {
			getEntityManager().persist(accountSys);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent AccountSys entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * AccountSysDAO.delete(accountSys);
	 * EntityManagerHelper.commit();
	 * accountSys = null;
	 * </pre>
	 * 
	 * @param accountSys
	 *            AccountSys entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AccountSys accountSys) {
		EntityManagerHelper.log("deleting AccountSys instance", Level.INFO,
				null);
		try {
			accountSys = getEntityManager().getReference(AccountSys.class,
					accountSys.getAccountId());
			getEntityManager().remove(accountSys);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved AccountSys entity and return it or a copy of
	 * it to the sender. A copy of the AccountSys entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * accountSys = AccountSysDAO.update(accountSys);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param accountSys
	 *            AccountSys entity to update
	 * @return AccountSys the persisted AccountSys entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AccountSys update(AccountSys accountSys) {
		EntityManagerHelper.log("updating AccountSys instance", Level.INFO,
				null);
		try {
			AccountSys result = getEntityManager().merge(accountSys);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public AccountSys findById(Integer accountId) {
		EntityManagerHelper.log("finding AccountSys instance with id: "
				+ accountId, Level.INFO, null);
		try {
			AccountSys instance = getEntityManager().find(AccountSys.class,
					accountId);
			EntityManagerHelper.log("find successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public AccountSys getReference(Integer accountId) {
		EntityManagerHelper.log("getReferencing AccountSys instance with id: "
				+ accountId, Level.INFO, null);
		try {
			AccountSys instance = getEntityManager().getReference(
					AccountSys.class, accountId);
			EntityManagerHelper
					.log("getReference successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getReference failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void detach(AccountSys accountSys) {
		EntityManagerHelper.log("detaching AccountSys instance", Level.INFO,
				null);
		try {
			getEntityManager().detach(accountSys);
			EntityManagerHelper.log("detach successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("detach failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void refresh(AccountSys accountSys) {
		EntityManagerHelper.log("refreshing AccountSys instance", Level.INFO,
				null);
		try {
			getEntityManager().refresh(accountSys);
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

	public void remove(AccountSys accountSys) {
		EntityManagerHelper.log("removing AccountSys instance", Level.INFO,
				null);
		try {
			getEntityManager().remove(accountSys);
			EntityManagerHelper.log("remove successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("remove failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void flush() {
		EntityManagerHelper.log("flushing AccountSys instance", Level.INFO,
				null);
		try {
			getEntityManager().flush();
			EntityManagerHelper.log("flush successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("flush failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void clear() {
		EntityManagerHelper.log("clearing AccountSys instance", Level.INFO,
				null);
		try {
			getEntityManager().clear();
			EntityManagerHelper.log("clear successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("clear failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String removeByAccountIdJPQL = "delete from AccountSys a where a.accountId in (?1)";

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

	private static final String increaseChainIncJPQL = "update AccountSys a set a.chainInc = a.chainInc + :count where a.accountId in (:accountId)";

	public boolean increaseChainInc(java.lang.Integer accountId, int count) {
		EntityManagerHelper.log(
				"increaseChainInc with accountId: " + accountId, Level.INFO,
				null);
		try {
			Query query = getEntityManager().createQuery(increaseChainIncJPQL);
			query.setParameter("accountId", accountId);
			query.setParameter("count", count);
			boolean result = query.executeUpdate() == 1;
			EntityManagerHelper.log("increaseChainInc successful", Level.INFO,
					null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper
					.log("increaseChainInc failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String increasePlaneIncJPQL = "update AccountSys a set a.planeInc = a.planeInc + :count where a.accountId in (:accountId)";

	public boolean increasePlaneInc(java.lang.Integer accountId, int count) {
		EntityManagerHelper.log(
				"increasePlaneInc with accountId: " + accountId, Level.INFO,
				null);
		try {
			Query query = getEntityManager().createQuery(increasePlaneIncJPQL);
			query.setParameter("accountId", accountId);
			query.setParameter("count", count);
			boolean result = query.executeUpdate() == 1;
			EntityManagerHelper.log("increasePlaneInc successful", Level.INFO,
					null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper
					.log("increasePlaneInc failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AccountSys entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AccountSys property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<AccountSys> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AccountSys> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding AccountSys instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AccountSys model where model."
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

	public List<AccountSys> findByChainInc(Object chainInc,
			int... rowStartIdxAndCount) {
		return findByProperty(CHAIN_INC, chainInc, rowStartIdxAndCount);
	}

	public List<AccountSys> findByPlaneInc(Object planeInc,
			int... rowStartIdxAndCount) {
		return findByProperty(PLANE_INC, planeInc, rowStartIdxAndCount);
	}

	/**
	 * Find all AccountSys entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<AccountSys> all AccountSys entities
	 */
	@SuppressWarnings("unchecked")
	public List<AccountSys> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding all AccountSys instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from AccountSys model";
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