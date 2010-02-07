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



package sonia.maven;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;

/**
 * @goal combine
 * @author Sebastian Sdorra
 */
public class CombineMojo extends AbstractMojo
{

  /**
   * Method description
   *
   *
   * @throws MojoExecutionException
   * @throws MojoFailureException
   */
  public void execute() throws MojoExecutionException, MojoFailureException
  {
    List<File> files = MavenUtil.getFiles(combineBasePath, combineIncludes,
                         combineExcludes);
    FileOutputStream fos = null;
    FileInputStream fis = null;

    try
    {
      if (combineOutput.exists())
      {
        if (!combineOutput.delete())
        {
          throw new MojoFailureException("could not delete "
                                         + combineOutput.getPath());
        }
      }

      fos = new FileOutputStream(combineOutput);

      for (File file : files)
      {
        try
        {
          fis = new FileInputStream(file);

          byte[] data = new byte[fis.available()];

          fis.read(data);
          fos.write(data);
        }
        finally
        {
          if (fis != null)
          {
            fis.close();
          }
        }
      }
    }
    catch (IOException ex)
    {
      getLog().error(ex);

      throw new MojoFailureException(ex.getMessage());
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
          getLog().error(ex);
        }
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /**
   *
   * @parameter
   */
  private File combineBasePath;

  /**
   *
   * @parameter
   */
  private String[] combineExcludes;

  /**
   *
   * @parameter
   */
  private String[] combineIncludes;

  /**
   *
   * @parameter
   */
  private File combineOutput;
}
