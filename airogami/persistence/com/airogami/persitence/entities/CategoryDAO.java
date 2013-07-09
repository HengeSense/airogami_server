package com.airogami.persitence.entities;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * Category entities. Transaction control of the save(), update() and delete()
 * operations must be handled externally by senders of these methods or must be
 * manually added to each of these methods for data to be persisted to the JPA
 * datastore.
 * 
 * @see com.airogami.persitence.entities.Category
 * @author MyEclipse Persistence Tools
 */

public class CategoryDAO {
	// property constants
	public static final String NAME = "name";

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved Category entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * CategoryDAO.save(category);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param category
	 *            Category entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Category category) {
		EntityManagerHelper.log("saving Category instance", Level.INFO, null);
		try {
			getEntityManager().persist(category);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent Category entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * CategoryDAO.delete(category);
	 * EntityManagerHelper.commit();
	 * category = null;
	 * </pre>
	 * 
	 * @param category
	 *            Category entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Category category) {
		EntityManagerHelper.log("deleting Category instance", Level.INFO, null);
		try {
			category = getEntityManager().getReference(Category.class,
					category.getCategoryId());
			getEntityManager().remove(category);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved Category entity and return it or a copy of it
	 * to the sender. A copy of the Category entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * category = CategoryDAO.update(category);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param category
	 *            Category entity to update
	 * @return Category the persisted Category entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Category update(Category category) {
		EntityManagerHelper.log("updating Category instance", Level.INFO, null);
		try {
			Category result = getEntityManager().merge(category);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Category findById(Short categoryId) {
		EntityManagerHelper.log("finding Category instance with id: "
				+ categoryId, Level.INFO, null);
		try {
			Category instance = getEntityManager().find(Category.class,
					categoryId);
			EntityManagerHelper.log("find successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Category getReference(Short categoryId) {
		EntityManagerHelper.log("getReferencing Category instance with id: "
				+ categoryId, Level.INFO, null);
		try {
			Category instance = getEntityManager().getReference(Category.class,
					categoryId);
			EntityManagerHelper
					.log("getReference successful", Level.INFO, null);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("getReference failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void detach(Category category) {
		EntityManagerHelper
				.log("detaching Category instance", Level.INFO, null);
		try {
			getEntityManager().detach(category);
			EntityManagerHelper.log("detach successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("detach failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void refresh(Category category) {
		EntityManagerHelper.log("refreshing Category instance", Level.INFO,
				null);
		try {
			getEntityManager().refresh(category);
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

	public void remove(Category category) {
		EntityManagerHelper.log("removing Category instance", Level.INFO, null);
		try {
			getEntityManager().remove(category);
			EntityManagerHelper.log("remove successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("remove failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void flush() {
		EntityManagerHelper.log("flushing Category instance", Level.INFO, null);
		try {
			getEntityManager().flush();
			EntityManagerHelper.log("flush successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("flush failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void clear() {
		EntityManagerHelper.log("clearing Category instance", Level.INFO, null);
		try {
			getEntityManager().clear();
			EntityManagerHelper.log("clear successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("clear failed", Level.SEVERE, re);
			throw re;
		}
	}

	private static final String removeByCategoryIdJPQL = "delete from Category a where a.categoryId in (?1)";

	public int removeByCategoryId(Short categoryId) {
		EntityManagerHelper.log("removeByCategoryId", Level.INFO, null);
		int ret = 0;
		try {
			Query query = getEntityManager()
					.createQuery(removeByCategoryIdJPQL);
			query.setParameter(1, categoryId);
			ret = query.executeUpdate();
			EntityManagerHelper.log("removeByCategoryId successful",
					Level.INFO, null);
			return ret;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("removeByCategoryId failed", Level.SEVERE,
					re);
			throw re;
		}
	}

	/**
	 * Find all Category entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Category property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<Category> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<Category> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding Category instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from Category model where model."
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

	public List<Category> findByName(Object name, int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}

	/**
	 * Find all Category entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<Category> all Category entities
	 */
	@SuppressWarnings("unchecked")
	public List<Category> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding all Category instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from Category model";
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