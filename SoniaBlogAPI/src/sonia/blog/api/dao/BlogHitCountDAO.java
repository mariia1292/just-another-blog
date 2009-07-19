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



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.util.BlogWrapper;
import sonia.blog.api.util.HitWrapper;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogHitCount;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public interface BlogHitCountDAO extends GenericDAO<BlogHitCount>
{

  /**
   * Method description
   *
   *
   * @param blog
   * @param date
   *
   * @return
   */
  public BlogHitCount findByBlogAndDate(Blog blog, Date date);

  /**
   * Method description
   *
   *
   * @param month
   * @param year
   *
   * @return
   */
  public List<BlogWrapper> findByMonth(int month, int year);

  /**
   * Method description
   *
   *
   * @param year
   *
   * @return
   */
  public List<BlogWrapper> findByYear(int year);

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public boolean increase(Blog blog);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blog
   * @param month
   * @param year
   *
   * @return
   */
  public Long getHitsByBlogAndMonth(Blog blog, int month, int year);

  /**
   * Method description
   *
   *
   * @param blog
   * @param year
   *
   * @return
   */
  public Long getHitsByBlogAndYear(Blog blog, int year);

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<HitWrapper> getHitsPerMonthByBlog(Blog blog);
}
