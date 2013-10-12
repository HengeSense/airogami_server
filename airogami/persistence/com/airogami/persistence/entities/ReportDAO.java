package com.airogami.persistence.entities;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * Report entities. Transaction control of the save(), update() and delete()
 * operations must be handled externally by senders of these methods or must be
 * manually added to each of these methods for data to be persisted to the JPA
 * datastore.
 * 
 * @see com.airogami.persistence.entities.Report
 * @author MyEclipse Persistence Tools
 */

public class ReportDAO {
	// property constants
	public static final String REPORT_COUNT = "reportCount";
	public static final String REASON = "reason";

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved Report entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ReportDAO.save(report);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param report
	 *            Report entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Report report) {
		EntityManagerHelper.log("saving Report instance", Level.INFO, null);
		try {
			getEntityManager().persist(report);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent Report entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ReportDAO.delete(report);
	 * EntityManagerHelper.commit();
	 * report = null;
	 * </pre>
	 * 
	 * @param report
	 *            Report entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Report report) {
		EntityManagerHelper.log("deleting Report instance", Level.INFO, null);
		try {
			report = getEntityManager().getReference(Report.class,
					report.getId());
			getEntityManager().remove(report);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved Report entity and return it or a copy of it to
	 * the sender. A copy of the Report entity parameter is returned when the
	 * JPA persistence mechanism has not previously been tracking the updated
	 * entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * report = ReportDAO.update(report);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param report
	 *            Report entity to update
	 * @return Report the persisted Report entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Report update(Report report) {
		EntityManagerHelper.log("updating Report instance", Level.INFO, null);
		try {
			Report result = getEntityManager().merge(report);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Report findById(ReportId id) {
		EntityManagerHelper.log("finding Report instance with id: " + id,
				Level.INFO, null);
		try {
			Report instance = getEntityManager().find(Report.class, id);
			EntityManagerHelper.log("find successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Report getReference(ReportId id) {
		EntityManagerHelper.log(
				"getReferencing Report instance with id: " + id, Level.INFO,
				null);
		try {
			Report instance = getEntityManager().getReference(Report.class, id);
			EntityManagerHelper
					.log("getReference successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getReference failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void detach(Report report) {
		EntityManagerHelper.log("detaching Report instance", Level.INFO, null);
		try {
			getEntityManager().detach(report);
			EntityManagerHelper.log("detach successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("detach failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void refresh(Report report) {
		EntityManagerHelper.log("refreshing Report instance", Level.INFO, null);
		try {
			getEntityManager().refresh(report);
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

	public void remove(Report report) {
		EntityManagerHelper.log("removing Report instance", Level.INFO, null);
		try {
			getEntityManager().remove(report);
			EntityManagerHelper.log("remove successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("remove failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void flush() {
		EntityManagerHelper.log("flushing Report instance", Level.INFO, null);
		try {
			getEntityManager().flush();
			EntityManagerHelper.log("flush successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("flush failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void clear() {
		EntityManagerHelper.log("clearing Report instance", Level.INFO, null);
		try {
			getEntityManager().clear();
			EntityManagerHelper.log("clear successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("clear failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String removeByIdJPQL = "delete from Report a where a.id in (?1)";

	public int removeById(ReportId id) {
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

	private static final String increaseReportCountJPQL = "update Report a set a.reportCount = a.reportCount + :count where a.id in (:id)";

	public boolean increaseReportCount(
			com.airogami.persistence.entities.ReportId id, int count) {
		EntityManagerHelper.log("increaseReportCount with id: " + id,
				Level.INFO, null);
		try {
			Query query = getEntityManager().createQuery(
					increaseReportCountJPQL);
			query.setParameter("id", id);
			query.setParameter("count", count);
			boolean result = query.executeUpdate() == 1;
			EntityManagerHelper.log("increaseReportCount successful",
					Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("increaseReportCount failed", Level.SEVERE,
					re);
			throw re;
		}
	}

	/**
	 * Find all Report entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Report property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<Report> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<Report> findByProperty(String propertyName, final Object value,
			final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding Report instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from Report model where model."
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

	public List<Report> findByReportCount(Object reportCount,
			int... rowStartIdxAndCount) {
		return findByProperty(REPORT_COUNT, reportCount, rowStartIdxAndCount);
	}

	public List<Report> findByReason(Object reason, int... rowStartIdxAndCount) {
		return findByProperty(REASON, reason, rowStartIdxAndCount);
	}

	/**
	 * Find all Report entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<Report> all Report entities
	 */
	@SuppressWarnings("unchecked")
	public List<Report> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding all Report instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from Report model";
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