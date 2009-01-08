/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Category;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.config.Configuration;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class BlogCreationBean extends AbstractBean
{

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

    // TODO replace with BlogDAO.findByServername
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Blog b = null;
    Query q = em.createNamedQuery("Blog.findByServername");

    q.setParameter("servername", blog.getServername());

    try
    {
      b = (Blog) q.getSingleResult();
    }
    catch (NoResultException ex) {}

    if (b == null)
    {
      result = saveBlog(em);
    }
    else
    {
      getMessageHandler().error("blogform:servername", "nameAllreadyExists",
                                null, blog.getServername());
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
   * @param em
   *
   * @return
   */
  private String saveBlog(EntityManager em)
  {
    String result = SUCCESS;

    if (isPermitted())
    {
      em.getTransaction().begin();

      try
      {
        ResourceBundle label = getResourceBundle("label");
        // TODO replace with BlogDAO.add
        em.persist(blog);

        Category category = new Category();

        category.setName(label.getString("defaultCategory"));
        category.setBlog(blog);
        // TODO replace with CategoryDAO.add
        em.persist(category);

        User user = getRequest().getUser();
        BlogMember member = new BlogMember(blog, user, Role.ADMIN);

        // TODO replace with MemberDAO.add
        em.persist(member);
        em.getTransaction().commit();
      }
      catch (Exception ex)
      {
        if (em.getTransaction().isActive())
        {
          em.getTransaction().rollback();
        }

        logger.log(Level.SEVERE, null, ex);
        getMessageHandler().error("unknownError");
        result = FAILURE;
      }
    }
    else
    {
      getMessageHandler().error("blogCreationDisabled");
      result = FAILURE;
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
    String servername = blog.getServername();

    if (!Util.isBlank(domain))
    {
      servername += "." + domain;
    }

    blog.setServername(servername);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Blog blog;

  /** Field description */
  private String domain;
}
