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

import sonia.injection.InjectionProvider;

import sonia.macro.browse.MacroInformationProvider;

import sonia.util.ServiceLocator;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public abstract class MacroParser
{

  /** Field description */
  private static MacroParser instance;

  /** Field description */
  private static Logger logger = Logger.getLogger(MacroParser.class.getName());

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static MacroParser getInstance()
  {
    if (instance == null)
    {
      instance = ServiceLocator.locateService(MacroParser.class,
              new RegexMacroParser());
    }

    return instance;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param environment
   * @param text
   *
   * @return
   */
  public abstract MacroResult parseText(Map<String, Object> environment,
          String text);

  /**
   * Method description
   *
   *
   * @param in
   *
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public void load(InputStream in) throws IOException
  {
    Properties props = new Properties();

    props.load(in);

    Set<Object> keys = props.keySet();

    if (keys != null)
    {
      for (Object o : keys)
      {
        try
        {
          String key = (String) o;
          String value = props.getProperty(key);

          putMacro(key, (Class<? extends Macro>) Class.forName(value));
        }
        catch (Exception ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   *
   * @return
   */
  public ParameterConverter putConverter(Class<?> key, ParameterConverter value)
  {
    return converter.put(key, value);
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param macro
   */
  public void putMacro(String name, Class<? extends Macro> macro)
  {
    macros.put(name, macro);
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public ParameterConverter removeConverter(Class<?> key)
  {
    return converter.remove(key);
  }

  /**
   * Method description
   *
   *
   * @param name
   */
  public void removeMacro(String name)
  {
    macros.remove(name);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public ParameterConverter getConverter(Class<?> key)
  {
    return converter.get(key);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public MacroInformationProvider getInformationProvider()
  {
    return MacroInformationProvider.getInstance();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public InjectionProvider getInjectionProvider()
  {
    return injectionProvider;
  }

  /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   */
  public Class<? extends Macro> getMacro(String name)
  {
    return macros.get(name);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Iterator<Class<? extends Macro>> getMacros()
  {
    return macros.values().iterator();
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param injectionProvider
   */
  public void setInjectionProvider(InjectionProvider injectionProvider)
  {
    this.injectionProvider = injectionProvider;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected Map<Class<?>, ParameterConverter> converter =
    new HashMap<Class<?>, ParameterConverter>();

  /** Field description */
  protected InjectionProvider injectionProvider;

  /** Field description */
  protected Map<String, Class<? extends Macro>> macros =
    new HashMap<String, Class<? extends Macro>>();
}
