/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.util;

/**
 *
 * @author sdorra
 */
public class BasicPageNavigation implements PageNavigation
{

  /**
   * Constructs ...
   *
   *
   * @param id
   * @param navigationTitle
   * @param navigationPosition
   */
  public BasicPageNavigation(Long id, String navigationTitle,
                             int navigationPosition)
  {
    this.id = id;
    this.navigationTitle = navigationTitle;
    this.navigationPosition = navigationPosition;
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
    out.append(" (").append(navigationPosition).append(")");

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
  public int getNavigationPosition()
  {
    return navigationPosition;
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
  private int navigationPosition;

  /** Field description */
  private String navigationTitle;
}
