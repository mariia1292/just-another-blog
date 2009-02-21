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
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.dao.CategoryDAO;
import sonia.blog.api.dao.CommentDAO;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.navigation.NavigationProvider;
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.template.Template;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.plugin.service.ServiceReference;

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
    actionReference = BlogContext.getInstance().getServiceRegistry().get(
      NavigationProvider.class, Constants.NAVIGATION_BLOGACTION);
  }

  //~--- methods --------------------------------------------------------------

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
    Blog b = blogDAO.findByIdentifier(blog.getIdentifier());

    if ((b != null) &&!b.equals(blog))
    {
      result = false;
      getMessageHandler().error("blogform:servername", "nameAllreadyExists",
                                null, blog.getIdentifier());
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
      getMessageHandler().info("rebuildIndex");
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
    Blog blog = (Blog) blogs.getRowData();

    if (blog != null)
    {
      if (!blog.equals(getRequest().getCurrentBlog()))
      {
        BlogDAO blogDAO = BlogContext.getDAOFactory().getBlogDAO();

        if (blogDAO.remove(blog))
        {
          getMessageHandler().info(null, "successBlogDelete", null,
                                   blog.getTitle());
        }
        else
        {
          result = FAILURE;
          getMessageHandler().error(null, "failureBlogDelete", null,
                                    blog.getTitle());
        }

        /*
         * EntityManager em = context.getEntityManager();
         *
         * em.getTransaction().begin();
         *
         * try
         * {
         * List<Category> categories = blog.getCategories();
         *
         * // TODO replace with CategoryDAO.remove
         * for (Category c : categories)
         * {
         *   em.remove(em.merge(c));
         * }
         *
         * List<BlogMember> members = blog.getMembers();
         *
         * // TODO replace with MemberDAO.remove
         * for (BlogMember m : members)
         * {
         *   em.remove(em.merge(m));
         * }
         *
         * // TODO replace with BlogDAO.remove
         * em.remove(em.merge(blog));
         * em.getTransaction().commit();
         *
         * File searchDir = resManager.getDirectory(Constants.RESOURCE_INDEX,
         *                    blog, false);
         *
         * if (searchDir.exists())
         * {
         *   Util.delete(searchDir);
         * }
         *
         * File attachmentDir =
         *  resManager.getDirectory(Constants.RESOURCE_ATTACHMENT, blog, false);
         *
         * if (attachmentDir.exists())
         * {
         *   Util.delete(attachmentDir);
         * }
         *
         * getMessageHandler().info(null, "successBlogDelete", null,
         *                          blog.getTitle());
         * }
         * catch (Exception ex)
         * {
         * if (em.getTransaction().isActive())
         * {
         *   em.getTransaction().rollback();
         * }
         *
         * logger.log(Level.SEVERE, null, ex);
         * getMessageHandler().error(null, "failureBlogDelete", null,
         *                           blog.getTitle());
         * }
         * finally
         * {
         * em.close();
         * }
         */
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
    User user = member.getUser();
    Blog blog = member.getBlog();
    Role role = (Role) event.getNewValue();

    if (role != null)
    {
      try
      {
        BlogContext.getDAOFactory().getUserDAO().setRole(blog, user, role);
        getMessageHandler().info("changeRoleSuccess");
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
        getMessageHandler().error("changeRoleFailure");
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
    BlogDAO blogDAO = BlogContext.getDAOFactory().getBlogDAO();

    try
    {
      if (checkServername(blogDAO))
      {
        try
        {
          if (blog.getId() != null)
          {
            blogDAO.edit(blog);
            getMessageHandler().info("updateBlogSuccess");
          }
          else
          {
            blogDAO.add(blog);
            getMessageHandler().info("createBlogSuccess");
          }
        }
        catch (Exception ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
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

    List<NavigationProvider> providers = actionReference.getAll();

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
   * @return
   */
  public long getAttachmentCount()
  {
    AttachmentDAO attachmentDAO =
      BlogContext.getDAOFactory().getAttachmentDAO();

    return attachmentDAO.countByBlog(blog);
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

    BlogDAO blogDAO = BlogContext.getDAOFactory().getBlogDAO();
    List<Blog> blogList = blogDAO.getAll();

    if ((blogList != null) &&!blogList.isEmpty())
    {
      blogs.setWrappedData(blogList);
    }

    return blogs;
  }

  /**
   * Method description
   *
   * @return
   */
  public long getCategoryCount()
  {
    CategoryDAO categegoryDAO = BlogContext.getDAOFactory().getCategoryDAO();

    return categegoryDAO.countByBlog(blog);
  }

  /**
   * Method description
   *
   * @return
   */
  public long getCommentCount()
  {
    CommentDAO commentDAO = BlogContext.getDAOFactory().getCommentDAO();

    return commentDAO.countByBlog(blog);
  }

  /**
   * Method description
   *
   * @return
   */
  public long getEntryCount()
  {
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();

    return entryDAO.countByBlog(blog);
  }

  /**
   * Method description
   *
   * @return
   */
  public long getMemberCount()
  {
    UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();

    return userDAO.count(blog);
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
    UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();
    List<BlogMember> memberList = userDAO.getMembers(blog, 0, 1000);

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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference<NavigationProvider> actionReference;

  /** Field description */
  private Blog blog;

  /** Field description */
  private DataModel blogs;

  /** Field description */
  private DataModel members;
}
