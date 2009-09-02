/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://kenai.com/projects/jab
 *
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogSession;
import sonia.blog.api.dao.DAOListener;
import sonia.blog.api.dao.DAOListener.Action;
import sonia.blog.api.dao.GenericDAO;
import sonia.blog.api.exception.BlogSecurityException;
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
 * @author Sebastian Sdorra
 *
 * @param <T>
 */
public abstract class JpaGenericDAO<T> implements GenericDAO<T>
{

  /** Field description */
  public static final int ACTION_ADD = 0;

  /** Field description */
  public static final int ACTION_EDIT = 1;

  /** Field description */
  public static final int ACTION_REMOVE = 2;

  //~--- constructors ---------------------------------------------------------

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

    BlogContext context = BlogContext.getInstance();

    reference = context.getServiceRegistry().get(DAOListener.class,
            servicePath);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract Logger getLogger();

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param session
   * @param item
   *
   * @return
   */
  public boolean add(BlogSession session, T item)
  {
    if (!isPrivileged(session, item, ACTION_ADD))
    {
      logUnprivilegedMessage(session, item, ACTION_ADD);

      throw new BlogSecurityException("Author session is required");
    }

    Logger logger = getLogger();

    fireEvent(Action.PREADD, item);

    boolean result = true;
    EntityManager em = createEntityManager();

    em.getTransaction().begin();

    try
    {
      em.persist(item);
      em.getTransaction().commit();

      if (logger.isLoggable(Level.FINER))
      {
        StringBuffer msg = new StringBuffer();

        msg.append("user ").append(session.getUser().getName());
        msg.append(" added ").append(item);
        logger.finer(msg.toString());
      }

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
   *
   * @param session
   * @param item
   *
   * @return
   */
  public boolean edit(BlogSession session, T item)
  {
    if (!isPrivileged(session, item, ACTION_EDIT))
    {
      logUnprivilegedMessage(session, item, ACTION_EDIT);

      throw new BlogSecurityException("Author session is required");
    }

    Logger logger = getLogger();

    fireEvent(Action.PREUPDATE, item);

    boolean result = true;
    EntityManager em = createEntityManager();

    em.getTransaction().begin();

    try
    {
      item = em.merge(item);
      em.getTransaction().commit();

      if (logger.isLoggable(Level.FINER))
      {
        StringBuffer msg = new StringBuffer();

        msg.append("user ").append(session.getUser().getName());
        msg.append(" edit ").append(item);
        logger.finer(msg.toString());
      }

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
   *
   * @param session
   * @param item
   *
   * @return
   */
  public boolean remove(BlogSession session, T item)
  {
    if (!isPrivileged(session, item, ACTION_REMOVE))
    {
      logUnprivilegedMessage(session, item, ACTION_REMOVE);

      throw new BlogSecurityException("Author session is required");
    }

    Logger logger = getLogger();

    fireEvent(Action.PREREMOVE, item);

    boolean result = true;
    EntityManager em = createEntityManager();

    em.getTransaction().begin();

    try
    {
      em.remove(em.merge(item));
      em.getTransaction().commit();

      if (logger.isLoggable(Level.FINER))
      {
        StringBuffer msg = new StringBuffer();

        msg.append("user ").append(session.getUser().getName());
        msg.append(" removes ").append(item);
        logger.finer(msg.toString());
      }

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
   * @param <O>
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  protected <O> List<O> excecuteListQuery(Class<O> type, EntityManager em,
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
   * @param <O>
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  protected <O> O excecuteQuery(Class<O> type, EntityManager em, Query q)
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

  /**
   * Method description
   *
   *
   * @param session
   * @param item
   * @param action
   */
  protected void logUnprivilegedMessage(BlogSession session, T item, int action)
  {
    StringBuffer msg = new StringBuffer();

    msg.append("unprivileged user ").append(session.getUser().getName());
    msg.append(" tried to ");

    switch (action)
    {
      case JpaGenericDAO.ACTION_ADD :
        msg.append("add");

        break;

      case JpaGenericDAO.ACTION_EDIT :
        msg.append("modify");

        break;

      case JpaGenericDAO.ACTION_REMOVE :
        msg.append("remove");

        break;

      default :
        msg.append("unknown");
    }

    msg.append(" ").append(item);
    getLogger().severe(msg.toString());
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param session
   * @param item
   * @param action
   *
   * @return
   */
  protected boolean isPrivileged(BlogSession session, T item, int action)
  {
    return true;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected EntityManagerFactory entityManagerFactory;

  /** Field description */
  private Class<T> clazz;

  /** Field description */
  private ServiceReference<DAOListener> reference;
}
