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

import java.util.logging.Level;

/**
 *
 * @author Sebastian Sdorra
 */
public class DefaultInjectionProvider extends InjectionProvider
{

  /**
   * Method description
   *
   *
   * @param object
   */
  @Override
  public void inject(Object object)
  {
    Field[] fields = object.getClass().getDeclaredFields();

    for (Field field : fields)
    {
      Annotation[] annotations = field.getDeclaredAnnotations();

      for (Annotation annotation : annotations)
      {
        Injector injector = injectorMap.get(annotation.annotationType());

        if (injector != null)
        {
          if (logger.isLoggable(Level.FINER))
          {
            StringBuffer msg = new StringBuffer();

            msg.append("call injector ");
            msg.append(injector.getClass().getName());
            msg.append(" for field ").append(field.getName());
            msg.append(" in class ").append(object.getClass().getName());
            logger.finer(msg.toString());
          }

          boolean access = field.isAccessible();

          if (!access)
          {
            field.setAccessible(true);
          }

          injector.inject(object, field, annotation);

          if (!access)
          {
            field.setAccessible(false);
          }
        }
        else if (logger.isLoggable(Level.FINER))
        {
          logger.finer("no injector found for "
                       + annotation.annotationType().getName());
        }
      }
    }
  }
}
