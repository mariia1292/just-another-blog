/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.authentication;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogResponse;

//~--- JDK imports ------------------------------------------------------------

import javax.security.auth.callback.Callback;

/**
 *
 * @author sdorra
 */
public class ResponseCallback implements Callback
{

  /**
   * Method description
   *
   *
   * @return
   */
  public BlogResponse getResponse()
  {
    return response;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param response
   */
  public void setResponse(BlogResponse response)
  {
    this.response = response;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private BlogResponse response;
}
