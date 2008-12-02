/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.myfaces.custom.navmenu.NavigationMenuItem;
import org.apache.myfaces.custom.navmenu.UINavigationMenuItem;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingEntry;
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.search.SearchEntry;
import sonia.blog.api.search.SearchException;
import sonia.blog.api.template.Template;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Role;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class BlogBean extends AbstractBean
{

  /**
   * Method description
   *
   *
   *
   */
  public void addComment()
  {
    if (entry instanceof ContentObject)
    {
      Entry e = (Entry) entry;

      comment.setAuthorAddress(getRequest().getRemoteAddr());
      comment.setEntry(e);

      EntityManager em = BlogContext.getInstance().getEntityManager();

      em.getTransaction().begin();

      try
      {
        em.persist(comment);
        e.getComments().add(comment);
        e = em.merge(e);

        Comment newComment = new Comment();

        newComment.setAuthorMail(comment.getAuthorMail());
        newComment.setAuthorName(comment.getAuthorName());
        newComment.setAuthorURL(comment.getAuthorURL());
        comment = newComment;
        em.getTransaction().commit();
        e = em.find(Entry.class, e.getId());
        getMessageHandler().info("createCommentSuccess");
      }
      catch (Exception ex)
      {
        if (em.getTransaction().isActive())
        {
          em.getTransaction().rollback();
        }

        logger.log(Level.SEVERE, null, ex);
        getMessageHandler().error("createCommentFailure");
      }
      finally
      {
        em.close();
      }

      String uri =
        BlogContext.getInstance().getLinkBuilder().buildLink(getRequest(), e);

      sendRedirect(uri);
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
  public List<NavigationMenuItem> getAdminNavigation()
  {
    List<NavigationMenuItem> navigation = new ArrayList<NavigationMenuItem>();

    if (getRequest().isUserInRole(Role.ADMIN))
    {
      ResourceBundle bundle = getResourceBundle("label");

      navigation.add(new NavigationMenuItem(bundle.getString("templates"),
              "templates"));
      navigation.add(new NavigationMenuItem(bundle.getString("general"),
              "general"));
      navigation.add(new NavigationMenuItem(bundle.getString("members"),
              "members"));
    }

    return navigation;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<NavigationMenuItem> getAuthorNavigation()
  {
    List<NavigationMenuItem> navigation = new ArrayList<NavigationMenuItem>();

    if (getRequest().isUserInRole(Role.AUTHOR)
        || getRequest().isUserInRole(Role.ADMIN))
    {
      ResourceBundle bundle = getResourceBundle("label");

      navigation.add(new NavigationMenuItem(bundle.getString("entries"),
              "personal"));
      navigation.add(new NavigationMenuItem(bundle.getString("categories"),
              "categories"));
      navigation.add(new NavigationMenuItem(bundle.getString("comments"),
              "comments"));
    }

    return navigation;
  }

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
   *
   * @return
   */
  public DataModel getCategories()
  {
    categories = new ListDataModel();

    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Category.findAllFromBlog");

    q.setParameter("blog", getRequest().getCurrentBlog());

    List list = q.getResultList();

    if (list != null)
    {
      categories.setWrappedData(list);
    }

    em.close();

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
   *
   *
   * @return
   */
  public DataModel getComments()
  {
    DataModel comments = new ListDataModel();
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Comment.entryOverview");

    q.setParameter("entry", entry);

    List commentList = q.getResultList();

    if (commentList != null)
    {
      comments.setWrappedData(commentList);
    }

    em.close();

    return comments;
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
    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    NavigationMenuItem random = new NavigationMenuItem();

    random.setLabel(bundle.getString("random"));
    random.setExternalLink(linkBuilder.buildLink(request, "/random.jab"));
    navigation.add(random);

    if (request.getUser() == null)
    {
      navigation.add(new NavigationMenuItem(bundle.getString("login"),
              "login"));
    }
    else
    {
      if (request.isUserInRole("author") || request.isUserInRole("admin"))
      {
        navigation.add(new NavigationMenuItem(bundle.getString("personal"),
                "personal"));
      }

      navigation.add(new NavigationMenuItem(bundle.getString("logout"),
              "#{LoginBean.logout}"));
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
    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    ResourceBundle bundle = getResourceBundle("label");
    NavigationMenuItem overview = new NavigationMenuItem();

    overview.setValue(bundle.getString("overview"));
    overview.setExternalLink(linkBuilder.buildLink(request, "/list/index.jab"));
    navigation.add(overview);

    return navigation;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ContentObject getNextEntry()
  {
    ContentObject co = null;

    if (entry != null)
    {
      int pos = entries.indexOf(entry);

      if (pos >= 0)
      {
        pos++;

        if (pos < entries.size())
        {
          co = entries.get(pos);
        }
      }
    }

    return co;
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
      LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();

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
  public ContentObject getPrevEntry()
  {
    ContentObject co = null;

    if (entry != null)
    {
      int pos = entries.indexOf(entry);

      if (pos > 0)
      {
        pos--;
        co = entries.get(pos);
      }
    }

    return co;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getPrevUri()
  {
    String prevUri = null;

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
  public DataModel getTags()
  {
    tags = new ListDataModel();

    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Tag.findFromBlog");

    q.setParameter("blog", getRequest().getCurrentBlog());

    try
    {
      List list = q.getResultList();

      tags.setWrappedData(list);
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      em.close();
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
    MappingEntry mapping = getRequest().getMapping();

    if (mapping != null)
    {
      result = mapping.isNavigationRendered();
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private DataModel categories;

  /** Field description */
  private Comment comment;

  /** Field description */
  private List<? extends ContentObject> entries;

  /** Field description */
  private ContentObject entry;

  /** Field description */
  private UINavigationMenuItem overviewItem;

  /** Field description */
  private DataModel pageEntries;

  /** Field description */
  private DataModel searchResult;

  /** Field description */
  private String searchString;

  /** Field description */
  private DataModel tags;
}
