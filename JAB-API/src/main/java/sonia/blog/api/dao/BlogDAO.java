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

import sonia.blog.api.app.BlogSession;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Map;

/**
 *
 * @author Sebastian Sdorra
 */
public interface BlogDAO extends GenericDAO<Blog>
{

  /**
   * Method description
   *
   *
   * @param identifier
   * @param active
   *
   * @return
   */
  public Blog get(String identifier, boolean active);

  /**
   * Method description
   *
   *
   * @param identifier
   *
   * @return
   */
  public Blog get(String identifier);

  /**
   * Method description
   *
   *
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  public List<Blog> getAll(boolean active, int start, int max);

  /**
   * Method description
   *
   *
   *
   * @param filter
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  public List<Blog> getAll(String filter, boolean active, int start, int max);

  /**
   * Method description
   *
   *
   *
   * @param filter
   * @param start
   * @param max
   *
   * @return
   */
  public List<Blog> getAll(String filter, int start, int max);

  /**
   * Method description
   *
   *
   * @param blog
   * @param active
   * @param commentNotify
   *
   * @return
   */
  public List<BlogMember> getCommentNotifyMembers(Blog blog, boolean active,
          boolean commentNotify);

  /**
   * Method description
   *
   *
   * @param blog
   * @param active
   * @param entryNotify
   *
   * @return
   */
  public List<BlogMember> getEntryNotifyMembers(Blog blog, boolean active,
          boolean entryNotify);

  /**
   * Method description
   *
   *
   * @param blog
   * @param start
   * @param max
   *
   * @return
   */
  public List<BlogMember> getMembers(Blog blog, int start, int max);

  /**
   * Method description
   *
   *
   * @param blog
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  public List<BlogMember> getMembers(Blog blog, boolean active, int start,
                                     int max);

  /**
   * Method description
   *
   *
   * @param blog
   * @param name
   *
   * @return
   */
  public String getParameter(Blog blog, String name);

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public Map<String, String> getParameters(Blog blog);

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param session
   * @param blog
   * @param name
   * @param value
   */
  public void setParameter(BlogSession session, Blog blog, String name,
                           String value);

  /**
   * Method description
   *
   *
   * @param session
   * @param blog
   * @param name
   * @param value
   * @param notifyListener
   */
  public void setParameter(BlogSession session, Blog blog, String name,
                           String value, boolean notifyListener);
}
