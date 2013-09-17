package com.airogami.persistence.entities;

import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * AccountStat entities. Transaction control of the save(), update() and
 * delete() operations must be handled externally by senders of these methods or
 * must be manually added to each of these methods for data to be persisted to
 * the JPA datastore.
 * 
 * @see com.airogami.persistence.entities.AccountStat
 * @author MyEclipse Persistence Tools
 */

public class AccountStatDAO {
	// property constants
	public static final String SIGNIN_COUNT = "signinCount";
	public static final String STATUS = "status";
	public static final String NEW_MSG_COUNT = "newMsgCount";
	public static final String NEW_CHAIN_MSG_COUNT = "newChainMsgCount";

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved AccountStat entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * AccountStatDAO.save(accountStat);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param accountStat
	 *            AccountStat entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AccountStat accountStat) {
		EntityManagerHelper
				.log("saving AccountStat instance", Level.INFO, null);
		try {
			getEntityManager().persist(accountStat);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent AccountStat entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * AccountStatDAO.delete(accountStat);
	 * EntityManagerHelper.commit();
	 * accountStat = null;
	 * </pre>
	 * 
	 * @param accountStat
	 *            AccountStat entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AccountStat accountStat) {
		EntityManagerHelper.log("deleting AccountStat instance", Level.INFO,
				null);
		try {
			accountStat = getEntityManager().getReference(AccountStat.class,
					accountStat.getAccountId());
			getEntityManager().remove(accountStat);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved AccountStat entity and return it or a copy of
	 * it to the sender. A copy of the AccountStat entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * accountStat = AccountStatDAO.update(accountStat);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param accountStat
	 *            AccountStat entity to update
	 * @return AccountStat the persisted AccountStat entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AccountStat update(AccountStat accountStat) {
		EntityManagerHelper.log("updating AccountStat instance", Level.INFO,
				null);
		try {
			AccountStat result = getEntityManager().merge(accountStat);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public AccountStat findById(Long accountId) {
		EntityManagerHelper.log("finding AccountStat instance with id: "
				+ accountId, Level.INFO, null);
		try {
			AccountStat instance = getEntityManager().find(AccountStat.class,
					accountId);
			EntityManagerHelper.log("find successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public AccountStat getReference(Long accountId) {
		EntityManagerHelper.log("getReferencing AccountStat instance with id: "
				+ accountId, Level.INFO, null);
		try {
			AccountStat instance = getEntityManager().getReference(
					AccountStat.class, accountId);
			EntityManagerHelper
					.log("getReference successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getReference failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void detach(AccountStat accountStat) {
		EntityManagerHelper.log("detaching AccountStat instance", Level.INFO,
				null);
		try {
			getEntityManager().detach(accountStat);
			EntityManagerHelper.log("detach successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("detach failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void refresh(AccountStat accountStat) {
		EntityManagerHelper.log("refreshing AccountStat instance", Level.INFO,
				null);
		try {
			getEntityManager().refresh(accountStat);
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

	public void remove(AccountStat accountStat) {
		EntityManagerHelper.log("removing AccountStat instance", Level.INFO,
				null);
		try {
			getEntityManager().remove(accountStat);
			EntityManagerHelper.log("remove successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("remove failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void flush() {
		EntityManagerHelper.log("flushing AccountStat instance", Level.INFO,
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
		EntityManagerHelper.log("clearing AccountStat instance", Level.INFO,
				null);
		try {
			getEntityManager().clear();
			EntityManagerHelper.log("clear successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("clear failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String removeByAccountIdJPQL = "delete from AccountStat a where a.accountId in (?1)";

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

	private static final String increaseSigninCountJPQL = "update AccountStat a set a.signinCount = a.signinCount + :count where a.accountId in (:accountId)";

	public boolean increaseSigninCount(java.lang.Long accountId, int count) {
		EntityManagerHelper.log("increaseNewChainMsgCount with accountId:"
				+ accountId, Level.INFO, null);
		try {
			Query query = getEntityManager().createQuery(
					increaseSigninCountJPQL);
			query.setParameter("accountId", accountId);
			query.setParameter("count", count);
			boolean result = query.executeUpdate() == 1;
			EntityManagerHelper.log("increaseNewChainMsgCount successful",
					Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("increaseNewChainMsgCount failed",
					Level.SEVERE, re);
			throw re;
		}
	}

	private static final String increaseNewMsgCountJPQL = "update AccountStat a set a.newMsgCount = a.newMsgCount + :count where a.accountId in (:accountId)";

	public boolean increaseNewMsgCount(java.lang.Long accountId, int count) {
		EntityManagerHelper.log("increaseNewChainMsgCount with accountId:"
				+ accountId, Level.INFO, null);
		try {
			Query query = getEntityManager().createQuery(
					increaseNewMsgCountJPQL);
			query.setParameter("accountId", accountId);
			query.setParameter("count", count);
			boolean result = query.executeUpdate() == 1;
			EntityManagerHelper.log("increaseNewChainMsgCount successful",
					Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("increaseNewChainMsgCount failed",
					Level.SEVERE, re);
			throw re;
		}
	}

	private static final String increaseNewChainMsgCountJPQL = "update AccountStat a set a.newChainMsgCount = a.newChainMsgCount + :count where a.accountId in (:accountId)";

	public boolean increaseNewChainMsgCount(java.lang.Long accountId, int count) {
		EntityManagerHelper.log("increaseNewChainMsgCount with accountId:"
				+ accountId, Level.INFO, null);
		try {
			Query query = getEntityManager().createQuery(
					increaseNewChainMsgCountJPQL);
			query.setParameter("accountId", accountId);
			query.setParameter("count", count);
			boolean result = query.executeUpdate() == 1;
			EntityManagerHelper.log("increaseNewChainMsgCount successful",
					Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("increaseNewChainMsgCount failed",
					Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AccountStat entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AccountStat property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<AccountStat> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AccountStat> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding AccountStat instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AccountStat model where model."
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

	public List<AccountStat> findBySigninCount(Object signinCount,
			int... rowStartIdxAndCount) {
		return findByProperty(SIGNIN_COUNT, signinCount, rowStartIdxAndCount);
	}

	public List<AccountStat> findByStatus(Object status,
			int... rowStartIdxAndCount) {
		return findByProperty(STATUS, status, rowStartIdxAndCount);
	}

	public List<AccountStat> findByNewMsgCount(Object newMsgCount,
			int... rowStartIdxAndCount) {
		return findByProperty(NEW_MSG_COUNT, newMsgCount, rowStartIdxAndCount);
	}

	public List<AccountStat> findByNewChainMsgCount(Object newChainMsgCount,
			int... rowStartIdxAndCount) {
		return findByProperty(NEW_CHAIN_MSG_COUNT, newChainMsgCount,
				rowStartIdxAndCount);
	}

	/**
	 * Find all AccountStat entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<AccountStat> all AccountStat entities
	 */
	@SuppressWarnings("unchecked")
	public List<AccountStat> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding all AccountStat instances",
				Level.INFO, null);
		try {
			final String queryString = "select model from AccountStat model";
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