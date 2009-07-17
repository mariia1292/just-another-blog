/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://kenai.com/projects/jab
 *
 */



package sonia.cache;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Sebastian Sdorra
 *
 * @param <K>
 * @param <V>
 */
public class ExpirationMap<K, V> implements Cache<K, V>
{

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param expirationTime
   * @param intervalCheck
   */
  public ExpirationMap(String name, long expirationTime, boolean intervalCheck)
  {
    this.name = name;
    this.expirationTime = expirationTime;
    this.cacheMap = new HashMap<K, ExpirationCacheObject<V>>();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  public void clear()
  {
    synchronized (cacheMap)
    {
      cacheMap.clear();
    }
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Set<K> keySet()
  {
    return cacheMap.keySet();
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
  public V put(K key, V value)
  {
    synchronized (cacheMap)
    {
      cacheMap.put(key, new ExpirationCacheObject<V>(value));
    }

    return value;
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public V remove(K key)
  {
    V result = null;

    synchronized (cacheMap)
    {
      ExpirationCacheObject<V> eco = cacheMap.remove(key);

      result = eco.getObject();
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int size()
  {
    return cacheMap.size();
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
  public V get(K key)
  {
    V result = null;
    ExpirationCacheObject<V> eco = cacheMap.get(key);

    if (eco != null)
    {
      long time = System.currentTimeMillis();

      if (time - eco.getTime() > expirationTime)
      {
        remove(key);
      }
      else
      {
        result = eco.getObject();
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return name;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isEmpty()
  {
    return cacheMap.isEmpty();
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/07/17
   * @author         Enter your name here...
   *
   * @param <V>
   */
  private static class ExpirationCacheObject<V>
  {

    /**
     * Constructs ...
     *
     *
     * @param object
     */
    public ExpirationCacheObject(V object)
    {
      this.object = object;
      this.time = System.currentTimeMillis();
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param obj
     *
     * @return
     */
    @Override
    public boolean equals(Object obj)
    {
      if (obj == null)
      {
        return false;
      }

      if (getClass() != obj.getClass())
      {
        return false;
      }

      final ExpirationCacheObject other = (ExpirationCacheObject) obj;

      if ((this.object != other.object)
          && ((this.object == null) ||!this.object.equals(other.object)))
      {
        return false;
      }

      return true;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public int hashCode()
    {
      int hash = 3;

      hash = 89 * hash + ((this.object != null)
                          ? this.object.hashCode()
                          : 0);

      return hash;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public V getObject()
    {
      return object;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public long getTime()
    {
      return time;
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private V object;

    /** Field description */
    private long time;
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final Map<K, ExpirationCacheObject<V>> cacheMap;

  /** Field description */
  private long expirationTime;

  /** Field description */
  private String name;
}
