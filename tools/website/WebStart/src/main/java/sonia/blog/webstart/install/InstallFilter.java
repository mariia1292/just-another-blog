/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webstart.install;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import sonia.blog.webstart.JnlpContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class InstallFilter implements Filter
{

  /**
   * Constructs ...
   *
   */
  public InstallFilter()
  {
    context = JnlpContext.getInstance();
  }

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
   * @param request
   * @param response
   * @param chain
   *
   * @throws IOException
   * @throws ServletException
   */
  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain)
          throws IOException, ServletException
  {
    if (!context.isInstalled())
    {
      chain.doFilter(request, response);
    }
    else
    {
      throw new ServletException("is allready installed");
    }
  }

  /**
   * Method description
   *
   *
   * @param filterConfig
   *
   * @throws ServletException
   */
  public void init(FilterConfig filterConfig) throws ServletException {}

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private JnlpContext context;
}
