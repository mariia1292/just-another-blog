/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://kenai.com/projects/jab
 *
 */



package sonia.blog.entity;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;

/**
 *
 * @author Sebastian Sdorra
 */
public class BlogHitCount implements PermaObject
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
