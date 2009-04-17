/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.util;

/**
 *
 * @author sdorra
 */
public class PageNavigation
{

  /**
   * Constructs ...
   *
   *
   * @param id
   * @param navigationTitle
   */
  public PageNavigation(Long id, String navigationTitle)
  {
    this.id = id;
    this.navigationTitle = navigationTitle;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String toString()
  {
    StringBuffer out = new StringBuffer();

    out.append("nav: ").append(id).append(" - ").append(navigationTitle);

    return out.toString();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Long getId()
  {
    return id;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getNavigationTitle()
  {
    return navigationTitle;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Long id;

  /** Field description */
  private String navigationTitle;
}
