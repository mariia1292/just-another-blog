/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    this.listeners = new ArrayList<ConfigurationListener>();
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
    this.listeners = new ArrayList<ConfigurationListener>();
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
    this.listeners = new ArrayList<ConfigurationListener>();
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
    this.listeners = new ArrayList<ConfigurationListener>();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param listener
   */
  public void addListener(ConfigurationListener listener)
  {
    synchronized (listeners)
    {
      listeners.add(listener);
    }
  }

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
    fireConfigChangedEvent(key);
  }

  /**
   * Method description
   *
   *
   * @param listener
   */
  public void removeListener(ConfigurationListener listener)
  {
    synchronized (listeners)
    {
      listeners.remove(listener);
    }
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
    return resolveVariable(properties.getProperty(key));
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
    String[] result = null;
    String value = properties.getProperty(key);

    if (value != null)
    {
      result = value.split(delimeter);
    }

    return resolveVariables(result);
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
      fireConfigChangedEvent(key);
    }
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param object
   */
  public void setMulti(String key, Object[] object)
  {
    StringBuffer buffer = new StringBuffer();
    int s = object.length;

    for (int i = 0; i < s; i++)
    {
      String v = parseString(object[i]);

      if (!isBlank(v))
      {
        buffer.append(v);
      }

      if (i < s)
      {
        buffer.append(delimeter);
      }
    }

    if (buffer.length() > 0)
    {
      properties.setProperty(key, buffer.toString());
      fireConfigChangedEvent(key);
    }
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   */
  private void fireConfigChangedEvent(String key)
  {
    for (ConfigurationListener listener : listeners)
    {
      listener.configChanged(this, key);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final List<ConfigurationListener> listeners;

  /** Field description */
  protected String delimeter = ";";

  /** Field description */
  protected Properties properties;
}
