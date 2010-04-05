/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webstart.repository;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URLConnection;

import java.util.Date;

/**
 *
 * @author Sebastian Sdorra
 */
public class DefaultFileObject implements FileObject
{

  /**
   * Constructs ...
   *
   *
   * @param file
   */
  public DefaultFileObject(File file)
  {
    this.file = file;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws IOException
   */
  public InputStream getInputStream() throws IOException
  {
    return new FileInputStream(file);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Date getLastModifiedDate()
  {
    return new Date(file.lastModified());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getLength()
  {
    return (int) file.length();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getMimeType()
  {
    return URLConnection.getFileNameMap().getContentTypeFor(file.getName());
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private File file;
}
