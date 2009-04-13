/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

//~--- JDK imports ------------------------------------------------------------

import javax.servlet.ServletContext;

/**
 *
 * @author sdorra
 */
public class WebVariableResolver extends DefaultVariableResolver
{

  /** Field description */
  public static final String PROVIDER_WEB = "web";

  /** Field description */
  public static final String VARIABLE_CONTEXTPATH = "contextpath";

  /** Field description */
  public static final String VARIABLE_REALPATH = "realpath";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param servletContext
   */
  public WebVariableResolver(ServletContext servletContext)
  {
    this.servletContext = servletContext;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param configuration
   * @param provider
   * @param variable
   *
   * @return
   */
  @Override
  public String resolveVariable(Configuration configuration, String provider,
                                String variable)
  {
    String result = "";

    if (PROVIDER_WEB.equals(provider))
    {
      if (VARIABLE_REALPATH.equals(variable))
      {
        result = servletContext.getRealPath("/");
      }
      else if (VARIABLE_CONTEXTPATH.equals(variable))
      {
        result = servletContext.getContextPath();
      }
      else
      {
        result = servletContext.getInitParameter(variable);
      }
    }
    else
    {
      result = super.resolveVariable(configuration, provider, variable);
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServletContext servletContext;
}
