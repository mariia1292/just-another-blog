/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webstart.repository;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.util.Date;

/**
 *
 * @author Sebastian Sdorra
 */
public interface FileObject
{

  /**
   * Method description
   *
   *
   * @return
   */
  public InputStream getInputStream() throws IOException;

  /**
   * Method description
   *
   *
   * @return
   */
  public Date getLastModifiedDate();

  /**
   * Method description
   *
   *
   * @return
   */
  public int getLength();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getMimeType();
}
