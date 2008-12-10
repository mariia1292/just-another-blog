/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.net;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class FileNameMap implements java.net.FileNameMap
{

  /** Field description */
  private static final String RESOURCE = "/sonia/net/mimetypes.properties";

  /** Field description */
  private static Logger logger = Logger.getLogger(FileNameMap.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public FileNameMap()
  {
    this.mimeTypes = new Properties();

    InputStream in = FileNameMap.class.getResourceAsStream(RESOURCE);

    if (in != null)
    {
      try
      {
        this.mimeTypes.load(in);
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }
  }

  /**
   * Constructs ...
   *
   *
   * @param mimeTypes
   */
  public FileNameMap(Properties mimeTypes)
  {
    this.mimeTypes = mimeTypes;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param fileName
   *
   * @return
   */
  public String getContentTypeFor(String fileName)
  {
    String mimeType = null;
    int index = fileName.indexOf(".");

    if ((index > 0) && (index < fileName.length()))
    {
      mimeType = mimeTypes.getProperty(fileName.substring(index));
    }

    if ((mimeType == null) || (mimeType.trim().length() == 0))
    {
      mimeType = "application/octet-stream";
    }

    return mimeType;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Properties mimeTypes;
}
