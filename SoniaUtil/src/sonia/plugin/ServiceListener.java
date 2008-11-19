/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.plugin;

/**
 *
 * @author sdorra
 */
public interface ServiceListener
{

  /**
   * Method description
   *
   *
   * @param path
   * @param reference
   */
  public void registered(String path, ServiceReference reference);

  /**
   * Method description
   *
   *
   * @param path
   * @param reference
   */
  public void unregistered(String path, ServiceReference reference);
}
