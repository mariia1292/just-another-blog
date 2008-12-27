/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Tag;

/**
 *
 * @author sdorra
 */
public class TagWrapper
{

  /**
   * Constructs ...
   *
   *
   * @param tag
   * @param count
   */
  public TagWrapper(Tag tag, Long count)
  {
    this.tag = tag;
    this.count = count;
  }

  //~--- get methods ----------------------------------------------------------

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

  /**
   * Method description
   *
   *
   * @return
   */
  public Tag getTag()
  {
    return tag;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Long count;

  /** Field description */
  private Tag tag;
}
