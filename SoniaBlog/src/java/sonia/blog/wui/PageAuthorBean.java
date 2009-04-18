/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.myfaces.custom.tree2.TreeNode;

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.PageDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Page;
import sonia.blog.wui.model.PageNavigationTreeNode;
import sonia.blog.wui.model.PageTreeNode;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;

/**
 *
 * @author sdorra
 */
public class PageAuthorBean extends AbstractBean
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
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String draft()
  {
    page.setPublished(false);

    return save();
  }

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

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private String save()
  {
    BlogRequest request = getRequest();

    page.setBlog(request.getCurrentBlog());
    page.setAuthor(request.getUser());

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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Page page;

  /** Field description */
  @Dao
  private PageDAO pageDAO;

  /** Field description */
  private Long parentId;
}
