/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author sdorra
 */
public class PropertiesConfiguration extends StringBasedConfiguration
        implements StoreableConfiguration, LoadableConfiguration
{

  /**
   * Constructs ...
   *
   */
  public PropertiesConfiguration()
  {
    this.properties = new Properties();
  }

  /**
   * Constructs ...
   *
   *
   * @param properties
   */
  public PropertiesConfiguration(Properties properties)
  {
    this.properties = properties;
  }

  /**
   * Constructs ...
   *
   *
   * @param delimeter
   */
  public PropertiesConfiguration(String delimeter)
  {
    this.delimeter = delimeter;
    this.properties = new Properties();
  }

  /**
   * Constructs ...
   *
   *
   * @param properties
   * @param delimeter
   */
  public PropertiesConfiguration(Properties properties, String delimeter)
  {
    this.properties = properties;
    this.delimeter = delimeter;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public boolean contains(String key)
  {
    return properties.containsKey(key);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Set<String> keySet()
  {
    Set<String> keys = new HashSet<String>();

    for (Object obj : properties.keySet())
    {
      keys.add((String) obj);
    }

    return keys;
  }

  /**
   * Method description
   *
   *
   * @param in
   *
   * @throws IOException
   */
  public void load(InputStream in) throws IOException
  {
    properties.load(in);
  }

  /**
   * Method description
   *
   *
   * @param key
   */
  public void remove(String key)
  {
    properties.remove(key);
  }

  /**
   * Method description
   *
   *
   * @param out
   *
   * @throws IOException
   */
  public void store(OutputStream out) throws IOException
  {
    properties.store(out, "SoniaConfiguration");
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
  public String getString(String key)
  {
    return properties.getProperty(key);
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public String[] getStrings(String key)
  {
    String result = properties.getProperty(key);

    if (result != null)
    {
      return result.split(delimeter);
    }

    return new String[] { result };
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isEmpty()
  {
    return properties.isEmpty();
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   * @param object
   */
  public void set(String key, Object object)
  {
    String value = parseString(object);

    if (!isBlank(value))
    {
      properties.setProperty(key, value);
    }
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param object
   */
  public void set(String key, Object[] object)
  {
    String value = "";
    int s = object.length;

    for (int i = 0; i < s; i++)
    {
      String v = parseString(object[i]);

      if (!isBlank(v))
      {
        value += v;
      }

      if (i < s)
      {
        value += delimeter;
      }
    }

    if (!isBlank(value))
    {
      properties.setProperty(key, value);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected String delimeter = ";";

  /** Field description */
  protected Properties properties;
}
