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
public abstract class Job implements Serializable
{

  /**
   * Method description
   *
   *
   * @throws JobException
   */
  public abstract void excecute() throws JobException;

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isFinished()
  {
    return finished;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param finished
   */
  void setFinished(boolean finished)
  {
    this.finished = finished;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private boolean finished = false;
}
