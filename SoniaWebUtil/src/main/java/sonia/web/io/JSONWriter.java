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

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Sebastian Sdorra
 */
public class JSONWriter implements Closeable, Flushable
{

  /**
   * Constructs ...
   *
   *
   * @param writer
   */
  public JSONWriter(Writer writer)
  {
    this.writer = writer;
  }

  /**
   * Constructs ...
   *
   *
   * @param writer
   * @param indent
   */
  public JSONWriter(Writer writer, boolean indent)
  {
    this.writer = writer;
    this.indent = indent;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws IOException
   */
  public void close() throws IOException
  {
    writer.close();
  }

  /**
   * Method description
   *
   *
   * @param last
   *
   *
   * @return
   * @throws IOException
   */
  public JSONWriter endArray(boolean last) throws IOException
  {
    writer.append("]");

    if (!last)
    {
      writer.append(",");
    }

    if (indent)
    {
      newLine();
    }

    return this;
  }

  /**
   * Method description
   *
   *
   * @param last
   *
   *
   * @return
   * @throws IOException
   */
  public JSONWriter endObject(boolean last) throws IOException
  {
    writer.append("}");

    if (!last)
    {
      writer.append(",");
    }

    if (indent)
    {
      newLine();
    }

    return this;
  }

  /**
   * Method description
   *
   *
   * @throws IOException
   */
  public void flush() throws IOException
  {
    writer.flush();
  }

  /**
   * Method description
   *
   *
   *
   * @return
   * @throws IOException
   */
  public JSONWriter newLine() throws IOException
  {
    writer.append("\n");

    return this;
  }

  /**
   * Method description
   *
   *
   *
   * @return
   * @throws IOException
   */
  public JSONWriter startArray() throws IOException
  {
    writer.append("[");

    return this;
  }

  /**
   * Method description
   *
   *
   *
   * @return
   * @throws IOException
   */
  public JSONWriter startObject() throws IOException
  {
    writer.append("{");

    return this;
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   * @param last
   *
   *
   * @return
   * @throws IOException
   */
  public JSONWriter write(String key, String value, boolean last)
          throws IOException
  {
    writer.append("\"").append(key).append("\": ");

    if (value != null)
    {
      writer.append("\"").append(value).append("\"");
    }
    else
    {
      writer.append("null");
    }

    if (!last)
    {
      writer.append(",");
    }

    return this;
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   * @param last
   *
   *
   * @return
   * @throws IOException
   */
  public JSONWriter write(String key, int value, boolean last)
          throws IOException
  {
    writer.append("\"").append(key).append("\": ");
    writer.append(Integer.toString(value));

    if (!last)
    {
      writer.append(",");
    }

    return this;
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   * @param last
   *
   *
   * @return
   * @throws IOException
   */
  public JSONWriter write(String key, long value, boolean last)
          throws IOException
  {
    writer.append("\"").append(key).append("\": ");
    writer.append(Long.toString(value));

    if (!last)
    {
      writer.append(",");
    }

    return this;
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   * @param last
   *
   *
   * @return
   * @throws IOException
   */
  public JSONWriter write(String key, boolean value, boolean last)
          throws IOException
  {
    writer.append("\"").append(key).append("\": ");
    writer.append(Boolean.toString(value));

    if (!last)
    {
      writer.append(",");
    }

    return this;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Writer getWriter()
  {
    return writer;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private boolean indent = false;

  /** Field description */
  private Writer writer;
}
