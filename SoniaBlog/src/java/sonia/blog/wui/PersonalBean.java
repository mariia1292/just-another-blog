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
import sonia.blog.api.navigation.NavigationProvider;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.plugin.service.ServiceReference;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class PersonalBean extends AbstractBean
{

  /**
   * Constructs ...
   *
   */
  public PersonalBean()
  {
    super();
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
        authorNavigation.add(
            new NavigationMenuItem(
                bundle.getString("newPage"), "#{PageAuthorBean.newPage}"));
        authorNavigation.add(new NavigationMenuItem(bundle.getString("pages"),
                "pages"));
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
            new NavigationMenuItem(
                label.getString("userAdministration"), "userAdministration"));
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

      if (user != null)
      {
        if (user.isSelfManaged())
        {
          readerNavigation.add(
              new NavigationMenuItem(
                  label.getString("userSettings"), "userSettings"));
        }

        handleProviders(Constants.NAVIGATION_READER, readerNavigation);
      }
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
   */
  private void handleProviders(String servicePath,
                               List<NavigationMenuItem> items)
  {
    BlogContext context = BlogContext.getInstance();
    ServiceReference<NavigationProvider> reference =
      context.getServiceRegistry().get(NavigationProvider.class, servicePath);

    if (reference != null)
    {
      List<NavigationProvider> providers = reference.getAll();

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
