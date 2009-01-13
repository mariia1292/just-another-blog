/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jobqueue;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

/**
 *
 * @author sdorra
 */
public interface Job extends Serializable
{

  /**
   * Method description
   *
   *
   * @throws JobException
   */
  public void excecute() throws JobException;
}
