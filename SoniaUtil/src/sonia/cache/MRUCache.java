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
 * THIS SOFTWARE IS PROObjectIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EObjectENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERObjectICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEObjectER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EObjectEN IF ADObjectISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://kenai.com/projects/jab
 *
 */



package sonia.cache;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Sebastian Sdorra
 *
 */
public class MRUCache extends AbstractCache
{

  /** Field description */
  public static final String PARAMETER_MAXITEMS = "max-items";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param maxItems
   */
  public MRUCache(String name, int maxItems)
  {
    super(name);
    this.maxItems = maxItems;
    this.cacheMap = new HashMap<Object, CacheObject>(maxItems);
  }

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param parameter
   *
   * @throws IllegalArgumentException
   * @throws NumberFormatException
   */
  MRUCache(String name, Map<String, String> parameter)
          throws IllegalArgumentException, NumberFormatException
  {
    super(name);

    String maxItemsParams = parameter.get(PARAMETER_MAXITEMS);

    if (Util.hasContent(maxItemsParams))
    {
      maxItems = Integer.parseInt(maxItemsParams);
    }
    else
    {
      StringBuffer msg = new StringBuffer();

      msg.append("the parameter ").append(PARAMETER_MAXITEMS);
      msg.append(" is required");

      throw new IllegalArgumentException(msg.toString());
    }

    this.cacheMap = new HashMap<Object, CacheObject>(maxItems);
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
  public Object put(Object key, Object value)
  {
    while (maxItems <= cacheMap.size())
    {
      removeEntry();
    }

    cacheMap.put(key, new CacheObject(value));

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
  public Object get(Object key)
  {
    Object value = null;
    CacheObject co = cacheMap.get(key);

    if (co != null)
    {
      value = co.getObject();
      co.update();
      hits++;
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
  protected Map<Object, CacheObject> getCacheMap()
  {
    return cacheMap;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  private void removeEntry()
  {
    Set<Map.Entry<Object, CacheObject>> entries = cacheMap.entrySet();
    Map.Entry<Object, CacheObject> etr = null;

    for (Map.Entry<Object, CacheObject> entry : entries)
    {
      CacheObject value = entry.getValue();

      if ((etr == null) || (value.getHits() < etr.getValue().getHits())
          || ((value.getHits() == etr.getValue().getHits())
              && (value.getLastAccess() < etr.getValue().getLastAccess())))
      {
        etr = entry;
      }
    }

    remove(etr.getKey());
  }

    @Override
  public Map<String, Object> getAdvancedInformations()
  {
    Map<String,Object> informations = new HashMap<String, Object>();
    informations.put("max-items", maxItems);
    return informations;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Map<Object, CacheObject> cacheMap;

  /** Field description */
  private int maxItems;
}
