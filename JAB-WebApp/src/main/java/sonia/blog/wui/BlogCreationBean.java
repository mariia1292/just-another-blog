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



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogSession;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.dao.CategoryDAO;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.config.Configuration;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class BlogCreationBean extends AbstractBean
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(BlogCreationBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public BlogCreationBean()
  {
    init();
    this.blog = new Blog();
    this.blog.setTemplate("/template/jab");
    this.domain = BlogContext.getInstance().getConfiguration().getString(
      Constants.CONFIG_DOMAIN, "");
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String save()
  {
    setServername();

    String result = SUCCESS;
    BlogDAO blogDAO = BlogContext.getDAOFactory().getBlogDAO();
    Blog b = blogDAO.get(blog.getIdentifier());

    if (b == null)
    {
      result = saveBlog(blogDAO);
    }
    else
    {
      getMessageHandler().error(getRequest(), "blogform:servername",
                                "nameAllreadyExists", null,
                                blog.getIdentifier());
      result = FAILURE;
    }

    if (result.equals(SUCCESS))
    {
      redirect();
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
  public String getDomain()
  {
    return domain;
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
   * @param domain
   */
  public void setDomain(String domain)
  {
    this.domain = domain;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  private void redirect()
  {
    BlogRequest request = getRequest();
    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    String uri = linkBuilder.buildLink(request, blog);

    sendRedirect(uri);
  }

  /**
   * Method description
   *
   *
   *
   * @param blogDAO
   *
   * @return
   */
  private String saveBlog(BlogDAO blogDAO)
  {
    String result = FAILURE;

    if (isPermitted())
    {
      BlogSession session = BlogContext.getInstance().getSystemBlogSession();

      if (blogDAO.add(session, blog))
      {
        ResourceBundle label = getResourceBundle("label");
        Category category = new Category();

        category.setName(label.getString("defaultCategory"));
        category.setBlog(blog);

        CategoryDAO categoryDAO = BlogContext.getDAOFactory().getCategoryDAO();

        if (categoryDAO.add(session, category))
        {
          User user = getRequest().getUser();
          UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();

          try
          {
            userDAO.setRole(session, blog, user, Role.ADMIN);
            result = SUCCESS;
          }
          catch ( /* TODO replace with DAOException */Exception ex)
          {
            logger.log(Level.SEVERE, null, ex);
            getMessageHandler().error(getRequest(), "couldNotCreateMember");
          }
        }
        else
        {
          getMessageHandler().error(getRequest(), "couldNotCreateCategory");
        }
      }
      else
      {
        getMessageHandler().error(getRequest(), "couldNotCreateBlog");
      }
    }
    else
    {
      getMessageHandler().error(getRequest(), "blogCreationDisabled");
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
  private boolean isPermitted()
  {
    Configuration config = BlogContext.getInstance().getConfiguration();

    return config.getBoolean(Constants.CONFIG_ALLOW_BLOGCREATION,
                             Boolean.FALSE);
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   */
  private void setServername()
  {
    String servername = blog.getIdentifier();

    if (!Util.isBlank(domain))
    {
      servername += "." + domain;
    }

    blog.setIdentifier(servername);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Blog blog;

  /** Field description */
  private String domain;
}
