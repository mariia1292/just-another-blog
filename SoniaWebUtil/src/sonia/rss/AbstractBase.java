/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.rss;

//~--- JDK imports ------------------------------------------------------------

import java.net.URL;

import java.util.Date;

/**
 *
 * @author sdorra
 */
public abstract class AbstractBase
{

  /**
   * Constructs ...
   *
   */
  public AbstractBase() {}

  /**
   * Constructs ...
   *
   *
   * @param title
   * @param link
   * @param description
   */
  public AbstractBase(String title, URL link, String description)
  {
    this.title = title;
    this.link = link;
    this.description = description;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDescription()
  {
    return description;
  }

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
  public Date getPubDate()
  {
    return pubDate;
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

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param description
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

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
   * @param pubDate
   */
  public void setPubDate(Date pubDate)
  {
    this.pubDate = pubDate;
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected String description;

  /** Field description */
  protected URL link;

  /** Field description */
  protected Date pubDate;

  /** Field description */
  protected String title;
}
