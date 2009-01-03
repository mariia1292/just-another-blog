/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

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
    super(entityManagerFactory, User.class);
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
   * @return
   */
  public List<User> findAllActive()
  {
    throw new UnsupportedOperationException("Not supported yet.");
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
