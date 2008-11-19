/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import javax.faces.context.FacesContext;

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
   * @param context
   * @param blog
   * @param args
   *
   * @return
   */
  public String handleMapping(FacesContext context, Blog blog, String[] args);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getMappingName();
}
