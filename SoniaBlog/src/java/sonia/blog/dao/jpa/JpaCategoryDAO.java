/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.dao.CategoryDAO;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.persistence.EntityManagerFactory;

/**
 *
 * @author sdorra
 */
public class JpaCategoryDAO extends JpaGenericDAO<Category>
        implements CategoryDAO
{

  /**
   * Constructs ...
   *
   *
   * @param entityManagerFactory
   */
  public JpaCategoryDAO(EntityManagerFactory entityManagerFactory)
  {
    super(entityManagerFactory, Category.class);
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
    return countQuery("Category.count");
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public long countByBlog(Blog blog)
  {
    return countQuery("Category.countByBlog", blog);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Category> findAll()
  {
    return findList("Category.findAll");
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<Category> findAllByBlog(Blog blog)
  {
    return findList("Category.findAllByBlog", blog);
  }
}
