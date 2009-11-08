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



package sonia.ant;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Sebastian Sdorra
 */
public class MercurialChangeLogTask extends Task
{

  /**
   * Method description
   *
   *
   * @throws BuildException
   */
  @Override
  public void execute() throws BuildException
  {
    StringBuffer buffer = new StringBuffer();

    if (hgPath != null)
    {
      buffer.append(hgPath);
    }
    else
    {
      buffer.append("hg");
    }

    buffer.append(" log");

    if (repositoryPath != null)
    {
      buffer.append(" -R ").append(repositoryPath);
    }

    buffer.append(" --template ");
    buffer.append("<revision><nr>{rev}</nr><tags>{tags}</tags>");
    buffer.append("<message>{desc|escape}</message></revision>");
    execute(buffer.toString());
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param hgPath
   */
  public void setHgPath(String hgPath)
  {
    this.hgPath = hgPath;
  }

  /**
   * Method description
   *
   *
   * @param path
   */
  public void setPath(String path)
  {
    this.path = path;
  }

  /**
   * Method description
   *
   *
   * @param repositoryPath
   */
  public void setRepositoryPath(String repositoryPath)
  {
    this.repositoryPath = repositoryPath;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   *
   * @throws IOException
   */
  private void copy(InputStream in, OutputStream out) throws IOException
  {
    byte[] buffer = new byte[0xFFFF];

    for (int len; (len = in.read(buffer)) != -1; )
    {
      out.write(buffer, 0, len);
    }
  }

  /**
   * Method description
   *
   *
   * @param cmd
   */
  private void execute(String cmd)
  {
    InputStream in = null;

    try
    {
      Process p = Runtime.getRuntime().exec(cmd);
      log( cmd );

        in = p.getInputStream();
        writeChangeLog(in);
    }
    catch (IOException ex)
    {
      throw new BuildException(ex);
    }
    finally
    {
      if (in != null)
      {
        try
        {
          in.close();
        }
        catch (IOException ex)
        {
          ex.printStackTrace();
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param in
   */
  private void writeChangeLog(InputStream in)
  {
    FileOutputStream fos = null;

    try
    {
      log("write to " + path);
      fos = new FileOutputStream(path);
      fos.write(
          "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<changelog>".getBytes());
      copy(in, fos);
      fos.write("</changelog>".getBytes());
    }
    catch (IOException ex)
    {
      throw new BuildException(ex);
    }
    finally
    {
      if (fos != null)
      {
        try
        {
          fos.close();
        }
        catch (IOException ex)
        {
          ex.printStackTrace();
        }
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String hgPath;

  /** Field description */
  private String path;

  /** Field description */
  private String repositoryPath;
}
