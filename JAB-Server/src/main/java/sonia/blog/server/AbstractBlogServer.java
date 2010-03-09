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

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.ChecksumUtil;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Sebastian Sdorra
 */
public abstract class AbstractBlogServer implements BlogServer
{

  /**
   * Method description
   *
   *
   *
   * @throws IOException
   */
  public void deploy() throws IOException
  {
    File webapp = config.getWebapp();

    if (webapp.exists())
    {
      if (isNew(webapp))
      {
        updateWebApp(webapp);
      }
    }
    else
    {
      copyWebApp(webapp);
    }
  }

  /**
   * Method description
   *
   *
   * @param config
   */
  @Override
  public void init(BlogServerConfig config)
  {
    try
    {
      this.config = config;
      deploy();
    }
    catch (IOException ex)
    {
      throw new BlogServerConfigException(ex);
    }
  }

  /**
   * Method description
   *
   *
   * @param warFile
   *
   * @throws IOException
   */
  private void copyWebApp(File warFile) throws IOException
  {
    InputStream in = null;
    FileOutputStream out = null;

    try
    {
      in = getWebApp();
      out = new FileOutputStream(warFile);
      Util.copy(in, out);
    }
    finally
    {
      if (in != null)
      {
        in.close();
      }

      if (out != null)
      {
        out.close();
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param warFile
   *
   * @throws IOException
   */
  private void updateWebApp(File warFile) throws IOException
  {
    if (!warFile.delete())
    {
      throw new IOException("could not delete war-file");
    }

    copyWebApp(warFile);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private InputStream getWebApp()
  {
    return AbstractBlogServer.class.getResourceAsStream("/webapp/jab.war");
  }

  /**
   * Method description
   *
   *
   * @param warFile
   *
   * @return
   *
   * @throws IOException
   */
  private boolean isNew(File warFile) throws IOException
  {
    String checksum = ChecksumUtil.createChecksum(warFile);
    String newChecksum = ChecksumUtil.createChecksum(getWebApp());

    return !checksum.equals(newChecksum);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected BlogServerConfig config;
}
