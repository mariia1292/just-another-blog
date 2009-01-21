/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
