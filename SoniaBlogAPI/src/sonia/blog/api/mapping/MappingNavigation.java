/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.PermaObject;

/**
 *
 * @author sdorra
 */
public interface MappingNavigation
{

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  public String getDetailUri(PermaObject object);

  /**
   * Method description
   *
   *
   * @return
   */
  public String getNextUri();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getPreviousUri();
}
