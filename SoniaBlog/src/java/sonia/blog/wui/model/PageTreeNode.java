/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.model;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.dao.PageDAO;
import sonia.blog.api.util.PageNavigation;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Page;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sdorra
 */
public class PageTreeNode extends PageNavigationTreeNode
{

  /** Field description */
  private static final long serialVersionUID = 470548412491252589L;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param pageDAO
   * @param page
   * @param leaf
   * @param onlyPublished
   */
  PageTreeNode(PageDAO pageDAO, PageNavigation page, boolean leaf,
               boolean onlyPublished)
  {
    super(pageDAO, page, leaf, onlyPublished);
  }

  /**
   * Constructs ...
   *
   *
   * @param pageDAO
   * @param blog
   * @param identifier
   * @param description
   * @param leaf
   * @param onlyPublished
   */
  public PageTreeNode(PageDAO pageDAO, Blog blog, String identifier,
                      String description, boolean leaf, boolean onlyPublished)
  {
    super(pageDAO, blog, identifier, description, leaf, onlyPublished);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public List getChildren()
  {
    List<? extends PageNavigation> pageNavigation = null;

    if (page == null)
    {
      if (onlyPublished)
      {
        pageNavigation = pageDAO.getRootPages(blog, true);
      }
      else
      {
        pageNavigation = pageDAO.getRootPages(blog);
      }
    }
    else
    {
      if (onlyPublished)
      {
        pageNavigation = pageDAO.getPageChildren((Page) page, true);
      }
      else
      {
        pageNavigation = pageDAO.getPageChildren((Page) page);
      }
    }

    children = new ArrayList<PageNavigationTreeNode>();

    if (pageNavigation != null)
    {
      for (PageNavigation nav : pageNavigation)
      {
        children.add(new PageTreeNode(pageDAO, nav, false, onlyPublished));
      }
    }

    return children;
  }
}
