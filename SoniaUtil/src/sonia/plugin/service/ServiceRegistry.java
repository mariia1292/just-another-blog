/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.plugin.service;

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
   * @param clazz
   * @param path
   *
   * @return
   */
  public <T>ServiceReference<T> register(Class<T> clazz, String path)
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
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public <T>ServiceReference<T> get(Class<T> clazz, String path)
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
   * @param clazz
   * @param path
   * @param check
   *
   * @return
   */
  private <T>ServiceReference<T> register(Class<T> clazz, String path,
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

    for (ServiceListener listener : listeners)
    {
      listener.registered(reference);
    }

    return reference;
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
