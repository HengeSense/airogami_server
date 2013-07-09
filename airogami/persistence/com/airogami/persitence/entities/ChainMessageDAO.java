package com.airogami.persitence.entities;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * ChainMessage entities. Transaction control of the save(), update() and
 * delete() operations must be handled externally by senders of these methods or
 * must be manually added to each of these methods for data to be persisted to
 * the JPA datastore.
 * 
 * @see com.airogami.persitence.entities.ChainMessage
 * @author MyEclipse Persistence Tools
 */

public class ChainMessageDAO {
	// property constants
	public static final String CONTENT = "content";
	public static final String TYPE = "type";
	public static final String STATUS = "status";

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved ChainMessage entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ChainMessageDAO.save(chainMessage);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param chainMessage
	 *            ChainMessage entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(ChainMessage chainMessage) {
		EntityManagerHelper.log("saving ChainMessage instance", Level.INFO,
				null);
		try {
			getEntityManager().persist(chainMessage);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent ChainMessage entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ChainMessageDAO.delete(chainMessage);
	 * EntityManagerHelper.commit();
	 * chainMessage = null;
	 * </pre>
	 * 
	 * @param chainMessage
	 *            ChainMessage entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(ChainMessage chainMessage) {
		EntityManagerHelper.log("deleting ChainMessage instance", Level.INFO,
				null);
		try {
			chainMessage = getEntityManager().getReference(ChainMessage.class,
					chainMessage.getId());
			getEntityManager().remove(chainMessage);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved ChainMessage entity and return it or a copy of
	 * it to the sender. A copy of the ChainMessage entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * chainMessage = ChainMessageDAO.update(chainMessage);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param chainMessage
	 *            ChainMessage entity to update
	 * @return ChainMessage the persisted ChainMessage entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public ChainMessage update(ChainMessage chainMessage) {
		EntityManagerHelper.log("updating ChainMessage instance", Level.INFO,
				null);
		try {
			ChainMessage result = getEntityManager().merge(chainMessage);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ChainMessage findById(ChainMessageId id) {
		EntityManagerHelper.log("finding ChainMessage instance with id: " + id,
				Level.INFO, null);
		try {
			ChainMessage instance = getEntityManager().find(ChainMessage.class,
					id);
			EntityManagerHelper.log("find successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ChainMessage getReference(ChainMessageId id) {
		EntityManagerHelper.log(
				"getReferencing ChainMessage instance with id: " + id,
				Level.INFO, null);
		try {
			ChainMessage instance = getEntityManager().getReference(
					ChainMessage.class, id);
			EntityManagerHelper
					.log("getReference successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getReference failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void detach(ChainMessage chainMessage) {
		EntityManagerHelper.log("detaching ChainMessage instance", Level.INFO,
				null);
		try {
			getEntityManager().detach(chainMessage);
			EntityManagerHelper.log("detach successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("detach failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void refresh(ChainMessage chainMessage) {
		EntityManagerHelper.log("refreshing ChainMessage instance", Level.INFO,
				null);
		try {
			getEntityManager().refresh(chainMessage);
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

	public void remove(ChainMessage chainMessage) {
		EntityManagerHelper.log("removing ChainMessage instance", Level.INFO,
				null);
		try {
			getEntityManager().remove(chainMessage);
			EntityManagerHelper.log("remove successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("remove failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void flush() {
		EntityManagerHelper.log("flushing ChainMessage instance", Level.INFO,
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
		EntityManagerHelper.log("clearing ChainMessage instance", Level.INFO,
				null);
		try {
			getEntityManager().clear();
			EntityManagerHelper.log("clear successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("clear failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String removeByIdJPQL = "delete from ChainMessage a where a.id in (?1)";

	public int removeById(ChainMessageId id) {
		EntityManagerHelper.log("removeById", Level.INFO, null);
		int ret = 0;
		try {
			Query query = getEntityManager().createQuery(removeByIdJPQL);
			query.setParameter(1, id);
			ret = query.executeUpdate();
			EntityManagerHelper.log("removeById successful", Level.INFO, null);
			return ret;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("removeById failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all ChainMessage entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the ChainMessage property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<ChainMessage> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<ChainMessage> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding ChainMessage instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from ChainMessage model where model."
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

	public List<ChainMessage> findByContent(Object content,
			int... rowStartIdxAndCount) {
		return findByProperty(CONTENT, content, rowStartIdxAndCount);
	}

	public List<ChainMessage> findByType(Object type,
			int... rowStartIdxAndCount) {
		return findByProperty(TYPE, type, rowStartIdxAndCount);
	}

	public List<ChainMessage> findByStatus(Object status,
			int... rowStartIdxAndCount) {
		return findByProperty(STATUS, status, rowStartIdxAndCount);
	}

	/**
	 * Find all ChainMessage entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<ChainMessage> all ChainMessage entities
	 */
	@SuppressWarnings("unchecked")
	public List<ChainMessage> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding all ChainMessage instances",
				Level.INFO, null);
		try {
			final String queryString = "select model from ChainMessage model";
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