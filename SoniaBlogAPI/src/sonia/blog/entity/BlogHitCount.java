/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.entity;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;

/**
 *
 * @author sdorra
 */
public class BlogHitCount
{

  /**
   * Constructs ...
   *
   */
  public BlogHitCount() {}

  /**
   * Constructs ...
   *
   *
   * @param blog
   * @param date
   */
  public BlogHitCount(Blog blog, Date date)
  {
    this.blog = blog;
    this.date = date;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param obj
   *
   * @return
   */
  @Override
  public boolean equals(Object obj)
  {
    if (obj == null)
    {
      return false;
    }

    if (getClass() != obj.getClass())
    {
      return false;
    }

    final BlogHitCount other = (BlogHitCount) obj;

    if ((this.id != other.id)
        && ((this.id == null) ||!this.id.equals(other.id)))
    {
      return false;
    }

    if ((this.date != other.date)
        && ((this.date == null) ||!this.date.equals(other.date)))
    {
      return false;
    }

    if ((this.blog != other.blog)
        && ((this.blog == null) ||!this.blog.equals(other.blog)))
    {
      return false;
    }

    if (this.hitCount != other.hitCount)
    {
      return false;
    }

    return true;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public int hashCode()
  {
    int hash = 7;

    hash = 89 * hash + ((this.id != null)
                        ? this.id.hashCode()
                        : 0);
    hash = 89 * hash + ((this.date != null)
                        ? this.date.hashCode()
                        : 0);
    hash = 89 * hash + ((this.blog != null)
                        ? this.blog.hashCode()
                        : 0);
    hash = 89 * hash + (int) (this.hitCount ^ (this.hitCount >>> 32));

    return hash;
  }

  /**
   * Method description
   *
   */
  public void inc()
  {
    this.hitCount++;
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
  public Date getDate()
  {
    return date;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getHitCount()
  {
    return hitCount;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Long getId()
  {
    return id;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blog
   */
  public void setBlog(Blog blog)
  {
    this.blog = blog;
  }

  /**
   * Method description
   *
   *
   * @param date
   */
  public void setDate(Date date)
  {
    this.date = date;
  }

  /**
   * Method description
   *
   *
   * @param hitCount
   */
  public void setHitCount(long hitCount)
  {
    this.hitCount = hitCount;
  }

  /**
   * Method description
   *
   *
   * @param id
   */
  public void setId(Long id)
  {
    this.id = id;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Blog blog;

  /** Field description */
  private Date date;

  /** Field description */
  private long hitCount = 0;

  /** Field description */
  private Long id;
}
