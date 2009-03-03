/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro;

//~--- non-JDK imports --------------------------------------------------------


import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author sdorra
 */
public class RegexMacroParser extends MacroParser
{

  /** Field description */
  private static final String REGEX =
    "\\{([a-zA-Z0-9]*)([:;=a-zA-Z0-9]*)\\b[^\\}]*\\}(.*?)\\{/\\1\\}";

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
  public String parseText(Map<String, ?> environment, String text)
  {
    Pattern p = Pattern.compile(REGEX);
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

    return text;
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

    try
    {
      method = clazz.getDeclaredMethod(name, String.class);
    }
    catch (Exception ex)
    {
      logger.log(Level.WARNING, null, ex);
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
      try
      {
        method.invoke(macro, value);
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }
    else
    {
      logger.fine("could not find a setter for parameter " + key);
    }
  }
}
