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

import java.io.Serializable;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author Sebastian Sdorra
 *
 */
public interface ObjectCache extends Serializable
{

  /**
   * Removes all cached objects in this cache
   *
   */
  public void clear();

  /**
   * Removes all cached objects matched by the condition
   *
   *
   * @param condition
   */
  public void clear(ClearCondition condition);

  /**
   * Returns a {@link java.util.Set} view of the keys contained in this cache.
   *
   *
   * @return a Set view of the keys contained in this map
   */
  public Set<Object> keySet();

  /**
   * Put an object to this cache.
   *
   *
   * @param key
   * @param value
   *
   * @return
   */
  public Object put(Object key, Object value);

  /**
   * Removes an object from this cache.
   *
   *
   * @param key whose mapping is to be removed from the cache
   *
   * @return a reference to the object that was removed
   */
  public Object remove(Object key);

  /**
   * Clears the cache and reset all counters.
   *
   */
  public void reset();

  /**
   * Returns the count of all cached objects.
   *
   *
   * @return the count of all cached objects
   */
  public int size();

  //~--- get methods ----------------------------------------------------------

  /**
   * Returns the value to which the specified key is mapped,
   * or null if this cache contains no mapping for the key.
   *
   *
   *
   * @param key
   *
   * @return the cached object
   */
  public Object get(Object key);

  /**
   * Returns a map of advanced informations.
   *
   *
   * @return a map of advanced informations
   */
  public Map<String, Object> getAdvancedInformations();

  /**
   * Returns the hit ratio of the cache.
   *
   *
   * @return the hit ratio of the cache
   */
  public double getHitRatio();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getHits();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getMissed();

  /**
   * Returns the name of this cache.
   *
   *
   * @return the name of this cache
   */
  public String getName();

  /**
   * Returns true if this cache contains no elements.
   *
   *
   * @return true if this collection contains no elements.
   */
  public boolean isEmpty();
}
