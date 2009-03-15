/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.entity;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

/**
 *
 * @author sdorra
 */
public class Trackback implements Serializable
{

  /** Field description */
  private static final long serialVersionUID = -6733835815011208749L;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public Trackback() {}

  /**
   * Constructs ...
   *
   *
   * @param url
   */
  public Trackback(String url)
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
  public String getBlogname()
  {
    return blogname;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Entry getEntry()
  {
    return entry;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getExcerpt()
  {
    return excerpt;
  }

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
  public String getUrl()
  {
    return url;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blogname
   */
  public void setBlogname(String blogname)
  {
    this.blogname = blogname;
  }

  /**
   * Method description
   *
   *
   * @param entry
   */
  public void setEntry(Entry entry)
  {
    this.entry = entry;
  }

  /**
   * Method description
   *
   *
   * @param excerpt
   */
  public void setExcerpt(String excerpt)
  {
    this.excerpt = excerpt;
  }

  /**
   * Method description
   *
   *
   * @param id
   */
  public void setId(Long id)
  {
    this.id = id;
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
  public void setUrl(String url)
  {
    this.url = url;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String blogname;

  /** Field description */
  private Entry entry;

  /** Field description */
  private String excerpt;

  /** Field description */
  private Long id;

  /** Field description */
  private String title;

  /** Field description */
  private String url;
}
