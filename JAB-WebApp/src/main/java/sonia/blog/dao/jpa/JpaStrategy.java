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

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Sebastian Sdorra
 */
public class JpaStrategy
{

  /** Field description */
  private static Logger logger = Logger.getLogger(JpaStrategy.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param entityManagerFactory
   */
  public JpaStrategy(EntityManagerFactory entityManagerFactory)
  {
    this.entityManagerFactory = entityManagerFactory;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  public void close()
  {
    entityManagerFactory.close();
    store = null;
  }

  /**
   * Method description
   *
   *
   * @param item
   * @param <T>
   *
   * @return
   */
  public <T> T edit(T item)
  {
    EntityManager em = getEntityManager(true);

    if (!em.contains(item))
    {
      item = em.merge(item);
    }

    return item;
  }

  /**
   * Method description
   *
   */
  public void flush()
  {
    EntityManager em = store.get();

    if (em != null)
    {
      if (logger.isLoggable(Level.FINEST))
      {
        logger.finest("commit transaction");
      }

      em.getTransaction().commit();
    }
  }

  /**
   * Method description
   *
   */
  public void release()
  {
    EntityManager em = store.get();

    if (em != null)
    {
      if (em.getTransaction().isActive())
      {
        if (logger.isLoggable(Level.WARNING))
        {
          logger.warning("rollback uncomitted transaction");
        }

        em.getTransaction().rollback();
      }

      if (logger.isLoggable(Level.FINER))
      {
        logger.finer("release EntityManager");
      }

      em.close();
      store.remove();
      em = null;
    }
  }

  /**
   * Method description
   *
   *
   * @param item
   */
  public void remove(Object item)
  {
    EntityManager em = getEntityManager(true);

    if (!em.contains(item))
    {
      item = em.merge(item);
    }

    em.remove(item);
  }

  /**
   * Method description
   *
   *
   * @param item
   * @param <T>
   *
   * @return
   */
  public <T> T store(T item)
  {
    EntityManager em = getEntityManager(true);

    if (!em.contains(item))
    {
      em.persist(item);
    }

    return item;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param clazz
   * @param id
   * @param <T>
   *
   * @return
   */
  public <T> T get(Class<T> clazz, Long id)
  {
    EntityManager em = getEntityManager(false);

    return em.find(clazz, id);
  }

  /**
   * Method description
   *
   *
   *
   * @param transaction
   * @return
   */
  public EntityManager getEntityManager(boolean transaction)
  {
    EntityManager em = store.get();

    if (em == null)
    {
      if (logger.isLoggable(Level.FINER))
      {
        logger.finer("create new EntityManager");
      }

      em = entityManagerFactory.createEntityManager();
      store.set(em);
    }

    if (transaction &&!em.getTransaction().isActive())
    {
      if (logger.isLoggable(Level.FINEST))
      {
        logger.finest("create new transaction");
      }

      em.getTransaction().begin();
    }

    return em;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public EntityManagerFactory getEntityManagerFactory()
  {
    return entityManagerFactory;
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param transaction
   *
   * @return
   */
  public Query getNamedQuery(String name, boolean transaction)
  {
    EntityManager em = getEntityManager(transaction);

    return em.createNamedQuery(name);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private EntityManagerFactory entityManagerFactory;

  /** Field description */
  private ThreadLocal<EntityManager> store = new ThreadLocal<EntityManager>();
}
