/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.ldap;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;

import sonia.config.ConfigurationListener;
import sonia.config.ModifyableConfiguration;
import sonia.config.XmlConfiguration;

import sonia.plugin.Activator;
import sonia.plugin.PluginContext;
import sonia.plugin.ServiceReference;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;

import javax.security.auth.login.AppConfigurationEntry;

/**
 *
 * @author sdorra
 */
public class PluginActivator implements Activator, ConfigurationListener
{

  /**
   * Method description
   *
   *
   * @param config
   * @param key
   */
  public void configChanged(ModifyableConfiguration config, String key)
  {
    if (key.equalsIgnoreCase(LdapConfigBean.CONFIG_LDAP_ENABLED))
    {
      boolean state = config.getBoolean(LdapConfigBean.CONFIG_LDAP_ENABLED,
                                        Boolean.FALSE);

      if (state &&!active)
      {
        enableLdapModule();
      }
      else if (!state && active)
      {
        disableLdapModule();
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param context
   */
  public void start(PluginContext context)
  {
    if (configReference == null)
    {
      configReference = context.getServiceRegistry().getServiceReference(
        Constants.SERVCIE_GLOBALCONFIGPROVIDER);
    }

    if (authReference == null)
    {
      authReference = context.getServiceRegistry().getServiceReference(
        Constants.SERVICE_AUTHENTICATION);
    }

    configReference.addImplementation("/view/ldap/config.xhtml");

    XmlConfiguration config = BlogContext.getInstance().getConfiguration();

    config.addListener(this);
    active = config.getBoolean(LdapConfigBean.CONFIG_LDAP_ENABLED,
                               Boolean.FALSE);

    if (active)
    {
      enableLdapModule();
    }
  }

  /**
   * Method description
   *
   *
   * @param context
   */
  public void stop(PluginContext context)
  {
    if (configReference != null)
    {
      configReference.removeImplementation("/view/ldap/config.xhtml");
    }

    BlogContext.getInstance().getConfiguration().removeListener(this);
    disableLdapModule();
  }

  /**
   * Method description
   *
   */
  private void disableLdapModule()
  {
    if (entry != null)
    {
      authReference.removeImplementation(entry);
    }

    active = false;
  }

  /**
   * Method description
   *
   */
  @SuppressWarnings("unchecked")
  private void enableLdapModule()
  {
    if (entry == null)
    {
      entry = new AppConfigurationEntry(
          LdapLoginModule.class.getName(),
          AppConfigurationEntry.LoginModuleControlFlag.OPTIONAL,
          new HashMap<String, Object>());
    }

    authReference.getImplementations().add(entry);
    active = true;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private boolean active;

  /** Field description */
  private ServiceReference authReference;

  /** Field description */
  private ServiceReference configReference;

  /** Field description */
  private AppConfigurationEntry entry;
}
