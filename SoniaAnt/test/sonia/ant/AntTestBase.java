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

import org.apache.tools.ant.Location;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Sebastian Sdorra
 */
public class AntTestBase
{

  /**
   * Method description
   *
   */
  protected void close()
  {
    if ((baseDir != null) && baseDir.exists())
    {
      try
      {
        delete(baseDir);
      }
      catch (Throwable th)
      {
        fail(th.getLocalizedMessage());
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param resource
   * @param out
   *
   * @throws IOException
   */
  protected void copy(String resource, File out) throws IOException
  {
    InputStream in = null;

    try
    {
      in = AntTestBase.class.getResourceAsStream(resource);
      copy(in, out);
    }
    finally
    {
      if (in != null)
      {
        in.close();
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   *
   * @throws IOException
   */
  protected void copy(InputStream in, File out) throws IOException
  {
    FileOutputStream fos = null;

    try
    {
      fos = new FileOutputStream(out);
      copy(in, fos);
    }
    finally
    {
      if (fos != null)
      {
        fos.close();
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   *
   * @throws IOException
   */
  protected void copy(InputStream in, OutputStream out) throws IOException
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
   * @param files
   *
   * @return
   */
  protected FileSet createFileSet(File... files)
  {
    FileSet fs = new FileSet();

    fs.setDir(baseDir);

    for (File f : files)
    {
      fs.appendIncludes(new String[] { f.getName() });
    }

    return fs;
  }

  /**
   * Method description
   *
   *
   * @param suffix
   *
   * @return
   */
  protected File createTempFile(String suffix)
  {
    return new File(baseDir, System.nanoTime() + suffix);
  }

  /**
   * Method description
   *
   *
   * @param file
   */
  protected void delete(File file)
  {
    if (file.isDirectory())
    {
      File[] children = file.listFiles();

      if (children != null)
      {
        for (File child : children)
        {
          delete(child);
        }
      }
    }

    if (!file.delete())
    {
      throw new RuntimeException("could not delete file " + file.getPath());
    }
  }

  /**
   * Method description
   *
   *
   * @param task
   */
  protected void init(Task task)
  {
    task.setProject(new Project());

    try
    {
      baseDir = File.createTempFile("sonia-ant-", ".junit");

      if (!baseDir.delete())
      {
        fail("cant delete temp file " + baseDir.getPath());
      }

      if (!baseDir.mkdir())
      {
        fail("cant create base dir " + baseDir.getPath());
      }

      task.setLocation(new Location(baseDir.getPath()));
    }
    catch (IOException ex)
    {
      fail(ex.getLocalizedMessage());
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected File baseDir;
}