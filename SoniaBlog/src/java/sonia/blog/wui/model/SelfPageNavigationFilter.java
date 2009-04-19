/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.model;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.util.PageNavigation;

/**
 *
 * @author sdorra
 */
public class SelfPageNavigationFilter implements PageNavigationFilter
{

  /**
   * Constructs ...
   *
   *
   * @param pageNavigation
   */
  public SelfPageNavigationFilter(PageNavigation pageNavigation)
  {
    this.pageNavigation = pageNavigation;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param pageNavigation
   *
   * @return
   */
  public boolean accept(PageNavigation pageNavigation)
  {
    return !this.pageNavigation.getId().equals(pageNavigation.getId());
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private PageNavigation pageNavigation;
}
