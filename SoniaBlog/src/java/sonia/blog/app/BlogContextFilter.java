/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;

//~--- JDK imports ------------------------------------------------------------

import java.io.*;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sdorra
 */
public class BlogContextFilter implements Filter
{

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

    chain.doFilter(request, resp);
  }

  /**
   * Method description
   *
   *
   * @param config
   *
   * @throws ServletException
   */
  public void init(FilterConfig config) throws ServletException {}
}
