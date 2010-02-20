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



package sonia.blog.util;

/**
 *
 * @author Sebastian Sdorra
 */
public class ErrorObject
{

  /**
   * Constructs ...
   *
   *
   * @param statusCode
   * @param message
   * @param exception
   * @param requestUri
   */
  public ErrorObject(int statusCode, String message, Throwable exception,
                     String requestUri)
  {
    this.statusCode = statusCode;
    this.message = message;
    this.exception = exception;
    this.requestUri = requestUri;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Throwable getException()
  {
    return exception;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getMessage()
  {
    return message;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getRequestUri()
  {
    return requestUri;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getStatusCode()
  {
    return statusCode;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Throwable exception;

  /** Field description */
  private String message;

  /** Field description */
  private String requestUri;

  /** Field description */
  private int statusCode;

  public static final String NAME = "ErrorObject";
}
