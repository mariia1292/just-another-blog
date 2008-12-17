/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.myfaces.custom.navmenu.NavigationMenuItem;

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
            new NavigationMenuItem(bundle.getString("entries"), "personal"));
        authorNavigation.add(
            new NavigationMenuItem(
                bundle.getString("categories"), "categories"));
        authorNavigation.add(
            new NavigationMenuItem(bundle.getString("comments"), "comments"));
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

      User user = getRequest().getUser();

      if (user.isSelfManaged())
      {
        readerNavigation.add(
            new NavigationMenuItem(
                label.getString("userSettings"), "userSettings"));
      }
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
