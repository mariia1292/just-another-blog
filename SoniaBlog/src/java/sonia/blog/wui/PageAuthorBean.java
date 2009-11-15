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

import org.apache.myfaces.custom.tree2.TreeNode;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogSession;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.PageDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Page;
import sonia.blog.wui.model.PageNavigationFilter;
import sonia.blog.wui.model.PageNavigationTreeNode;
import sonia.blog.wui.model.PageTreeNode;
import sonia.blog.wui.model.SelfPageNavigationFilter;

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

/**
 *
 * @author Sebastian Sdorra
 */
public class PageAuthorBean extends AbstractEditorBean
{

  /** Field description */
  public static final String NAME = "PageAuthorBean";

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
   * @param event
   */
  @Override
  public void preview(ActionEvent event)
  {
    BlogRequest request = getRequest();
    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    String uri = linkBuilder.buildLink(request, "/page-preview.jab");

    if (page.getPublishingDate() == null)
    {
      page.setPublishingDate(new Date());
    }

    if (page.getAuthor() == null)
    {
      page.setAuthor(request.getUser());
    }

    sendRedirect(uri);
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

    if (Util.isEmpty(pageDAO.getChildren(page.toPageNavigation())))
    {
      if (pageDAO.remove(getBlogSession(), page))
      {
        newPage();
        getMessageHandler().info(getRequest(), "removePageSuccess");
      }
      else
      {
        getMessageHandler().error(getRequest(), "removePageFailure");
        result = FAILURE;
      }
    }
    else
    {
      getMessageHandler().warn(getRequest(), "removePageFailureChildren");
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
      page.setTitle("NewPage " + blog.getDateFormatter().format(new Date()));
      page.setPublished(false);
    }

    if (page.getNavigationTitle() == null)
    {
      page.setNavigationTitle(page.getTitle());
    }

    if (parentId != null)
    {
      Page parent = pageDAO.get(parentId);

      page.setParent(parent);
    }

    String result = SUCCESS;
    BlogSession session = request.getBlogSession();

    if (page.getId() == null)
    {
      if (pageDAO.add(session, page))
      {
        getMessageHandler().info(request, "createPageSuccess");
      }
      else
      {
        getMessageHandler().error(request, "pageActionFailure");
        result = FAILURE;
      }
    }
    else
    {
      if (pageDAO.edit(session, page))
      {
        getMessageHandler().info(request, "updatePageSuccess");
      }
      else
      {
        getMessageHandler().error(request, "pageActionFailure");
        result = FAILURE;
      }
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
    PageNavigationFilter filter = null;

    if ((page != null) && (page.getId() != null))
    {
      filter = new SelfPageNavigationFilter(page);
    }

    return new PageNavigationTreeNode(pageDAO, request.getCurrentBlog(),
                                      filter, "root", "RootNode", false, false);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public ContentObject getObject()
  {
    return getPage();
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

    return new PageTreeNode(pageDAO, request.getCurrentBlog(), null, "root",
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
