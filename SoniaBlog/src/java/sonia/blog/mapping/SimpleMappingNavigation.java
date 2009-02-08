/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.mapping.MappingNavigation;
import sonia.blog.entity.PermaObject;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.text.MessageFormat;

/**
 *
 * @author sdorra
 */
public class SimpleMappingNavigation implements MappingNavigation
{

  /**
   * Constructs ...
   *
   *
   * @param previousUri
   * @param nextUri
   */
  public SimpleMappingNavigation(String previousUri, String nextUri)
  {
    this.previousUri = previousUri;
    this.nextUri = nextUri;
  }

  /**
   * Constructs ...
   *
   *
   *
   * @param previousUri
   * @param nextUri
   * @param detailPattern
   */
  public SimpleMappingNavigation(String previousUri, String nextUri,
                                 String detailPattern)
  {
    this.previousUri = previousUri;
    this.nextUri = nextUri;
    this.detailPattern = detailPattern;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  public String getDetailUri(PermaObject object)
  {
    String result = null;

    if (!Util.isBlank(detailPattern))
    {
      result = MessageFormat.format(detailPattern, object.getId());
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getNextUri()
  {
    return nextUri;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getPreviousUri()
  {
    return previousUri;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String detailPattern;

  /** Field description */
  private String nextUri;

  /** Field description */
  private String previousUri;
}
