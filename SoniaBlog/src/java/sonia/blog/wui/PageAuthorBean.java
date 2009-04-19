/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.myfaces.custom.tree2.TreeNode;

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.PageDAO;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Page;
import sonia.blog.wui.model.PageNavigationTreeNode;
import sonia.blog.wui.model.PageTreeNode;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;
import sonia.blog.entity.Blog;

/**
 *
 * @author sdorra
 */
public class PageAuthorBean extends AbstractEditorBean
{

  /** Field description */
  public static final String PAGEEDITOR = "page-editor";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(PageAuthorBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public PageAuthorBean()
  {
    super();
    page = new Page();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param event
   */
  public void editListener(ActionEvent event)
  {
    UIComponent cmp = event.getComponent();
    List<UIComponent> children = cmp.getChildren();

    if (Util.hasContent(children))
    {
      for (UIComponent child : children)
      {
        if (child instanceof UIParameter)
        {
          UIParameter param = (UIParameter) child;

          if (param.getName().equals("pageId"))
          {
            try
            {
              String idString = (String) param.getValue();

              page = pageDAO.get(Long.parseLong(idString));
              setSessionVar();
            }
            catch (NumberFormatException ex)
            {
              logger.log(Level.FINEST, null, ex);
            }
          }
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String newPage()
  {
    page = new Page();
    setSessionVar();

    return PAGEEDITOR;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String publish()
  {
    page.setPublished(true);
    page.setPublishingDate(new Date());

    return save();
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

    if (pageDAO.remove(page))
    {
      newPage();
      getMessageHandler().info("removePageSuccess");
    }
    else
    {
      getMessageHandler().error("removePageFailure");
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
  public String save()
  {
    BlogRequest request = getRequest();

    Blog blog = request.getCurrentBlog();

    page.setBlog(blog);
    page.setAuthor(request.getUser());

    if (page.getTitle() == null)
        {
          page.setTitle("NewPage " + blog.getDateFormatter().format(new Date()) );
        }
    if ( page.getNavigationTitle() == null )
    {
      page.setNavigationTitle( page.getTitle() );
    }

    if (parentId != null)
    {
      Page parent = pageDAO.get(parentId);

      page.setParent(parent);
    }

    String result = SUCCESS;

    if (page.getId() == null)
    {
      if (pageDAO.add(page))
      {
        getMessageHandler().info("createPageSuccess");
      }
      else
      {
        getMessageHandler().error("pageActionFailure");
        result = FAILURE;
      }
    }
    else
    {
      if (pageDAO.edit(page))
      {
        getMessageHandler().info("updatePageSuccess");
      }
      else
      {
        getMessageHandler().error("pageActionFailure");
        result = FAILURE;
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String saveDraft()
  {
    page.setPublished(false);

    return save();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public TreeNode getNavigationTreeNode()
  {
    BlogRequest request = getRequest();

    return new PageNavigationTreeNode(pageDAO, request.getCurrentBlog(),
                                      "root", "RootNode", false, false);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Page getPage()
  {
    if (page == null)
    {
      page = new Page();
    }

    return page;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Long getParentId()
  {
    return parentId;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public TreeNode getTreeData()
  {
    BlogRequest request = getRequest();

    return new PageTreeNode(pageDAO, request.getCurrentBlog(), "root",
                            "RootNode", false, false);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public boolean isNew()
  {
    return page.getId() == null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public boolean isPublished()
  {
    return page.isPublished();
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param page
   */
  public void setPage(Page page)
  {
    this.page = page;
  }

  /**
   * Method description
   *
   *
   * @param parentId
   */
  public void setParentId(Long parentId)
  {
    this.parentId = parentId;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param parent
   *
   * @return
   */
  @Override
  protected File getAttachmentDirectory(File parent)
  {
    StringBuffer path = new StringBuffer();

    path.append(Constants.RESOURCE_PAGES).append(File.separator);
    path.append(page.getId());

    return new File(parent, path.toString());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected List<Attachment> getAttachmentList()
  {
    return attachmentDAO.getAll(page);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected List<Attachment> getThumbnailList()
  {
    return attachmentDAO.getAllImages(page);
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param attachment
   */
  @Override
  protected void setRelation(Attachment attachment)
  {
    attachment.setPage(page);
  }

  /**
   * Method description
   *
   */
  private void setSessionVar()
  {
    getRequest().getSession().setAttribute("editor", "page");
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Dao
  private AttachmentDAO attachmentDAO;

  /** Field description */
  private Page page;

  /** Field description */
  @Dao
  private PageDAO pageDAO;

  /** Field description */
  private Long parentId;
}
