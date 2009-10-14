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



package sonia.blog.api.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.mapping.Mapping;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.security.Principal;

import java.util.Enumeration;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Sebastian Sdorra
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

    if (logger.isLoggable(Level.FINEST))
    {
      startTime = System.currentTimeMillis();
    }
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  public void finish()
  {
    if (logger.isLoggable(Level.FINEST))
    {
      StringBuffer msg = new StringBuffer();

      msg.append("request at ").append(getRequestURI());
      msg.append(" finished in ");
      msg.append(System.currentTimeMillis() - startTime).append("ms");
      logger.finest(msg.toString());
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public BlogSession getBlogSession()
  {
    return (BlogSession) getSession(true).getAttribute(BlogSession.SESSIONVAR);
  }

  /**
   * Method description
   *
   * @return
   */
  public Blog getCurrentBlog()
  {
    if ((blog == null) && BlogContext.getInstance().isInstalled())
    {
      String servername = getServerName();
      BlogDAO blogDAO = BlogContext.getDAOFactory().getBlogDAO();

      blog = blogDAO.get(servername, true);

      if (blog == null)
      {
        if (logger.isLoggable(Level.INFO))
        {
          logger.info("blog " + servername + " not found, using default blog");
        }

        blog = BlogContext.getInstance().getDefaultBlog();
      }
    }

    return blog;
  }

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws IOException
   */
  @Override
  public ServletInputStream getInputStream() throws IOException
  {
    charsetIsSetable = false;

    return super.getInputStream();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Mapping getMapping()
  {
    return mapping;
  }

  /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   */
  @Override
  public String getParameter(String name)
  {
    charsetIsSetable = false;

    return super.getParameter(name);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public Map getParameterMap()
  {
    charsetIsSetable = false;

    return super.getParameterMap();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public Enumeration getParameterNames()
  {
    charsetIsSetable = false;

    return super.getParameterNames();
  }

  /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   */
  @Override
  public String[] getParameterValues(String name)
  {
    charsetIsSetable = false;

    return super.getParameterValues(name);
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
   *
   * @throws IOException
   */
  @Override
  public BufferedReader getReader() throws IOException
  {
    charsetIsSetable = false;

    return super.getReader();
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
  public long getStartTime()
  {
    return startTime;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public User getUser()
  {
    BlogSession bs = getBlogSession();

    return (bs != null)
           ? bs.getUser()
           : null;
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
   * @return
   */
  public String getViewId()
  {
    return viewId;
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
    boolean result = false;
    BlogSession session = getBlogSession();

    if (session != null)
    {
      result = session.hasRole(role);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   *
   * @param roleName
   *
   * @return
   */
  @Override
  public boolean isUserInRole(String roleName)
  {
    boolean result = false;
    Role role = Role.valueOf(roleName);

    if (role != null)
    {
      result = isUserInRole(role);
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
    if (charsetIsSetable)
    {
      super.setCharacterEncoding(enc);
    }
  }

  /**
   * Method description
   *
   *
   * @param mapping
   */
  public void setMapping(Mapping mapping)
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
  private boolean charsetIsSetable = true;

  /** Field description */
  private Mapping mapping;

  /** Field description */
  private String redirect;

  /** Field description */
  private long startTime = -1l;

  /** Field description */
  private String viewId;
}
