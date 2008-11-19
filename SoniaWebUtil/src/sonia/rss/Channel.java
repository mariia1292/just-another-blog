/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.rss;

//~--- JDK imports ------------------------------------------------------------

import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author sdorra
 */
public class Channel extends AbstractBase
{

  /**
   * Constructs ...
   *
   */
  public Channel() {}

  /**
   * Constructs ...
   *
   *
   * @param title
   * @param link
   * @param description
   */
  public Channel(String title, URL link, String description)
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
  public String getCopyright()
  {
    return copyright;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Image getImage()
  {
    return image;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Item> getItems()
  {
    if (items == null)
    {
      items = new ArrayList<Item>();
    }

    return items;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Locale getLanguage()
  {
    return language;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param copyright
   */
  public void setCopyright(String copyright)
  {
    this.copyright = copyright;
  }

  /**
   * Method description
   *
   *
   * @param image
   */
  public void setImage(Image image)
  {
    this.image = image;
  }

  /**
   * Method description
   *
   *
   * @param items
   */
  public void setItems(List<Item> items)
  {
    this.items = items;
  }

  /**
   * Method description
   *
   *
   * @param language
   */
  public void setLanguage(Locale language)
  {
    this.language = language;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String copyright;

  /** Field description */
  private Image image;

  /** Field description */
  private List<Item> items;

  /** Field description */
  private Locale language;
}
