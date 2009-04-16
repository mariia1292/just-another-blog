/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogRequestListener;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.authentication.LoginBean;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.entity.Blog;

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
 * @author sdorra
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
