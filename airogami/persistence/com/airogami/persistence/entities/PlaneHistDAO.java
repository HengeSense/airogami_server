package com.airogami.persistence.entities;

import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * PlaneHist entities. Transaction control of the save(), update() and delete()
 * operations must be handled externally by senders of these methods or must be
 * manually added to each of these methods for data to be persisted to the JPA
 * datastore.
 * 
 * @see com.airogami.persistence.entities.PlaneHist
 * @author MyEclipse Persistence Tools
 */

public class PlaneHistDAO {
	// property constants

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved PlaneHist entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * PlaneHistDAO.save(planeHist);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param planeHist
	 *            PlaneHist entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PlaneHist planeHist) {
		EntityManagerHelper.log("saving PlaneHist instance", Level.INFO, null);
		try {
			getEntityManager().persist(planeHist);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PlaneHist entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * PlaneHistDAO.delete(planeHist);
	 * EntityManagerHelper.commit();
	 * planeHist = null;
	 * </pre>
	 * 
	 * @param planeHist
	 *            PlaneHist entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PlaneHist planeHist) {
		EntityManagerHelper
				.log("deleting PlaneHist instance", Level.INFO, null);
		try {
			planeHist = getEntityManager().getReference(PlaneHist.class,
					planeHist.getId());
			getEntityManager().remove(planeHist);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PlaneHist entity and return it or a copy of it
	 * to the sender. A copy of the PlaneHist entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * planeHist = PlaneHistDAO.update(planeHist);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param planeHist
	 *            PlaneHist entity to update
	 * @return PlaneHist the persisted PlaneHist entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PlaneHist update(PlaneHist planeHist) {
		EntityManagerHelper
				.log("updating PlaneHist instance", Level.INFO, null);
		try {
			PlaneHist result = getEntityManager().merge(planeHist);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PlaneHist findById(PlaneHistId id) {
		EntityManagerHelper.log("finding PlaneHist instance with id: " + id,
				Level.INFO, null);
		try {
			PlaneHist instance = getEntityManager().find(PlaneHist.class, id);
			EntityManagerHelper.log("find successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PlaneHist getReference(PlaneHistId id) {
		EntityManagerHelper.log("getReferencing PlaneHist instance with id: "
				+ id, Level.INFO, null);
		try {
			PlaneHist instance = getEntityManager().getReference(
					PlaneHist.class, id);
			EntityManagerHelper
					.log("getReference successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getReference failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void detach(PlaneHist planeHist) {
		EntityManagerHelper.log("detaching PlaneHist instance", Level.INFO,
				null);
		try {
			getEntityManager().detach(planeHist);
			EntityManagerHelper.log("detach successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("detach failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void refresh(PlaneHist planeHist) {
		EntityManagerHelper.log("refreshing PlaneHist instance", Level.INFO,
				null);
		try {
			getEntityManager().refresh(planeHist);
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

	public void remove(PlaneHist planeHist) {
		EntityManagerHelper
				.log("removing PlaneHist instance", Level.INFO, null);
		try {
			getEntityManager().remove(planeHist);
			EntityManagerHelper.log("remove successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("remove failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void flush() {
		EntityManagerHelper
				.log("flushing PlaneHist instance", Level.INFO, null);
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
				.log("clearing PlaneHist instance", Level.INFO, null);
		try {
			getEntityManager().clear();
			EntityManagerHelper.log("clear successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("clear failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String removeByIdJPQL = "delete from PlaneHist a where a.id in (?1)";

	public int removeById(PlaneHistId id) {
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
	 * Find all PlaneHist entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PlaneHist property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<PlaneHist> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PlaneHist> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding PlaneHist instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PlaneHist model where model."
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
	 * Find all PlaneHist entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PlaneHist> all PlaneHist entities
	 */
	@SuppressWarnings("unchecked")
	public List<PlaneHist> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding all PlaneHist instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from PlaneHist model";
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