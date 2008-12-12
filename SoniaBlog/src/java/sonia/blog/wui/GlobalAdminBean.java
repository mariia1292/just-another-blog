/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sonia.blog.wui;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.myfaces.custom.navmenu.NavigationMenuItem;
import sonia.blog.api.util.AbstractBean;

/**
 *
 * @author sdorra
 */
public class GlobalAdminBean extends AbstractBean
{

  private List<NavigationMenuItem> navigation;

  public List<NavigationMenuItem> getNavigation()
  {
    if ( navigation == null )
    {
      ResourceBundle label = getResourceBundle("label");

      navigation = new ArrayList<NavigationMenuItem>();

      navigation.add( new NavigationMenuItem( label.getString("authorArea"), "personal" ) );
      navigation.add( new NavigationMenuItem( label.getString("blogAdministration"), "administration" ) );
      navigation.add( new NavigationMenuItem( label.getString("configuration"), "config" ) );
    }

    return navigation;
  }

  public void setNavigation(List<NavigationMenuItem> navigation)
  {
    this.navigation = navigation;
  }

}
