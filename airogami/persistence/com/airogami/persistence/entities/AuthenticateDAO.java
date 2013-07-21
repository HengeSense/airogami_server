package com.airogami.persistence.entities;

import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * Authenticate entities. Transaction control of the save(), update() and
 * delete() operations must be handled externally by senders of these methods or
 * must be manually added to each of these methods for data to be persisted to
 * the JPA datastore.
 * 
 * @see com.airogami.persistence.entities.Authenticate
 * @author MyEclipse Persistence Tools
 */

public class AuthenticateDAO {
	// property constants
	public static final String EMAIL = "email";
	public static final String SCREEN_NAME = "screenName";
	public static final String PASSWORD = "password";

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved Authenticate entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * AuthenticateDAO.save(authenticate);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param authenticate
	 *            Authenticate entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Authenticate authenticate) {
		EntityManagerHelper.log("saving Authenticate instance", Level.INFO,
				null);
		try {
			getEntityManager().persist(authenticate);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent Authenticate entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * AuthenticateDAO.delete(authenticate);
	 * EntityManagerHelper.commit();
	 * authenticate = null;
	 * </pre>
	 * 
	 * @param authenticate
	 *            Authenticate entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Authenticate authenticate) {
		EntityManagerHelper.log("deleting Authenticate instance", Level.INFO,
				null);
		try {
			authenticate = getEntityManager().getReference(Authenticate.class,
					authenticate.getAccountId());
			getEntityManager().remove(authenticate);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved Authenticate entity and return it or a copy of
	 * it to the sender. A copy of the Authenticate entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * authenticate = AuthenticateDAO.update(authenticate);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param authenticate
	 *            Authenticate entity to update
	 * @return Authenticate the persisted Authenticate entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Authenticate update(Authenticate authenticate) {
		EntityManagerHelper.log("updating Authenticate instance", Level.INFO,
				null);
		try {
			Authenticate result = getEntityManager().merge(authenticate);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Authenticate findById(Long accountId) {
		EntityManagerHelper.log("finding Authenticate instance with id: "
				+ accountId, Level.INFO, null);
		try {
			Authenticate instance = getEntityManager().find(Authenticate.class,
					accountId);
			EntityManagerHelper.log("find successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Authenticate getReference(Long accountId) {
		EntityManagerHelper.log(
				"getReferencing Authenticate instance with id: " + accountId,
				Level.INFO, null);
		try {
			Authenticate instance = getEntityManager().getReference(
					Authenticate.class, accountId);
			EntityManagerHelper
					.log("getReference successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getReference failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void detach(Authenticate authenticate) {
		EntityManagerHelper.log("detaching Authenticate instance", Level.INFO,
				null);
		try {
			getEntityManager().detach(authenticate);
			EntityManagerHelper.log("detach successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("detach failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void refresh(Authenticate authenticate) {
		EntityManagerHelper.log("refreshing Authenticate instance", Level.INFO,
				null);
		try {
			getEntityManager().refresh(authenticate);
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

	public void remove(Authenticate authenticate) {
		EntityManagerHelper.log("removing Authenticate instance", Level.INFO,
				null);
		try {
			getEntityManager().remove(authenticate);
			EntityManagerHelper.log("remove successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("remove failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void flush() {
		EntityManagerHelper.log("flushing Authenticate instance", Level.INFO,
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
		EntityManagerHelper.log("clearing Authenticate instance", Level.INFO,
				null);
		try {
			getEntityManager().clear();
			EntityManagerHelper.log("clear successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("clear failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String removeByAccountIdJPQL = "delete from Authenticate a where a.accountId in (?1)";

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

	/**
	 * Find all Authenticate entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Authenticate property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<Authenticate> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<Authenticate> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding Authenticate instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from Authenticate model where model."
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

	public List<Authenticate> findByEmail(Object email,
			int... rowStartIdxAndCount) {
		return findByProperty(EMAIL, email, rowStartIdxAndCount);
	}

	public List<Authenticate> findByScreenName(Object screenName,
			int... rowStartIdxAndCount) {
		return findByProperty(SCREEN_NAME, screenName, rowStartIdxAndCount);
	}

	public List<Authenticate> findByPassword(Object password,
			int... rowStartIdxAndCount) {
		return findByProperty(PASSWORD, password, rowStartIdxAndCount);
	}

	/**
	 * Find all Authenticate entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<Authenticate> all Authenticate entities
	 */
	@SuppressWarnings("unchecked")
	public List<Authenticate> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding all Authenticate instances",
				Level.INFO, null);
		try {
			final String queryString = "select model from Authenticate model";
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