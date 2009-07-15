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


package sonia.injection;

//~--- JDK imports ------------------------------------------------------------

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class ObjectInjector implements Injector
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ObjectInjector.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param context
   */
  public ObjectInjector(Object context)
  {
    this.context = context;
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
    Class<?> type = field.getType();

    try
    {
      Object value = getValue(type);

      if (value != null)
      {
        if (logger.isLoggable(Level.FINER))
        {
          StringBuffer msg = new StringBuffer();

          msg.append("injecting ").append(value.getClass().getName());
          msg.append(" in field ").append(field.getName());
          logger.finer(msg.toString());
        }

        field.set(object, value);
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param type
   *
   * @return
   *
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  protected Object getValue(Class<?> type)
          throws IllegalArgumentException, IllegalAccessException,
                 InvocationTargetException
  {
    Object value = null;

    if (type.isAssignableFrom(context.getClass()))
    {
      value = context;
    }
    else
    {
      Method[] methods = context.getClass().getMethods();

      for (Method m : methods)
      {
        if (type.isAssignableFrom(m.getReturnType())
            && ((m.getParameterTypes() == null)
                || (m.getParameterTypes().length == 0)))
        {
          if (logger.isLoggable(Level.FINEST))
          {
            StringBuffer msg = new StringBuffer();

            msg.append("invoke method ").append(m.getName());
            logger.finest(msg.toString());
          }

          value = m.invoke(context);

          break;
        }
      }
    }

    return value;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Object context;
}