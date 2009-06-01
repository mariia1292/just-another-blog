/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.cache;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.commons.collections.map.LRUMap;

import sonia.blog.api.cache.Cache;

/**
 *
 * @author sdorra
 */
public class DefaultCache implements Cache
{

  /**
   * Constructs ...
   *
   */
  public DefaultCache(int maxSize)
  {
    cacheMap = new LRUMap(maxSize);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  public void clear()
  {
    cacheMap.clear();
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   */
  public void put(Object key, Object value)
  {
    cacheMap.put(key, value);
  }

  /**
   * Method description
   *
   *
   * @param key
   */
  public void remove(Object key)
  {
    cacheMap.remove(key);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public int getSize()
  {
    return cacheMap.size();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private LRUMap cacheMap;
}
