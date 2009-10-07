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

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;

/**
 *
 * @author Sebastian Sdorra
 *
 */
public class SimpleExpirationCache extends AbstractCache
        implements ExpirationCache
{

  /** Field description */
  private static final String PARAMETER_EXPIRATIONTIME = "expiration-time";

  /** Field description */
  private static final String PARAMETER_INTERVALCHECK = "interval-check";

  //~--- constructors ---------------------------------------------------------

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
  SimpleExpirationCache(String name, Map<String, String> parameter)
          throws NumberFormatException, IllegalArgumentException
  {
    super(name);
    cacheMap = new HashMap<Object, CacheObject>();

    String timeParam = parameter.get(PARAMETER_EXPIRATIONTIME);

    if (Util.hasContent(timeParam))
    {
      expirationTime = Long.parseLong(timeParam);
    }
    else
    {
      StringBuffer msg = new StringBuffer();

      msg.append("the parameter ").append(PARAMETER_EXPIRATIONTIME);
      msg.append(" is required");

      throw new IllegalArgumentException(msg.toString());
    }

    String checkParam = parameter.get(PARAMETER_INTERVALCHECK);

    if (Util.hasContent(checkParam))
    {
      intervalCheck = Boolean.parseBoolean(checkParam);

      if (intervalCheck)
      {
        createTimer(name);
      }
    }
  }

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param expirationTime
   * @param intervalCheck
   */
  public SimpleExpirationCache(String name, long expirationTime,
                               boolean intervalCheck)
  {
    super(name);
    this.expirationTime = expirationTime;
    this.cacheMap = new HashMap<Object, CacheObject>();

    if (intervalCheck) {}
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
    synchronized (cacheMap)
    {
      cacheMap.put(key, new CacheObject(value));
    }

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
    Object result = null;
    CacheObject co = cacheMap.get(key);

    if (co != null)
    {
      long time = System.currentTimeMillis();

      if (time - co.getTime() > expirationTime)
      {
        remove(key);
      }
      else
      {
        co.update();
        result = co.getObject();
      }
    }

    if (co != null)
    {
      hits++;
    }
    else
    {
      missed++;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public Map<String, Object> getAdvancedInformations()
  {
    Map<String, Object> informations = new HashMap<String, Object>();

    informations.put(PARAMETER_EXPIRATIONTIME, expirationTime);
    informations.put(PARAMETER_INTERVALCHECK, intervalCheck);

    return informations;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Map<Object, CacheObject> getCacheMap()
  {
    return cacheMap;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Collection<Entry<Object, CacheObject>> getEntries()
  {
    return cacheMap.entrySet();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param name
   */
  private void createTimer(String name)
  {
    Timer timer = new Timer(name);

    timer.schedule(new ExpirationTimerTask(this, expirationTime), 0l,
                   expirationTime);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final Map<Object, CacheObject> cacheMap;

  /** Field description */
  private long expirationTime;

  /** Field description */
  private boolean intervalCheck;
}
