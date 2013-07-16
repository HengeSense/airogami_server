package com.airogami.persitence.entities;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * Account entities. Transaction control of the save(), update() and delete()
 * operations must be handled externally by senders of these methods or must be
 * manually added to each of these methods for data to be persisted to the JPA
 * datastore.
 * 
 * @see com.airogami.persitence.entities.Account
 * @author MyEclipse Persistence Tools
 */

public class AccountDAO {
	// property constants
	public static final String FULL_NAME = "fullName";
	public static final String SCREEN_NAME = "screenName";
	public static final String SEX = "sex";
	public static final String ICON = "icon";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String STATUS = "status";
	public static final String CITY = "city";
	public static final String PROVINCE = "province";
	public static final String COUNTRY = "country";

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved Account entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * AccountDAO.save(account);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param account
	 *            Account entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Account account) {
		EntityManagerHelper.log("saving Account instance", Level.INFO, null);
		try {
			getEntityManager().persist(account);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent Account entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * AccountDAO.delete(account);
	 * EntityManagerHelper.commit();
	 * account = null;
	 * </pre>
	 * 
	 * @param account
	 *            Account entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Account account) {
		EntityManagerHelper.log("deleting Account instance", Level.INFO, null);
		try {
			account = getEntityManager().getReference(Account.class,
					account.getAccountId());
			getEntityManager().remove(account);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved Account entity and return it or a copy of it
	 * to the sender. A copy of the Account entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * account = AccountDAO.update(account);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param account
	 *            Account entity to update
	 * @return Account the persisted Account entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Account update(Account account) {
		EntityManagerHelper.log("updating Account instance", Level.INFO, null);
		try {
			Account result = getEntityManager().merge(account);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Account findById(Long accountId) {
		EntityManagerHelper.log("finding Account instance with id: "
				+ accountId, Level.INFO, null);
		try {
			Account instance = getEntityManager()
					.find(Account.class, accountId);
			EntityManagerHelper.log("find successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Account getReference(Long accountId) {
		EntityManagerHelper.log("getReferencing Account instance with id: "
				+ accountId, Level.INFO, null);
		try {
			Account instance = getEntityManager().getReference(Account.class,
					accountId);
			EntityManagerHelper
					.log("getReference successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getReference failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void detach(Account account) {
		EntityManagerHelper.log("detaching Account instance", Level.INFO, null);
		try {
			getEntityManager().detach(account);
			EntityManagerHelper.log("detach successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("detach failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void refresh(Account account) {
		EntityManagerHelper
				.log("refreshing Account instance", Level.INFO, null);
		try {
			getEntityManager().refresh(account);
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

	public void remove(Account account) {
		EntityManagerHelper.log("removing Account instance", Level.INFO, null);
		try {
			getEntityManager().remove(account);
			EntityManagerHelper.log("remove successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("remove failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void flush() {
		EntityManagerHelper.log("flushing Account instance", Level.INFO, null);
		try {
			getEntityManager().flush();
			EntityManagerHelper.log("flush successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("flush failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void clear() {
		EntityManagerHelper.log("clearing Account instance", Level.INFO, null);
		try {
			getEntityManager().clear();
			EntityManagerHelper.log("clear successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("clear failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String removeByAccountIdJPQL = "delete from Account a where a.accountId in (?1)";

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
	 * Find all Account entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Account property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<Account> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<Account> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding Account instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from Account model where model."
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

	public List<Account> findByFullName(Object fullName,
			int... rowStartIdxAndCount) {
		return findByProperty(FULL_NAME, fullName, rowStartIdxAndCount);
	}

	public List<Account> findByScreenName(Object screenName,
			int... rowStartIdxAndCount) {
		return findByProperty(SCREEN_NAME, screenName, rowStartIdxAndCount);
	}

	public List<Account> findBySex(Object sex, int... rowStartIdxAndCount) {
		return findByProperty(SEX, sex, rowStartIdxAndCount);
	}

	public List<Account> findByIcon(Object icon, int... rowStartIdxAndCount) {
		return findByProperty(ICON, icon, rowStartIdxAndCount);
	}

	public List<Account> findByLongitude(Object longitude,
			int... rowStartIdxAndCount) {
		return findByProperty(LONGITUDE, longitude, rowStartIdxAndCount);
	}

	public List<Account> findByLatitude(Object latitude,
			int... rowStartIdxAndCount) {
		return findByProperty(LATITUDE, latitude, rowStartIdxAndCount);
	}

	public List<Account> findByStatus(Object status, int... rowStartIdxAndCount) {
		return findByProperty(STATUS, status, rowStartIdxAndCount);
	}

	public List<Account> findByCity(Object city, int... rowStartIdxAndCount) {
		return findByProperty(CITY, city, rowStartIdxAndCount);
	}

	public List<Account> findByProvince(Object province,
			int... rowStartIdxAndCount) {
		return findByProperty(PROVINCE, province, rowStartIdxAndCount);
	}

	public List<Account> findByCountry(Object country,
			int... rowStartIdxAndCount) {
		return findByProperty(COUNTRY, country, rowStartIdxAndCount);
	}

	/**
	 * Find all Account entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<Account> all Account entities
	 */
	@SuppressWarnings("unchecked")
	public List<Account> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding all Account instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from Account model";
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