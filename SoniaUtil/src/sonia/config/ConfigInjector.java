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



package sonia.config;

//~--- non-JDK imports --------------------------------------------------------

import sonia.injection.IllegalInjectionException;
import sonia.injection.Injector;

//~--- JDK imports ------------------------------------------------------------

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class ConfigInjector implements Injector
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ConfigInjector.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param configuration
   */
  public ConfigInjector(Configuration configuration)
  {
    this.configuration = configuration;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param object
   * @param field
   * @param annotation
   */
  public void inject(Object object, Field field, Annotation annotation)
  {
    if (annotation instanceof Config)
    {
      Config config = (Config) annotation;
      String key = config.value();
      Class type = field.getType();
      Object value = null;

      if (type.isArray())
      {
        value = getValue(key, type.getComponentType(), true);
      }
      else
      {
        value = getValue(key, type, false);
      }

      if ((value != null) || config.injectNullValues())
      {
        if (logger.isLoggable(Level.FINER))
        {
          StringBuffer msg = new StringBuffer();

          msg.append("injecting ").append(value).append(" from ");
          msg.append(key).append(" in field ").append(field.getName());
          logger.finer(msg.toString());
        }

        try
        {
          field.set(object, value);
        }
        catch (IllegalAccessException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   * @param type
   * @param array
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  private Object getValue(String key, Class type, boolean array)
  {
    Object value = null;

    if (type.isAssignableFrom(Integer.class))
    {
      if (array)
      {
        value = configuration.getIntegers(key);
      }
      else
      {
        value = configuration.getInteger(key);
      }
    }
    else if (type.isAssignableFrom(Long.class))
    {
      if (array)
      {
        value = configuration.getLongs(key);
      }
      else
      {
        value = configuration.getLong(key);
      }
    }
    else if (type.isAssignableFrom(Double.class))
    {
      if (array)
      {
        value = configuration.getDoubles(key);
      }
      else
      {
        value = configuration.getDouble(key);
      }
    }
    else if (type.isAssignableFrom(Float.class))
    {
      if (array)
      {
        value = configuration.getFloats(key);
      }
      else
      {
        value = configuration.getFloat(key);
      }
    }
    else if (type.isAssignableFrom(Boolean.class))
    {
      if (array)
      {
        value = configuration.getBooleans(key);
      }
      else
      {
        value = configuration.getBoolean(key);
      }
    }
    else if (type.isAssignableFrom(String.class))
    {
      if (array)
      {
        value = configuration.getStrings(key);
      }
      else
      {
        value = configuration.getString(key);
      }
    }
    else
    {
      throw new IllegalInjectionException(type.getName()
              + " is not supported by ConfigInjection");
    }

    return value;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Configuration configuration;
}
