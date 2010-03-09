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



package sonia.blog.server;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

/**
 *
 * @author Sebastian Sdorra
 */
public class BlogServerConfig
{

  /**
   * Constructs ...
   *
   *
   * @param resourceDirectory
   * @param port
   * @param contextPath
   */
  public BlogServerConfig(File resourceDirectory, int port, String contextPath)
  {
    this.resourceDirectory = resourceDirectory;
    this.port = port;
    this.contextPath = contextPath;
    this.webappDirectory = new File(resourceDirectory, "webapp");

    if (!webappDirectory.exists() &&!webappDirectory.mkdirs())
    {
      throw new BlogServerConfigException("could not create directory");
    }

    File tempDirectory = new File(webappDirectory, "temp");

    if (!tempDirectory.exists() &&!tempDirectory.mkdirs())
    {
      throw new BlogServerConfigException("could not create directory");
    }

    webapp = new File(webappDirectory, "jab.war");
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getContextPath()
  {
    return contextPath;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getPort()
  {
    return port;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public File getResourceDirectory()
  {
    return resourceDirectory;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public File getWebapp()
  {
    return webapp;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String contextPath;

  /** Field description */
  private int port;

  /** Field description */
  private File resourceDirectory;

  /** Field description */
  private File webapp;

  /** Field description */
  private File webappDirectory;
}
