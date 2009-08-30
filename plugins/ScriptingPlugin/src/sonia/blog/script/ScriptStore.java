/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.script;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import sonia.util.Util;
import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author Sebastian Sdorra
 */
public class ScriptStore
{

  /** Field description */
  private static Logger logger = Logger.getLogger(ScriptStore.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param file
   */
  public ScriptStore(File file)
  {
    this.store = new HashMap<String, List<String>>();
    this.file = file;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws IOException
   */
  public void load() throws IOException
  {
    if ((file != null) && file.exists())
    {
      try
      {
        store.clear();

        Document document = ScriptManager.getInstance().getDocument(file);
        Element root = document.getDocumentElement();
        NodeList childNodes = root.getChildNodes();

        if (XmlUtil.hasContent(childNodes))
        {
          for (int i = 0; i < childNodes.getLength(); i++)
          {
            Node childNode = childNodes.item(i);
            String nodeName = childNode.getNodeName();

            if (nodeName.equals("entry"))
            {
              parseEntry(childNode);
            }
          }
        }
      }
      catch (SAXException ex)
      {
        logger.log(Level.SEVERE, null, ex);

        throw new IOException(ex.getMessage());
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   */
  public void put(String key, String value)
  {
    List<String> values = store.get(key);

    if (values == null)
    {
      values = new ArrayList<String>();
      store.put(key, values);
    }

    values.add(value);
  }

  /**
   * Method description
   *
   *
   * @param key
   */
  public void remove(String key)
  {
    store.remove(key);
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   */
  public void remove(String key, String value)
  {
    List<String> values = store.get(key);

    if (values != null)
    {
      values.remove(value);
    }
  }

  /**
   * Method description
   *
   *
   * @throws IOException
   */
  public void store() throws IOException
  {
    if (file != null)
    {
      try
      {
        Document document = ScriptManager.getInstance().getDocument();
        Element root = document.createElement("script-store");

        for (String key : store.keySet())
        {
          Element entryEl = document.createElement("entry");
          Element keyEl = document.createElement("key");

          keyEl.setTextContent(key);
          entryEl.appendChild(keyEl);

          List<String> values = store.get(key);

          if (values != null)
          {
            for (String value : values)
            {
              Element valueEl = document.createElement("value");

              valueEl.setTextContent(value);
              entryEl.appendChild(valueEl);
            }
          }

          root.appendChild(entryEl);
        }

        document.appendChild(root);

        Transformer transformer =
          TransformerFactory.newInstance().newTransformer();

        transformer.transform(new DOMSource(document), new StreamResult(file));
      }
      catch (TransformerException ex)
      {
        logger.log(Level.SEVERE, null, ex);

        throw new IOException(ex.getMessage());
      }
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
  public String get(String key)
  {
    String result = null;
    List<String> values = store.get(key);

    if (values != null)
    {
      result = values.get(0);
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
  public List<String> getAll(String key)
  {
    return store.get(key);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param node
   */
  private void parseEntry(Node node)
  {
    String key = null;
    List<String> values = null;
    NodeList childNodes = node.getChildNodes();

    if (XmlUtil.hasContent(childNodes))
    {
      values = new ArrayList<String>();

      for (int i = 0; i < childNodes.getLength(); i++)
      {
        Node childNode = childNodes.item(i);
        String name = childNode.getNodeName();
        String value = childNode.getTextContent();

        if (name.equals("key"))
        {
          key = value;
        }
        else if (name.equals("value"))
        {
          values.add(value);
        }
      }
    }

    if (Util.hasContent(key))
    {
      store.put(key, values);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private File file;

  /** Field description */
  private Map<String, List<String>> store;
}
