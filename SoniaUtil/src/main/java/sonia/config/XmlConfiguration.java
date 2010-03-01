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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import sonia.util.Util;
import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author Sebastian Sdorra
 */
public class XmlConfiguration extends StringBasedConfiguration
        implements LoadableConfiguration, StoreableConfiguration,
                   SecureConfiguration
{

  /**
   * Constructs ...
   *
   */
  public XmlConfiguration()
  {
    this.properties = new HashMap<String, String[]>();
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
    return properties.keySet();
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
      NodeList list = doc.getElementsByTagName("property");

      for (int i = 0; i < list.getLength(); i++)
      {
        Node node = list.item(i);

        if (node.getNodeName().equals("property"))
        {
          Node attriute = node.getAttributes().getNamedItem("name");
          String key = attriute.getTextContent();
          List<String> values = new ArrayList<String>();
          NodeList children = node.getChildNodes();

          for (int j = 0; j < children.getLength(); j++)
          {
            Node child = children.item(j);

            if (child.getNodeName().equals("value"))
            {
              values.add(child.getTextContent());
            }
          }

          properties.put(key, values.toArray(new String[0]));
        }
      }
    }
    catch (ParserConfigurationException ex)
    {
      getExceptionHandler().handleException(ex);
    }
    catch (SAXException ex)
    {
      getExceptionHandler().handleException(ex);
    }
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
    try
    {
      Document doc = XmlUtil.createDocument();
      Element root = doc.createElement("config");

      doc.appendChild(root);

      Set<String> keys = properties.keySet();

      for (String key : keys)
      {
        Element keyElement = doc.createElement("property");

        keyElement.setAttribute("name", key);

        String[] values = properties.get(key);

        for (String value : values)
        {
          Element valueElement = doc.createElement("value");

          valueElement.setTextContent(value);
          keyElement.appendChild(valueElement);
        }

        root.appendChild(keyElement);
      }

      Transformer transformer =
        TransformerFactory.newInstance().newTransformer();

      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.transform(new DOMSource(doc), new StreamResult(out));
    }
    catch (ParserConfigurationException ex)
    {
      getExceptionHandler().handleException(ex);
    }
    catch (TransformerConfigurationException ex)
    {
      getExceptionHandler().handleException(ex);
    }
    catch (TransformerException ex)
    {
      getExceptionHandler().handleException(ex);
    }
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
  public String getSecureString(String key)
  {
    if (cipher == null)
    {
      throw new IllegalStateException("no cipher found");
    }

    String result = null;
    String[] values = properties.get(key);

    if ((values != null) && (values.length > 0))
    {
      if ( Util.isNotEmpty( values[0] ) )
      {
        result = cipher.decode(values[0]);
      }
    }

    return result;
  }

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
    String result = null;
    String[] values = getStrings(key);

    if ((values != null) && (values.length > 0))
    {
      result = values[0];
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
  public String[] getStrings(String key)
  {
    return resolveVariables(properties.get(key));
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
    if (object != null)
    {
      String value = parseString(object);

      if (value != null)
      {
        String[] newValue = new String[] { value };
        String[] oldValue = properties.get(key);

        properties.put(key, newValue);

        if ((oldValue == null) ||!Arrays.deepEquals(oldValue, newValue))
        {
          fireConfigChangedEvent(key);
        }
      }
    }
    else
    {
      if (contains(key))
      {
        remove(key);
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
    int s = object.length;
    String[] value = new String[s];

    for (int i = 0; i < s; i++)
    {
      value[i] = parseString(object[i]);
    }

    properties.put(key, value);
    fireConfigChangedEvent(key);
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
  private Map<String, String[]> properties;
}
