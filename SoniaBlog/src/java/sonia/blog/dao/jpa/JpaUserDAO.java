/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

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
public class JpaUserDAO extends JpaGenericDAO<User> implements UserDAO
{

  /** Field description */
  private static Logger logger = Logger.getLogger(JpaUserDAO.class.getName());

  //~--- constructors ---------------------------------------------------------

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
   * @param blog
   *
   * @return
   */
  public long count(Blog blog)
  {
    return countQuery("BlogMember.countByBlog", blog);
  }

  /**
   * Method description
   *
   *
   * @param filter
   *
   * @return
   */
  public long count(String filter)
  {
    long result = -1;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("User.countByFilter");

    q.setParameter("filter", filter);

    try
    {
      result = (Long) q.getSingleResult();
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
   * @param filter
   * @param active
   *
   * @return
   */
  public long count(String filter, boolean active)
  {
    long result = -1;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("User.countByFilterAndActive");

    q.setParameter("filter", filter);
    q.setParameter("active", active);

    try
    {
      result = (Long) q.getSingleResult();
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
   * @param active
   *
   * @return
   */
  public long count(boolean active)
  {
    long result = -1;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("User.countByActive");

    q.setParameter("active", active);

    try
    {
      result = (Long) q.getSingleResult();
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
   * @param username
   * @param active
   *
   * @return
   */
  public User get(String username, boolean active)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("User.getByNameAndActive");

    q.setParameter("name", username);
    q.setParameter("active", active);

    return excecuteQuery(em, q);
  }

  /**
   * Method description
   *
   *
   * @param username
   *
   * @return
   */
  public User get(String username)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("User.getByName");

    q.setParameter("name", username);

    return excecuteQuery(em, q);
  }

  /**
   * Method description
   *
   *
   * @param username
   * @param password
   * @param active
   *
   * @return
   */
  public User get(String username, String password, boolean active)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("User.getByNamePasswordAndActive");

    q.setParameter("name", username);
    q.setParameter("password", password);
    q.setParameter("active", active);

    return excecuteQuery(em, q);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<User> getAll()
  {
    return findList("User.getAll");
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
  public List<User> getAll(int start, int max)
  {
    return findList("User.getAll", start, max);
  }

  /**
   * Method description
   *
   *
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  public List<User> getAll(boolean active, int start, int max)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("User.getAllByActive");

    q.setParameter("active", active);
    q.setFirstResult(start);
    q.setMaxResults(max);

    return excecuteListQuery(em, q);
  }

  /**
   * Method description
   *
   *
   * @param filter
   * @param start
   * @param max
   *
   * @return
   */
  public List<User> getAll(String filter, int start, int max)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("User.getAllByFilter");

    q.setParameter("filter", filter);
    q.setFirstResult(start);
    q.setMaxResults(max);

    return excecuteListQuery(em, q);
  }

  /**
   * Method description
   *
   *
   * @param filter
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  public List<User> getAll(String filter, boolean active, int start, int max)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("User.getAllByFilterAndActive");

    q.setParameter("filter", filter);
    q.setParameter("active", active);
    q.setFirstResult(start);
    q.setMaxResults(max);

    return excecuteListQuery(em, q);
  }

  /**
   * Method description
   *
   *
   * @param mail
   *
   * @return
   */
  public User getByMail(String mail)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("User.getByMail");

    q.setParameter("mail", mail);

    return excecuteQuery(em, q);
  }

  /**
   * Method description
   *
   *
   * @param username
   * @param code
   *
   * @return
   */
  public User getByNameAndCode(String username, String code)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("User.getByNameAndCode");

    q.setParameter("name", username);
    q.setParameter("code", code);

    return excecuteQuery(em, q);
  }

  /**
   * Method description
   *
   *
   * @param user
   * @param start
   * @param max
   *
   * @return
   */
  public List<BlogMember> getMembers(User user, int start, int max)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("BlogMember.getAllByUser");

    q.setParameter("user", user);
    q.setFirstResult(start);
    q.setMaxResults(max);

    return excecuteListQuery(BlogMember.class, em, q);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param user
   *
   * @return
   */
  public Role getRole(Blog blog, User user)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("BlogMember.getByBlogAndUser");

    q.setParameter("blog", blog);
    q.setParameter("user", user);

    Role role = null;
    BlogMember member = excecuteQuery(BlogMember.class, em, q);

    if (member != null)
    {
      role = member.getRole();
    }

    return role;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blog
   * @param user
   * @param role
   */
  public void setRole(Blog blog, User user, Role role)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("BlogMember.getByBlogAndUser");

    q.setParameter("blog", blog);
    q.setParameter("user", user);

    BlogMember member = null;

    try
    {
      member = (BlogMember) q.getSingleResult();
    }
    catch (NoResultException ex) {}

    em.getTransaction().begin();

    try
    {
      if (member == null)
      {
        member = new BlogMember(blog, user, role);
        em.persist(member);
      }
      else
      {
        member.setRole(role);
        em.merge(member);
      }

      em.getTransaction().commit();
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected Logger getLogger()
  {
    return logger;
  }
}
