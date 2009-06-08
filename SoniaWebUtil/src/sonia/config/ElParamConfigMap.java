/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author sdorra
 */
public class ElParamConfigMap implements Map<String, String>
{

  /**
   * Constructs ...
   *
   *
   * @param config
   */
  public ElParamConfigMap(Configuration config)
  {
    this.config = config;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  public void clear()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public boolean containsKey(Object key)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Method description
   *
   *
   * @param value
   *
   * @return
   */
  public boolean containsValue(Object value)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Set<Entry<String, String>> entrySet()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Set<String> keySet()
  {
    return config.keySet();
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   *
   * @return
   */
  public String put(String key, String value)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Method description
   *
   *
   * @param t
   */
  public void putAll(Map<? extends String, ? extends String> t)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public String remove(Object key)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int size()
  {
    return config.getSize();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Collection<String> values()
  {
    throw new UnsupportedOperationException("Not supported yet.");
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
  public String get(Object key)
  {
    String result = null;

    if (key instanceof String)
    {
      result = config.getString((String) key);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isEmpty()
  {
    return config.isEmpty();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Configuration config;
}
