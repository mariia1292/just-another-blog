/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

/**
 *
 * @author sdorra
 */
public interface SelectAction
{

  /**
   * Method description
   *
   *
   * @return
   */
  public String getLable();

  /**
   * Method description
   *
   *
   * @param request
   * @param param
   *
   * @return
   */
  public String getOutput(BlogRequest request, Map<String, ?> param);
}
