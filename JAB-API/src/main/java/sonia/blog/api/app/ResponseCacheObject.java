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

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public class ResponseCacheObject
{

  /**
   * Constructs ...
   *
   */
  public ResponseCacheObject()
  {
    headers = new ArrayList<Header>();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param name
   * @param value
   */
  public void addDateHeader(String name, long value)
  {
    headers.add(new Header(name, value));
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param value
   */
  public void addHeader(String name, String value)
  {
    headers.add(new Header(name, value));
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param value
   */
  public void addIntHeader(String name, int value)
  {
    headers.add(new Header(name, value));
  }

  /**
   * Method description
   *
   *
   * @param response
   *
   * @throws IOException
   */
  public void apply(BlogResponse response) throws IOException
  {
    if (contentType != null)
    {
      response.setContentType(contentType);
    }

    if (Util.hasContent(headers))
    {
      for (Header header : headers)
      {
        switch (header.getType())
        {
          case Header.TYPE_STRING :
            response.addHeader(header.getName(), header.getStringValue());

            break;

          case Header.TYPE_INT :
            response.addIntHeader(header.getName(), header.getIntValue());

            break;

          case Header.TYPE_DATE :
            response.addDateHeader(header.getName(), header.getDateValue());

            break;
        }
      }
    }

    if (content != null)
    {
      response.getOutputStream().write(content);
    }
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param content
   */
  public void setContent(byte[] content)
  {
    this.content = content;
  }

  /**
   * Method description
   *
   *
   * @param contentType
   */
  public void setContentType(String contentType)
  {
    this.contentType = contentType;
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/09/06
   * @author         Enter your name here...
   */
  private static class Header
  {

    /** Field description */
    private static final int TYPE_DATE = 2;

    /** Field description */
    private static final int TYPE_INT = 1;

    /** Field description */
    private static final int TYPE_STRING = 0;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     *
     * @param name
     * @param value
     */
    public Header(String name, int value)
    {
      this.name = name;
      this.intValue = value;
      this.type = TYPE_INT;
    }

    /**
     * Constructs ...
     *
     *
     * @param name
     * @param value
     */
    public Header(String name, long value)
    {
      this.name = name;
      this.dateValue = value;
      this.type = TYPE_DATE;
    }

    /**
     * Constructs ...
     *
     *
     * @param name
     * @param value
     */
    public Header(String name, String value)
    {
      this.name = name;
      this.stringValue = value;
      this.type = TYPE_STRING;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public long getDateValue()
    {
      return dateValue;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public int getIntValue()
    {
      return intValue;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getName()
    {
      return name;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getStringValue()
    {
      return stringValue;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public int getType()
    {
      return type;
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private long dateValue;

    /** Field description */
    private int intValue;

    /** Field description */
    private String name;

    /** Field description */
    private String stringValue;

    /** Field description */
    private int type;
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private byte[] content;

  /** Field description */
  private String contentType;

  /** Field description */
  private List<Header> headers;
}
