/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.util.AbstractBean;

import sonia.plugin.Plugin;
import sonia.plugin.PluginContext;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author sdorra
 */
public class PluginBean extends AbstractBean
{

  /**
   * Constructs ...
   *
   */
  public PluginBean()
  {
    context = BlogContext.getInstance().getPluginContext();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String start()
  {
    Plugin plugin = (Plugin) plugins.getRowData();

    if (plugin != null)
    {
      context.start(plugin);
      getMessageHandler().info("pluginStarted");
    }

    return SUCCESS;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String stop()
  {
    Plugin plugin = (Plugin) plugins.getRowData();

    if (plugin != null)
    {
      context.stop(plugin);
      getMessageHandler().info("pluginStopped");
    }

    return SUCCESS;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getPlugins()
  {
    plugins = new ListDataModel(context.getPlugins());

    return plugins;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private PluginContext context;

  /** Field description */
  private DataModel plugins;
}
