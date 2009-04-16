/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
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
 * @author sdorra
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
    super();
    this.blog = new Blog();
    this.blog.setTemplate("jab");
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
      getMessageHandler().error("blogform:servername", "nameAllreadyExists",
                                null, blog.getIdentifier());
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
      if (blogDAO.add(blog))
      {
        ResourceBundle label = getResourceBundle("label");
        Category category = new Category();

        category.setName(label.getString("defaultCategory"));
        category.setBlog(blog);

        CategoryDAO categoryDAO = BlogContext.getDAOFactory().getCategoryDAO();

        if (categoryDAO.add(category))
        {
          User user = getRequest().getUser();
          UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();

          try
          {
            userDAO.setRole(blog, user, Role.ADMIN);
            result = SUCCESS;
          }
          catch ( /* TODO replace with DAOException */Exception ex)
          {
            logger.log(Level.SEVERE, null, ex);
            getMessageHandler().error("couldNotCreateMember");
          }
        }
        else
        {
          getMessageHandler().error("couldNotCreateCategory");
        }
      }
      else
      {
        getMessageHandler().error("couldNotCreateBlog");
      }
    }
    else
    {
      getMessageHandler().error("blogCreationDisabled");
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
