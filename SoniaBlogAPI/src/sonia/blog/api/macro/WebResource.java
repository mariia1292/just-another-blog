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
   * Method description
   *
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
