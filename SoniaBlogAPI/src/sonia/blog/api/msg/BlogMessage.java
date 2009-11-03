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



package sonia.blog.api.msg;

/**
 *
 * @author Sebastian Sdorra
 */
public class BlogMessage
{

  /** Field description */
  public static final int LEVEL_ERROR = 2;

  /** Field description */
  public static final int LEVEL_FATAL = 3;

  /** Field description */
  public static final int LEVEL_INFO = 0;

  /** Field description */
  public static final int LEVEL_WARN = 1;

  /** Field description */
  public static final String SESSION_VAR = "sonia.blog.msg.store";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param level
   * @param clientId
   * @param summary
   * @param detail
   */
  public BlogMessage(int level, String clientId, String summary, String detail)
  {
    this.level = level;
    this.clientId = clientId;
    this.summary = summary;
    this.detail = detail;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getClientId()
  {
    return clientId;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDetail()
  {
    return detail;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getLevel()
  {
    return level;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSummary()
  {
    return summary;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String clientId;

  /** Field description */
  private String detail;

  /** Field description */
  private int level;

  /** Field description */
  private String summary;
}
