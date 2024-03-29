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



package sonia.blog.api.macro.browse;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.entity.ContentObject;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

/**
 *
 * @author Sebastian Sdorra
 */
public class SelectWidget extends AbstractBlogMacroWidget
{

  /**
   * Method description
   *
   *
   * @param request
   * @param object
   * @param name
   * @param param
   *
   * @return
   */
  public String getFormElement(BlogRequest request, ContentObject object,
                               String name, String param)
  {
    StringBuffer out = new StringBuffer();
    Map<String, String> params = buildParameterMap(param);

    if ((params != null) &&!params.isEmpty())
    {
      String optionParam = params.get("options");

      if (Util.hasContent(optionParam))
      {
        String[] options = optionParam.split("\\|");

        out.append("<select name=\"").append(name).append("\">");

        if ("true".equalsIgnoreCase(params.get("nullable")))
        {
          out.append("<option value=\"\">---</option>");
        }

        for (String option : options)
        {
          out.append("<option");

          int index = option.indexOf(":");

          if (index > 0)
          {
            out.append(" value=\"").append(option.substring(index + 1));
            out.append("\"");
            option = option.substring(0, index);
          }

          if (option.startsWith("*"))
          {
            out.append(" selected=\"true\"");
            option = option.substring(1);
          }

          out.append(">").append(option).append("</option>");
        }

        out.append("</select>");
      }
      else
      {
        out.append("-- option param is required --");
      }
    }
    else
    {
      out.append("-- option param is required --");
    }

    return out.toString();
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param obejct
   * @param name
   * @param param
   *
   * @return
   */
  public String getResult(BlogRequest request, ContentObject obejct,
                          String name, String param)
  {
    String result = request.getParameter(name);

    if (Util.isBlank(result))
    {
      result = null;
    }

    return result;
  }
}
