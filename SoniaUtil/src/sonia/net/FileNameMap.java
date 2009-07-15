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


package sonia.net;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
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
      mimeType = mimeTypes.getProperty(fileName.substring(index).toLowerCase());
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