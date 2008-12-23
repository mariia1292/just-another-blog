/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;

/**
 *
 * @author sdorra
 */
public interface MappingHandler
{

  /**
   * Method description
   *
   *
   * @param path
   * @param entry
   */
  public void addMappging(String path, MappingEntry entry);

  /**
   * Method description
   *
   *
   * @param path
   *
   * @return
   */
  public boolean containsPath(String path);

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @return
   */
  public boolean handleMapping(BlogRequest request, BlogResponse response);

  /**
   * Method description
   *
   *
   * @param path
   */
  public void removeMapping(String path);
}
