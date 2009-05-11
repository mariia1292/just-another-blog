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
public class LinkResource extends WebResource
{

  /** Field description */
  public static final String REL_FAVICON = "shortcut icon";

  /** Field description */
  public static final String REL_OPENSEARCH = "search";

  /** Field description */
  public static final String REL_RSSFEED = "alternate";

  /** Field description */
  public static final String REL_STYLESHEET = "stylesheet";

  /** Field description */
  public static final String TYPE_OPENSEARCH =
    "application/opensearchdescription+xml";

  /** Field description */
  public static final String TYPE_RSSFEED = "application/rss+xml";

  /** Field description */
  public static final String TYPE_STYLESHEET = "text/css";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public LinkResource(int index)
  {
    super(index);
  }

  /**
   * Constructs ...
   *
   *
   *
   * @param type
   * @param href
   * @param rel
   * @param title
   * @param clazz
   * @param iePatch
   */
  public LinkResource(int index, String type, String href, String rel, String title,
                      String clazz, boolean iePatch)
  {
    super(index);
    this.type = type;
    this.href = href;
    this.rel = rel;
    this.title = title;
    this.clazz = clazz;
    this.iePatch = iePatch;
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

    final LinkResource other = (LinkResource) obj;

    if ((this.clazz == null)
        ? (other.clazz != null)
        : !this.clazz.equals(other.clazz))
    {
      return false;
    }

    if ((this.href == null)
        ? (other.href != null)
        : !this.href.equals(other.href))
    {
      return false;
    }

    if (this.iePatch != other.iePatch)
    {
      return false;
    }

    if ((this.rel == null)
        ? (other.rel != null)
        : !this.rel.equals(other.rel))
    {
      return false;
    }

    if ((this.title == null)
        ? (other.title != null)
        : !this.title.equals(other.title))
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
    int hash = 5;

    hash = 37 * hash + ((this.clazz != null)
                        ? this.clazz.hashCode()
                        : 0);
    hash = 37 * hash + ((this.href != null)
                        ? this.href.hashCode()
                        : 0);
    hash = 37 * hash + (this.iePatch
                        ? 1
                        : 0);
    hash = 37 * hash + ((this.rel != null)
                        ? this.rel.hashCode()
                        : 0);
    hash = 37 * hash + ((this.title != null)
                        ? this.title.hashCode()
                        : 0);
    hash = 37 * hash + ((this.type != null)
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

    if (iePatch)
    {
      buffer.append("<!--[if lte IE 7]>\n");
    }

    buffer.append("<link ");

    if (Util.hasContent(type))
    {
      buffer.append(" type=\"").append(type).append("\"");
    }

    if (Util.hasContent(title))
    {
      buffer.append(" title=\"").append(title).append("\"");
    }

    if (Util.hasContent(rel))
    {
      buffer.append(" rel=\"").append(rel).append("\"");
    }

    if (Util.hasContent(clazz))
    {
      buffer.append(" class=\"").append(clazz).append("\"");
    }

    buffer.append(" href=\"").append(href).append("\" />");

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
  public String getType()
  {
    return type;
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
   * @param type
   */
  public void setType(String type)
  {
    this.type = type;
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

  /** Field description */
  private String title;

  /** Field description */
  private String type;
}
