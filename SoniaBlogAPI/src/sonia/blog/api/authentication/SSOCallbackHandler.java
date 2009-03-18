/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.authentication;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 *
 * @author sdorra
 */
public class SSOCallbackHandler implements CallbackHandler
{

  /**
   * Constructs ...
   *
   *
   * @param request
   * @param response
   */
  public SSOCallbackHandler(BlogRequest request, BlogResponse response)
  {
    this.request = request;
    this.response = response;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param callbacks
   *
   * @throws IOException
   * @throws UnsupportedCallbackException
   */
  public void handle(Callback[] callbacks)
          throws IOException, UnsupportedCallbackException
  {
    for (Callback callback : callbacks)
    {
      if (callback instanceof RequestCallback)
      {
        ((RequestCallback) callback).setRequest(request);
      }
      else if (callback instanceof ResponseCallback)
      {
        ((ResponseCallback) callback).setResponse(response);
      }
      else
      {
        throw new UnsupportedCallbackException(callback);
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private BlogRequest request;

  /** Field description */
  private BlogResponse response;
}
