/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
