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



package sonia.blog.scripting;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.api.mapping.MappingConfig;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
@MappingConfig(compressable = true, regex = "^/script/([0-9A-Za-z]+)$")
public class ScriptingMapping extends FinalMapping
{

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void handleFinalMapping(BlogRequest request, BlogResponse response,
                                    String[] param)
          throws IOException, ServletException
  {
    if ((param != null) && (param.length > 0))
    {
      String action = param[0];

      if ("syntax".equals(action))
      {
        renderGetSyntax(request, response);
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @throws IOException
   */
  private void renderGetSyntax(BlogRequest request, BlogResponse response)
          throws IOException
  {
    String lang = request.getParameter("lang");

    if (Util.isNotEmpty(lang))
    {
      String syntax = ScriptingContext.getInstance().getSyntax(lang);

      if (Util.isNotEmpty(syntax))
      {
        response.setContentType("text/plain");

        PrintWriter writer = response.getWriter();

        try
        {
          writer.print(syntax);
        }
        finally
        {
          if (writer != null)
          {
            writer.close();
          }
        }
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }
}
