/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.search;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.ContentObject;

/**
 *
 * @author sdorra
 */
public interface SearchEntry extends ContentObject
{

  /**
   * Method description
   *
   *
   * @return
   */
  public ContentObject getData();
}
