/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.navigation;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.myfaces.custom.navmenu.NavigationMenuItem;

import sonia.blog.api.app.BlogRequest;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public interface NavigationProvider
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
                               List<NavigationMenuItem> items);
}
