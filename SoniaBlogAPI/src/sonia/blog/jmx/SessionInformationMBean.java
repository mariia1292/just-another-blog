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


package sonia.blog.jmx;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;

/**
 *
 * @author Sebastian Sdorra
 */
public interface SessionInformationMBean
{

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAttachmentDirectorySize();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getImageDirectorySize();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getIndexDirectorySize();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getOpenSessions();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getPluginCount();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getResourceDirectorySize();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getRuntime();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getServiceCount();

  /**
   * Method description
   *
   *
   * @return
   */
  public Date getStartTime();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalAttachments();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalBlogs();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalCategories();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalComments();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalEntries();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalPages();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalSessions();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalTags();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalUsers();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getVersion();
}