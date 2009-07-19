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



package sonia.blog.devel;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.myfaces.custom.navmenu.NavigationMenuItem;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.navigation.NavigationProvider;

import sonia.plugin.Activator;
import sonia.plugin.PluginContext;
import sonia.plugin.service.ServiceReference;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.faces.context.FacesContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class PluginActivator implements Activator
{

  /**
   * Method description
   *
   *
   * @param context
   */
  public void start(PluginContext context)
  {
    if (reference == null)
    {
      reference = getServiceReference(context);
    }

    navigationProvider = new GlobalAdminNavigationProvider();
    reference.add(navigationProvider);
  }

  /**
   * Method description
   *
   *
   * @param context
   */
  public void stop(PluginContext context)
  {
    if (reference == null)
    {
      reference = getServiceReference(context);
    }

    if (navigationProvider != null)
    {
      reference.remove(navigationProvider);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isDatabaseEmbedded()
  {
    return BlogContext.getInstance().getConfiguration().getBoolean(
        Constants.CONFIG_DB_EMBEDDED, Boolean.FALSE);
  }

  /**
   * Method description
   *
   *
   * @param context
   *
   * @return
   */
  private ServiceReference<NavigationProvider> getServiceReference(
          PluginContext context)
  {
    return context.getServiceRegistry().get(NavigationProvider.class,
            Constants.NAVIGATION_GLOBALADMIN);
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version    Enter version here..., 09/01/17
   * @author     Enter your name here...
   */
  private static class GlobalAdminNavigationProvider
          implements NavigationProvider
  {

    /**
     * Method description
     *
     *
     * @param facesContext
     * @param request
     * @param items
     */
    public void handleNavigation(FacesContext facesContext,
                                 BlogRequest request,
                                 List<NavigationMenuItem> items)
    {
      if (request.getUser().isGlobalAdmin())
      {
        items.add(new NavigationMenuItem("Development", "devel"));
      }
    }
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private NavigationProvider navigationProvider;

  /** Field description */
  private ServiceReference<NavigationProvider> reference;
}
