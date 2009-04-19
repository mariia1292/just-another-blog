/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
    super();
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

      if (child.getId().equals(p.getId()))
      {
        childNav.setOpen(true);
        buildSubNavigation(childNav, linkBase, breadcrum, 0);
      }

      if (page.getId().equals(child.getId()))
      {
        childNav.setActive(true);
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
