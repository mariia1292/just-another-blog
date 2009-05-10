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
public class ScriptResource implements WebResource
{

  /**
   * Constructs ...
   *
   *
   * @param src
   */
  public ScriptResource(String src)
  {
    this.src = src;
  }

  /**
   * Constructs ...
   *
   *
   * @param src
   * @param type
   */
  public ScriptResource(String src, String type)
  {
    this.src = src;
    this.type = type;
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

    buffer.append("<script type=\"");

    if (Util.hasContent(type))
    {
      buffer.append(type);
    }
    else
    {
      buffer.append("text/javascript");
    }

    buffer.append("\" src=\"").append(src).append("\"></script>");

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
  public String getSrc()
  {
    return src;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getType()
  {
    return type;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param src
   */
  public void setSrc(String src)
  {
    this.src = src;
  }

  /**
   * Method description
   *
   *
   * @param type
   */
  public void setType(String type)
  {
    this.type = type;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String src;

  /** Field description */
  private String type;
}
