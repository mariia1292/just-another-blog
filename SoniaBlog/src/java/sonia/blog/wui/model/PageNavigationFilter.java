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
public interface PageNavigationFilter
{

  /**
   * Method description
   *
   *
   * @param pageNavigation
   *
   * @return
   */
  public boolean accept(PageNavigation pageNavigation);
}
