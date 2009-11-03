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
import org.apache.myfaces.custom.navmenu.UINavigationMenuItem;

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.Context;
import sonia.blog.api.dao.CategoryDAO;
import sonia.blog.api.dao.CommentDAO;
import sonia.blog.api.dao.TagDAO;
import sonia.blog.api.dao.TrackbackDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.Mapping;
import sonia.blog.api.mapping.MappingNavigation;
import sonia.blog.api.navigation.NavigationProvider;
import sonia.blog.api.template.Template;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.api.util.PageNavigation;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Comment;
import sonia.blog.entity.CommentAble;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Tag;
import sonia.blog.entity.Trackback;

import sonia.config.ElParamConfigMap;

import sonia.plugin.service.Service;
import sonia.plugin.service.ServiceReference;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Sebastian Sdorra
 */
public class BlogBean extends AbstractBean
{

  /** Field description */
  public static final String NAME = "BlogBean";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public BlogBean()
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
  public Blog getBlog()
  {
    return getRequest().getCurrentBlog();
  }

  /**
   * Method description
   *
   * @return
   */
  public DataModel getCategories()
  {
    categories = new ListDataModel();

    CategoryDAO categoryDAO = BlogContext.getDAOFactory().getCategoryDAO();
    List<Category> categoryList =
      categoryDAO.getAll(getRequest().getCurrentBlog());

    if ((categoryList != null) &&!categoryList.isEmpty())
    {
      categories.setWrappedData(categoryList);
    }

    return categories;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Comment getComment()
  {
    if (comment == null)
    {
      comment = new Comment();
    }

    return comment;
  }

  /**
   * Method description
   * TODO replace with CommentDAO.findActivesByEntry
   *
   * @return
   */
  public DataModel getComments()
  {
    DataModel comments = new ListDataModel();

    if (entry instanceof Entry)
    {
      CommentDAO commentDAO = BlogContext.getDAOFactory().getCommentDAO();
      List<Comment> commentList =
        commentDAO.findAllActivesByEntry((Entry) entry);

      if ((commentList != null) &&!commentList.isEmpty())
      {
        comments.setWrappedData(commentList);
      }
    }

    return comments;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Map<String, String> getConfigurationMap()
  {
    if (configurationMap == null)
    {
      configurationMap =
        new ElParamConfigMap(BlogContext.getInstance().getConfiguration());
    }

    return configurationMap;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getCurrentPage()
  {
    int page = 0;
    String param = getRequest().getParameter("page");

    if (param != null)
    {
      try
      {
        page = Integer.parseInt(param);
      }
      catch (NumberFormatException ex) {}
    }

    return page;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<? extends ContentObject> getEntries()
  {
    return entries;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ContentObject getEntry()
  {
    return entry;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<NavigationMenuItem> getExtraNavigation()
  {
    List<NavigationMenuItem> navigation = new ArrayList<NavigationMenuItem>();
    ResourceBundle bundle = getResourceBundle("label");
    BlogRequest request = getRequest();
    NavigationMenuItem random = new NavigationMenuItem();

    random.setLabel(bundle.getString("random"));
    random.setExternalLink(linkBuilder.getRelativeLink(request, "/random.jab"));
    navigation.add(random);

    if (request.getUser() == null)
    {
      NavigationMenuItem loginItem = new NavigationMenuItem();

      loginItem.setValue(bundle.getString("login"));
      loginItem.setExternalLink(linkBuilder.getRelativeLink(request,
              "/login.jab"));
      navigation.add(loginItem);

      if (config.getBoolean(Constants.CONFIG_ALLOW_REGISTRATION, Boolean.FALSE))
      {
        NavigationMenuItem registerItem = new NavigationMenuItem();

        registerItem.setValue(bundle.getString("register"));
        registerItem.setExternalLink(linkBuilder.getRelativeLink(request,
                "/register.jab"));
        navigation.add(registerItem);
      }
    }
    else
    {
      String dashboardLink = linkBuilder.getRelativeLink(request,
                               "/personal/dashboard.jab");
      NavigationMenuItem dashboardItem = new NavigationMenuItem();

      dashboardItem.setLabel(bundle.getString("personal"));
      dashboardItem.setExternalLink(dashboardLink);
      navigation.add(dashboardItem);

      if (config.getBoolean(Constants.CONFIG_ALLOW_BLOGCREATION, Boolean.FALSE))
      {
        NavigationMenuItem createBlogItem = new NavigationMenuItem();

        createBlogItem.setValue(bundle.getString("createBlog"));
        createBlogItem.setExternalLink(linkBuilder.getRelativeLink(request,
                "/blog.jab"));
        navigation.add(createBlogItem);
      }

      NavigationMenuItem logoutItem = new NavigationMenuItem();

      logoutItem.setValue(bundle.getString("logout"));
      logoutItem.setExternalLink(linkBuilder.getRelativeLink(request,
              "/logout"));
      navigation.add(logoutItem);
    }

    List<NavigationProvider> providers = extraNavigationReference.getAll();

    if ((providers != null) &&!providers.isEmpty())
    {
      FacesContext facesContext = FacesContext.getCurrentInstance();

      for (NavigationProvider provider : providers)
      {
        provider.handleNavigation(facesContext, request, navigation);
      }
    }

    return navigation;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<NavigationMenuItem> getMainNavigation()
  {
    List<NavigationMenuItem> navigation = new ArrayList<NavigationMenuItem>();
    BlogRequest request = getRequest();
    ResourceBundle bundle = getResourceBundle("label");
    String linkBase = linkBuilder.getRelativeLink(request, "/");
    NavigationMenuItem overview = new NavigationMenuItem();

    overview.setValue(bundle.getString("overview"));
    overview.setExternalLink(linkBuilder.getRelativeLink(request,
            linkBase + "list/index.jab"));
    navigation.add(overview);

    if (BlogContext.getInstance().isInstalled())
    {
      List<? extends PageNavigation> pageNav =
        BlogContext.getDAOFactory().getPageDAO().getAllRoot(
            request.getCurrentBlog(), true);

      if (pageNav != null)
      {
        for (PageNavigation nav : pageNav)
        {
          NavigationMenuItem item = new NavigationMenuItem();

          item.setValue(nav.getNavigationTitle());

          StringBuilder link = new StringBuilder();

          link.append(linkBase).append("page/").append(nav.getId()).append(
              ".jab");
          item.setExternalLink(link.toString());
          navigation.add(item);
        }
      }
    }

    return navigation;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getNextUri()
  {
    String nextUri = null;
    BlogRequest request = getRequest();
    Mapping mapping = request.getMapping();

    if (mapping != null)
    {
      MappingNavigation navigation = mapping.getMappingNavigation();

      if (navigation != null)
      {
        nextUri = navigation.getNextUri();
      }
    }

    return nextUri;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public UINavigationMenuItem getOverviewItem()
  {
    if (overviewItem == null)
    {
      overviewItem = new UINavigationMenuItem();
      overviewItem.setValue(getResourceBundle("label").getString("overview"));
      overviewItem.setExternalLink(linkBuilder.getRelativeLink(getRequest(),
              "/list/index.jab"));
    }

    return overviewItem;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getPageEntries()
  {
    return pageEntries;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getPermalink()
  {
    String result = null;

    if ((entry != null) && (entry instanceof Entry))
    {
      BlogRequest request = getRequest();
      StringBuffer link = new StringBuffer();

      link.append("/list/").append(entry.getId()).append(".jab");
      result = linkBuilder.buildLink(request, link.toString());
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getPreviousUri()
  {
    String prevUri = null;
    BlogRequest request = getRequest();
    Mapping mapping = request.getMapping();

    if (mapping != null)
    {
      MappingNavigation navigation = mapping.getMappingNavigation();

      if (navigation != null)
      {
        prevUri = navigation.getPreviousUri();
      }
    }

    return prevUri;
  }

  /**
   * Method description
   *
   * @return
   */
  public DataModel getTags()
  {
    tags = new ListDataModel();

    TagDAO tagDAO = BlogContext.getDAOFactory().getTagDAO();
    List<Tag> tagList = tagDAO.findAllByBlog(getRequest().getCurrentBlog());

    if ((tagList != null) &&!tagList.isEmpty())
    {
      tags.setWrappedData(tagList);
    }

    return tags;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Template getTemplate()
  {
    return BlogContext.getInstance().getTemplateManager().getTemplate(
        getBlog());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTrackbackLink()
  {
    String result = null;

    if ((entry != null) && (entry instanceof Entry))
    {
      BlogRequest request = getRequest();
      StringBuffer link = new StringBuffer();

      link.append("/trackback/").append(entry.getId());
      result = linkBuilder.buildLink(request, link.toString());
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getTrackbacks()
  {
    trackbacks = new ListDataModel();

    if (entry instanceof Entry)
    {
      TrackbackDAO trackbackDAO = BlogContext.getDAOFactory().getTrackbackDAO();
      List<Trackback> trackbackList = trackbackDAO.getAll((Entry) entry);

      if ((trackbackList != null) &&!trackbackList.isEmpty())
      {
        trackbacks.setWrappedData(trackbackList);
      }
    }

    return trackbacks;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getVersion()
  {
    return "r" + BlogContext.getInstance().getVersion();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isInstalled()
  {
    return BlogContext.getInstance().isInstalled();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isNavigationRendered()
  {
    boolean result = false;
    Mapping mapping = getRequest().getMapping();

    if (mapping != null)
    {
      result = mapping.getMappingNavigation() != null;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isShowComments()
  {
    boolean result = false;

    if (entry instanceof CommentAble)
    {
      result = getBlog().isAllowComments();
    }

    return result;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param comment
   */
  public void setComment(Comment comment)
  {
    this.comment = comment;
  }

  /**
   * Method description
   *
   *
   * @param entries
   */
  public void setEntries(List<? extends ContentObject> entries)
  {
    this.entries = entries;
  }

  /**
   * Method description
   *
   *
   * @param entry
   */
  public void setEntry(ContentObject entry)
  {
    this.entry = entry;
  }

  /**
   * Method description
   *
   *
   * @param overviewItem
   */
  public void setOverviewItem(UINavigationMenuItem overviewItem)
  {
    this.overviewItem = overviewItem;
  }

  /**
   * Method description
   *
   *
   * @param pageEntries
   */
  public void setPageEntries(DataModel pageEntries)
  {
    this.pageEntries = pageEntries;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected int getType()
  {
    return TYPE_FRONTEND;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private DataModel categories;

  /** Field description */
  private Comment comment;

  /** Field description */
  @Context
  private BlogConfiguration config;

  /** Field description */
  private Map<String, String> configurationMap;

  /** Field description */
  private List<? extends ContentObject> entries;

  /** Field description */
  private ContentObject entry;

  /** Field description */
  @Service(Constants.NAVIGATION_EXTRA)
  private ServiceReference<NavigationProvider> extraNavigationReference;

  /** Field description */
  @Context
  private LinkBuilder linkBuilder;

  /** Field description */
  private UINavigationMenuItem overviewItem;

  /** Field description */
  private DataModel pageEntries;

  /** Field description */
  private DataModel tags;

  /** Field description */
  private DataModel trackbacks;
}
