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
 * @author Sebastian Sdorra
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
