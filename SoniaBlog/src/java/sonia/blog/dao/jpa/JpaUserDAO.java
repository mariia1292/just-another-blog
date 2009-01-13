/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.entity.User;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class JpaUserDAO extends JpaGenericDAO<User> implements UserDAO
{

  /**
   * Constructs ...
   *
   *
   * @param entityManagerFactory
   */
  public JpaUserDAO(EntityManagerFactory entityManagerFactory)
  {
    super(entityManagerFactory, User.class, Constants.LISTENER_USER);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public long count()
  {
    return countQuery("User.count");
  }

  /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   */
  public User findActiveByName(String name)
  {
    User user = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("User.findActiveByName");

    q.setParameter("name", name);

    try
    {
      user = (User) q.getSingleResult();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return user;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<User> findAll()
  {
    return findList("User.findAll");
  }

  /**
   * Method description
   *
   *
   * @param start
   * @param max
   *
   * @return
   */
  public List<User> findAll(int start, int max)
  {
    return findList("User.findAll", start, max);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<User> findAllActives()
  {
    return findList("User.findAllActives");
  }

  /**
   * Method description
   *
   *
   * @param start
   * @param max
   *
   * @return
   */
  public List<User> findAllActives(int start, int max)
  {
    return findList("User.findAllActives", start, max);
  }

  /**
   * Method description
   *
   *
   * @param email
   *
   * @return
   */
  public User findByEmail(String email)
  {
    User user = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("User.findByEmail");

    q.setParameter("email", email);

    try {}
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return user;
  }

  /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   */
  public User findByName(String name)
  {
    User user = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("User.findByName");

    q.setParameter("name", name);

    try
    {
      user = (User) q.getSingleResult();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return user;
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param code
   *
   * @return
   */
  public User findByNameAndCode(String name, String code)
  {
    User user = null;
    EntityManager em = createEntityManager();

    try
    {
      Query q = em.createNamedQuery("User.findByNameAndCode");

      q.setParameter("name", name);
      q.setParameter("code", code);
      user = (User) q.getSingleResult();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return user;
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param password
   *
   * @return
   */
  public User findByNameAndPassword(String name, String password)
  {
    User user = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("User.findByNameAndPassword");

    q.setParameter("name", name);
    q.setParameter("password", password);

    try
    {
      user = (User) q.getSingleResult();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return user;
  }
}
