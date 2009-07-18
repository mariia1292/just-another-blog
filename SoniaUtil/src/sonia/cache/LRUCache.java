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
public class LRUCache<K, V> extends AbstractCache<K, V>
{

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param size
   */
  public LRUCache(String name, int size)
  {
    super(name);
    this.maxItems = size;
    this.cacheMap = new HashMap<K, CacheObject<V>>(size);
  }

  //~--- methods --------------------------------------------------------------

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
    while (maxItems <= cacheMap.size())
    {
      removeLastEntry();
    }

    cacheMap.put(key, new CacheObject<V>(value));

    return value;
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
    V value = null;
    CacheObject<V> co = cacheMap.get(key);

    if (co != null)
    {
      value = co.getObject();
      hits++;
      co.update();
    }
    else
    {
      missed++;
    }

    return value;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected Map<K, CacheObject<V>> getCacheMap()
  {
    return cacheMap;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  private void removeLastEntry()
  {
    Set<Map.Entry<K, CacheObject<V>>> entries = cacheMap.entrySet();
    Map.Entry<K, CacheObject<V>> lastCo = null;

    for (Map.Entry<K, CacheObject<V>> entry : entries)
    {
      CacheObject<V> value = entry.getValue();

      if ((lastCo == null)
          || (value.getLastAccess() < lastCo.getValue().getLastAccess())
          || ((value.getLastAccess() == lastCo.getValue().getLastAccess())
              && (value.getTime() < lastCo.getValue().getTime())))
      {
        lastCo = entry;
      }
    }

    remove(lastCo.getKey());
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final Map<K, CacheObject<V>> cacheMap;

  /** Field description */
  private int maxItems;
}
