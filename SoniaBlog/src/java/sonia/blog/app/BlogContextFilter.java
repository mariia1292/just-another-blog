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
import sonia.blog.api.app.ResponseCacheObject;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingHandler;
import sonia.blog.api.mapping.MappingInstructions;
import sonia.blog.entity.Blog;
import sonia.blog.wui.LoginBean;

import sonia.cache.ObjectCache;

import sonia.plugin.service.ServiceReference;

import sonia.util.Util;

import sonia.web.util.WebUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.net.URLConnection;

import java.util.logging.Level;
import java.util.logging.Logger;

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
  public static final String PARAM_DONTCACHE = "__dontCache";

  /** Field description */
  public static final String SSO_SESSION_VAR = "jab.auth.sso";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(BlogContextFilter.class.getName());

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

    try
    {
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

      handleRequest(request, response, chain);

      for (BlogRequestListener listener : listenerReference.getAll())
      {
        listener.afterMapping(request);
      }
    }
    finally
    {
      request.finish();
      response.finish();
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
    BlogContext blogCtx = BlogContext.getInstance();

    configuration = blogCtx.getConfiguration();
    listenerReference =
      blogCtx.getServiceRegistry().get(BlogRequestListener.class,
                                       Constants.SERVICE_REQUESTLISTENER);
    cache = blogCtx.getCacheManager().get(Constants.CACHE_MAPPING);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param compress
   * @param cacheKeys
   *
   * @return
   */
  private String createCacheKey(BlogRequest request, boolean compress,
                                String[] cacheKeys)
  {
    StringBuffer result = new StringBuffer();

    result.append(request.getCurrentBlog().getId()).append(":");
    result.append(request.getRequestURI()).append(":");
    result.append(request.getQueryString()).append(":");
    result.append(Boolean.toString(compress)).append(":");
    result.append(request.getRedirect());

    if (cacheKeys != null)
    {
      for (String key : cacheKeys)
      {
        if (key.equalsIgnoreCase("user"))
        {
          result.append(":user=").append((request.getUser() != null)
                                         ? request.getUser().getId()
                                         : -1);
        }
        else if (key.equalsIgnoreCase("ip"))
        {
          result.append(":ip=").append(request.getRemoteAddr());
        }
        else if (key.equalsIgnoreCase("locale"))
        {
          result.append(":locale=").append(request.getLocale().toString());
        }
      }
    }

    return result.toString();
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

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param chain
   *
   * @throws IOException
   * @throws ServletException
   */
  private void handleRequest(BlogRequest request, BlogResponse response,
                             FilterChain chain)
          throws IOException, ServletException
  {
    MappingHandler mappingHandler =
      BlogContext.getInstance().getMappingHandler();
    MappingInstructions instructions =
      mappingHandler.getMappingInstructions(request);
    String cacheKey = null;
    boolean process = true;
    boolean compress = isCompressAble(request, instructions);

    if ((cache != null) && (instructions != null) && instructions.isCacheable()
        && (request.getParameter(PARAM_DONTCACHE) == null)
        && BlogContext.getInstance().isInstalled())
    {
      cacheKey = createCacheKey(request, compress, instructions.getCacheKeys());

      ResponseCacheObject cacheObject =
        (ResponseCacheObject) cache.get(cacheKey);

      if (cacheObject != null)
      {
        if (logger.isLoggable(Level.FINE))
        {
          StringBuffer msg = new StringBuffer();

          msg.append("process page ").append(request.getRequestURI());
          msg.append(" from cache");
          logger.fine(msg.toString());
        }

        response.setHeader("x-jab-cache", "true");
        cacheObject.apply(response);
        process = false;
      }
      else
      {
        response.setCacheEnabled(true);
      }
    }

    if (process)
    {
      response.setCompressionEnabled(compress);

      if ((instructions == null)
          || mappingHandler.handleMapping(request, response, instructions))
      {
        chain.doFilter(request, response);
      }
    }

    if ((cache != null) && (cacheKey != null) && response.isCacheEnabled()
        && (response.getStatusCode() < 300))
    {
      ResponseCacheObject cacheObject = response.getCachedObject();

      if (cacheObject != null)
      {
        cache.put(cacheKey, cacheObject);
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param instructions
   *
   * @return
   */
  private boolean isCompressAble(BlogRequest request,
                                 MappingInstructions instructions)
  {
    boolean result = false;

    if (WebUtil.isGzipSupported(request))
    {
      if (instructions != null)
      {
        result = instructions.isCompressable();
      }
      else
      {
        String path = request.getRequestURI();

        if (Util.hasContent(path))
        {
          String mime = URLConnection.getFileNameMap().getContentTypeFor(path);

          if (Util.hasContent(mime))
          {
            result = mime.startsWith("text")
                     || "application/x-javascript".equals(mime);
          }
        }
      }
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ObjectCache cache;

  /** Field description */
  private BlogConfiguration configuration;

  /** Field description */
  private ServiceReference<BlogRequestListener> listenerReference;
}
