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



package sonia.blog.ldap;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;

import sonia.config.ConfigurationListener;
import sonia.config.ModifyableConfiguration;

import sonia.plugin.Activator;
import sonia.plugin.PluginContext;
import sonia.plugin.service.ServiceReference;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;

import javax.security.auth.login.AppConfigurationEntry;

/**
 *
 * @author Sebastian Sdorra
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
      configReference = context.getServiceRegistry().get(String.class,
              Constants.SERVCIE_GLOBALCONFIGPROVIDER);
    }

    if (authReference == null)
    {
      authReference =
        context.getServiceRegistry().get(AppConfigurationEntry.class,
                                         Constants.SERVICE_AUTHENTICATION);
    }

    configReference.add("/view/ldap/config.xhtml");

    BlogConfiguration config = BlogContext.getInstance().getConfiguration();

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
      configReference.remove("/view/ldap/config.xhtml");
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
      authReference.remove(entry);
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

    authReference.add(entry);
    active = true;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private boolean active;

  /** Field description */
  private ServiceReference<AppConfigurationEntry> authReference;

  /** Field description */
  private ServiceReference<String> configReference;

  /** Field description */
  private AppConfigurationEntry entry;
}
