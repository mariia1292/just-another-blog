/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.model;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.myfaces.custom.tree2.TreeNode;

import sonia.blog.api.dao.PageDAO;
import sonia.blog.api.util.PageNavigation;
import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sdorra
 */
public class PageNavigationTreeNode implements TreeNode
{

  /** Field description */
  public static final String DEFAULT_TYPE = "page";

  /** Field description */
  private static final long serialVersionUID = 6821864888325304237L;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param pageDAO
   * @param page
   * @param filter
   * @param leaf
   * @param onlyPublished
   */
  PageNavigationTreeNode(PageDAO pageDAO, PageNavigation page,
                         PageNavigationFilter filter, boolean leaf,
                         boolean onlyPublished)
  {
    this.pageDAO = pageDAO;
    this.page = page;
    this.filter = filter;
    this.leaf = leaf;
    this.onlyPublished = onlyPublished;
    this.type = DEFAULT_TYPE;
    this.identifier = page.getId().toString();
    this.description = page.getNavigationTitle();
  }

  /**
   * Constructs ...
   *
   *
   * @param pageDAO
   * @param blog
   * @param filter
   * @param identifier
   * @param description
   * @param leaf
   * @param onlyPublished
   */
  public PageNavigationTreeNode(PageDAO pageDAO, Blog blog,
                                PageNavigationFilter filter, String identifier,
                                String description, boolean leaf,
                                boolean onlyPublished)
  {
    this.pageDAO = pageDAO;
    this.blog = blog;
    this.filter = filter;
    this.identifier = identifier;
    this.description = description;
    this.leaf = leaf;
    this.onlyPublished = onlyPublished;
    this.type = DEFAULT_TYPE;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public int getChildCount()
  {
    if (children == null)
    {
      getChildren();
    }

    return children.size();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List getChildren()
  {
    List<? extends PageNavigation> pageNavigation = null;

    if (page == null)
    {
      if (onlyPublished)
      {
        pageNavigation = pageDAO.getAllRoot(blog, true);
      }
      else
      {
        pageNavigation = pageDAO.getAllRoot(blog);
      }
    }
    else
    {
      if (onlyPublished)
      {
        pageNavigation = pageDAO.getChildren(page, true);
      }
      else
      {
        pageNavigation = pageDAO.getChildren(page);
      }
    }

    children = new ArrayList<PageNavigationTreeNode>();

    if (pageNavigation != null)
    {
      for (PageNavigation nav : pageNavigation)
      {
        if ((filter == null) || filter.accept(nav))
        {
          children.add(new PageNavigationTreeNode(pageDAO, nav, filter, false,
                  onlyPublished));
        }
      }
    }

    return children;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getIdentifier()
  {
    return identifier;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public PageNavigation getPage()
  {
    return page;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getType()
  {
    return type;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isLeaf()
  {
    return getChildCount() == 0;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param description
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Method description
   *
   *
   * @param identifier
   */
  public void setIdentifier(String identifier)
  {
    this.identifier = identifier;
  }

  /**
   * Method description
   *
   *
   * @param leaf
   */
  public void setLeaf(boolean leaf)
  {
    this.leaf = leaf;
  }

  /**
   * Method description
   *
   *
   * @param type
   */
  public void setType(String type)
  {
    this.type = type;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected Blog blog;

  /** Field description */
  protected List<PageNavigationTreeNode> children;

  /** Field description */
  protected String description;

  /** Field description */
  protected PageNavigationFilter filter;

  /** Field description */
  protected String identifier;

  /** Field description */
  protected boolean leaf;

  /** Field description */
  protected boolean onlyPublished;

  /** Field description */
  protected PageNavigation page;

  /** Field description */
  protected PageDAO pageDAO;

  /** Field description */
  protected String type;
}
