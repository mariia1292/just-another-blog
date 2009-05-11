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
public class ScriptResource extends WebResource
{

  /**
   * Constructs ...
   *
   *
   * @param src
   */
  public ScriptResource(int index, String src)
  {
    super( index );
    this.src = src;
  }

  /**
   * Constructs ...
   *
   *
   * @param src
   * @param type
   */
  public ScriptResource(int index, String src, String type)
  {
    super( index );
    this.src = src;
    this.type = type;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param obj
   *
   * @return
   */
  @Override
  public boolean equals(Object obj)
  {
    if (obj == null)
    {
      return false;
    }

    if (getClass() != obj.getClass())
    {
      return false;
    }

    final ScriptResource other = (ScriptResource) obj;

    if ((this.src == null)
        ? (other.src != null)
        : !this.src.equals(other.src))
    {
      return false;
    }

    if ((this.type == null)
        ? (other.type != null)
        : !this.type.equals(other.type))
    {
      return false;
    }

    return true;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public int hashCode()
  {
    int hash = 7;

    hash = 97 * hash + ((this.src != null)
                        ? this.src.hashCode()
                        : 0);
    hash = 97 * hash + ((this.type != null)
                        ? this.type.hashCode()
                        : 0);

    return hash;
  }

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
