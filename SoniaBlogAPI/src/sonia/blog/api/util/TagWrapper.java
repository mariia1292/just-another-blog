/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Tag;

//~--- JDK imports ------------------------------------------------------------

import java.util.Comparator;

/**
 *
 * @author sdorra
 */
public class TagWrapper implements Comparable<TagWrapper>
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

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param o
   *
   * @return
   */
  public int compareTo(TagWrapper o)
  {
    int result = -1;

    if (o != null)
    {
      result = o.count.compareTo(count);

      if (result == 0)
      {
        result = tag.getName().compareTo(o.tag.getName());
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
