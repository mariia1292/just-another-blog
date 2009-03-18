/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sdorra
 */
public abstract class StringBasedConfiguration extends ConfigurationBase
{

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Boolean getBoolean(String key)
  {
    return Boolean.parseBoolean(getString(key));
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Boolean[] getBooleans(String key)
  {
    Boolean[] result = null;
    String[] values = getStrings(key);

    if ((values != null) && (values.length > 0))
    {
      try
      {
        int s = values.length;

        result = new Boolean[s];

        for (int i = 0; i < s; i++)
        {
          result[i] = Boolean.parseBoolean(values[i]);
        }
      }
      catch (Exception ex)
      {
        getExceptionHandler().handleException(ex);
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Double getDouble(String key)
  {
    Double result = null;

    try
    {
      result = Double.parseDouble(getString(key));
    }
    catch (Exception ex)
    {
      getExceptionHandler().handleException(ex);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Double[] getDoubles(String key)
  {
    Double[] result = null;
    String[] values = getStrings(key);

    if ((values != null) && (values.length > 0))
    {
      try
      {
        int s = values.length;

        result = new Double[s];

        for (int i = 0; i < s; i++)
        {
          result[i] = Double.parseDouble(values[i]);
        }
      }
      catch (Exception ex)
      {
        getExceptionHandler().handleException(ex);
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Float getFloat(String key)
  {
    Float result = null;

    try
    {
      result = Float.parseFloat(getString(key));
    }
    catch (Exception ex)
    {
      getExceptionHandler().handleException(ex);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Float[] getFloats(String key)
  {
    Float[] result = null;
    String[] values = getStrings(key);

    if ((values != null) && (values.length > 0))
    {
      try
      {
        int s = values.length;

        result = new Float[s];

        for (int i = 0; i < s; i++)
        {
          result[i] = Float.parseFloat(values[i]);
        }
      }
      catch (Exception ex)
      {
        getExceptionHandler().handleException(ex);
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Integer getInteger(String key)
  {
    Integer result = null;
    String value = getString(key);

    if (value != null)
    {
      try
      {
        result = Integer.parseInt(value);
      }
      catch (Exception ex)
      {
        getExceptionHandler().handleException(ex);
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Integer[] getIntegers(String key)
  {
    Integer[] result = null;
    String[] values = getStrings(key);

    if ((values != null) && (values.length > 0))
    {
      try
      {
        int s = values.length;

        result = new Integer[s];

        for (int i = 0; i < s; i++)
        {
          result[i] = Integer.parseInt(values[i]);
        }
      }
      catch (Exception ex)
      {
        getExceptionHandler().handleException(ex);
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Long getLong(String key)
  {
    Long result = null;

    try
    {
      result = Long.parseLong(getString(key));
    }
    catch (Exception ex)
    {
      getExceptionHandler().handleException(ex);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Long[] getLongs(String key)
  {
    Long[] result = null;
    String[] values = getStrings(key);

    if ((values != null) && (values.length > 0))
    {
      try
      {
        int s = values.length;

        result = new Long[s];

        for (int i = 0; i < s; i++)
        {
          result[i] = Long.parseLong(values[i]);
        }
      }
      catch (Exception ex)
      {
        getExceptionHandler().handleException(ex);
      }
    }

    return result;
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
  public <T>T getObject(Class<T> clazz, String key)
  {
    T result = null;

    try
    {
      Object obj = Class.forName(getString(key)).newInstance();

      result = clazz.cast(obj);
    }
    catch (Exception ex)
    {
      getExceptionHandler().handleException(ex);
    }

    return result;
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
  @SuppressWarnings("unchecked")
  public <T>T[] getObjects(Class<T> clazz, String key)
  {
    T[] result = null;
    String[] values = getStrings(key);

    if ((values != null) && (values.length > 0))
    {
      try
      {
        List<T> resultList = new ArrayList<T>();

        for (String value : values)
        {
          resultList.add((T) Class.forName(value).newInstance());
        }

        result = (T[]) resultList.toArray();
      }
      catch (Exception ex)
      {
        getExceptionHandler().handleException(ex);
      }
    }

    return result;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  protected String parseString(Object object)
  {
    String result = null;

    if (object instanceof String)
    {
      result = (String) object;
    }
    else if (object instanceof Integer)
    {
      result = String.valueOf((Integer) object);
    }
    else if (object instanceof Long)
    {
      result = String.valueOf((Long) object);
    }
    else if (object instanceof Float)
    {
      result = String.valueOf((Float) object);
    }
    else if (object instanceof Double)
    {
      result = String.valueOf((Double) object);
    }
    else if (object instanceof Boolean)
    {
      result = String.valueOf((Boolean) object);
    }
    else
    {
      result = object.getClass().getName();
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param value
   *
   * @return
   */
  protected boolean isBlank(String value)
  {
    return (value == null) || (value.trim().length() == 0);
  }
}
