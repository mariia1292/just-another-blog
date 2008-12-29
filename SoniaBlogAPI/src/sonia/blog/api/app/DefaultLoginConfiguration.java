/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.plugin.ServiceReference;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

/**
 *
 * @author sdorra
 */
public class DefaultLoginConfiguration extends Configuration
{

  /**
   * Constructs ...
   *
   */
  public DefaultLoginConfiguration( String serviceName )
  {
    reference =
      BlogContext.getInstance().getServiceRegistry().getServiceReference(
        serviceName);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  @Override
  public void refresh()
  {

    // do nothing
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param moduleName
   *
   * @return
   */
  @Override @SuppressWarnings("unchecked")
  public AppConfigurationEntry[] getAppConfigurationEntry(String moduleName)
  {
    List<AppConfigurationEntry> entries = reference.getImplementations();

    return entries.toArray(new AppConfigurationEntry[0]);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference reference;
}
