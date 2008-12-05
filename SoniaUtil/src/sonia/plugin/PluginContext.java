/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.plugin;

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

    if (plugin.isAutostart())
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
    int oldState = plugin.getState();

    plugin.getActivator().stop(this);
    plugin.setState(Plugin.STATE_STOPED);
    fireStateChange(oldState, plugin);
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
  public ServiceRegistry getServiceRegistry()
  {
    return serviceRegistry;
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<PluginStateListener> listeners;

  /** Field description */
  private Map<String, Plugin> plugins;

  /** Field description */
  private ServiceRegistry serviceRegistry;
}
