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
import sonia.blog.entity.PermaObject;

import sonia.plugin.service.ServiceReference;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Sebastian Sdorra
 *
 * @param <T>
 */
public abstract class JpaGenericDAO<T extends PermaObject>
        implements GenericDAO<T>
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
   *
   * @param strategy
   * @param clazz
   * @param servicePath
   */
  public JpaGenericDAO(JpaStrategy strategy, Class<T> clazz, String servicePath)
  {
    this.strategy = strategy;
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
   * @param session
   * @param item
   *
   * @return
   */
  public boolean add(BlogSession session, T item)
  {
    return add(session, item, true);
  }

  /**
   * Method description
   *
   *
   *
   * @param session
   * @param item
   * @param notifyListener
   *
   * @return
   */
  public boolean add(BlogSession session, T item, boolean notifyListener)
  {
    if (!isPrivileged(session, item, ACTION_ADD))
    {
      logUnprivilegedMessage(session, item, ACTION_ADD);

      throw new BlogSecurityException("Author session is required");
    }

    Logger logger = getLogger();

    if (notifyListener)
    {
      fireEvent(Action.PREADD, item);
    }

    boolean result = true;

    try
    {
      strategy.store(item);
      strategy.flush();

      if (logger.isLoggable(Level.FINER))
      {
        StringBuffer msg = new StringBuffer();

        msg.append("user ").append(getUser(session));
        msg.append(" added ").append(item);
        logger.finer(msg.toString());
      }

      if (notifyListener)
      {
        fireEvent(Action.POSTADD, item);
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
      result = false;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param session
   * @param item
   *
   * @return
   */
  public boolean edit(BlogSession session, T item)
  {
    return edit(session, item, true);
  }

  /**
   * Method description
   *
   *
   *
   * @param session
   * @param item
   * @param notifyListener
   *
   * @return
   */
  public boolean edit(BlogSession session, T item, boolean notifyListener)
  {
    if (!isPrivileged(session, item, ACTION_EDIT))
    {
      logUnprivilegedMessage(session, item, ACTION_EDIT);

      throw new BlogSecurityException("Author session is required");
    }

    Logger logger = getLogger();

    if (notifyListener)
    {
      fireEvent(Action.PREUPDATE, item);
    }

    boolean result = true;

    try
    {
      strategy.edit(item);
      strategy.flush();

      if (logger.isLoggable(Level.FINER))
      {
        StringBuffer msg = new StringBuffer();

        msg.append("user ").append(getUser(session));
        msg.append(" edit ").append(item);
        logger.finer(msg.toString());
      }

      if (notifyListener)
      {
        fireEvent(Action.POSTUPDATE, item);
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
      result = false;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param session
   * @param item
   *
   * @return
   */
  public boolean remove(BlogSession session, T item)
  {
    return remove(session, item, true);
  }

  /**
   * Method description
   *
   *
   *
   * @param session
   * @param item
   * @param notifyListener
   *
   * @return
   */
  public boolean remove(BlogSession session, T item, boolean notifyListener)
  {
    if (!isPrivileged(session, item, ACTION_REMOVE))
    {
      logUnprivilegedMessage(session, item, ACTION_REMOVE);

      throw new BlogSecurityException("Author session is required");
    }

    Logger logger = getLogger();

    fireEvent(Action.PREREMOVE, item);

    boolean result = true;

    try
    {
      strategy.remove(item);
      strategy.flush();

      if (logger.isLoggable(Level.FINER))
      {
        StringBuffer msg = new StringBuffer();

        msg.append("user ").append(getUser(session));
        msg.append(" removes ").append(item);
        logger.finer(msg.toString());
      }

      fireEvent(Action.POSTREMOVE, item);
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
      result = false;
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

    try
    {
      item = strategy.get(clazz, id);
    }
    catch (NoResultException ex) {}
    finally {}

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
    Query q = strategy.getNamedQuery(queryName, false);

    if (blog != null)
    {
      q.setParameter("blog", blog);
    }

    return (Long) q.getSingleResult();
  }

  /**
   * Method description
   *
   *
   * @param value
   *
   * @return
   */
  protected String createFilter(String value)
  {
    if (!value.endsWith("%"))
    {
      value += "%";
    }

    return value;
  }

  /**
   * Method description
   *
   *
   *
   * @param q
   *
   * @return
   */
  protected List<T> excecuteListQuery(Query q)
  {
    return excecuteListQuery(clazz, q);
  }

  /**
   * Method description
   *
   *
   * @param type
   * @param q
   * @param <O>
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  protected <O> List<O> excecuteListQuery(Class<O> type, Query q)
  {
    List<O> resultList = null;

    try
    {
      resultList = q.getResultList();
    }
    catch (NoResultException ex) {}

    return resultList;
  }

  /**
   * Method description
   *
   *
   *
   * @param q
   *
   * @return
   */
  protected T excecuteQuery(Query q)
  {
    return excecuteQuery(clazz, q);
  }

  /**
   * Method description
   *
   *
   * @param type
   * @param q
   * @param <O>
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  protected <O> O excecuteQuery(Class<O> type, Query q)
  {
    O item = null;

    try
    {
      item = (O) q.getSingleResult();
    }
    catch (NoResultException ex) {}

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
    Query q = strategy.getNamedQuery(queryName, false);

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

    return excecuteListQuery(q);
  }

  /**
   * Method description
   *
   *
   * @param action
   * @param object
   */
  protected void fireEvent(Action action, PermaObject object)
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

  /**
   * Method description
   *
   *
   * @param session
   *
   * @return
   */
  private String getUser(BlogSession session)
  {
    String user = "anonymous";

    if ((session != null) && (session.getUser() != null))
    {
      user = session.getUser().getName();
    }

    return user;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected JpaStrategy strategy;

  /** Field description */
  private Class<T> clazz;

  /** Field description */
  private ServiceReference<DAOListener> reference;
}
