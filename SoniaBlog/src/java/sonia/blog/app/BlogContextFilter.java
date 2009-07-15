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


package sonia.blog.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogRequestListener;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.entity.Blog;
import sonia.blog.wui.LoginBean;

import sonia.plugin.service.ServiceReference;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
public class BlogContextFilter implements Filter
{

  /** Field description */
  public static final String SSO_SESSION_VAR = "jab.auth.sso";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  public void destroy() {}

  /**
   * Method description
   *
   *
   * @param req
   * @param resp
   * @param chain
   *
   * @throws IOException
   * @throws ServletException
   */
  public void doFilter(ServletRequest req, ServletResponse resp,
                       FilterChain chain)
          throws IOException, ServletException
  {
    BlogRequest request = new BlogRequest((HttpServletRequest) req);
    BlogResponse response = new BlogResponse((HttpServletResponse) resp);

    for (BlogRequestListener listener : listenerReference.getAll())
    {
      listener.beforeMapping(request);
    }

    if (request.getUserPrincipal() == null)
    {
      int value = configuration.getInteger(Constants.CONFIG_SSO,
                    Constants.SSO_ONEPERSESSION);

      if ((value == Constants.SSO_ONEPERSESSION)
          && (request.getSession(true).getAttribute(SSO_SESSION_VAR) == null))
      {
        doSSOLogin(request, response);
        request.getSession().setAttribute(SSO_SESSION_VAR, Boolean.TRUE);
      }
      else if (value == Constants.SSO_EVERYREQUEST)
      {
        doSSOLogin(request, response);
      }
    }

    if (BlogContext.getInstance().isInstalled())
    {
      LinkBuilder builder = BlogContext.getInstance().getLinkBuilder();

      if (!builder.isInit())
      {
        builder.init(request);
      }

      if ((request.getSession() == null) || request.getSession().isNew())
      {
        Blog blog = request.getCurrentBlog();

        if (blog != null)
        {
          BlogContext.getDAOFactory().getBlogHitCountDAO().increase(blog);
        }
      }
    }

    if (BlogContext.getInstance().getMappingHandler().handleMapping(request,
            response))
    {
      chain.doFilter(request, response);
    }

    for (BlogRequestListener listener : listenerReference.getAll())
    {
      listener.afterMapping(request);
    }
  }

  /**
   * Method description
   *
   *
   * @param config
   *
   * @throws ServletException
   */
  public void init(FilterConfig config) throws ServletException
  {
    configuration = BlogContext.getInstance().getConfiguration();
    listenerReference = BlogContext.getInstance().getServiceRegistry().get(
      BlogRequestListener.class, Constants.SERVICE_REQUESTLISTENER);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   */
  private void doSSOLogin(BlogRequest request, BlogResponse response)
  {
    LoginBean loginBean =
      (LoginBean) request.getSession().getAttribute("LoginBean");

    if (loginBean == null)
    {
      loginBean = new LoginBean();
      request.getSession().setAttribute("LoginBean", loginBean);
    }

    loginBean.login(request, response);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private BlogConfiguration configuration;

  /** Field description */
  private ServiceReference<BlogRequestListener> listenerReference;
}