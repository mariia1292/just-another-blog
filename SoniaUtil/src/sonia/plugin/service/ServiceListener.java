/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sonia.plugin.service;

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
  public void registered(ServiceReference reference);

  /**
   * Method description
   *
   *
   * @param path
   * @param reference
   */
  public void unregistered(ServiceReference reference);

}
