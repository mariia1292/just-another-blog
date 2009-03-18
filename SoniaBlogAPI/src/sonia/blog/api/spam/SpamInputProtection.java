/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.spam;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.ResponseWriter;

/**
 *
 * @author sdorra
 */
public interface SpamInputProtection extends Serializable
{

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param writer
   *
   * @return
   *
   * @throws IOException
   */
  public String renderInput(BlogRequest request, ResponseWriter writer)
          throws IOException;

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getLabel();
}
