/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.cache;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.util.Collection;

/**
 *
 * @author sdorra
 */
public interface CacheManager
{

  /**
   * Method description
   *
   */
  public void clearAll();

  /**
   * Method description
   *
   *
   * @return
   */
  public Cache createCache(int maxSize);

  /**
   * Method description
   *
   *
   * @param config
   *
   * @throws IOException
   */
  public void load(InputStream config) throws IOException;

  /**
   * Method description
   *
   *
   * @param name
   * @param cache
   */
  public void putCache(String name, Cache cache);

  /**
   * Method description
   *
   *
   * @param name
   */
  public void removeCache(String name);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   */
  public Cache getCache(String name);

  /**
   * Method description
   *
   *
   * @return
   */
  public Collection<Cache> getCaches();
}
