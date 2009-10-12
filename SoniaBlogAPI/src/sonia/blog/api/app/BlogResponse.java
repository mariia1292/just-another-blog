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

import sonia.io.TeeWriter;

import sonia.web.io.TeeServletOutputStream;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 *
 * @author Sebastian Sdorra
 */
public class BlogResponse extends HttpServletResponseWrapper
{

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
    if (cacheEnabled)
    {
      cacheObject.addDateHeader(name, date);
    }

    super.addDateHeader(name, date);
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
    if (cacheEnabled)
    {
      cacheObject.addHeader(name, value);
    }

    super.addHeader(name, value);
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
    if (cacheEnabled)
    {
      cacheObject.addIntHeader(name, value);
    }

    super.addIntHeader(name, value);
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
    if (cacheEnabled && ((cacheWriter != null) || (cacheStream != null)))
    {
      try
      {
        if (cacheStream != null)
        {
          stream.flush();
          cacheObject.setContent(cacheStream.toByteArray());
        }
        else
        {
          writer.flush();
          cacheObject.setContent(cacheWriter.toString().getBytes("UTF-8"));
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
    ServletOutputStream result = null;

    if (cacheEnabled)
    {
      if (stream != null)
      {
        result = stream;
      }
      else
      {
        cacheStream = new ByteArrayOutputStream();
        stream = new TeeServletOutputStream(super.getOutputStream(),
                cacheStream);
        result = stream;
      }
    }
    else
    {
      result = super.getOutputStream();
    }

    return result;
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
    PrintWriter result = null;

    if (cacheEnabled)
    {
      if (writer != null)
      {
        result = writer;
      }
      else
      {
        cacheWriter = new StringWriter();
        writer = new PrintWriter(new TeeWriter(super.getWriter(), cacheWriter));
        result = writer;
      }
    }
    else
    {
      result = super.getWriter();
    }

    return result;
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
  private boolean cacheEnabled;

  /** Field description */
  private ResponseCacheObject cacheObject;

  /** Field description */
  private ByteArrayOutputStream cacheStream;

  /** Field description */
  private StringWriter cacheWriter;

  /** Field description */
  private int statusCode = HttpServletResponse.SC_OK;

  /** Field description */
  private ServletOutputStream stream;

  /** Field description */
  private PrintWriter writer;
}
