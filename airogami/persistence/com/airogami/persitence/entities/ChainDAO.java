package com.airogami.persitence.entities;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for Chain
 * entities. Transaction control of the save(), update() and delete() operations
 * must be handled externally by senders of these methods or must be manually
 * added to each of these methods for data to be persisted to the JPA datastore.
 * 
 * @see com.airogami.persitence.entities.Chain
 * @author MyEclipse Persistence Tools
 */

public class ChainDAO {
	// property constants
	public static final String MESSAGE_COUNT = "messageCount";
	public static final String CITY = "city";
	public static final String PROVINCE = "province";
	public static final String COUNTRY = "country";
	public static final String PASS_COUNT = "passCount";
	public static final String MATCH_COUNT = "matchCount";

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved Chain entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ChainDAO.save(chain);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param chain
	 *            Chain entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Chain chain) {
		EntityManagerHelper.log("saving Chain instance", Level.INFO, null);
		try {
			getEntityManager().persist(chain);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent Chain entity. This operation must be performed within
	 * the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ChainDAO.delete(chain);
	 * EntityManagerHelper.commit();
	 * chain = null;
	 * </pre>
	 * 
	 * @param chain
	 *            Chain entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Chain chain) {
		EntityManagerHelper.log("deleting Chain instance", Level.INFO, null);
		try {
			chain = getEntityManager().getReference(Chain.class,
					chain.getChainId());
			getEntityManager().remove(chain);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved Chain entity and return it or a copy of it to
	 * the sender. A copy of the Chain entity parameter is returned when the JPA
	 * persistence mechanism has not previously been tracking the updated
	 * entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * chain = ChainDAO.update(chain);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param chain
	 *            Chain entity to update
	 * @return Chain the persisted Chain entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Chain update(Chain chain) {
		EntityManagerHelper.log("updating Chain instance", Level.INFO, null);
		try {
			Chain result = getEntityManager().merge(chain);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Chain findById(Long chainId) {
		EntityManagerHelper.log("finding Chain instance with id: " + chainId,
				Level.INFO, null);
		try {
			Chain instance = getEntityManager().find(Chain.class, chainId);
			EntityManagerHelper.log("find successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Chain getReference(Long chainId) {
		EntityManagerHelper.log("getReferencing Chain instance with id: "
				+ chainId, Level.INFO, null);
		try {
			Chain instance = getEntityManager().getReference(Chain.class,
					chainId);
			EntityManagerHelper
					.log("getReference successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getReference failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void detach(Chain chain) {
		EntityManagerHelper.log("detaching Chain instance", Level.INFO, null);
		try {
			getEntityManager().detach(chain);
			EntityManagerHelper.log("detach successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("detach failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void refresh(Chain chain) {
		EntityManagerHelper.log("refreshing Chain instance", Level.INFO, null);
		try {
			getEntityManager().refresh(chain);
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

	public void remove(Chain chain) {
		EntityManagerHelper.log("removing Chain instance", Level.INFO, null);
		try {
			getEntityManager().remove(chain);
			EntityManagerHelper.log("remove successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("remove failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void flush() {
		EntityManagerHelper.log("flushing Chain instance", Level.INFO, null);
		try {
			getEntityManager().flush();
			EntityManagerHelper.log("flush successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("flush failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void clear() {
		EntityManagerHelper.log("clearing Chain instance", Level.INFO, null);
		try {
			getEntityManager().clear();
			EntityManagerHelper.log("clear successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("clear failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String removeByChainIdJPQL = "delete from Chain a where a.chainId in (?1)";

	public int removeByChainId(Long chainId) {
		EntityManagerHelper.log("removeByChainId", Level.INFO, null);
		int ret = 0;
		try {
			Query query = getEntityManager().createQuery(removeByChainIdJPQL);
			query.setParameter(1, chainId);
			ret = query.executeUpdate();
			EntityManagerHelper.log("removeByChainId successful", Level.INFO,
					null);
			return ret;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("removeByChainId failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String accountJPQL = "select a.account.accountId from Chain a where a.chainId = :chainId";

	public java.lang.Long getAccount(java.lang.Long chainId) {
		EntityManagerHelper.log("getAccountId with chainId" + chainId,
				Level.INFO, null);
		java.lang.Long accountId;
		try {
			Query query = getEntityManager().createQuery(accountJPQL);
			query.setParameter("chainId", chainId);
			accountId = (java.lang.Long) query.getSingleResult();
			EntityManagerHelper
					.log("getAccountId successful", Level.INFO, null);
			return accountId;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getAccountId failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String increaseMessageCountJPQL = "update Chain a set a.messageCount = a.messageCount + :count where a.chainId in (:chainId)";

	public void increaseMessageCount(java.lang.Long chainId, int count) {
		EntityManagerHelper.log("increaseMatchCount with chainId:" + chainId,
				Level.INFO, null);
		try {
			Query query = getEntityManager().createQuery(
					increaseMessageCountJPQL);
			query.setParameter("chainId", chainId);
			query.setParameter("count", count);
			query.executeUpdate();
			EntityManagerHelper.log("increaseMatchCount successful",
					Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("increaseMatchCount failed", Level.SEVERE,
					re);
			throw re;
		}
	}

	private static final String increasePassCountJPQL = "update Chain a set a.passCount = a.passCount + :count where a.chainId in (:chainId)";

	public void increasePassCount(java.lang.Long chainId, int count) {
		EntityManagerHelper.log("increaseMatchCount with chainId:" + chainId,
				Level.INFO, null);
		try {
			Query query = getEntityManager().createQuery(increasePassCountJPQL);
			query.setParameter("chainId", chainId);
			query.setParameter("count", count);
			query.executeUpdate();
			EntityManagerHelper.log("increaseMatchCount successful",
					Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("increaseMatchCount failed", Level.SEVERE,
					re);
			throw re;
		}
	}

	private static final String increaseMatchCountJPQL = "update Chain a set a.matchCount = a.matchCount + :count where a.chainId in (:chainId)";

	public void increaseMatchCount(java.lang.Long chainId, int count) {
		EntityManagerHelper.log("increaseMatchCount with chainId:" + chainId,
				Level.INFO, null);
		try {
			Query query = getEntityManager()
					.createQuery(increaseMatchCountJPQL);
			query.setParameter("chainId", chainId);
			query.setParameter("count", count);
			query.executeUpdate();
			EntityManagerHelper.log("increaseMatchCount successful",
					Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("increaseMatchCount failed", Level.SEVERE,
					re);
			throw re;
		}
	}

	/**
	 * Find all Chain entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Chain property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<Chain> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<Chain> findByProperty(String propertyName, final Object value,
			final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding Chain instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from Chain model where model."
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

	public List<Chain> findByMessageCount(Object messageCount,
			int... rowStartIdxAndCount) {
		return findByProperty(MESSAGE_COUNT, messageCount, rowStartIdxAndCount);
	}

	public List<Chain> findByCity(Object city, int... rowStartIdxAndCount) {
		return findByProperty(CITY, city, rowStartIdxAndCount);
	}

	public List<Chain> findByProvince(Object province,
			int... rowStartIdxAndCount) {
		return findByProperty(PROVINCE, province, rowStartIdxAndCount);
	}

	public List<Chain> findByCountry(Object country, int... rowStartIdxAndCount) {
		return findByProperty(COUNTRY, country, rowStartIdxAndCount);
	}

	public List<Chain> findByPassCount(Object passCount,
			int... rowStartIdxAndCount) {
		return findByProperty(PASS_COUNT, passCount, rowStartIdxAndCount);
	}

	public List<Chain> findByMatchCount(Object matchCount,
			int... rowStartIdxAndCount) {
		return findByProperty(MATCH_COUNT, matchCount, rowStartIdxAndCount);
	}

	/**
	 * Find all Chain entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<Chain> all Chain entities
	 */
	@SuppressWarnings("unchecked")
	public List<Chain> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper
				.log("finding all Chain instances", Level.INFO, null);
		try {
			final String queryString = "select model from Chain model";
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