/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.plugin;

/**
 *
 * @author sdorra
 */
public interface PluginStore
{

  /**
   * Method description
   *
   *
   * @param plugin
   */
  public void startPlugin(Plugin plugin);

  /**
   * Method description
   *
   *
   * @param plugin
   */
  public void stopPlugin(Plugin plugin);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param plugin
   *
   * @return
   */
  public boolean isStartAble(Plugin plugin);
}
