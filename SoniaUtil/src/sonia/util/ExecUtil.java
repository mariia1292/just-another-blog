/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.util;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
  public static String processWithOutput(String cmd, String[] args,
          long timeout)
          throws IOException, InterruptedException
  {
    String result = null;
    Process p = getProcess(cmd, args);
    ProcessInterruptScheduler task = new ProcessInterruptScheduler(p);
    Timer timer = new Timer();

    timer.schedule(task, timeout);
    result = getOutput(p);

    return result;
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
  public static String processWithOutput(String cmd, long timeout)
          throws IOException, InterruptedException
  {
    System.out.println(cmd);

    return processWithOutput(cmd, null, timeout);
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
  public static String processWithOutput(String cmd)
          throws IOException, InterruptedException
  {
    Process p = getProcess(cmd, null);

    return getOutput(p);
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
  public static String processWithOutput(String cmd, String[] args)
          throws IOException, InterruptedException
  {
    Process p = getProcess(cmd, args);

    return getOutput(p);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param p
   *
   * @return
   *
   * @throws IOException
   */
  private static String getOutput(Process p) throws IOException
  {
    StringBuffer result = new StringBuffer();
    boolean first = true;
    BufferedReader reader = null;

    try
    {
      reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

      String line = reader.readLine();

      while (line != null)
      {
        System.out.println("LINE:" + line);

        if (!first)
        {
          result.append("\n");
          first = false;
        }
        else
        {
          first = true;
        }

        result.append(line);
        line = reader.readLine();
      }
    }
    finally
    {
      if (reader != null)
      {
        reader.close();
      }
    }

    return result.toString();
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
