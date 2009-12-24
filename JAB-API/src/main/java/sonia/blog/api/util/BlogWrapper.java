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



package sonia.blog.api.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Blog;

/**
 *
 * @author Sebastian Sdorra
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

    final BlogWrapper other = (BlogWrapper) obj;

    if ((this.blog != other.blog)
        && ((this.blog == null) ||!this.blog.equals(other.blog)))
    {
      return false;
    }

    if ((this.count != other.count)
        && ((this.count == null) ||!this.count.equals(other.count)))
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
    int hash = 3;

    hash = 29 * hash + ((this.blog != null)
                        ? this.blog.hashCode()
                        : 0);
    hash = 29 * hash + ((this.count != null)
                        ? this.count.hashCode()
                        : 0);

    return hash;
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
