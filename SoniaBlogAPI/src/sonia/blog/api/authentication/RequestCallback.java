/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.authentication;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;

//~--- JDK imports ------------------------------------------------------------

import javax.security.auth.callback.Callback;

/**
 *
 * @author sdorra
 */
public class RequestCallback implements Callback
{

  /**
   * Method description
   *
   *
   * @return
   */
  public BlogRequest getRequest()
  {
    return request;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   */
  public void setRequest(BlogRequest request)
  {
    this.request = request;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private BlogRequest request;
}
