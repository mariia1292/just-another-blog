/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://kenai.com/projects/jab
 *
 */



package sonia.util;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Timer;

/**
 *
 * @author Sebastian Sdorra
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
