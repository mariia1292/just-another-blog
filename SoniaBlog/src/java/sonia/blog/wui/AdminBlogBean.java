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
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.navigation.NavigationProvider;
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.template.Template;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Category;
import sonia.blog.entity.Role;

import sonia.plugin.ServiceReference;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class AdminBlogBean extends AbstractBean
{

  /** Field description */
  public static final String BACK = "back";

  /** Field description */
  public static final String DETAIL = "detail";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public AdminBlogBean()
  {
    actionReference =
      BlogContext.getInstance().getServiceRegistry().getServiceReference(
        Constants.NAVIGATION_BLOGACTION);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param em
   *
   * @return
   */
  public boolean checkServername(EntityManager em)
  {
    boolean result = true;

    try
    {
      Query q = em.createNamedQuery("Blog.findByServername");

      q.setParameter("servername", blog.getServername());

      Blog b = (Blog) q.getSingleResult();

      if ((b != null) &&!b.equals(blog))
      {
        result = false;
        getMessageHandler().error("blogform:servername", "nameAllreadyExists",
                                  null, blog.getServername());
      }
    }
    catch (NoResultException ex) {}

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
    File attachmentDir =
      BlogContext.getInstance().getResourceManager().getDirectory(
          Constants.RESOURCE_ATTACHMENT, blog);
    File imageDir = new File(attachmentDir, Constants.RESOURCE_IMAGE);

    try
    {
      Util.delete(imageDir);
      getMessageHandler().info("successClearImageCache");
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
      getMessageHandler().error("failureClearImageCache");
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
   * @return
   */
  public String rebuildIndex()
  {
    String result = SUCCESS;
    SearchContext context = BlogContext.getInstance().getSearchContext();

    if ((context != null) && context.isReIndexable() &&!context.isLocked())
    {
      context.reIndex(blog);
      getMessageHandler().info( "rebuildIndex" );
    }
    else
    {
      getMessageHandler().warn("failureRebuildIndex");
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
    BlogContext context = BlogContext.getInstance();
    ResourceManager resManager = context.getResourceManager();
    Blog blog = (Blog) blogs.getRowData();

    if (blog != null)
    {
      if (!blog.equals(getRequest().getCurrentBlog()))
      {
        EntityManager em = context.getEntityManager();

        em.getTransaction().begin();

        try
        {
          List<Category> categories = blog.getCategories();

          for (Category c : categories)
          {
            em.remove(em.merge(c));
          }

          List<BlogMember> members = blog.getMembers();

          for (BlogMember m : members)
          {
            em.remove(em.merge(m));
          }

          em.remove(em.merge(blog));
          em.getTransaction().commit();

          File searchDir = resManager.getDirectory(Constants.RESOURCE_INDEX,
                             blog, false);

          if (searchDir.exists())
          {
            Util.delete(searchDir);
          }

          File attachmentDir =
            resManager.getDirectory(Constants.RESOURCE_ATTACHMENT, blog, false);

          if (attachmentDir.exists())
          {
            Util.delete(attachmentDir);
          }

          getMessageHandler().info(null, "successBlogDelete", null,
                                   blog.getTitle());
        }
        catch (Exception ex)
        {
          if (em.getTransaction().isActive())
          {
            em.getTransaction().rollback();
          }

          logger.log(Level.SEVERE, null, ex);
          getMessageHandler().error(null, "failureBlogDelete", null,
                                    blog.getTitle());
        }
        finally
        {
          em.close();
        }
      }
      else
      {
        getMessageHandler().warn("cantDeleteCurrentBlog");
        result = FAILURE;
      }
    }
    else
    {
      getMessageHandler().error("unknownError");
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

    member.setRole((Role) event.getNewValue());

    if (member != null)
    {
      EntityManager em = BlogContext.getInstance().getEntityManager();

      em.getTransaction().begin();

      try
      {
        member = em.merge(member);
        em.getTransaction().commit();
        getMessageHandler().info("changeRoleSuccess");
      }
      catch (Exception ex)
      {
        if (em.getTransaction().isActive())
        {
          em.getTransaction().rollback();
        }

        logger.log(Level.SEVERE, null, ex);
        getMessageHandler().error("changeRoleFailure");
      }
      finally
      {
        em.close();
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
    EntityManager em = BlogContext.getInstance().getEntityManager();

    try
    {
      if (checkServername(em))
      {
        em.getTransaction().begin();

        try
        {
          if (blog.getId() != null)
          {
            blog = em.merge(blog);
            getMessageHandler().info("updateBlogSuccess");
          }
          else
          {
            em.persist(blog);
            getMessageHandler().info("createBlogSuccess");
          }

          em.getTransaction().commit();
        }
        catch (Exception ex)
        {
          if (em.getTransaction().isActive())
          {
            em.getTransaction().rollback();
          }

          logger.log(Level.SEVERE, null, ex);
        }
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      em.close();
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
    List<NavigationMenuItem> items = new ArrayList<NavigationMenuItem>();
    ResourceBundle label = getResourceBundle("label");
    SearchContext context = BlogContext.getInstance().getSearchContext();

    if ((context != null) && context.isReIndexable() &&!context.isLocked())
    {
      items.add(new NavigationMenuItem(label.getString("reIndexSearch"),
                                       "#{AdminBlogBean.rebuildIndex}"));
    }

    items.add(new NavigationMenuItem(label.getString("clearImageCache"),
                                     "#{AdminBlogBean.clearImageCache}"));

    List<NavigationProvider> providers = actionReference.getImplementations();

    if ((providers != null) &&!providers.isEmpty())
    {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      BlogRequest request = getRequest();

      for (NavigationProvider provider : providers)
      {
        provider.handleNavigation(facesContext, request, items);
      }
    }

    return items;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getAttachmentCount()
  {
    return countQuery("Attachment.countFromBlog");
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
    blogs = new ListDataModel();

    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Blog.findAll");

    try
    {
      List list = q.getResultList();

      blogs.setWrappedData(list);
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      em.close();
    }

    return blogs;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getCategoryCount()
  {
    return countQuery("Category.countFromBlog");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getCommentCount()
  {
    return countQuery("Comment.countFromBlog");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getEntryCount()
  {
    return countQuery("Entry.countFromBlog");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getMemberCount()
  {
    return countQuery("BlogMember.countFromBlog");
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

    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("BlogMember.findByBlog");

    q.setParameter("blog", blog);

    try
    {
      List list = q.getResultList();

      members.setWrappedData(list);
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      em.close();
    }

    return members;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SelectItem[] getRoleItems()
  {
    SelectItem[] items = new SelectItem[3];
    ResourceBundle bundle = getResourceBundle("label");

    items[0] = new SelectItem(Role.READER,
                              bundle.getString(Role.READER.name()));
    items[1] = new SelectItem(Role.AUTHOR,
                              bundle.getString(Role.AUTHOR.name()));
    items[2] = new SelectItem(Role.ADMIN, bundle.getString(Role.ADMIN.name()));

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
      BlogContext.getInstance().getTemplateManager().getTemplates();
    int size = templates.size();

    items = new SelectItem[size];

    for (int i = 0; i < size; i++)
    {
      Template template = templates.get(i);

      items[i] = new SelectItem(template.getName(), template.getPath(),
                                template.getDescription());
    }

    return items;
  }

  //~--- set methods ----------------------------------------------------------

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

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param queryName
   *
   * @return
   */
  private long countQuery(String queryName)
  {
    long result = 0;
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery(queryName);

    q.setParameter("blog", getRequest().getCurrentBlog());
    result = (Long) q.getSingleResult();
    em.close();

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference actionReference;

  /** Field description */
  private Blog blog;

  /** Field description */
  private DataModel blogs;

  /** Field description */
  private DataModel members;
}
