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



package sonia.plugin.service;

//~--- non-JDK imports --------------------------------------------------------

import sonia.injection.Injector;

//~--- JDK imports ------------------------------------------------------------

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class ServiceInjector implements Injector
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ServiceInjector.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param registry
   */
  public ServiceInjector(ServiceRegistry registry)
  {
    this.registry = registry;
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
  @SuppressWarnings("unchecked")
  public void inject(Object object, Field field, Annotation annotation)
  {
    if (annotation instanceof Service)
    {
      Service serviceAnnotation = (Service) annotation;
      String path = serviceAnnotation.value();
      Class serviceClass = getServiceClass(field);

      if (serviceClass != null)
      {
        ServiceReference reference = registry.get(serviceClass, path);

        if (reference != null)
        {
          Object injection = null;

          if (isReferenceField(field))
          {
            injection = reference;
          }
          else if (isListField(field))
          {
            injection = reference.getAll();
          }
          else
          {
            injection = reference.get();
          }

          if (injection != null)
          {
            if (logger.isLoggable(Level.FINER))
            {
              StringBuffer msg = new StringBuffer();

              msg.append("injecting ").append(injection.getClass().getName());
              msg.append(" to ").append(field.getName());
              logger.finer(msg.toString());
            }

            try
            {
              field.set(object, injection);
            }
            catch (IllegalAccessException ex)
            {
              logger.log(Level.SEVERE, null, ex);
            }
          }
          else if (logger.isLoggable(Level.FINER))
          {
            StringBuffer msg = new StringBuffer();

            msg.append("no injection found for ");
            msg.append(serviceClass.getName()).append(":").append(path);
            logger.finer(msg.toString());
          }
        }
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param field
   *
   * @return
   */
  private Class getServiceClass(Field field)
  {
    Class result = null;
    Class fieldClass = field.getType();

    if (isListField(field) || isReferenceField(field))
    {
      Type type = field.getGenericType();

      if (type instanceof ParameterizedType)
      {
        ParameterizedType paramType = (ParameterizedType) type;
        Type[] types = paramType.getActualTypeArguments();

        if ((types != null) && (types.length > 0))
        {
          result = (Class) types[0];
        }
      }
    }
    else
    {
      result = fieldClass;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param field
   *
   * @return
   */
  private boolean isListField(Field field)
  {
    return field.getType().equals(List.class);
  }

  /**
   * Method description
   *
   *
   * @param field
   *
   * @return
   */
  private boolean isReferenceField(Field field)
  {
    return field.getType().equals(ServiceReference.class);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceRegistry registry;
}
