/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.script;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.myfaces.custom.navmenu.NavigationMenuItem;

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.navigation.NavigationProvider;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.faces.context.FacesContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class ScriptNavigationProvider implements NavigationProvider
{

  /**
   * Method description
   *
   *
   * @param facesContext
   * @param request
   * @param items
   */
  public void handleNavigation(FacesContext facesContext, BlogRequest request,
                               List<NavigationMenuItem> items)
  {
    if (request.getUser().isGlobalAdmin())
    {
      items.add(new NavigationMenuItem("Scripting", "script"));
    }
  }
}
