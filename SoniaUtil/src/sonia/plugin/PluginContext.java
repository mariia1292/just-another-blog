/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.plugin;

//~--- non-JDK imports --------------------------------------------------------

import sonia.injection.InjectionProvider;

import sonia.plugin.service.ServiceRegistry;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sdorra
 */
public class PluginContext
{

  /**
   * Constructs ...
   *
   */
  public PluginContext()
  {
    this.serviceRegistry = new ServiceRegistry();
    this.listeners = new ArrayList<PluginStateListener>();
    this.plugins = new HashMap<String, Plugin>();
    this.store = new DefaultPluginStore();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param listener
   */
  public void addStateChangeListener(PluginStateListener listener)
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
   * @param plugin
   */
  public void register(Plugin plugin)
  {
    plugin.setState(Plugin.STATE_REGISTERED);
    this.plugins.put(plugin.getName(), plugin);
    fireStateChange(Plugin.STATE_UNREGISTERED, plugin);

    if (plugin.isAutostart() && store.isStartAble(plugin))
    {
      start(plugin);
    }
  }

  /**
   * Method description
   *
   *
   * @param listener
   */
  public void removeStateChangeListener(PluginStateListener listener)
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
   * @param classpath
   *
   * @throws IOException
   */
  public void searchClasspath(String classpath) throws IOException
  {
    PluginReader reader = new PluginReader(this);

    reader.readClasspath(classpath);
  }

  /**
   * Method description
   *
   */
  public void shutdown()
  {
    for (Plugin plugin : plugins.values())
    {
      stop(plugin, false);
      unregister(plugin);
    }
  }

  /**
   * Method description
   *
   *
   * @param plugin
   */
  public void start(Plugin plugin)
  {
    int oldState = plugin.getState();

    plugin.getActivator().start(this);
    plugin.setState(Plugin.STATE_STARTED);
    store.startPlugin(plugin);
    fireStateChange(oldState, plugin);
  }

  /**
   * Method description
   *
   *
   * @param plugin
   */
  public void stop(Plugin plugin)
  {
    stop(plugin, true);
  }

  /**
   * Method description
   *
   *
   * @param name
   */
  public void unregister(String name)
  {
    Plugin plugin = plugins.get(name);

    if (plugin != null)
    {
      unregister(plugin);
    }
  }

  /**
   * Method description
   *
   *
   * @param plugin
   */
  public void unregister(Plugin plugin)
  {
    int oldState = plugin.getState();

    plugin.setState(Plugin.STATE_UNREGISTERED);
    fireStateChange(oldState, plugin);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public InjectionProvider getInjectionProvider()
  {
    return injectionProvider;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Plugin> getPlugins()
  {
    return new ArrayList<Plugin>(plugins.values());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ServiceRegistry getServiceRegistry()
  {
    return serviceRegistry;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public PluginStore getStore()
  {
    return store;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param injectionProvider
   */
  public void setInjectionProvider(InjectionProvider injectionProvider)
  {
    this.injectionProvider = injectionProvider;
  }

  /**
   * Method description
   *
   *
   * @param serviceRegistry
   */
  public void setServiceRegistry(ServiceRegistry serviceRegistry)
  {
    this.serviceRegistry = serviceRegistry;
  }

  /**
   * Method description
   *
   *
   * @param store
   */
  public void setStore(PluginStore store)
  {
    this.store = store;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param oldState
   * @param plugin
   */
  private void fireStateChange(int oldState, Plugin plugin)
  {
    for (PluginStateListener listener : listeners)
    {
      listener.stateChanged(oldState, plugin.getState(), plugin);
    }
  }

  /**
   * Method description
   *
   *
   * @param plugin
   * @param save
   */
  private void stop(Plugin plugin, boolean save)
  {
    int oldState = plugin.getState();

    plugin.getActivator().stop(this);
    plugin.setState(Plugin.STATE_STOPED);

    if (save)
    {
      store.stopPlugin(plugin);
    }

    fireStateChange(oldState, plugin);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final List<PluginStateListener> listeners;

  /** Field description */
  private InjectionProvider injectionProvider;

  /** Field description */
  private Map<String, Plugin> plugins;

  /** Field description */
  private ServiceRegistry serviceRegistry;

  /** Field description */
  private PluginStore store;
}
