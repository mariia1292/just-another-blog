/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

//~--- JDK imports ------------------------------------------------------------

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 *
 * @author sdorra
 */
public class BlogResponse extends HttpServletResponseWrapper
{

  /**
   * Constructs ...
   *
   *
   * @param response
   */
  public BlogResponse(HttpServletResponse response)
  {
    super(response);
  }
}
