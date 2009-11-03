/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://kenai.com/projects/jab
 *
 */



package sonia.web.msg;

//~--- JDK imports ------------------------------------------------------------

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Sebastian Sdorra
 */
public abstract class MessageHandler
{

  /**
   * Constructs ...
   *
   *
   * @param bundle
   */
  public MessageHandler(ResourceBundle bundle)
  {
    this.bundle = bundle;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param key
   */
  public abstract void error(HttpServletRequest request, String key);

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param clientId
   * @param key
   */
  public abstract void error(HttpServletRequest request, String clientId,
                             String key);

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param clientId
   * @param key
   * @param detailKey
   */
  public abstract void error(HttpServletRequest request, String clientId,
                             String key, String detailKey);

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param clientId
   * @param key
   * @param detailKey
   * @param params
   */
  public abstract void error(HttpServletRequest request, String clientId,
                             String key, String detailKey, Object... params);

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param clientId
   * @param key
   * @param detailKey
   * @param params
   */
  public abstract void fatal(HttpServletRequest request, String clientId,
                             String key, String detailKey, Object... params);

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param key
   */
  public abstract void fatal(HttpServletRequest request, String key);

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param clientId
   * @param key
   */
  public abstract void fatal(HttpServletRequest request, String clientId,
                             String key);

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param clientId
   * @param key
   * @param detailKey
   */
  public abstract void fatal(HttpServletRequest request, String clientId,
                             String key, String detailKey);

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param clientId
   * @param key
   * @param detailKey
   * @param params
   */
  public abstract void info(HttpServletRequest request, String clientId,
                            String key, String detailKey, Object... params);

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param key
   */
  public abstract void info(HttpServletRequest request, String key);

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param clientId
   * @param key
   */
  public abstract void info(HttpServletRequest request, String clientId,
                            String key);

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param clientId
   * @param key
   * @param detailKey
   */
  public abstract void info(HttpServletRequest request, String clientId,
                            String key, String detailKey);

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param clientId
   * @param key
   * @param detailKey
   * @param params
   */
  public abstract void warn(HttpServletRequest request, String clientId,
                            String key, String detailKey, Object... params);

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param key
   */
  public abstract void warn(HttpServletRequest request, String key);

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param clientId
   * @param key
   */
  public abstract void warn(HttpServletRequest request, String clientId,
                            String key);

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param clientId
   * @param key
   * @param detailKey
   */
  public abstract void warn(HttpServletRequest request, String clientId,
                            String key, String detailKey);

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected ResourceBundle bundle;
}
