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
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.navigation.NavigationItem;
import sonia.blog.api.navigation.NavigationProvider;
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.template.Template;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;
import sonia.blog.util.BlogUtil;
import sonia.blog.wui.model.GenericDataModel;

import sonia.config.Config;

import sonia.plugin.service.Service;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

/**
 *
 * @author Sebastian Sdorra
 */
@RequireRole(Role.ADMIN)
public class AdminBlogBean extends AbstractInformationBean
{

  /** Field description */
  public static final String BACK = "back";

  /** Field description */
  public static final String DETAIL = "detail";

  /** Field description */
  public static final String NEW = "new";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(AdminBlogBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public AdminBlogBean()
  {
    init();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String addMember()
  {
    BlogRequest request = getRequest();

    if (Util.hasContent(addMemberName))
    {
      User user = userDAO.get(addMemberName);

      if (user != null)
      {
        userDAO.setRole(request.getBlogSession(), blog, user, Role.READER);
        getMessageHandler().info(request, "addUserToBlogSuccess");
        addMemberName = null;
      }
      else
      {
        getMessageHandler().warn(request, "addUserToBlogFailure");
      }
    }
    else
    {
      getMessageHandler().warn(request, "addUserToBlogFailure");
    }

    return DETAIL;
  }

  /**
   * Method description
   *
   *
   *
   * @param blogDAO
   *
   * @return
   */
  public boolean checkServername(BlogDAO blogDAO)
  {
    boolean result = true;
    Blog b = blogDAO.get(blog.getIdentifier());

    if ((b != null) &&!b.equals(blog))
    {
      result = false;
      getMessageHandler().error(getRequest(), "blogform:servername",
                                "nameAllreadyExists", null,
                                blog.getIdentifier());
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String clearImageCache()
  {
    String result = SUCCESS;
    File imageDir = BlogContext.getInstance().getResourceManager().getDirectory(
                        Constants.RESOURCE_IMAGE, blog);

    try
    {
      Util.delete(imageDir);
      getMessageHandler().info(getRequest(), "successClearImageCache");
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
      getMessageHandler().error(getRequest(), "failureClearImageCache");
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String detail()
  {
    blog = (Blog) blogs.getRowData();

    return DETAIL;
  }

  /**
   * Method description
   *
   *
   * @param event
   */
  public void imageValueChanged(ValueChangeEvent event)
  {
    clearImageCache();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String newBlog()
  {
    blog = new Blog();

    return NEW;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String rebuildIndex()
  {
    String result = SUCCESS;
    SearchContext context = BlogContext.getInstance().getSearchContext();

    if (context != null)
    {
      context.reIndex(getBlogSession(), blog);
      getMessageHandler().info(getRequest(), "rebuildIndex");
    }
    else
    {
      getMessageHandler().warn(getRequest(), "failureRebuildIndex");
      result = FAILURE;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String remove()
  {
    String result = SUCCESS;
    Blog blog = (Blog) blogs.getRowData();

    if (blog != null)
    {
      if (!blog.equals(getRequest().getCurrentBlog()))
      {
        if (blogDAO.remove(getBlogSession(), blog))
        {
          getMessageHandler().info(getRequest(), null, "successBlogDelete",
                                   null, blog.getTitle());
        }
        else
        {
          result = FAILURE;
          getMessageHandler().error(getRequest(), null, "failureBlogDelete",
                                    null, blog.getTitle());
        }
      }
      else
      {
        getMessageHandler().warn(getRequest(), "cantDeleteCurrentBlog");
        result = FAILURE;
      }
    }
    else
    {
      getMessageHandler().error(getRequest(), "unknownError");
      result = FAILURE;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   *
   * @param event
   */
  public void roleChanged(ValueChangeEvent event)
  {
    BlogMember member = (BlogMember) members.getRowData();
    User user = member.getUser();
    Blog blog = member.getBlog();
    Role role = (Role) event.getNewValue();

    if (role != null)
    {
      try
      {
        userDAO.setRole(getBlogSession(), blog, user, role);
        getMessageHandler().info(getRequest(), "changeRoleSuccess");
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
        getMessageHandler().error(getRequest(), "changeRoleFailure");
      }
    }
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String save()
  {
    String result = SUCCESS;

    try
    {
      if (checkServername(blogDAO))
      {
        if (blog.getId() != null)
        {
          blogDAO.edit(getBlogSession(), blog);
          getMessageHandler().info(getRequest(), "updateBlogSuccess");
        }
        else
        {
          blogDAO.add(getBlogSession(), blog);
          getMessageHandler().info(getRequest(), "createBlogSuccess");
        }
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
      result = FAILURE;
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<NavigationMenuItem> getActions()
  {
    List<NavigationItem> items = new ArrayList<NavigationItem>();
    ResourceBundle label = getResourceBundle("label");
    SearchContext context = BlogContext.getInstance().getSearchContext();

    if (context != null)
    {
      items.add(new NavigationItem(label.getString("reIndexSearch"),
                                   "#{AdminBlogBean.rebuildIndex}"));
    }

    items.add(new NavigationItem(label.getString("clearImageCache"),
                                 "#{AdminBlogBean.clearImageCache}"));

    return BlogUtil.createNavigation(getRequest(), providers, items);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAddMemberName()
  {
    return addMemberName;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Blog getBlog()
  {
    return blog;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getBlogs()
  {
    blogs = new GenericDataModel(blogDAO, pageSize);

    return blogs;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SelectItem[] getLocaleItems()
  {
    return BlogUtil.getLocaleItems(FacesContext.getCurrentInstance());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getMembers()
  {
    members = new ListDataModel();

    // TODO scrolling
    BlogDAO blogDAO = BlogContext.getDAOFactory().getBlogDAO();
    List<BlogMember> memberList = blogDAO.getMembers(blog, 0, 1000);

    if ((memberList != null) &&!memberList.isEmpty())
    {
      members.setWrappedData(memberList);
    }

    return members;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Integer getPageSize()
  {
    return pageSize;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SelectItem[] getRoleItems()
  {
    SelectItem[] items = new SelectItem[4];
    ResourceBundle bundle = getResourceBundle("label");

    items[0] = new SelectItem(Role.READER,
                              bundle.getString(Role.READER.name()));
    items[1] = new SelectItem(Role.AUTHOR,
                              bundle.getString(Role.AUTHOR.name()));
    items[2] = new SelectItem(Role.CONTENTMANAGER,
                              bundle.getString(Role.CONTENTMANAGER.name()));
    items[3] = new SelectItem(Role.ADMIN, bundle.getString(Role.ADMIN.name()));

    return items;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SelectItem[] getTemplateItems()
  {
    SelectItem[] items = null;
    List<Template> templates =
      BlogContext.getInstance().getTemplateManager().getTemplates(blog);
    int size = templates.size();

    items = new SelectItem[size];

    for (int i = 0; i < size; i++)
    {
      Template template = templates.get(i);

      items[i] = new SelectItem(template.getPath(), template.getName(),
                                template.getDescription());
    }

    return items;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SelectItem[] getTimeZoneItems()
  {
    return BlogUtil.getTimeZoneItems();
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param addMemberName
   */
  public void setAddMemberName(String addMemberName)
  {
    this.addMemberName = addMemberName;
  }

  /**
   * Method description
   *
   *
   * @param blog
   */
  public void setBlog(Blog blog)
  {
    this.blog = blog;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String addMemberName;

  /** Field description */
  private Blog blog;

  /** Field description */
  @Dao
  private BlogDAO blogDAO;

  /** Field description */
  private DataModel blogs;

  /** Field description */
  private DataModel members;

  /** Field description */
  @Config(Constants.CONFIG_ADMIN_PAGESIZE)
  private Integer pageSize = Integer.valueOf(20);

  /** Field description */
  @Service(Constants.NAVIGATION_BLOGACTION)
  private List<NavigationProvider> providers;

  /** Field description */
  @Dao
  private UserDAO userDAO;
}
