/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import java.util.List;
import sonia.blog.entity.Blog;

/**
 *
 * @author sdorra
 */
public interface BlogDAO extends GenericDAO<Blog>
{

  /**
   * Method description
   *
   *
   * @param servername
   *
   * @return
   */
  public Blog findByServername(String servername);

  public List<Blog> findAllActives();
}
