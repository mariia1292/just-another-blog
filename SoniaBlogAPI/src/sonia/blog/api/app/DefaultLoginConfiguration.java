/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.plugin.service.ServiceReference;

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
   *
   * @param serviceName
   */
  public DefaultLoginConfiguration(String serviceName)
  {
    reference = BlogContext.getInstance().getServiceRegistry().get(
      AppConfigurationEntry.class, serviceName);
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
  public AppConfigurationEntry[] getAppConfigurationEntry(String moduleName)
  {
    List<AppConfigurationEntry> entries = reference.getAll();

    return entries.toArray(new AppConfigurationEntry[0]);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference<AppConfigurationEntry> reference;
}
