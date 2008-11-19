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
public class Item extends AbstractBase
{

  /**
   * Constructs ...
   *
   */
  public Item() {}

  /**
   * Constructs ...
   *
   *
   * @param title
   * @param link
   * @param description
   */
  public Item(String title, URL link, String description)
  {
    super(title, link, description);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAuthor()
  {
    return author;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getGuid()
  {
    return guid;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param autor
   */
  public void setAuthor(String autor)
  {
    this.author = autor;
  }

  /**
   * Method description
   *
   *
   * @param guid
   */
  public void setGuid(String guid)
  {
    this.guid = guid;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String author;

  /** Field description */
  private String guid;
}
