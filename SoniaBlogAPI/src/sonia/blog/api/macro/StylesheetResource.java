/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

/**
 *
 * @author sdorra
 */
public class StylesheetResource implements WebResource
{

  /**
   * Constructs ...
   *
   */
  public StylesheetResource() {}

  /**
   * Constructs ...
   *
   *
   * @param path
   * @param rel
   * @param clazz
   * @param iePatch
   */
  public StylesheetResource(String path, String rel, String clazz,
                            boolean iePatch)
  {
    this.href = path;
    this.rel = rel;
    this.clazz = clazz;
    this.iePatch = iePatch;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String toHTML()
  {
    StringBuffer buffer = new StringBuffer();

    if (iePatch)
    {
      buffer.append("<!--[if lte IE 7]>\n");
    }

    buffer.append("<link type=\"text/css\"");

    if (Util.hasContent(rel))
    {
      buffer.append(" rel=\"").append(rel).append("\"");
    }

    if (Util.hasContent(clazz))
    {
      buffer.append(" class=\"").append(clazz).append("\"");
    }

    buffer.append(" href=\"").append(href).append("\" />\n");

    if (iePatch)
    {
      buffer.append("<![endif]-->\n");
    }

    return buffer.toString();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String toString()
  {
    return toHTML();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getClazz()
  {
    return clazz;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getHref()
  {
    return href;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getRel()
  {
    return rel;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isIePatch()
  {
    return iePatch;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param clazz
   */
  public void setClazz(String clazz)
  {
    this.clazz = clazz;
  }

  /**
   * Method description
   *
   *
   * @param href
   */
  public void setHref(String href)
  {
    this.href = href;
  }

  /**
   * Method description
   *
   *
   * @param iePatch
   */
  public void setIePatch(boolean iePatch)
  {
    this.iePatch = iePatch;
  }

  /**
   * Method description
   *
   *
   * @param rel
   */
  public void setRel(String rel)
  {
    this.rel = rel;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String clazz;

  /** Field description */
  private String href;

  /** Field description */
  private boolean iePatch;

  /** Field description */
  private String rel;
}
