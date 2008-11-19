/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.plugin;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sdorra
 */
public class ServiceRegistry
{

  /**
   * Constructs ...
   *
   */
  public ServiceRegistry()
  {
    this.services = new HashMap<String, ServiceReference>();
    this.listeners = new ArrayList<ServiceListener>();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param listener
   */
  public void addServiceListener(ServiceListener listener)
  {
    synchronized (listeners)
    {
      this.listeners.add(listener);
    }
  }

  /**
   * Method description
   *
   *
   * @param path
   *
   * @return
   */
  public ServiceReference registerService(String path)
  {
    ServiceReference reference = new ServiceReference();

    this.services.put(path, reference);

    for (ServiceListener listener : listeners)
    {
      listener.registered(path, reference);
    }

    return reference;
  }

  /**
   * Method description
   *
   *
   * @param listener
   */
  public void removeServiceListener(ServiceListener listener)
  {
    synchronized (listeners)
    {
      this.listeners.remove(listener);
    }
  }

  /**
   * Method description
   *
   *
   * @param path
   *
   * @return
   */
  public ServiceReference unregisterService(String path)
  {
    ServiceReference reference = services.get(path);

    if (reference != null)
    {
      this.services.remove(path);

      for (ServiceListener listener : listeners)
      {
        listener.unregistered(path, reference);
      }
    }

    return reference;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param path
   *
   * @return
   */
  public ServiceReference getServiceReference(String path)
  {
    return services.get(path);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<ServiceListener> listeners;

  /** Field description */
  private Map<String, ServiceReference> services;
}
