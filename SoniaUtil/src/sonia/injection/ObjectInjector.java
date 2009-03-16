/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
