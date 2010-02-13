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



package sonia.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Sebastian Sdorra
 */
public class RegexMacroParser extends MacroParser
{

  /** Field description */
  private static final String REGEX =
    "\\{([a-zA-Z0-9]*)([:;=/\\-_\\.,\\ \\*a-zA-Z0-9]*)\\b[^\\}]*\\}(.*?)\\{/\\1\\}";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param environment
   * @param text
   *
   * @return
   */
  public MacroResult parseText(Map<String, Object> environment, String text)
  {
    MacroResult result = new MacroResult();
    Pattern p = Pattern.compile(REGEX, Pattern.DOTALL);
    Matcher m = p.matcher(text);
    Map<String, String> replaceMap = new HashMap<String, String>();

    while (m.find())
    {
      String name = m.group(1);

      if (!Util.isBlank(name))
      {
        MacroFactory macroFactory = macroFactories.get(name);

        if (macroFactory != null)
        {
          Map<String, String> parameters = new HashMap<String, String>();
          String paramString = m.group(2);

          if (!Util.isBlank(paramString))
          {
            addParameters(parameters, paramString);
          }

          Macro macro = macroFactory.createMacro(parameters);

          if (injectionProvider != null)
          {
            injectionProvider.inject(macro);
          }

          result.addMacro(macro);

          String body = m.group(3);
          String replacement = macro.doBody(environment, body);

          replaceMap.put(m.group(0), replacement);
        }
      }
    }

    for (Map.Entry<String, String> entry : replaceMap.entrySet())
    {
      text = text.replace(entry.getKey(), entry.getValue());
    }

    text = text.replaceAll("\\\\\\{", "{").replaceAll("\\\\\\}", "}");
    result.setText(text);

    return result;
  }

  /**
   * Method description
   *
   *
   *
   * @param paramMap
   * @param paramString
   */
  private void addParameters(Map<String, String> paramMap, String paramString)
  {
    if (paramString.startsWith(":"))
    {
      paramString = paramString.substring(1);
    }

    String[] parameters = paramString.split(";");

    for (String param : parameters)
    {
      int index = param.indexOf("=");

      if (index > 0)
      {
        String key = param.substring(0, index);
        String value = param.substring(index + 1, param.length());

        paramMap.put(key, value);
      }
    }
  }
}
