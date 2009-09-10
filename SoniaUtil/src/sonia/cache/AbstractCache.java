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

import java.util.Map;
import java.util.Set;

/**
 *
 * @author Sebastian Sdorra
 *
 */
public abstract class AbstractCache implements ObjectCache
{

  /**
   * Constructs ...
   *
   *
   * @param name
   */
  public AbstractCache(String name)
  {
    this.name = name;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract Map<Object, CacheObject> getCacheMap();

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  public void clear()
  {
    Map<Object, CacheObject> cacheMap = getCacheMap();

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
  public Set<Object> keySet()
  {
    return getCacheMap().keySet();
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Object remove(Object key)
  {
    Object result = null;
    Map<Object, CacheObject> cacheMap = getCacheMap();

    synchronized (cacheMap)
    {
      CacheObject co = cacheMap.remove(key);

      result = co.getObject();
    }

    return result;
  }

  /**
   * Method description
   *
   */
  public void reset()
  {
    Map<Object, CacheObject> cacheMap = getCacheMap();

    synchronized (cacheMap)
    {
      cacheMap.clear();
      hits = 0l;
      missed = 0l;
    }
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int size()
  {
    return getCacheMap().size();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public double getHitRatio()
  {
    double dHits = (double) hits;
    double dMissed = (double) missed;

    return (dHits + dMissed > 0)
           ? 100d / (dHits + dMissed) * dHits
           : 0d;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getHits()
  {
    return hits;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getMissed()
  {
    return missed;
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
    return getCacheMap().isEmpty();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected long hits = 0l;

  /** Field description */
  protected long missed = 0l;

  /** Field description */
  private String name;
}
