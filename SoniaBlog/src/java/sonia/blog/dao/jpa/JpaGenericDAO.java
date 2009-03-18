/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.dao.DAOListener;
import sonia.blog.api.dao.DAOListener.Action;
import sonia.blog.api.dao.GenericDAO;
import sonia.blog.entity.Blog;

import sonia.plugin.service.ServiceReference;

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
   * @param servicePath
   */
  public JpaGenericDAO(EntityManagerFactory entityManagerFactory,
                       Class<T> clazz, String servicePath)
  {
    this.entityManagerFactory = entityManagerFactory;
    this.clazz = clazz;
    logger = Logger.getLogger(getClass().getName());

    BlogContext context = BlogContext.getInstance();

    reference = context.getServiceRegistry().get(DAOListener.class,
            servicePath);
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
    fireEvent(Action.PREADD, item);

    boolean result = true;
    EntityManager em = createEntityManager();

    em.getTransaction().begin();

    try
    {
      em.persist(item);
      em.getTransaction().commit();
      fireEvent(Action.POSTADD, item);
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
    fireEvent(Action.PREUPDATE, item);

    boolean result = true;
    EntityManager em = createEntityManager();

    em.getTransaction().begin();

    try
    {
      item = em.merge(item);
      em.getTransaction().commit();
      fireEvent(Action.POSTUPDATE, item);
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
  public boolean remove(T item)
  {
    fireEvent(Action.PREREMOVE, item);

    boolean result = true;
    EntityManager em = createEntityManager();

    em.getTransaction().begin();

    try
    {
      em.remove(em.merge(item));
      em.getTransaction().commit();
      fireEvent(Action.POSTREMOVE, item);
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
   * @param id
   *
   * @return
   */
  public T get(Long id)
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
   *
   * @param em
   * @param q
   *
   * @return
   */
  protected List<T> excecuteListQuery(EntityManager em, Query q)
  {
    return excecuteListQuery(clazz, em, q);
  }

  /**
   * Method description
   *
   *
   * @param type
   * @param em
   * @param q
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  protected <O>List<O> excecuteListQuery(Class<O> type, EntityManager em,
          Query q)
  {
    List<O> resultList = null;

    try
    {
      resultList = q.getResultList();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return resultList;
  }

  /**
   * Method description
   *
   *
   *
   * @param em
   * @param q
   *
   * @return
   */
  protected T excecuteQuery(EntityManager em, Query q)
  {
    return excecuteQuery(clazz, em, q);
  }

  /**
   * Method description
   *
   *
   * @param type
   * @param em
   * @param q
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  protected <O>O excecuteQuery(Class<O> type, EntityManager em, Query q)
  {
    O item = null;

    try
    {
      item = (O) q.getSingleResult();
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

    return excecuteListQuery(em, q);
  }

  /**
   * Method description
   *
   *
   * @param action
   * @param object
   */
  protected void fireEvent(Action action, Object object)
  {
    List<DAOListener> listeners = reference.getAll();

    if ((listeners != null) &&!listeners.isEmpty())
    {
      for (DAOListener listener : listeners)
      {
        listener.handleEvent(action, object);
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected EntityManagerFactory entityManagerFactory;

  /** Field description */
  protected Logger logger;

  /** Field description */
  private Class<T> clazz;

  /** Field description */
  private ServiceReference<DAOListener> reference;
}
