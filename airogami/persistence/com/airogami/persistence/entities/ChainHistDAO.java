package com.airogami.persistence.entities;

import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * ChainHist entities. Transaction control of the save(), update() and delete()
 * operations must be handled externally by senders of these methods or must be
 * manually added to each of these methods for data to be persisted to the JPA
 * datastore.
 * 
 * @see com.airogami.persistence.entities.ChainHist
 * @author MyEclipse Persistence Tools
 */

public class ChainHistDAO {
	// property constants

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved ChainHist entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ChainHistDAO.save(chainHist);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param chainHist
	 *            ChainHist entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(ChainHist chainHist) {
		EntityManagerHelper.log("saving ChainHist instance", Level.INFO, null);
		try {
			getEntityManager().persist(chainHist);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent ChainHist entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ChainHistDAO.delete(chainHist);
	 * EntityManagerHelper.commit();
	 * chainHist = null;
	 * </pre>
	 * 
	 * @param chainHist
	 *            ChainHist entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(ChainHist chainHist) {
		EntityManagerHelper
				.log("deleting ChainHist instance", Level.INFO, null);
		try {
			chainHist = getEntityManager().getReference(ChainHist.class,
					chainHist.getId());
			getEntityManager().remove(chainHist);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved ChainHist entity and return it or a copy of it
	 * to the sender. A copy of the ChainHist entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * chainHist = ChainHistDAO.update(chainHist);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param chainHist
	 *            ChainHist entity to update
	 * @return ChainHist the persisted ChainHist entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public ChainHist update(ChainHist chainHist) {
		EntityManagerHelper
				.log("updating ChainHist instance", Level.INFO, null);
		try {
			ChainHist result = getEntityManager().merge(chainHist);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ChainHist findById(ChainHistId id) {
		EntityManagerHelper.log("finding ChainHist instance with id: " + id,
				Level.INFO, null);
		try {
			ChainHist instance = getEntityManager().find(ChainHist.class, id);
			EntityManagerHelper.log("find successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ChainHist getReference(ChainHistId id) {
		EntityManagerHelper.log("getReferencing ChainHist instance with id: "
				+ id, Level.INFO, null);
		try {
			ChainHist instance = getEntityManager().getReference(
					ChainHist.class, id);
			EntityManagerHelper
					.log("getReference successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getReference failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void detach(ChainHist chainHist) {
		EntityManagerHelper.log("detaching ChainHist instance", Level.INFO,
				null);
		try {
			getEntityManager().detach(chainHist);
			EntityManagerHelper.log("detach successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("detach failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void refresh(ChainHist chainHist) {
		EntityManagerHelper.log("refreshing ChainHist instance", Level.INFO,
				null);
		try {
			getEntityManager().refresh(chainHist);
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

	public void remove(ChainHist chainHist) {
		EntityManagerHelper
				.log("removing ChainHist instance", Level.INFO, null);
		try {
			getEntityManager().remove(chainHist);
			EntityManagerHelper.log("remove successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("remove failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void flush() {
		EntityManagerHelper
				.log("flushing ChainHist instance", Level.INFO, null);
		try {
			getEntityManager().flush();
			EntityManagerHelper.log("flush successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("flush failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void clear() {
		EntityManagerHelper
				.log("clearing ChainHist instance", Level.INFO, null);
		try {
			getEntityManager().clear();
			EntityManagerHelper.log("clear successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("clear failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String removeByIdJPQL = "delete from ChainHist a where a.id in (?1)";

	public int removeById(ChainHistId id) {
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
	 * Find all ChainHist entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the ChainHist property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<ChainHist> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<ChainHist> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding ChainHist instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from ChainHist model where model."
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

	/**
	 * Find all ChainHist entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<ChainHist> all ChainHist entities
	 */
	@SuppressWarnings("unchecked")
	public List<ChainHist> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding all ChainHist instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from ChainHist model";
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