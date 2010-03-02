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



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogSession;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Role;

/**
 *
 * @author Sebastian Sdorra
 */
public class ErrorBean extends AbstractBean
{

  /** Field description */
  public static final String NAME = "ErrorBean";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public ErrorBean()
  {
    init();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDescription()
  {
    return description;
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
  public String getStacktrace()
  {
    return stacktrace;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isGlobalAdmin()
  {
    boolean result = false;
    BlogSession session = getBlogSession();

    if (session != null)
    {
      result = session.hasRole(Role.GLOBALADMIN);
    }

    return result;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param description
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Method description
   *
   *
   * @param message
   */
  public void setMessage(String message)
  {
    this.message = message;
  }

  /**
   * Method description
   *
   *
   * @param stacktrace
   */
  public void setStacktrace(String stacktrace)
  {
    this.stacktrace = stacktrace;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String description;

  /** Field description */
  private String message;

  /** Field description */
  private String stacktrace;
}
