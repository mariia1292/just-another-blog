/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.plugin;

/**
 *
 * @author sdorra
 */
public interface PluginStateListener
{

  /**
   * Method description
   *
   *
   * @param oldState
   * @param newState
   * @param plugin
   */
  public void stateChanged(int oldState, int newState, Plugin plugin);
}
