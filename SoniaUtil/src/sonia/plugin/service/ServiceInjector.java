/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
