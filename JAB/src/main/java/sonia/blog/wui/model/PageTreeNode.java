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

import sonia.blog.api.dao.PageDAO;
import sonia.blog.api.util.PageNavigation;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Page;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
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
   * @param filter
   * @param leaf
   * @param onlyPublished
   */
  PageTreeNode(PageDAO pageDAO, PageNavigation page,
               PageNavigationFilter filter, boolean leaf, boolean onlyPublished)
  {
    super(pageDAO, page, filter, leaf, onlyPublished);
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
  public PageTreeNode(PageDAO pageDAO, Blog blog, PageNavigationFilter filter,
                      String identifier, String description, boolean leaf,
                      boolean onlyPublished)
  {
    super(pageDAO, blog, filter, identifier, description, leaf, onlyPublished);
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
        if ((filter == null) || filter.accept(nav))
        {
          children.add(new PageTreeNode(pageDAO, nav, filter, false,
                                        onlyPublished));
        }
      }
    }

    return children;
  }
}
