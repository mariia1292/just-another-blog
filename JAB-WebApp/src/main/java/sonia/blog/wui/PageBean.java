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

import org.apache.myfaces.custom.navmenu.NavigationMenuItem;

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Context;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.PageDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.api.util.PageNavigation;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Page;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public class PageBean extends AbstractBean
{

  /** Field description */
  public static final String NAME = "PageBean";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public PageBean()
  {
    init();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Page getPage()
  {
    return page;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<NavigationMenuItem> getPageNavigation()
  {
    BlogRequest request = getRequest();
    Blog blog = request.getCurrentBlog();
    String linkBase = linkBuilder.buildLink(request, "/page/");
    List<NavigationMenuItem> navigation = new ArrayList<NavigationMenuItem>();
    List<Page> breadcrum = new ArrayList<Page>();
    Page p = page;

    while (p.getParent() != null)
    {
      breadcrum.add(p);
      p = p.getParent();
    }

    breadcrum.add(p);
    Collections.reverse(breadcrum);

    List<? extends PageNavigation> children = pageDAO.getAllRoot(blog, true);

    for (PageNavigation child : children)
    {
      NavigationMenuItem childNav = new NavigationMenuItem();

      if (page.getId() != null)
      {
        if (child.getId().equals(p.getId()))
        {
          childNav.setOpen(true);
          buildSubNavigation(childNav, linkBase, breadcrum, 0);
        }

        if (page.getId().equals(child.getId()))
        {
          childNav.setActive(true);
        }
      }

      childNav.setValue(child.getNavigationTitle());

      StringBuffer linkBuffer = new StringBuffer();

      linkBuffer.append(linkBase).append(child.getId()).append(".jab");
      childNav.setExternalLink(linkBuffer.toString());
      navigation.add(childNav);
    }

    return navigation;
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

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param parentNav
   * @param linkBase
   * @param breadcrum
   * @param i
   */
  private void buildSubNavigation(NavigationMenuItem parentNav,
                                  String linkBase, List<Page> breadcrum, int i)
  {
    if (i < breadcrum.size())
    {
      Page p = breadcrum.get(i);
      List<? extends PageNavigation> children = pageDAO.getChildren(p, true);
      Page next = null;

      if ((i + 1) < breadcrum.size())
      {
        next = breadcrum.get(i + 1);
      }

      for (PageNavigation child : children)
      {
        NavigationMenuItem childNav = new NavigationMenuItem();

        if ((next != null) && child.getId().equals(next.getId()))
        {
          childNav.setOpen(true);
          buildSubNavigation(childNav, linkBase, breadcrum, ++i);
        }

        if (page.getId().equals(child.getId()))
        {
          childNav.setActive(true);
        }

        childNav.setValue(child.getNavigationTitle());

        StringBuffer linkBuffer = new StringBuffer();

        linkBuffer.append(linkBase).append(child.getId()).append(".jab");
        childNav.setExternalLink(linkBuffer.toString());
        parentNav.add(childNav);
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Context
  private LinkBuilder linkBuilder;

  /** Field description */
  private Page page;

  /** Field description */
  @Dao
  private PageDAO pageDAO;
}
