/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
