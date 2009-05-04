/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.util;

//~--- JDK imports ------------------------------------------------------------

import java.util.TimerTask;

/**
 *
 * @author sdorra
 */
public class ProcessInterruptScheduler extends TimerTask
{

  /**
   * Constructs ...
   *
   *
   * @param p
   */
  public ProcessInterruptScheduler(Process p)
  {
    this.p = p;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  @Override
  public void run()
  {
    p.destroy();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Process p;
}
