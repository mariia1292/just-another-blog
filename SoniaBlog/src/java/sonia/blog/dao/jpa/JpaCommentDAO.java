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



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.CommentDAO;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Comment.Type;
import sonia.blog.entity.Entry;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Sebastian Sdorra
 */
public class JpaCommentDAO extends JpaGenericDAO<Comment> implements CommentDAO
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(JpaCommentDAO.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   *
   * @param strategy
   */
  public JpaCommentDAO(JpaStrategy strategy)
  {
    super(strategy, Comment.class, Constants.LISTENER_COMMENT);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public long count()
  {
    return countQuery("Comment.count");
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public long count(Blog blog)
  {
    return countQuery("Comment.countByBlog", blog);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param type
   *
   * @return
   */
  public long count(Blog blog, Type type)
  {
    Query q = strategy.getNamedQuery("Comment.countByBlogAndType", false);

    q.setParameter("blog", blog);
    q.setParameter("type", type);

    return (Long) q.getSingleResult();
  }

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
  public List<Comment> getAll(Entry entry, boolean spam)
  {
    return getAll(entry, spam, -1, -1);
  }

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
  @SuppressWarnings("unchecked")
  public List<Comment> getAll(Entry entry, boolean spam, int start, int max)
  {
    List<Comment> comments = null;
    Query q = strategy.getNamedQuery("Comment.getAllByEntryAndSpam", false);

    q.setParameter("entry", entry);
    q.setParameter("spam", spam);

    if (start > 0)
    {
      q.setFirstResult(start);
    }

    if (max > 0)
    {
      q.setMaxResults(max);
    }

    try
    {
      comments = q.getResultList();
    }
    catch (NoResultException ex) {}

    return comments;
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<Comment> getAll(Blog blog)
  {
    return findList("Comment.getAllByBlog", blog);
  }

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
  public List<Comment> getAll(Blog blog, int start, int max)
  {
    return findList("Comment.getAllByBlog", blog, start, max);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Comment> getAll()
  {
    return findList("Comment.getAll");
  }

  /**
   * Method description
   *
   *
   * @param start
   * @param max
   *
   * @return
   */
  public List<Comment> getAll(int start, int max)
  {
    return findList("Comment.getAll", start, max);
  }

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
  @SuppressWarnings("unchecked")
  public List<Comment> getAll(Entry entry, Type type, int start, int max)
  {
    Query q = strategy.getNamedQuery("Comment.getAllByEntryAndType", false);

    q.setParameter("entry", entry);
    q.setParameter("type", type);

    if (start > 0)
    {
      q.setFirstResult(start);
    }

    if (max > 0)
    {
      q.setMaxResults(max);
    }

    List<Comment> result = null;

    try
    {
      result = q.getResultList();
    }
    catch (NoResultException ex) {}

    return result;
  }

  /**
   * Method description
   *
   *
   * @param entry
   * @param type
   *
   * @return
   */
  public List<Comment> getAll(Entry entry, Type type)
  {
    return getAll(entry, type, -1, -1);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected Logger getLogger()
  {
    return logger;
  }

  public long count(Entry entry, Type type)
  {
        Query q = strategy.getNamedQuery("Comment.countByEntryAndType", false);

    q.setParameter("entry", entry);
    q.setParameter("type", type);

    return (Long) q.getSingleResult();
  }
}
