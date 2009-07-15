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


package sonia.jobqueue;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class JobQueueTest
{

  /**
   * Method description
   *
   *
   * @throws InterruptedException
   */
  @Test
  public void test() throws InterruptedException
  {
    Logger.getLogger("sonia").setLevel(Level.FINEST);

    JobQueue<SleepJob> queue = new JobQueue<SleepJob>();

    queue.addListener(new SleepJobListener());

    for (int i = 0; i < 100; i++)
    {
      queue.add(new SleepJob(i));
    }

    queue.start();
    System.out.println(queue.count());

    for (int i = 100; i < 200; i++)
    {
      queue.add(new SleepJob(i));
    }

    System.out.println(queue.count());

    int i = 0;

    for (SleepJob job : queue.getJobs())
    {
      i++;
    }

    System.out.println(i);

    // Thread.sleep(1000 * 60 * 1);
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version    Enter version here..., 09/01/12
   * @author     Enter your name here...
   */
  private class SleepJob implements Job
  {

    /** Field description */
    private static final long serialVersionUID = -7434266792417661844L;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     *
     * @param id
     */
    public SleepJob(int id)
    {
      this.id = id;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @throws JobException
     */
    public void excecute() throws JobException
    {
      System.out.println("Hello from Job " + id + ", goto sleep");

      try
      {
        Thread.sleep(5000);
      }
      catch (InterruptedException ex)
      {
        throw new JobException(ex);
      }

      System.out.println("By from Job " + id);
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private int id;
  }


  /**
   * Class description
   *
   *
   * @version    Enter version here..., 09/02/14
   * @author     Enter your name here...
   */
  private class SleepJobListener implements JobListener
  {

    /**
     * Method description
     *
     *
     * @param job
     * @param ex
     */
    public void aborted(Job job, JobException ex)
    {
      System.out.println("abort " + job.getClass().getName());
      ex.printStackTrace();
    }

    /**
     * Method description
     *
     *
     * @param job
     */
    public void finished(Job job)
    {
      System.out.println("finish " + job.getClass().getName());
    }

    /**
     * Method description
     *
     *
     * @param job
     */
    public void started(Job job)
    {
      System.out.println("start " + job.getClass().getName());
    }
  }
}