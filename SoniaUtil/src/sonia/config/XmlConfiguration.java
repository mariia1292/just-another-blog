/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
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
 * @author sdorra
 */
public class XmlConfiguration extends StringBasedConfiguration
        implements LoadableConfiguration, StoreableConfiguration
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
      result = cipher.decode(values[0]);
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
        properties.put(key, new String[] { value });
        fireConfigChangedEvent(key);
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

    value = cipher.encode(value);
    set(key, value);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   */
  private void fireConfigChangedEvent(String key)
  {
    for (ConfigurationListener listener : listeners)
    {
      listener.configChanged(this, key);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final List<ConfigurationListener> listeners;

  /** Field description */
  private Map<String, String[]> properties;
}
