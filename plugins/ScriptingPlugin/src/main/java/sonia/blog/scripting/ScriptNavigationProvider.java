/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.scripting;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.navigation.NavigationItem;
import sonia.blog.api.navigation.NavigationProvider;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

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
   * @param request
   * @param items
   */
  public void handleNavigation(BlogRequest request, List<NavigationItem> items)
  {
    if (request.getUser().isGlobalAdmin())
    {
      items.add(new NavigationItem("Scripting", "script"));
    }
  }
}
