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

import sonia.macro.browse.DefaultMacroInformationProvider;
import sonia.macro.browse.MacroInformation;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Method;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class ClassMacroFactory implements MacroFactory
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ClassMacroFactory.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param clazz
   */
  public ClassMacroFactory(Class<? extends Macro> clazz)
  {
    this.clazz = clazz;
  }

  /**
   * Constructs ...
   *
   *
   * @param clazz
   * @param information
   */
  public ClassMacroFactory(Class<? extends Macro> clazz,
                           MacroInformation information)
  {
    this.clazz = clazz;
    this.information = information;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   *
   * @param parameters
   * @return
   */
  public Macro createMacro(Map<String, String> parameters)
  {
    Macro result = null;

    try
    {
      result = clazz.newInstance();
      applyParameters(result, parameters);
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param locale
   *
   * @return
   */
  public MacroInformation getInformation(Locale locale)
  {
    if (information == null)
    {
      information =
        DefaultMacroInformationProvider.getInstance().getInformation(clazz,
          locale);
    }

    return information;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param macro
   * @param parameters
   */
  private void applyParameters(Macro macro, Map<String, String> parameters)
  {
    if (macro instanceof InitableMacro)
    {
      ((InitableMacro) macro).init(parameters);
    }
    else
    {
      injectParameters(macro, parameters);
    }
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
        Object injectValue = Util.convertString(paramType, value);

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

  /**
   * Method description
   *
   *
   * @param macro
   * @param parameters
   */
  private void injectParameters(Macro macro, Map<String, String> parameters)
  {
    if (Util.hasContent(parameters))
    {
      Set<Entry<String, String>> entries = parameters.entrySet();

      for (Entry<String, String> entry : entries)
      {
        injectParameter(macro, entry.getKey(), entry.getValue());
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Class<? extends Macro> clazz;

  /** Field description */
  private MacroInformation information;
}
