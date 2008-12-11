/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.authentication.LoginBean;
import sonia.blog.api.mapping.MappingEntry;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

//~--- JDK imports ------------------------------------------------------------

import java.io.UnsupportedEncodingException;

import java.security.Principal;

import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 *
 * @author sdorra
 */
public class BlogRequest extends HttpServletRequestWrapper
{

  /** Field description */
  private static final String REDIRECT = "sonia.blog.redirect";

  /** Field description */
  private static Logger logger = Logger.getLogger(BlogRequest.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param request
   */
  public BlogRequest(HttpServletRequest request)
  {
    super(request);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Blog getCurrentBlog()
  {
    if (blog == null)
    {
      String servername = getServerName();
      EntityManager em = BlogContext.getInstance().getEntityManager();
      Query q = em.createNamedQuery("Blog.findByServername");

      q.setParameter("servername", servername);

      try
      {
        blog = (Blog) q.getSingleResult();
      }
      catch (NoResultException ex)
      {
        logger.info("blog " + servername + " not found, using default Blog");
        blog = BlogContext.getInstance().getDefaultBlog();
      }

      em.close();
    }

    return blog;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public LoginBean getLoginBean()
  {
    LoginBean loginBean = null;
    HttpSession session = getSession();

    if (session != null)
    {
      loginBean = (LoginBean) session.getAttribute("LoginBean");
    }

    return loginBean;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public MappingEntry getMapping()
  {
    return mapping;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String getPathInfo()
  {
    String pathInfo = null;

    if (viewId != null)
    {
      pathInfo = viewId;
    }
    else
    {
      pathInfo = super.getPathInfo();
    }

    return pathInfo;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getRedirect()
  {
    if (redirect != null)
    {
      HttpSession session = getSession();

      if (session != null)
      {
        redirect = (String) session.getAttribute(REDIRECT);
        session.removeAttribute(REDIRECT);
      }
    }

    return redirect;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String getRemoteUser()
  {
    String username = null;
    Principal principal = getUserPrincipal();

    if (principal != null)
    {
      username = principal.getName();
    }

    return username;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String getServletPath()
  {
    String servletPath = super.getServletPath();

    if ((viewId != null) && servletPath.contains(viewId))
    {
      servletPath = servletPath.substring(0, servletPath.indexOf(viewId));
    }

    return servletPath;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public User getUser()
  {
    User user = null;
    LoginBean loginBean = getLoginBean();

    if ((loginBean != null) && loginBean.isAuthenticated())
    {
      Set<User> userSet =
        loginBean.getLoginContext().getSubject().getPrincipals(User.class);

      if ((userSet != null) &&!userSet.isEmpty())
      {
        user = userSet.iterator().next();
      }
    }

    return user;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public Principal getUserPrincipal()
  {
    return getUser();
  }

  /**
   * Method description
   *
   *
   * @param role
   *
   * @return
   */
  public boolean isUserInRole(Role role)
  {
    return isUserInRole(role.name());
  }

  /**
   * Method description
   *
   *
   * @param role
   *
   * @return
   */
  @Override
  public boolean isUserInRole(String role)
  {
    boolean result = false;
    User user = (User) getUserPrincipal();

    if (user != null)
    {
      if (!user.isGlobalAdmin())
      {
        if (!searchForMember)
        {
          EntityManager em = BlogContext.getInstance().getEntityManager();
          Query q = em.createNamedQuery("BlogMember.findByBlogAndUser");

          q.setParameter("user", user);
          q.setParameter("blog", getCurrentBlog());

          try
          {
            member = (BlogMember) q.getSingleResult();
          }
          catch (NoResultException ex) {}

          searchForMember = true;
        }

        if (member != null)
        {
          result = role.equalsIgnoreCase(member.getRole().name());
        }
      }
      else
      {
        result = true;
        searchForMember = true;
      }
    }

    return result;
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
   * @param enc
   *
   * @throws UnsupportedEncodingException
   */
  @Override
  public void setCharacterEncoding(String enc)
          throws UnsupportedEncodingException
  {

    // do nothing
  }

  /**
   * Method description
   *
   *
   * @param mapping
   */
  public void setMapping(MappingEntry mapping)
  {
    this.mapping = mapping;
  }

  /**
   * Method description
   *
   *
   * @param redirect
   */
  public void setRedirect(String redirect)
  {
    this.getSession(true).setAttribute(REDIRECT, redirect);
  }

  /**
   * Method description
   *
   *
   * @param viewId
   */
  public void setViewId(String viewId)
  {
    this.viewId = viewId;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Blog blog;

  /** Field description */
  private MappingEntry mapping;

  /** Field description */
  private BlogMember member;

  /** Field description */
  private String redirect;

  /** Field description */
  private boolean searchForMember = false;

  /** Field description */
  private String viewId;
}
