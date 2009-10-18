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



package sonia.blog.spam;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingHandler;
import sonia.blog.api.spam.SpamInputProtection;
import sonia.blog.mapping.CaptchaMapping;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.Writer;

import java.util.Random;

/**
 *
 * @author Sebastian Sdorra
 */
public class CaptchaSpamProtection implements SpamInputProtection
{

  /** Field description */
  public static final String LABEL = "Captcha";

  /** Field description */
  private static final char[] CHARARRAY =
  {
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'i', 'J', 'K', 'L', 'M', 'N', 'o',
    'P', 'Q', 'R', 'S', 'T', 'u', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5',
    '6', '7', '8', '9'
  };

  /** Field description */
  private static final int HEIGHT = 44;

  /** Field description */
  private static final int WIDTH = 134;

  /** Field description */
  private static final Random RANDOM = new Random();

  /** Field description */
  private static final long serialVersionUID = -7729920336258957980L;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public CaptchaSpamProtection()
  {
    MappingHandler handler = BlogContext.getInstance().getMappingHandler();

    if (!handler.contains("^/captcha.jab$"))
    {
      handler.add("^/captcha.jab$", CaptchaMapping.class);
    }
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param writer
   *
   * @return
   *
   * @throws IOException
   */
  public String renderInput(BlogRequest request, Writer writer)
          throws IOException
  {
    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    char[] buffer = new char[6];

    for (int i = 0; i < 6; i++)
    {
      buffer[i] = CHARARRAY[RANDOM.nextInt(CHARARRAY.length)];
    }

    String text = new String(buffer);
    String link = linkBuilder.buildLink(request, "/captcha.jab");

    request.getSession().setAttribute(CaptchaMapping.SESSIONVAR, text);
    writer.append("<img src=\"").append(link).append("\" style=\"");
    writer.append("width: ").append(Integer.toString(WIDTH));
    writer.append("; height: ").append(Integer.toString(HEIGHT));
    writer.append("\" />");

    return text;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getLabel()
  {
    return LABEL;
  }
}
