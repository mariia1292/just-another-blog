/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.myfaces.custom.navmenu.NavigationMenuItem;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.navigation.NavigationProvider;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.plugin.ServiceReference;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class PersonalBean extends AbstractBean
{

  /**
   * Method description
   *
   *
   * @return
   */
  public List<NavigationMenuItem> getAdminNavigation()
  {
    if (isAdmin())
    {
      if (adminNavigation == null)
      {
        ResourceBundle label = getResourceBundle("label");

        adminNavigation = new ArrayList<NavigationMenuItem>();
        adminNavigation.add(
            new NavigationMenuItem(label.getString("templates"), "templates"));
        adminNavigation.add(new NavigationMenuItem(label.getString("members"),
                "members"));
        adminNavigation.add(new NavigationMenuItem(label.getString("general"),
                "general"));
        handleProviders(Constants.NAVIGATION_ADMIN, adminNavigation);
      }
    }
    else
    {
      adminNavigation = new ArrayList<NavigationMenuItem>();
    }

    return adminNavigation;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<NavigationMenuItem> getAuthorNavigation()
  {
    if (isAuthor())
    {
      if (authorNavigation == null)
      {
        ResourceBundle bundle = getResourceBundle("label");

        authorNavigation = new ArrayList<NavigationMenuItem>();
        authorNavigation.add(
            new NavigationMenuItem(
                bundle.getString("newEntry"), "#{EntryBean.newEntry}"));
        authorNavigation.add(
            new NavigationMenuItem(bundle.getString("entries"), "entries"));
        authorNavigation.add(
            new NavigationMenuItem(
                bundle.getString("categories"), "categories"));
        authorNavigation.add(
            new NavigationMenuItem(bundle.getString("comments"), "comments"));
        handleProviders(Constants.NAVIGATION_AUTHOR, authorNavigation);
      }
    }
    else
    {
      authorNavigation = new ArrayList<NavigationMenuItem>();
    }

    return authorNavigation;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<NavigationMenuItem> getGlobalAdminNavigation()
  {
    if (isGloablAdmin())
    {
      if (globalAdminNavigation == null)
      {
        ResourceBundle label = getResourceBundle("label");

        globalAdminNavigation = new ArrayList<NavigationMenuItem>();
        globalAdminNavigation.add(
            new NavigationMenuItem(
                label.getString("blogAdministration"), "administration"));
        globalAdminNavigation.add(
            new NavigationMenuItem(label.getString("plugins"), "plugins"));
        globalAdminNavigation.add(
            new NavigationMenuItem(label.getString("status"), "globalStatus"));
        globalAdminNavigation.add(
            new NavigationMenuItem(label.getString("configuration"), "config"));
        handleProviders(Constants.NAVIGATION_GLOBALADMIN,
                        globalAdminNavigation);
      }
    }
    else
    {
      globalAdminNavigation = new ArrayList<NavigationMenuItem>();
    }

    return globalAdminNavigation;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<NavigationMenuItem> getReaderNavigation()
  {
    if (readerNavigation == null)
    {
      ResourceBundle label = getResourceBundle("label");

      readerNavigation = new ArrayList<NavigationMenuItem>();
      readerNavigation.add(new NavigationMenuItem(label.getString("dashboard"),
              "dashboard"));

      User user = getRequest().getUser();

      if (user.isSelfManaged())
      {
        readerNavigation.add(
            new NavigationMenuItem(
                label.getString("userSettings"), "userSettings"));
      }

      handleProviders(Constants.NAVIGATION_READER, readerNavigation);
    }
    else
    {
      readerNavigation = new ArrayList<NavigationMenuItem>();
    }

    return readerNavigation;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isAdmin()
  {
    return getRequest().isUserInRole(Role.ADMIN);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isAuthor()
  {
    BlogRequest request = getRequest();

    return request.isUserInRole(Role.AUTHOR)
           || request.isUserInRole(Role.ADMIN);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isGloablAdmin()
  {
    User user = getRequest().getUser();

    return (user != null) && user.isActive() && user.isGlobalAdmin();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param servicePath
   * @param items
   */
  @SuppressWarnings("unchecked")
  private void handleProviders(String servicePath,
                               List<NavigationMenuItem> items)
  {
    BlogContext context = BlogContext.getInstance();
    ServiceReference reference =
      context.getServiceRegistry().getServiceReference(servicePath);

    if (reference != null)
    {
      List<NavigationProvider> providers = reference.getImplementations();

      if ((providers != null) &&!providers.isEmpty())
      {
        BlogRequest request = getRequest();
        FacesContext facesContext = FacesContext.getCurrentInstance();

        for (NavigationProvider provider : providers)
        {
          provider.handleNavigation(facesContext, request, items);
        }
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<NavigationMenuItem> adminNavigation;

  /** Field description */
  private List<NavigationMenuItem> authorNavigation;

  /** Field description */
  private List<NavigationMenuItem> globalAdminNavigation;

  /** Field description */
  private List<NavigationMenuItem> readerNavigation;
}
