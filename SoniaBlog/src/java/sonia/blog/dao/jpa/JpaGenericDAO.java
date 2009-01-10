/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.dao.GenericDAO;
import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public abstract class JpaGenericDAO<T> implements GenericDAO<T>
{

  /**
   * Constructs ...
   *
   *
   *
   * @param entityManagerFactory
   * @param clazz
   */
  public JpaGenericDAO(EntityManagerFactory entityManagerFactory,
                       Class<T> clazz)
  {
    this.entityManagerFactory = entityManagerFactory;
    this.clazz = clazz;
    logger = Logger.getLogger(getClass().getName());
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param item
   *
   * @return
   */
  public boolean add(T item)
  {
    boolean result = true;
    EntityManager em = createEntityManager();

    em.getTransaction().begin();

    try
    {
      em.persist(item);
      em.getTransaction().commit();
    }
    catch (Exception ex)
    {
      if (em.getTransaction().isActive())
      {
        em.getTransaction().rollback();
      }

      logger.log(Level.SEVERE, null, ex);
      result = false;
    }
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param item
   *
   * @return
   */
  public boolean edit(T item)
  {
    boolean result = true;
    EntityManager em = createEntityManager();

    em.getTransaction().begin();

    try
    {
      item = em.merge(item);
      em.getTransaction().commit();
    }
    catch (Exception ex)
    {
      if (em.getTransaction().isActive())
      {
        em.getTransaction().rollback();
      }

      logger.log(Level.SEVERE, null, ex);
      result = false;
    }
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param id
   *
   * @return
   */
  public T find(Long id)
  {
    T item = null;
    EntityManager em = createEntityManager();

    try
    {
      item = em.find(clazz, id);
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return item;
  }

  /**
   * Method description
   *
   *
   * @param item
   *
   * @return
   */
  public boolean remove(T item)
  {
    boolean result = true;
    EntityManager em = createEntityManager();

    em.getTransaction().begin();

    try
    {
      em.remove(em.merge(item));
      em.getTransaction().commit();
    }
    catch (Exception ex)
    {
      if (em.getTransaction().isActive())
      {
        em.getTransaction().rollback();
      }

      logger.log(Level.SEVERE, null, ex);
      result = false;
    }
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isEmpty()
  {
    return count() == 0;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param queryName
   *
   * @return
   */
  protected long countQuery(String queryName)
  {
    return countQuery(queryName, null);
  }

  /**
   * Method description
   *
   *
   * @param queryName
   * @param blog
   *
   * @return
   */
  protected long countQuery(String queryName, Blog blog)
  {
    long result = 0;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery(queryName);

    if (blog != null)
    {
      q.setParameter("blog", blog);
    }

    result = (Long) q.getSingleResult();
    em.close();

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  protected EntityManager createEntityManager()
  {
    return entityManagerFactory.createEntityManager();
  }

  /**
   * Method description
   *
   *
   * @param queryName
   *
   * @return
   */
  protected List<T> findList(String queryName)
  {
    return findList(queryName, null);
  }

  /**
   * Method description
   *
   *
   * @param queryName
   * @param blog
   *
   * @return
   */
  protected List<T> findList(String queryName, Blog blog)
  {
    return findList(queryName, blog, -1, -1);
  }

  /**
   * Method description
   *
   *
   * @param queryName
   * @param start
   * @param max
   *
   * @return
   */
  protected List<T> findList(String queryName, int start, int max)
  {
    return findList(queryName, null, start, max);
  }

  /**
   * Method description
   *
   *
   * @param queryName
   * @param blog
   * @param start
   * @param max
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  protected List<T> findList(String queryName, Blog blog, int start, int max)
  {
    List<T> result = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery(queryName);

    if (blog != null)
    {
      q.setParameter("blog", blog);
    }

    if (start > 0)
    {
      q.setFirstResult(start);
    }

    if (max > 0)
    {
      q.setMaxResults(max);
    }

    try
    {
      result = q.getResultList();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected EntityManagerFactory entityManagerFactory;

  /** Field description */
  protected Logger logger;

  /** Field description */
  private Class<T> clazz;
}
