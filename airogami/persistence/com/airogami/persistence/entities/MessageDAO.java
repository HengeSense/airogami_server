package com.airogami.persistence.entities;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * Message entities. Transaction control of the save(), update() and delete()
 * operations must be handled externally by senders of these methods or must be
 * manually added to each of these methods for data to be persisted to the JPA
 * datastore.
 * 
 * @see com.airogami.persistence.entities.Message
 * @author MyEclipse Persistence Tools
 */

public class MessageDAO {
	// property constants
	public static final String CONTENT = "content";
	public static final String TYPE = "type";
	public static final String STATUS = "status";

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved Message entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * MessageDAO.save(message);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param message
	 *            Message entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Message message) {
		EntityManagerHelper.log("saving Message instance", Level.INFO, null);
		try {
			getEntityManager().persist(message);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent Message entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * MessageDAO.delete(message);
	 * EntityManagerHelper.commit();
	 * message = null;
	 * </pre>
	 * 
	 * @param message
	 *            Message entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Message message) {
		EntityManagerHelper.log("deleting Message instance", Level.INFO, null);
		try {
			message = getEntityManager().getReference(Message.class,
					message.getMessageId());
			getEntityManager().remove(message);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved Message entity and return it or a copy of it
	 * to the sender. A copy of the Message entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * message = MessageDAO.update(message);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param message
	 *            Message entity to update
	 * @return Message the persisted Message entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Message update(Message message) {
		EntityManagerHelper.log("updating Message instance", Level.INFO, null);
		try {
			Message result = getEntityManager().merge(message);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Message findById(Long messageId) {
		EntityManagerHelper.log("finding Message instance with id: "
				+ messageId, Level.INFO, null);
		try {
			Message instance = getEntityManager()
					.find(Message.class, messageId);
			EntityManagerHelper.log("find successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Message getReference(Long messageId) {
		EntityManagerHelper.log("getReferencing Message instance with id: "
				+ messageId, Level.INFO, null);
		try {
			Message instance = getEntityManager().getReference(Message.class,
					messageId);
			EntityManagerHelper
					.log("getReference successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getReference failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void detach(Message message) {
		EntityManagerHelper.log("detaching Message instance", Level.INFO, null);
		try {
			getEntityManager().detach(message);
			EntityManagerHelper.log("detach successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("detach failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void refresh(Message message) {
		EntityManagerHelper
				.log("refreshing Message instance", Level.INFO, null);
		try {
			getEntityManager().refresh(message);
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

	public void remove(Message message) {
		EntityManagerHelper.log("removing Message instance", Level.INFO, null);
		try {
			getEntityManager().remove(message);
			EntityManagerHelper.log("remove successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("remove failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void flush() {
		EntityManagerHelper.log("flushing Message instance", Level.INFO, null);
		try {
			getEntityManager().flush();
			EntityManagerHelper.log("flush successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("flush failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void clear() {
		EntityManagerHelper.log("clearing Message instance", Level.INFO, null);
		try {
			getEntityManager().clear();
			EntityManagerHelper.log("clear successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("clear failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String removeByMessageIdJPQL = "delete from Message a where a.messageId in (?1)";

	public int removeByMessageId(Long messageId) {
		EntityManagerHelper.log("removeByMessageId", Level.INFO, null);
		int ret = 0;
		try {
			Query query = getEntityManager().createQuery(removeByMessageIdJPQL);
			query.setParameter(1, messageId);
			ret = query.executeUpdate();
			EntityManagerHelper.log("removeByMessageId successful", Level.INFO,
					null);
			return ret;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("removeByMessageId failed", Level.SEVERE,
					re);
			throw re;
		}
	}

	private static final String accountJPQL = "select a.account.accountId from Message a where a.messageId = :messageId";

	public java.lang.Long getAccount(java.lang.Long messageId) {
		EntityManagerHelper.log("getPlaneId with messageId" + messageId,
				Level.INFO, null);
		java.lang.Long accountId;
		try {
			Query query = getEntityManager().createQuery(accountJPQL);
			query.setParameter("messageId", messageId);
			accountId = (java.lang.Long) query.getSingleResult();
			EntityManagerHelper.log("getPlaneId successful", Level.INFO, null);
			return accountId;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getPlaneId failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String planeJPQL = "select a.plane.planeId from Message a where a.messageId = :messageId";

	public java.lang.Long getPlane(java.lang.Long messageId) {
		EntityManagerHelper.log("getPlaneId with messageId" + messageId,
				Level.INFO, null);
		java.lang.Long planeId;
		try {
			Query query = getEntityManager().createQuery(planeJPQL);
			query.setParameter("messageId", messageId);
			planeId = (java.lang.Long) query.getSingleResult();
			EntityManagerHelper.log("getPlaneId successful", Level.INFO, null);
			return planeId;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getPlaneId failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all Message entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Message property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<Message> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<Message> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding Message instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from Message model where model."
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

	public List<Message> findByContent(Object content,
			int... rowStartIdxAndCount) {
		return findByProperty(CONTENT, content, rowStartIdxAndCount);
	}

	public List<Message> findByType(Object type, int... rowStartIdxAndCount) {
		return findByProperty(TYPE, type, rowStartIdxAndCount);
	}

	public List<Message> findByStatus(Object status, int... rowStartIdxAndCount) {
		return findByProperty(STATUS, status, rowStartIdxAndCount);
	}

	/**
	 * Find all Message entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<Message> all Message entities
	 */
	@SuppressWarnings("unchecked")
	public List<Message> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding all Message instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from Message model";
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