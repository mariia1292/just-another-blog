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

import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public interface CommentDAO extends GenericDAO<Comment>
{

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public long count(Blog blog);

  /**
   * Method description
   *
   *
   * @param blog
   * @param type
   *
   * @return
   */
  public long count(Blog blog, Comment.Type type);

  /**
   * Method description
   *
   *
   * @param entry
   * @param type
   *
   * @return
   */
  public long count(Entry entry, Comment.Type type);

  /**
   * Method description
   *
   *
   * @param blog
   * @param b
   *
   * @return
   */
  public long count(Blog blog, boolean b);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param entry
   * @param spam
   *
   * @return
   */
  public List<Comment> getAll(Entry entry, boolean spam);

  /**
   * Method description
   *
   *
   * @param entry
   * @param spam
   * @param start
   * @param max
   *
   * @return
   */
  public List<Comment> getAll(Entry entry, boolean spam, int start, int max);

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<Comment> getAll(Blog blog);

  /**
   * Method description
   *
   *
   * @param entry
   * @param type
   * @param start
   * @param max
   *
   * @return
   */
  public List<Comment> getAll(Entry entry, Comment.Type type, int start,
                              int max);

  /**
   * Method description
   *
   *
   * @param entry
   * @param type
   *
   * @return
   */
  public List<Comment> getAll(Entry entry, Comment.Type type);

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
  public List<Comment> getAll(Blog blog, int start, int max);

  /**
   * Method description
   *
   *
   * @param blog
   * @param spam
   * @param start
   * @param max
   *
   * @return
   */
  public List<Comment> getAll(Blog blog, boolean spam, int start, int max);
}
