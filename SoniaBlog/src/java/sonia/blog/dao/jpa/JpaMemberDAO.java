/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.dao.MemberDAO;
import sonia.blog.entity.BlogMember;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.persistence.EntityManagerFactory;

/**
 *
 * @author sdorra
 */
public class JpaMemberDAO extends JpaGenericDAO<BlogMember> implements MemberDAO
{

  /**
   * Constructs ...
   *
   *
   * @param entityManagerFactory
   */
  public JpaMemberDAO(EntityManagerFactory entityManagerFactory)
  {
    super(entityManagerFactory, BlogMember.class);
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
    return countQuery("BlogMember.count");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<BlogMember> findAll()
  {
    return findList("BlogMember.findAll");
  }
}
