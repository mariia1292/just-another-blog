/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.injection;

//~--- JDK imports ------------------------------------------------------------

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import java.util.logging.Level;

/**
 *
 * @author sdorra
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
