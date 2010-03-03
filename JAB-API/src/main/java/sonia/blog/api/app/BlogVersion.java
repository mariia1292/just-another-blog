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

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sonia.blog.api.exception.BlogException;

import sonia.util.Util;
import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.text.SimpleDateFormat;

import java.util.Date;

/**
 *
 * @author Sebastian Sdorra
 */
public class BlogVersion
{

  /**
   * Constructs ...
   *
   *
   * @param file
   */
  public BlogVersion(File file)
  {
    try
    {
      Document document = XmlUtil.buildDocument(file);
      NodeList children = document.getDocumentElement().getChildNodes();

      for (int i = 0; i < children.getLength(); i++)
      {
        Node node = children.item(i);
        String name = node.getNodeName();
        String value = node.getTextContent();

        if (Util.isNotEmpty(value) &&!value.trim().startsWith("$"))
        {
          if ("display-version".equalsIgnoreCase(name))
          {
            displayVersion = value;
          }
          else if ("revision".equalsIgnoreCase(name))
          {
            revision = Integer.valueOf(value);
          }
          else if ("build-date".equalsIgnoreCase(name))
          {
            SimpleDateFormat dateFormat =
              new SimpleDateFormat("yyyy-MM-dd HH:mm");

            buildDate = dateFormat.parse(value);
          }
        }
      }
    }
    catch (Exception ex)
    {
      throw new BlogException(ex);
    }
  }

  /**
   * Constructs ...
   *
   *
   * @param revision
   * @param displayVersion
   * @param buildDate
   */
  public BlogVersion(int revision, String displayVersion, Date buildDate)
  {
    this.revision = revision;
    this.displayVersion = displayVersion;
    this.buildDate = buildDate;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Date getBuildDate()
  {
    return buildDate;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDisplayVersion()
  {
    return displayVersion;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getRevision()
  {
    return revision;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Date buildDate;

  /** Field description */
  private String displayVersion;

  /** Field description */
  private int revision;
}
