/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class DefaultVariableResolver implements VariableResolver
{

  /** Field description */
  public static final String PROVIDER_CONFIG = "cnf";

  /** Field description */
  public static final String PROVIDER_ENVIRONMENT = "env";

  /** Field description */
  public static final String PROVIDER_SYSTEM = "sys";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultVariableResolver.class.getName());

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
  public String resolveVariable(Configuration configuration, String provider,
                                String variable)
  {
    String result = "";

    if (PROVIDER_SYSTEM.equals(provider))
    {
      result = System.getProperty(variable);
    }
    else if (PROVIDER_ENVIRONMENT.equals(provider))
    {
      result = System.getenv( variable );
    }
    else if (PROVIDER_CONFIG.equals(provider))
    {
      result = configuration.getString( variable, "" );
    }

    if (logger.isLoggable(Level.FINER))
    {
      StringBuffer log = new StringBuffer();

      log.append("resolve ").append(provider).append(".").append(variable);
      log.append( " to " ).append( result );
    }

    return result;
  }
}
