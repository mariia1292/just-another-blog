/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Blog;

/**
 *
 * @author sdorra
 */
public class BlogWrapper implements Comparable<BlogWrapper>
{

  /**
   * Constructs ...
   *
   *
   * @param blog
   * @param count
   */
  public BlogWrapper(Blog blog, Long count)
  {
    this.blog = blog;
    this.count = count;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param o
   *
   * @return
   */
  public int compareTo(BlogWrapper o)
  {
    int result = -1;

    if (o != null)
    {
      result = o.count.compareTo(count);

      if (result == 0)
      {
        result = blog.compareTo(o.blog);
      }
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Blog getBlog()
  {
    return blog;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Long getCount()
  {
    return count;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Blog blog;

  /** Field description */
  private Long count;
}
