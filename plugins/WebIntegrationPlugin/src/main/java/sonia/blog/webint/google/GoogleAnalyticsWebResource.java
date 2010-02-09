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



package sonia.blog.webint.google;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.exception.BlogException;
import sonia.blog.api.macro.WebResource;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class GoogleAnalyticsWebResource extends WebResource
{

  /** Field description */
  public static final String PARAMETER = "sonia.webint.googleanalytics.code";

  /** Field description */
  public static final String REGEX = "\\$\\{code\\}";

  /** Field description */
  public static final String TEMPLATE =
    "/sonia/blog/webint/google-analytics.html";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param index
   */
  public GoogleAnalyticsWebResource(int index)
  {
    super(index);

    InputStream in =
      GoogleAnalyticsWebResource.class.getResourceAsStream(TEMPLATE);

    try
    {
      template = Util.getContent(in);
    }
    catch (IOException ex)
    {
      throw new BlogException(ex);
    }
    finally
    {
      if (in != null)
      {
        try
        {
          in.close();
        }
        catch (IOException ex)
        {
          Logger.getLogger(GoogleAnalyticsWebResource.class.getName()).log(
              Level.SEVERE, null, ex);
        }
      }
    }
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  @Override
  public String toHTML(BlogRequest request)
  {
    String result = "";
    String code = BlogContext.getDAOFactory().getBlogDAO().getParameter(
                      request.getCurrentBlog(), PARAMETER);

    if (Util.isNotEmpty(code))
    {
      result = template.replaceFirst(REGEX, code);
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String template;
}
