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



package sonia.config;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author Sebastian Sdorra
 */
public class PropertiesConfiguration extends StringBasedConfiguration
        implements StoreableConfiguration, LoadableConfiguration,
                   SecureConfiguration
{

  /**
   * Constructs ...
   *
   */
  public PropertiesConfiguration()
  {
    this.properties = new Properties();
    this.listeners = new ArrayList<ConfigurationListener>();
  }

  /**
   * Constructs ...
   *
   *
   * @param properties
   */
  public PropertiesConfiguration(Properties properties)
  {
    this.properties = properties;
    this.listeners = new ArrayList<ConfigurationListener>();
  }

  /**
   * Constructs ...
   *
   *
   * @param delimeter
   */
  public PropertiesConfiguration(String delimeter)
  {
    this.delimeter = delimeter;
    this.properties = new Properties();
    this.listeners = new ArrayList<ConfigurationListener>();
  }

  /**
   * Constructs ...
   *
   *
   * @param properties
   * @param delimeter
   */
  public PropertiesConfiguration(Properties properties, String delimeter)
  {
    this.properties = properties;
    this.delimeter = delimeter;
    this.listeners = new ArrayList<ConfigurationListener>();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param listener
   */
  public void addListener(ConfigurationListener listener)
  {
    synchronized (listeners)
    {
      listeners.add(listener);
    }
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public boolean contains(String key)
  {
    return properties.containsKey(key);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Set<String> keySet()
  {
    Set<String> keys = new HashSet<String>();

    for (Object obj : properties.keySet())
    {
      keys.add((String) obj);
    }

    return keys;
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
    properties.load(in);
  }

  /**
   * Method description
   *
   *
   * @param key
   */
  public void remove(String key)
  {
    properties.remove(key);
    fireConfigChangedEvent(key);
  }

  /**
   * Method description
   *
   *
   * @param listener
   */
  public void removeListener(ConfigurationListener listener)
  {
    synchronized (listeners)
    {
      listeners.remove(listener);
    }
  }

  /**
   * Method description
   *
   *
   * @param out
   *
   * @throws IOException
   */
  public void store(OutputStream out) throws IOException
  {
    properties.store(out, "SoniaConfiguration");
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public String getSecureString(String key, String def)
  {
    String result = getSecureString(key);

    if (result == null)
    {
      result = def;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public String getSecureString(String key)
  {
    if (cipher == null)
    {
      throw new IllegalStateException("no cipher found");
    }

    String result = null;
    String value = properties.getProperty(key);

    if ((value != null) && (value.length() > 0))
    {
      result = cipher.decode(value);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getSize()
  {
    return properties.size();
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public String getString(String key)
  {
    return resolveVariable(properties.getProperty(key));
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public String[] getStrings(String key)
  {
    String[] result = null;
    String value = properties.getProperty(key);

    if (value != null)
    {
      result = value.split(delimeter);
    }

    return resolveVariables(result);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isEmpty()
  {
    return properties.isEmpty();
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   * @param object
   */
  public void set(String key, Object object)
  {
    String value = parseString(object);

    if (!isBlank(value))
    {
      String oldValue = properties.getProperty(key);
      properties.setProperty(key, value);
      if ( oldValue == null || ! value.equals( oldValue ) ){
        fireConfigChangedEvent(key);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param object
   */
  public void setMulti(String key, Object[] object)
  {
    StringBuffer buffer = new StringBuffer();
    int s = object.length;

    for (int i = 0; i < s; i++)
    {
      String v = parseString(object[i]);

      if (!isBlank(v))
      {
        buffer.append(v);
      }

      if (i < s)
      {
        buffer.append(delimeter);
      }
    }

    if (buffer.length() > 0)
    {
      properties.setProperty(key, buffer.toString());
      fireConfigChangedEvent(key);
    }
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   */
  public void setSecureString(String key, String value)
  {
    if (cipher == null)
    {
      throw new IllegalStateException("no cipher found");
    }

    if (Util.hasContent(value))
    {
      value = cipher.encode(value);
    }

    set(key, value);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   */
  protected void fireConfigChangedEvent(String key)
  {
    for (ConfigurationListener listener : listeners)
    {
      listener.configChanged(this, key);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected final List<ConfigurationListener> listeners;

  /** Field description */
  protected String delimeter = ";";

  /** Field description */
  protected Properties properties;
}
