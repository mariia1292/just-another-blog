/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.PageDAO;
import sonia.blog.api.dao.TagDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.Mapping;
import sonia.blog.api.mapping.MappingNavigation;
import sonia.blog.api.navigation.NavigationProvider;
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.search.SearchEntry;
import sonia.blog.api.search.SearchException;
import sonia.blog.api.spam.SpamCheck;
import sonia.blog.api.spam.SpamInputProtection;
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

import sonia.plugin.service.Service;
import sonia.plugin.service.ServiceReference;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author sdorra
 */
public class BlogBean extends AbstractBean
{

  /** Field description */
  public static final String NAME = "BlogBean";

  /** Field description */
  private static Logger logger = Logger.getLogger(BlogBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public BlogBean()
  {
    super();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   */
  public void addComment()
  {
    if (entry instanceof CommentAble)
    {
      CommentAble ca = (CommentAble) entry;

      comment.setAuthorAddress(getRequest().getRemoteAddr());
      ca.addComment(comment);
      checkSpam(comment);

      if (entry instanceof Entry)
      {
        CommentDAO commentDAO = BlogContext.getDAOFactory().getCommentDAO();

        if (commentDAO.add(comment))
        {
          if (comment.isSpam())
          {
            getMessageHandler().warn("createCommentSpam");
          }
          else
          {
            getMessageHandler().info("createCommentSuccess");
          }
        }
        else
        {
          getMessageHandler().error("createCommentFailure");
        }
      }
      else
      {
        getMessageHandler().error("commentOnlyOnEntries");
      }

      Comment newComment = new Comment();

      newComment.setAuthorMail(comment.getAuthorMail());
      newComment.setAuthorName(comment.getAuthorName());
      newComment.setAuthorURL(comment.getAuthorURL());
      comment = newComment;
    }
    else
    {
      getMessageHandler().error("commentOnlyOnEntries");
    }
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
    random.setExternalLink(linkBuilder.buildLink(request, "/random.jab"));
    navigation.add(random);

    if (request.getUser() == null)
    {
      navigation.add(new NavigationMenuItem(bundle.getString("login"),
              "login"));

      if (config.getBoolean(Constants.CONFIG_ALLOW_REGISTRATION, Boolean.FALSE))
      {
        navigation.add(new NavigationMenuItem(bundle.getString("register"),
                "register"));
      }
    }
    else
    {
      String dashboardLink = linkBuilder.buildLink(request,
                               "/personal/dashboard.jab");
      NavigationMenuItem dashboardItem = new NavigationMenuItem();

      dashboardItem.setLabel(bundle.getString("personal"));
      dashboardItem.setExternalLink(dashboardLink);
      navigation.add(dashboardItem);

      if (config.getBoolean(Constants.CONFIG_ALLOW_BLOGCREATION, Boolean.FALSE))
      {
        navigation.add(new NavigationMenuItem(bundle.getString("createBlog"),
                "createBlog"));
      }

      navigation.add(new NavigationMenuItem(bundle.getString("logout"),
              "#{LoginBean.logout}"));
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
    String linkBase = linkBuilder.buildLink(request, "/");
    NavigationMenuItem overview = new NavigationMenuItem();

    overview.setValue(bundle.getString("overview"));
    overview.setExternalLink(linkBuilder.buildLink(request,
            linkBase + "list/index.jab"));
    navigation.add(overview);

    if (BlogContext.getInstance().isInstalled())
    {
      List<? extends PageNavigation> pageNav =
        BlogContext.getDAOFactory().getPageDAO().getAllRoot(
            request.getCurrentBlog());

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
      overviewItem.setExternalLink(linkBuilder.buildLink(getRequest(),
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
   *
   * @return
   */
  public DataModel getSearchResult()
  {
    searchResult = new ListDataModel();

    SearchContext context = BlogContext.getInstance().getSearchContext();
    Blog blog = getRequest().getCurrentBlog();

    try
    {
      List<SearchEntry> resultList = context.search(blog, searchString);

      if (resultList != null)
      {
        searchResult.setWrappedData(resultList);
      }
    }
    catch (SearchException ex)
    {
      FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                           ex.getMessage(), null);

      FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    return searchResult;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSearchString()
  {
    return searchString;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public SpamInputProtection getSpamInputMethod()
  {
    SpamInputProtection method = null;
    String configString = config.getString(Constants.CONFIG_SPAMMETHOD);

    if (!Util.isBlank(configString))
    {
      if (!configString.equalsIgnoreCase("none"))
      {
        List<SpamInputProtection> list = spamServiceReference.getAll();

        for (SpamInputProtection sp : list)
        {
          if (sp.getClass().getName().equals(configString))
          {
            method = sp;
          }
        }

        if (method == null)
        {
          logger.warning("method " + configString
                         + " not found, using default");
          method = list.get(0);
        }
      }
    }
    else
    {
      method = spamServiceReference.get();
    }

    return method;
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

  /**
   * Method description
   *
   *
   * @param searchResult
   */
  public void setSearchResult(DataModel searchResult)
  {
    this.searchResult = searchResult;
  }

  /**
   * Method description
   *
   *
   * @param searchString
   */
  public void setSearchString(String searchString)
  {
    this.searchString = searchString;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param comment
   */
  private void checkSpam(Comment comment)
  {
    comment.setSpam(false);

    List<SpamCheck> list = spamCheckReference.getAll();

    if (Util.hasContent(list))
    {
      BlogRequest request = getRequest();

      for (SpamCheck check : list)
      {
        if (check.isSpam(request, comment))
        {
          comment.setSpam(true);

          break;
        }
      }
    }
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
  private DataModel searchResult;

  /** Field description */
  private String searchString;

  /** Field description */
  @Service(Constants.SERVICE_SPAMCHECK)
  private ServiceReference<SpamCheck> spamCheckReference;

  /** Field description */
  @Service(Constants.SERVICE_SPAMPROTECTIONMETHOD)
  private ServiceReference<SpamInputProtection> spamServiceReference;

  /** Field description */
  private DataModel tags;
}
