/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.PageDAO;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Page;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class JpaPageDAO extends JpaGenericDAO<Page> implements PageDAO
{

  /**
   * Constructs ...
   *
   *
   * @param entityManagerFactory
   */
  public JpaPageDAO(EntityManagerFactory entityManagerFactory)
  {
    super(entityManagerFactory, Page.class, Constants.LISTENER_PAGE);
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
    return countQuery("Page.count");
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
    return countQuery("Page.count", blog);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param id
   * @param blog
   * @param published
   *
   * @return
   */
  public Page get(Long id, Blog blog, boolean published)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Page.getByIdBlogAndPublished");

    q.setParameter("id", id);
    q.setParameter("blog", blog);
    q.setParameter("published", published);

    return excecuteQuery(em, q);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Page> getAll()
  {
    return findList("Page.getAll");
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
  public List<Page> getAll(int start, int max)
  {
    return findList("Page.getAll", start, max);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param published
   *
   * @return
   */
  public List<Page> getAllRoot(Blog blog, boolean published)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Page.getAllRootWithPublished");

    q.setParameter("blog", blog);
    q.setParameter("published", published);

    return excecuteListQuery(em, q);
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<Page> getAllRoot(Blog blog)
  {
    return findList("Page.getAllRoot", blog);
  }

  /**
   * Method description
   *
   *
   * @param parent
   * @param published
   *
   * @return
   */
  public List<Page> getChildren(Page parent, boolean published)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Page.getChildrenWithPublished");

    q.setParameter("parent", parent);
    q.setParameter("published", published);

    return excecuteListQuery(em, q);
  }

  /**
   * Method description
   *
   *
   * @param parent
   *
   * @return
   */
  public List<Page> getChildren(Page parent)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Page.getChildren");

    q.setParameter("parent", parent);

    return excecuteListQuery(em, q);
  }
}
