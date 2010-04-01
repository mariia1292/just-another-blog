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



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.myfaces.custom.navmenu.NavigationMenuItem;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.authentication.RequireRole;
import sonia.blog.api.navigation.NavigationItem;
import sonia.blog.api.navigation.NavigationProvider;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Role;
import sonia.blog.util.BlogUtil;

import sonia.plugin.service.ServiceReference;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author Sebastian Sdorra
 */
@RequireRole(Role.READER)
public class PersonalBean extends AbstractBean
{

  /**
   * Constructs ...
   *
   */
  public PersonalBean()
  {
    init();
  }

  //~--- get methods ----------------------------------------------------------

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
        List<NavigationItem> adminNavList = new ArrayList<NavigationItem>();

        adminNavList.add(new NavigationItem(label.getString("templates"),
                "templates"));
        adminNavList.add(new NavigationItem(label.getString("members"),
                "members"));
        adminNavList.add(new NavigationItem(label.getString("general"),
                "general"));
        adminNavigation = handleProviders(Constants.NAVIGATION_ADMIN,
                                          adminNavList);
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
        List<NavigationItem> authorNavList = new ArrayList<NavigationItem>();

        authorNavList.add(new NavigationItem(bundle.getString("newEntry"),
                "#{EntryBean.newEntry}"));
        authorNavList.add(new NavigationItem(bundle.getString("entries"),
                "entries"));
        authorNavList.add(new NavigationItem(bundle.getString("categories"),
                "categories"));
        authorNavList.add(new NavigationItem(bundle.getString("comments"),
                "comments"));
        authorNavList.add(new NavigationItem(bundle.getString("newPage"),
                "#{PageAuthorBean.newPage}"));
        authorNavList.add(new NavigationItem(bundle.getString("pages"),
                "pages"));
        authorNavigation = handleProviders(Constants.NAVIGATION_AUTHOR,
                                           authorNavList);
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
        List<NavigationItem> globalAdminNav = new ArrayList<NavigationItem>();

        globalAdminNav.add(
            new NavigationItem(
                label.getString("blogAdministration"), "administration"));
        globalAdminNav.add(
            new NavigationItem(
                label.getString("userAdministration"), "userAdministration"));
        globalAdminNav.add(new NavigationItem(label.getString("plugins"),
                "plugins"));
        globalAdminNav.add(new NavigationItem(label.getString("status"),
                "globalStatus"));
        globalAdminNav.add(new NavigationItem(label.getString("configuration"),
                "config"));
        globalAdminNavigation = handleProviders(Constants.NAVIGATION_GLOBALADMIN, globalAdminNav);
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
      List<NavigationItem> readerNav = new ArrayList<NavigationItem>();

      readerNav.add(new NavigationItem(label.getString("dashboard"),
                                       "dashboard"));

      BlogRequest request = getRequest();

      readerNav.add(new NavigationItem(label.getString("userSettings"),
                                       "userSettings"));

      NavigationItem logoutItem = new NavigationItem();

      logoutItem.setLabel(label.getString("logout"));

      String link =
        BlogContext.getInstance().getLinkBuilder().getRelativeLink(request,
          "/logout");

      logoutItem.setHref(link);
      readerNav.add(logoutItem);
      readerNavigation = handleProviders(Constants.NAVIGATION_READER,
                                         readerNav);
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
    return getBlogSession().hasRole(Role.ADMIN);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isAuthor()
  {
    return getBlogSession().hasRole(Role.AUTHOR);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isGloablAdmin()
  {
    return getBlogSession().hasRole(Role.GLOBALADMIN);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param servicePath
   * @param items
   *
   * @return
   */
  private List<NavigationMenuItem> handleProviders(String servicePath,
          List<NavigationItem> items)
  {
    BlogContext context = BlogContext.getInstance();
    ServiceReference<NavigationProvider> reference =
      context.getServiceRegistry().get(NavigationProvider.class, servicePath);
    BlogRequest request = getRequest();
    List<NavigationProvider> providers = null;

    if (reference != null)
    {
      providers = reference.getAll();
    }

    return BlogUtil.createNavigation(request, providers, items);
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
