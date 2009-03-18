/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.rss;

//~--- JDK imports ------------------------------------------------------------

import java.net.URL;

/**
 *
 * @author sdorra
 */
public class Image
{

  /**
   * Constructs ...
   *
   */
  public Image() {}

  /**
   * Constructs ...
   *
   *
   * @param url
   */
  public Image(URL url)
  {
    this.url = url;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public URL getLink()
  {
    return link;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTitle()
  {
    return title;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public URL getUrl()
  {
    return url;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param link
   */
  public void setLink(URL link)
  {
    this.link = link;
  }

  /**
   * Method description
   *
   *
   * @param title
   */
  public void setTitle(String title)
  {
    this.title = title;
  }

  /**
   * Method description
   *
   *
   * @param url
   */
  public void setUrl(URL url)
  {
    this.url = url;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private URL link;

  /** Field description */
  private String title;

  /** Field description */
  private URL url;
}
