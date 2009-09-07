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

import java.lang.reflect.Method;

import java.math.BigInteger;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    "\\{([a-zA-Z0-9]*)([:;=/-_a-zA-Z0-9]*)\\b[^\\}]*\\}(.*?)\\{/\\1\\}";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(RegexMacroParser.class.getName());

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
  public MacroResult parseText(Map<String, ?> environment, String text)
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
        Macro macro = buildMacro(name);

        if (macro != null)
        {
          result.addMacro(macro);

          String paramString = m.group(2);

          if (!Util.isBlank(paramString))
          {
            injectParameter(macro, paramString);
          }

          String body = m.group(3);
          String replacement = macro.doBody(environment, body);

          replaceMap.put(m.group(0), replacement);
        }
      }
    }

    for (String key : replaceMap.keySet())
    {
      text = text.replace(key, replaceMap.get(key));
    }

    result.setText(text);

    return result;
  }

  /**
   * Method description
   *
   *
   * @param type
   * @param value
   *
   * @return
   */
  protected Object convertValue(Class<?> type, String value)
  {
    Object result = null;

    try
    {
      if (type.isAssignableFrom(String.class))
      {
        result = value;
      }
      else if (type.isAssignableFrom(Short.class))
      {
        result = value;
      }
      else if (type.isAssignableFrom(Integer.class))
      {
        result = Integer.parseInt(value);
      }
      else if (type.isAssignableFrom(Long.class))
      {
        result = Long.parseLong(value);
      }
      else if (type.isAssignableFrom(BigInteger.class))
      {
        result = new BigInteger(value);
      }
      else if (type.isAssignableFrom(Float.class))
      {
        result = Float.parseFloat(value);
      }
      else if (type.isAssignableFrom(Double.class))
      {
        result = Double.parseDouble(value);
      }
      else if (type.isAssignableFrom(Boolean.class))
      {
        result = Boolean.parseBoolean(value);
      }
      else
      {
        ParameterConverter conv = getConverter(type);

        if (conv != null)
        {
          result = conv.convert(value);
        }
      }
    }
    catch (NumberFormatException ex)
    {
      logger.log(Level.FINER, null, ex);
    }
    catch (ConvertException ex)
    {
      logger.log(Level.FINER, null, ex);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   */
  private Macro buildMacro(String name)
  {
    Macro macro = null;
    Class<? extends Macro> clazz = getMacro(name);

    if (clazz != null)
    {
      try
      {
        macro = clazz.newInstance();

        if (injectionProvider != null)
        {
          injectionProvider.inject(macro);
        }
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }

    return macro;
  }

  /**
   * Method description
   *
   *
   * @param clazz
   * @param key
   *
   * @return
   */
  private Method findSetter(Class<? extends Macro> clazz, String key)
  {
    Method method = null;
    String name = "set" + key.toUpperCase().charAt(0) + key.substring(1);
    Method[] methods = clazz.getMethods();

    if (methods != null)
    {
      for (Method m : methods)
      {
        if (m.getName().equals(name))
        {
          method = m;

          break;
        }
      }
    }

    return method;
  }

  /**
   * Method description
   *
   *
   * @param macro
   * @param paramString
   */
  private void injectParameter(Macro macro, String paramString)
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

        injectParameter(macro, key, value);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param macro
   * @param key
   * @param value
   */
  private void injectParameter(Macro macro, String key, String value)
  {
    Method method = findSetter(macro.getClass(), key);

    if (method != null)
    {
      Class<?>[] paramTypes = method.getParameterTypes();

      if ((paramTypes != null) && (paramTypes.length == 1))
      {
        Class<?> paramType = paramTypes[0];
        Object injectValue = convertValue(paramType, value);

        if (injectValue != null)
        {
          try
          {
            if (logger.isLoggable(Level.FINER))
            {
              StringBuffer msg = new StringBuffer();

              msg.append("invoke method ").append(method.getName());
              msg.append(" with parameter ").append(injectValue);
              logger.finer(msg.toString());
            }

            method.invoke(macro, injectValue);
          }
          catch (Exception ex)
          {
            logger.log(Level.SEVERE, null, ex);
          }
        }
        else
        {
          StringBuffer log = new StringBuffer();

          log.append("type ").append(paramType.getName());
          log.append(" is not supported");
          logger.fine(log.toString());
        }
      }
    }
    else
    {
      logger.fine("could not find a setter for parameter " + key);
    }
  }
}
