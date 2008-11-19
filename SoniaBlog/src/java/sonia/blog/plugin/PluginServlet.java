/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.plugin;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.plugin.AbstractPluginServlet;

import sonia.plugin.ServiceReference;
import sonia.plugin.ServiceRegistry;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class PluginServlet extends HttpServlet
{

  /** Field description */
  private static final long serialVersionUID = 2293668878899710205L;

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  @Override @SuppressWarnings("unchecked")
  public void destroy()
  {
    List<AbstractPluginServlet> list = reference.getImplementations();

    for (AbstractPluginServlet servlet : list)
    {
      servlet.destroy();
    }
  }

  /**
   * Method description
   *
   *
   * @throws ServletException
   */
  @Override @SuppressWarnings("unchecked")
  public void init() throws ServletException
  {
    ServiceRegistry serviceRegistry =
      BlogContext.getInstance().getServiceRegistry();

    this.reference =
      serviceRegistry.getServiceReference(Constants.SERVICE_SERVLET);

    List<AbstractPluginServlet> list = reference.getImplementations();

    for (AbstractPluginServlet servlet : list)
    {
      servlet.init();
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override @SuppressWarnings("unchecked")
  public void service(ServletRequest request, ServletResponse response)
          throws ServletException, IOException
  {
    String contextPath = getServletContext().getContextPath();
    String requestUri = ((HttpServletRequest) request).getRequestURI();
    AbstractPluginServlet servlet = null;
    List<AbstractPluginServlet> list = reference.getImplementations();

    for (AbstractPluginServlet ps : list)
    {
      if (requestUri.matches(contextPath + "/servlet/" + ps.getMapping()))
      {
        servlet = ps;

        break;
      }
    }

    if (servlet != null)
    {
      servlet.service(request, response);
    }
    else
    {
      HttpServletResponse httpResponse = (HttpServletResponse) response;

      httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
      super.service(request, response);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference reference;
}
