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

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sonia.util.Util;
import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Constructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class CacheManager
{

  /** Field description */
  private static Logger logger = Logger.getLogger(CacheManager.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public CacheManager()
  {
    caches = new HashMap<String, ObjectCache>();
  }

  /**
   * Constructs the CacheManager with JMX support
   *
   *
   * @param mbeanManager
   */
  public CacheManager(CacheMBeanManager mbeanManager)
  {
    this.mbeanManager = mbeanManager;
    caches = new HashMap<String, ObjectCache>();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param clazz
   *
   * @return
   */
  public static boolean isCacheable(Class<?> clazz)
  {
    return clazz.getAnnotation(Cacheable.class) != null;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param cache
   */
  public void add(ObjectCache cache)
  {
    String name = cache.getName();

    if (Util.hasContent(name))
    {
      if (logger.isLoggable(Level.FINEST))
      {
        StringBuffer msg = new StringBuffer();

        msg.append("add ").append(cache.getName()).append(" cache of type ");
        msg.append(cache.getClass().getName());
        logger.finest(msg.toString());
      }

      caches.put(name, cache);

      if (mbeanManager != null)
      {
        mbeanManager.register(cache);
      }
    }
    else
    {
      throw new IllegalArgumentException("the cache has no name");
    }
  }

  /**
   * Method description
   *
   */
  public void clearAll()
  {
    if (!caches.isEmpty())
    {
      Collection<ObjectCache> cacheValues = caches.values();

      for (ObjectCache cache : cacheValues)
      {
        cache.clear();
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param in
   *
   * @throws IOException
   */
  public void load(InputStream in) throws IOException
  {
    try
    {
      Document doc = XmlUtil.buildDocument(in);
      NodeList children = doc.getElementsByTagName("cache");

      if (XmlUtil.hasContent(children))
      {
        for (int i = 0; i < children.getLength(); i++)
        {
          Node child = children.item(i);

          if (child.getNodeName().equals("cache"))
          {
            parseCacheNode(child);
          }
        }
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);

      throw new IOException(ex.getMessage());
    }
    finally
    {
      if (in != null)
      {
        in.close();
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param name
   */
  public void remove(String name)
  {
    ObjectCache cache = caches.remove(name);

    if (cache != null)
    {
      if (mbeanManager != null)
      {
        mbeanManager.unregister(cache);
      }

      if (logger.isLoggable(Level.FINEST))
      {
        StringBuffer msg = new StringBuffer();

        msg.append("remove cache ").append(cache.getName());
        logger.finest(msg.toString());
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   */
  public ObjectCache get(String name)
  {
    return caches.get(name);
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param mbeanManager
   */
  public void setMbeanManager(CacheMBeanManager mbeanManager)
  {
    this.mbeanManager = mbeanManager;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param node
   */
  @SuppressWarnings("unchecked")
  private void parseCacheNode(Node node)
  {
    NodeList children = node.getChildNodes();

    if (XmlUtil.hasContent(children))
    {
      try
      {
        String name = null;
        Class<? extends ObjectCache> clazz = null;
        Map<String, String> parameters = new HashMap<String, String>();

        for (int i = 0; i < children.getLength(); i++)
        {
          Node child = children.item(i);
          String childName = child.getNodeName();

          if ("name".equals(childName))
          {
            name = child.getTextContent();
          }
          else if ("class".equals(childName))
          {
            clazz = (Class<? extends ObjectCache>) Class.forName(
              child.getTextContent().trim());
          }
          else if ("parameter".equals(childName))
          {
            parseParameterNode(parameters, child);
          }
        }

        if (Util.hasContent(name) && (clazz != null))
        {
          Constructor<?>[] constructors = clazz.getDeclaredConstructors();

          if ((constructors != null) && (constructors.length > 0))
          {
            Constructor<?> constructor = null;

            for (Constructor<?> c : constructors)
            {
              Class<?>[] parameterTypes = c.getParameterTypes();

              if ((parameterTypes.length == 2)
                  && parameterTypes[0].equals(String.class)
                  && parameterTypes[1].equals(Map.class))
              {
                constructor = c;

                break;
              }
            }

            if (constructor != null)
            {
              ObjectCache cache = (ObjectCache) constructor.newInstance(name,
                                    parameters);

              add(cache);
            }
            else if (logger.isLoggable(Level.WARNING))
            {
              StringBuffer msg = new StringBuffer();

              msg.append("found no String, Map constructor in ");
              msg.append(clazz.getName());
              logger.warning(msg.toString());
            }
          }
        }
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param parameters
   * @param node
   */
  private void parseParameterNode(Map<String, String> parameters, Node node)
  {
    NodeList children = node.getChildNodes();

    if (XmlUtil.hasContent(children))
    {
      String key = null;
      String value = null;

      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);
        String childName = child.getNodeName();
        String childValue = child.getTextContent();

        if ("param-name".equals(childName))
        {
          key = childValue;
        }
        else if ("param-value".equals(childName))
        {
          value = childValue;
        }
      }

      if ((key != null) && (value != null))
      {
        parameters.put(key, value);
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Map<String, ObjectCache> caches;

  /** Field description */
  private CacheMBeanManager mbeanManager;
}
