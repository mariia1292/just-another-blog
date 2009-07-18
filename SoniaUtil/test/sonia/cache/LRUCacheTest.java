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

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Sebastian Sdorra
 */
public class LRUCacheTest extends CacheTestBase
{

  /**
   * Method description
   *
   */
  @Test
  public void lruTest() throws InterruptedException
  {
    Cache<String, String> cache = getCache();

    assertTrue(cache.isEmpty());
    assertEquals("value1", cache.put("key1", "value1"));
    assertEquals("value2", cache.put("key2", "value2"));
    assertEquals("value3", cache.put("key3", "value3"));
    assertEquals("value4", cache.put("key4", "value4"));
    assertEquals("value5", cache.put("key5", "value5"));
    assertFalse(cache.isEmpty());
    assertTrue(cache.size() == 5);
    assertEquals("value1", cache.get("key1"));
    Thread.sleep(10l);
    assertEquals("value2", cache.get("key2"));
    assertEquals("value3", cache.get("key3"));
    assertEquals("value4", cache.get("key4"));
    assertEquals("value5", cache.get("key5"));
    assertEquals("value7", cache.put("key7", "value7"));
    assertNull(cache.get("key1"));
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected Cache<String, String> getCache()
  {
    return new LRUCache<String, String>("junit", 5);
  }
}
