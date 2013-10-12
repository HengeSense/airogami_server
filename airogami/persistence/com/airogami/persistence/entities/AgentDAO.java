package com.airogami.persistence.entities;

import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for Agent
 * entities. Transaction control of the save(), update() and delete() operations
 * must be handled externally by senders of these methods or must be manually
 * added to each of these methods for data to be persisted to the JPA datastore.
 * 
 * @see com.airogami.persistence.entities.Agent
 * @author MyEclipse Persistence Tools
 */

public class AgentDAO {
	// property constants
	public static final String DEV_TYPE = "devType";
	public static final String DEV_VERSION = "devVersion";
	public static final String DEV_TOKEN = "devToken";

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved Agent entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * AgentDAO.save(agent);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param agent
	 *            Agent entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Agent agent) {
		EntityManagerHelper.log("saving Agent instance", Level.INFO, null);
		try {
			getEntityManager().persist(agent);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent Agent entity. This operation must be performed within
	 * the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * AgentDAO.delete(agent);
	 * EntityManagerHelper.commit();
	 * agent = null;
	 * </pre>
	 * 
	 * @param agent
	 *            Agent entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Agent agent) {
		EntityManagerHelper.log("deleting Agent instance", Level.INFO, null);
		try {
			agent = getEntityManager().getReference(Agent.class,
					agent.getAccountId());
			getEntityManager().remove(agent);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved Agent entity and return it or a copy of it to
	 * the sender. A copy of the Agent entity parameter is returned when the JPA
	 * persistence mechanism has not previously been tracking the updated
	 * entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * agent = AgentDAO.update(agent);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param agent
	 *            Agent entity to update
	 * @return Agent the persisted Agent entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Agent update(Agent agent) {
		EntityManagerHelper.log("updating Agent instance", Level.INFO, null);
		try {
			Agent result = getEntityManager().merge(agent);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Agent findById(Integer accountId) {
		EntityManagerHelper.log("finding Agent instance with id: " + accountId,
				Level.INFO, null);
		try {
			Agent instance = getEntityManager().find(Agent.class, accountId);
			EntityManagerHelper.log("find successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Agent getReference(Integer accountId) {
		EntityManagerHelper.log("getReferencing Agent instance with id: "
				+ accountId, Level.INFO, null);
		try {
			Agent instance = getEntityManager().getReference(Agent.class,
					accountId);
			EntityManagerHelper
					.log("getReference successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getReference failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void detach(Agent agent) {
		EntityManagerHelper.log("detaching Agent instance", Level.INFO, null);
		try {
			getEntityManager().detach(agent);
			EntityManagerHelper.log("detach successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("detach failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void refresh(Agent agent) {
		EntityManagerHelper.log("refreshing Agent instance", Level.INFO, null);
		try {
			getEntityManager().refresh(agent);
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

	public void remove(Agent agent) {
		EntityManagerHelper.log("removing Agent instance", Level.INFO, null);
		try {
			getEntityManager().remove(agent);
			EntityManagerHelper.log("remove successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("remove failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void flush() {
		EntityManagerHelper.log("flushing Agent instance", Level.INFO, null);
		try {
			getEntityManager().flush();
			EntityManagerHelper.log("flush successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("flush failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void clear() {
		EntityManagerHelper.log("clearing Agent instance", Level.INFO, null);
		try {
			getEntityManager().clear();
			EntityManagerHelper.log("clear successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("clear failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String removeByAccountIdJPQL = "delete from Agent a where a.accountId in (?1)";

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
	 * Find all Agent entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Agent property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<Agent> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<Agent> findByProperty(String propertyName, final Object value,
			final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding Agent instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from Agent model where model."
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

	public List<Agent> findByDevType(Object devType, int... rowStartIdxAndCount) {
		return findByProperty(DEV_TYPE, devType, rowStartIdxAndCount);
	}

	public List<Agent> findByDevVersion(Object devVersion,
			int... rowStartIdxAndCount) {
		return findByProperty(DEV_VERSION, devVersion, rowStartIdxAndCount);
	}

	public List<Agent> findByDevToken(Object devToken,
			int... rowStartIdxAndCount) {
		return findByProperty(DEV_TOKEN, devToken, rowStartIdxAndCount);
	}

	/**
	 * Find all Agent entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<Agent> all Agent entities
	 */
	@SuppressWarnings("unchecked")
	public List<Agent> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper
				.log("finding all Agent instances", Level.INFO, null);
		try {
			final String queryString = "select model from Agent model";
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