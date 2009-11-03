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



package sonia.web.io;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
public class ExtendedServletOuputStream extends ServletOutputStream
{

  /**
   * Constructs ...
   *
   *
   * @param response
   * @param cache
   * @param compress
   *
   * @throws IOException
   */
  public ExtendedServletOuputStream(HttpServletResponse response,
                                    boolean cache, boolean compress)
          throws IOException
  {
    this(response, response.getOutputStream(), cache, compress);
  }

  /**
   * Constructs ...
   *
   *
   *
   * @param response
   * @param stream
   * @param cache
   * @param compress
   *
   * @throws IOException
   */
  public ExtendedServletOuputStream(HttpServletResponse response,
                                    ServletOutputStream stream, boolean cache,
                                    boolean compress)
          throws IOException
  {
    this.response = response;
    this.stream = stream;
    this.closed = false;

    if (cache || compress)
    {
      cachedStream = new ByteArrayOutputStream();

      if (compress)
      {
        compressedStream = new GZIPOutputStream(cachedStream);
      }
    }
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws IOException
   */
  @Override
  public void close() throws IOException
  {
    if (!closed)
    {
      closed = true;

      if (compressedStream != null)
      {
        compressedStream.finish();
        response.addHeader("Content-Encoding", "gzip");
      }

      if (cachedStream != null)
      {
        cachedStream.flush();
        content = cachedStream.toByteArray();
        response.addIntHeader("Content-Length", content.length);
        stream.write(content);
      }

      stream.close();
    }
  }

  /**
   * Method description
   *
   *
   * @throws IOException
   */
  @Override
  public void flush() throws IOException
  {
    if (compressedStream != null)
    {
      compressedStream.flush();
    }
    else if (cachedStream != null)
    {
      cachedStream.flush();
    }
    else
    {
      stream.flush();
    }
  }

  /**
   * Method description
   *
   *
   * @param b
   *
   * @throws IOException
   */
  @Override
  public void write(int b) throws IOException
  {
    if (closed)
    {
      throw new IOException("Cannot write to a closed output stream");
    }

    if (compressedStream != null)
    {
      compressedStream.write(b);
    }
    else if (cachedStream != null)
    {
      cachedStream.write(b);
    }
    else
    {
      stream.write(b);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public byte[] getContent()
  {
    return content;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ByteArrayOutputStream cachedStream;

  /** Field description */
  private boolean closed;

  /** Field description */
  private GZIPOutputStream compressedStream;

  /** Field description */
  private byte[] content;

  /** Field description */
  private HttpServletResponse response;

  /** Field description */
  private ServletOutputStream stream;
}
