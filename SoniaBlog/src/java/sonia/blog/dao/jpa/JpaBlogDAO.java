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

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogSession;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.dao.DAOListener.Action;
import sonia.blog.api.exception.BlogSecurityException;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogHitCount;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.BlogParameter;
import sonia.blog.entity.Role;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Sebastian Sdorra
 */
public class JpaBlogDAO extends JpaGenericDAO<Blog> implements BlogDAO
{

  /** Field description */
  private static Logger logger = Logger.getLogger(JpaBlogDAO.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   *
   * @param strategy
   */
  public JpaBlogDAO(JpaStrategy strategy)
  {
    super(strategy, Blog.class, Constants.LISTENER_BLOG);
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
    return countQuery("Blog.count");
  }

  /**
   * Method description
   *
   *
   *
   * @param session
   * @param blog
   *
   * @return
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean remove(BlogSession session, Blog blog)
  {
    if (!isPrivileged(session, blog, ACTION_REMOVE))
    {
      logUnprivilegedMessage(session, blog, ACTION_REMOVE);

      throw new BlogSecurityException("Admin Session is required");
    }

    fireEvent(Action.PREREMOVE, blog);

    boolean result = false;

    try
    {
      Query q = strategy.getNamedQuery("BlogHitCount.findByBlog", false);

      q.setParameter("blog", blog);

      List<BlogHitCount> hits = q.getResultList();

      for (BlogHitCount hit : hits)
      {
        strategy.remove(hit);
      }

      strategy.remove(blog);
      strategy.flush();
      fireEvent(Action.POSTREMOVE, blog);

      ResourceManager resManager =
        BlogContext.getInstance().getResourceManager();
      File indexDir = resManager.getDirectory(Constants.RESOURCE_INDEX, blog,
                        false);

      if (indexDir.exists())
      {
        logger.fine("removing directory " + indexDir.getPath());
        Util.delete(indexDir);
      }

      File attachmentDir =
        resManager.getDirectory(Constants.RESOURCE_ATTACHMENT, blog, false);

      if (attachmentDir.exists())
      {
        logger.fine("removing directory " + attachmentDir.getPath());
        Util.delete(attachmentDir);
      }

      result = true;
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param identifier
   * @param active
   *
   * @return
   */
  public Blog get(String identifier, boolean active)
  {
    Query q = strategy.getNamedQuery("Blog.getByIdentifierAndActive", false);

    q.setParameter("identifier", identifier);
    q.setParameter("active", active);

    return excecuteQuery(q);
  }

  /**
   * Method description
   *
   *
   * @param identifier
   *
   * @return
   */
  public Blog get(String identifier)
  {
    Query q = strategy.getNamedQuery("Blog.getByIdentifier", false);

    q.setParameter("identifier", identifier);

    return excecuteQuery(q);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Blog> getAll()
  {
    return findList("Blog.findAll");
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
  public List<Blog> getAll(int start, int max)
  {
    return findList("Blog.findAll", start, max);
  }

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
  public List<Blog> getAll(boolean active, int start, int max)
  {
    Query q = strategy.getNamedQuery("Blog.getByActive", false);

    q.setParameter("active", active);
    q.setFirstResult(start);
    q.setMaxResults(max);

    return excecuteListQuery(q);
  }

  /**
   * Method description
   *
   *
   * @param filter
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  public List<Blog> getAll(String filter, boolean active, int start, int max)
  {
    Query q = strategy.getNamedQuery("Blog.getAllByFilterAndActive",
                                     false);

    q.setParameter("filter", createFilter(filter));
    q.setParameter("active", active);
    q.setFirstResult(start);
    q.setMaxResults(max);

    return excecuteListQuery(q);
  }

  /**
   * Method description
   *
   *
   * @param filter
   * @param start
   * @param max
   *
   * @return
   */
  public List<Blog> getAll(String filter, int start, int max)
  {
    Query q = strategy.getNamedQuery("Blog.getAllByFilter", false);

    q.setParameter("filter", createFilter(filter));
    q.setFirstResult(start);
    q.setMaxResults(max);

    return excecuteListQuery(q);
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
  public List<BlogMember> getMembers(Blog blog, int start, int max)
  {
    Query q = strategy.getNamedQuery("BlogMember.getAllByBlog", false);

    q.setParameter("blog", blog);
    q.setFirstResult(start);
    q.setMaxResults(max);

    return excecuteListQuery(BlogMember.class, q);
  }

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
                                     int max)
  {
    Query q = strategy.getNamedQuery("BlogMember.getAllByBalogAndActive",
                                     false);

    q.setParameter("blog", blog);
    q.setParameter("active", active);
    q.setFirstResult(start);
    q.setMaxResults(max);

    return excecuteListQuery(BlogMember.class, q);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param active
   * @param notify
   *
   * @return
   */
  public List<BlogMember> getMembers(Blog blog, boolean active, boolean notify)
  {
    Query q = strategy.getNamedQuery("BlogMember.getAllByBlogActiveAndNotify",
                                     false);

    q.setParameter("blog", blog);
    q.setParameter("active", active);
    q.setParameter("notify", notify);

    return excecuteListQuery(BlogMember.class, q);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param name
   *
   * @return
   */
  public String getParameter(Blog blog, String name)
  {
    Query q = strategy.getNamedQuery("BlogParameter.getValueByBlogAndName",
                                     false);

    q.setParameter("blog", blog);
    q.setParameter("name", name);

    return excecuteQuery(String.class, q);
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public Map<String, String> getParameters(Blog blog)
  {
    Query q = strategy.getNamedQuery("BlogParameter.getAllByBlog", false);

    q.setParameter("blog", blog);

    Map<String, String> paramMap = new HashMap<String, String>();
    List<BlogParameter> parameters = excecuteListQuery(BlogParameter.class, q);

    if (Util.hasContent(parameters))
    {
      for (BlogParameter param : parameters)
      {
        paramMap.put(param.getName(), param.getValue());
      }
    }

    return paramMap;
  }

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
                           String value)
  {
    if (!(session.hasRole(Role.GLOBALADMIN)
          || (session.hasRole(Role.ADMIN) && session.getBlog().equals(blog))))
    {
      throw new BlogSecurityException("AdminSession is required");
    }

    Query q = strategy.getNamedQuery("BlogParameter.getByBlogAndName", false);

    q.setParameter("blog", blog);
    q.setParameter("name", name);

    BlogParameter param = null;

    try
    {
      param = (BlogParameter) q.getSingleResult();
    }
    catch (NoResultException ex) {}

    try
    {
      if (param != null)
      {
        param.setValue(value);
        strategy.edit(param);
      }
      else
      {
        param = new BlogParameter(blog, name, value);
        strategy.store(param);
      }

      strategy.flush();
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  //~--- get methods ----------------------------------------------------------

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
}
