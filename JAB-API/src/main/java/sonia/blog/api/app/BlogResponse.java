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



package sonia.blog.api.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.exception.BlogException;

import sonia.web.io.ExtendedServletOuputStream;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 *
 * @author Sebastian Sdorra
 */
public class BlogResponse extends HttpServletResponseWrapper
{

  /** Field description */
  private static final Logger logger =
    Logger.getLogger(BlogResponse.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param response
   */
  public BlogResponse(HttpServletResponse response)
  {
    super(response);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param name
   * @param date
   */
  @Override
  public void addDateHeader(String name, long date)
  {
    setDateHeader(name, date);
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param value
   */
  @Override
  public void addHeader(String name, String value)
  {
    setHeader(name, value);
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param value
   */
  @Override
  public void addIntHeader(String name, int value)
  {
    setIntHeader(name, value);
  }

  /**
   * Method description
   *
   */
  public void finish()
  {
    try
    {
      if (writer != null)
      {
        writer.close();
      }

      if (stream != null)
      {
        stream.close();
      }
    }
    catch (IOException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Method description
   *
   *
   * @param sc
   *
   * @throws IOException
   */
  @Override
  public void sendError(int sc) throws IOException
  {
    statusCode = sc;
    super.sendError(sc);
  }

  /**
   * Method description
   *
   *
   * @param sc
   * @param msg
   *
   * @throws IOException
   */
  @Override
  public void sendError(int sc, String msg) throws IOException
  {
    statusCode = sc;
    super.sendError(sc, msg);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public ResponseCacheObject getCachedObject()
  {
    if (cacheEnabled && (stream != null)
        && (stream instanceof ExtendedServletOuputStream))
    {
      ExtendedServletOuputStream extStream =
        (ExtendedServletOuputStream) stream;

      try
      {
        if (writer != null)
        {
          writer.close();
        }

        extStream.close();

        byte[] content = extStream.getContent();

        if (content != null)
        {
          cacheObject.setContent(content);
        }
      }
      catch (IOException ex)
      {
        throw new BlogException(ex);
      }
    }

    return cacheObject;
  }

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws IOException
   */
  @Override
  public ServletOutputStream getOutputStream() throws IOException
  {
    if (writer != null)
    {
      throw new IllegalStateException("getWriter() has already been called!");
    }

    if (cacheEnabled || compressionEnabled)
    {
      stream = new ExtendedServletOuputStream(this, super.getOutputStream(),
              cacheEnabled, compressionEnabled);
    }
    else
    {

      // stream = super.getOutputStream();
      stream = new ExtendedServletOuputStream(this, super.getOutputStream(),
              false, false);
    }

    return stream;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getStatusCode()
  {
    return statusCode;
  }

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws IOException
   */
  @Override
  public PrintWriter getWriter() throws IOException
  {
    if (writer == null)
    {
      if (stream == null)
      {
        getOutputStream();
      }

      writer = new PrintWriter(new OutputStreamWriter(stream, "UTF-8"));
    }

    return writer;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isCacheEnabled()
  {
    return cacheEnabled;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isCompressionEnabled()
  {
    return compressionEnabled;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param cacheEnabled
   */
  public void setCacheEnabled(boolean cacheEnabled)
  {
    this.cacheEnabled = cacheEnabled;

    if (cacheEnabled)
    {
      cacheObject = new ResponseCacheObject();
    }
    else
    {
      cacheObject = null;
    }
  }

  /**
   * Method description
   *
   *
   * @param compressionEnabled
   */
  public void setCompressionEnabled(boolean compressionEnabled)
  {
    this.compressionEnabled = compressionEnabled;
  }

  /**
   * Method description
   *
   *
   * @param type
   */
  @Override
  public void setContentType(String type)
  {
    if (cacheEnabled)
    {
      cacheObject.setContentType(type);
    }

    super.setContentType(type);
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param date
   */
  @Override
  public void setDateHeader(String name, long date)
  {
    if (cacheEnabled)
    {
      cacheObject.addDateHeader(name, date);
    }

    super.setDateHeader(name, date);
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param value
   */
  @Override
  public void setHeader(String name, String value)
  {
    if (cacheEnabled)
    {
      cacheObject.addHeader(name, value);
    }

    super.setHeader(name, value);
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param value
   */
  @Override
  public void setIntHeader(String name, int value)
  {
    if (cacheEnabled)
    {
      cacheObject.addIntHeader(name, value);
    }

    super.setIntHeader(name, value);
  }

  /**
   * Method description
   *
   *
   * @param sc
   */
  @Override
  public void setStatus(int sc)
  {
    statusCode = sc;
    super.setStatus(sc);
  }

  /**
   * Method description
   *
   *
   * @param sc
   * @param sm
   */
  @Override
  public void setStatus(int sc, String sm)
  {
    statusCode = sc;
    super.setStatus(sc, sm);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private boolean cacheEnabled = false;

  /** Field description */
  private ResponseCacheObject cacheObject;

  /** Field description */
  private boolean compressionEnabled = false;

  /** Field description */
  private int statusCode = HttpServletResponse.SC_OK;

  /** Field description */
  private ServletOutputStream stream;

  /** Field description */
  private PrintWriter writer;
}
