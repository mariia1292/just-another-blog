/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.mapping.MappingHandler;
import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class SimpleMapping implements MappingHandler
{

  /**
   * Constructs ...
   *
   *
   * @param mappingName
   * @param mapping
   */
  public SimpleMapping(String mappingName, String mapping)
  {
    this.mapping = mapping;
    this.mappingName = mappingName;
  }

  //~--- methods --------------------------------------------------------------

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
  public String handleMapping(FacesContext context, Blog blog, String[] args)
  {
    return mapping;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getMappingName()
  {
    return mappingName;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String mapping;

  /** Field description */
  private String mappingName;
}
