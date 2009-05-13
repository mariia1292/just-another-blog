/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.macro;

/**
 *
 * @author sdorra
 */
public abstract class WebResource implements Comparable<WebResource>
{

  /**
   * Constructs ...
   *
   *
   * @param index
   */
  public WebResource(int index)
  {
    this.index = index;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Allowed Patterns:
   * {1} - id of the current blog
   * {2} - identifier of the current blog
   * {3} - title of the current blog
   * {4} - description of the current blog
   *
   * @return
   */
  public abstract String toHTML();

  /**
   * Method description
   *
   *
   * @param resource
   *
   * @return
   */
  public int compareTo(WebResource resource)
  {
    return index - resource.index;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private int index;
}
