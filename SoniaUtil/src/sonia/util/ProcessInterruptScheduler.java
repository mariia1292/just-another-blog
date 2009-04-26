/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sonia.util;

import java.util.TimerTask;

/**
 *
 * @author sdorra
 */
public class ProcessInterruptScheduler extends TimerTask
{

  private Process p;

  public ProcessInterruptScheduler( Process p )
  {
    this.p = p;
  }



  @Override
  public void run()
  {
    p.destroy();
  }

}
