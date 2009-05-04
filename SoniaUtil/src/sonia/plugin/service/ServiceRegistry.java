/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.plugin.service;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import sonia.util.Util;
import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author sdorra
 */
public class ServiceRegistry
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ServiceRegistry.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public ServiceRegistry()
  {
    this(false);
  }

  /**
   * Constructs ...
   *
   *
   * @param autoRegister
   */
  public ServiceRegistry(boolean autoRegister)
  {
    this.autoRegister = autoRegister;
    this.services = new HashMap<ServiceKey, ServiceReference>();
    this.listeners = new ArrayList<ServiceListener>();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param listener
   */
  public void addListener(ServiceListener listener)
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
   * @param in
   *
   * @throws IOException
   */
  public void load(InputStream in) throws IOException
  {
    try
    {
      Document doc = XmlUtil.buildDocument(in);
      NodeList list = doc.getElementsByTagName("service");

      if (list != null)
      {
        for (int i = 0; i < list.getLength(); i++)
        {
          Node node = list.item(i);

          if ((node != null) && node.getNodeName().equals("service"))
          {
            loadService(node);
          }
        }
      }
    }
    catch (SAXException ex)
    {
      logger.log(Level.SEVERE, null, ex);

      throw new IOException(ex.getMessage());
    }
    catch (ParserConfigurationException ex)
    {
      logger.log(Level.SEVERE, null, ex);

      throw new IOException(ex.getMessage());
    }
  }

  /**
   * Method description
   *
   *
   * @param clazz
   * @param path
   * @param <T>
   *
   * @return
   */
  public <T> ServiceReference<T> register(Class<T> clazz, String path)
  {
    return register(clazz, path, true);
  }

  /**
   * Method description
   *
   *
   * @param listener
   */
  public void removeListener(ServiceListener listener)
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
   * @param clazz
   * @param path
   */
  public void unregister(Class clazz, String path)
  {
    ServiceKey key = new ServiceKey(clazz, path);
    ServiceReference reference = services.get(key);

    services.remove(key);

    for (ServiceListener listener : listeners)
    {
      listener.unregistered(reference);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param clazz
   * @param path
   * @param <T>
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public <T> ServiceReference<T> get(Class<T> clazz, String path)
  {
    ServiceReference<T> reference = services.get(new ServiceKey(clazz, path));

    if ((reference == null) && autoRegister)
    {
      reference = register(clazz, path, false);
    }

    return reference;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param node
   * @param ref
   */
  @SuppressWarnings("unchecked")
  private void loadImplementations(Node node, ServiceReference ref)
  {
    NodeList children = node.getChildNodes();

    if (XmlUtil.hasContent(children))
    {
      List list = new ArrayList();

      for (int i = 0; i < children.getLength(); i++)
      {
        Node item = children.item(i);

        if ((item != null) && item.getNodeName().equals("impl"))
        {
          Object impl = getImplementation(item);

          if (impl != null)
          {
            list.add(impl);
          }
        }
      }

      Collections.reverse(list);

      for (Object o : list)
      {
        ref.add(o);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param node
   */
  @SuppressWarnings("unchecked")
  private void loadService(Node node)
  {
    NamedNodeMap attributes = node.getAttributes();
    String className = XmlUtil.getAttributeValue(attributes, "class");
    String path = XmlUtil.getAttributeValue(attributes, "path");

    if (Util.hasContent(className) && Util.hasContent(path))
    {
      try
      {
        Class cl = Class.forName(className);
        ServiceReference reference = register(cl, path);

        loadImplementations(node, reference);
      }
      catch (ClassNotFoundException ex)
      {
        logger.log(Level.WARNING, null, ex);
      }
    }
    else
    {
      logger.warning("no valid service-tag found");
    }
  }

  /**
   * Method description
   *
   *
   * @param clazz
   * @param path
   * @param check
   * @param <T>
   *
   * @return
   */
  private <T> ServiceReference<T> register(Class<T> clazz, String path,
          boolean check)
  {
    ServiceKey key = new ServiceKey(clazz, path);

    if (check)
    {
      if (services.containsKey(key))
      {
        throw new ServiceAllreadyRegisteredException();
      }
    }

    ServiceReference<T> reference = new ServiceReference<T>(clazz, path);

    services.put(key, reference);

    if (logger.isLoggable(Level.FINEST))
    {
      StringBuffer log = new StringBuffer();

      log.append("register service ").append(clazz.getName());
      log.append(":").append(path);
      logger.finest(log.toString());
    }

    for (ServiceListener listener : listeners)
    {
      listener.registered(reference);
    }

    return reference;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param node
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  private Object getImplementation(Node node)
  {
    Object obj = null;
    String className = XmlUtil.getAttributeValue(node, "class");

    try
    {
      Class c = Class.forName(className);

      obj = c.newInstance();
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }

    return obj;
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version    Enter version here..., 09/01/10
   * @author     Enter your name here...
   */
  private static class ServiceKey
  {

    /**
     * Constructs ...
     *
     *
     * @param clazz
     * @param path
     */
    public ServiceKey(Class clazz, String path)
    {
      this.clazz = clazz;
      this.path = path;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     * TODO: only one return
     *
     * @param obj
     *
     * @return
     */
    @Override
    public boolean equals(Object obj)
    {
      if (obj == null)
      {
        return false;
      }

      if (getClass() != obj.getClass())
      {
        return false;
      }

      final ServiceKey other = (ServiceKey) obj;

      if ((this.clazz != other.clazz)
          && ((this.clazz == null) ||!this.clazz.equals(other.clazz)))
      {
        return false;
      }

      if ((this.path == null)
          ? (other.path != null)
          : !this.path.equals(other.path))
      {
        return false;
      }

      return true;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public int hashCode()
    {
      int hash = 7;

      hash = 31 * hash + ((this.clazz != null)
                          ? this.clazz.hashCode()
                          : 0);
      hash = 31 * hash + ((this.path != null)
                          ? this.path.hashCode()
                          : 0);

      return hash;
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private Class clazz;

    /** Field description */
    private String path;
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final List<ServiceListener> listeners;

  /** Field description */
  private boolean autoRegister;

  /** Field description */
  private Map<ServiceKey, ServiceReference> services;
}
