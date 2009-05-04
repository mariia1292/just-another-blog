/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.Timer;

/**
 *
 * @author sdorra
 */
public class ExecUtil
{

  /**
   * Method description
   *
   *
   * @param cmd
   * @param args
   * @param timeout
   *
   * @return
   *
   * @throws IOException
   * @throws InterruptedException
   */
  public static int process(String cmd, String[] args, long timeout)
          throws IOException, InterruptedException
  {
    int result = 0;
    Process p = getProcess(cmd, args);
    ProcessInterruptScheduler task = new ProcessInterruptScheduler(p);
    Timer timer = new Timer();

    timer.schedule(task, timeout);
    result = p.waitFor();
    task.cancel();

    return result;
  }

  /**
   * Method description
   *
   *
   * @param cmd
   * @param args
   *
   * @return
   *
   * @throws IOException
   * @throws InterruptedException
   */
  public static int process(String cmd, String[] args)
          throws IOException, InterruptedException
  {
    Process p = getProcess(cmd, args);

    return p.waitFor();
  }

  /**
   * Method description
   *
   *
   * @param cmd
   *
   * @return
   *
   * @throws IOException
   * @throws InterruptedException
   */
  public static int process(String cmd) throws IOException, InterruptedException
  {
    return process(cmd, null);
  }

  /**
   * Method description
   *
   *
   * @param cmd
   * @param timeout
   *
   * @return
   *
   * @throws IOException
   * @throws InterruptedException
   */
  public static int process(String cmd, long timeout)
          throws IOException, InterruptedException
  {
    return process(cmd, null, timeout);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param cmd
   * @param args
   *
   * @return
   *
   * @throws IOException
   */
  private static Process getProcess(String cmd, String[] args)
          throws IOException
  {
    Process p = null;

    if (args != null)
    {
      p = Runtime.getRuntime().exec(cmd, args);
    }
    else
    {
      p = Runtime.getRuntime().exec(cmd);
    }

    return p;
  }
}
