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

import sonia.logging.SimpleFormatter;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class JobQueueTest
{

  /** Field description */
  private static int COUNTER = 0;

  /** Field description */
  private static boolean ABORT = false;

  /** Field description */
  private static boolean START = false;

  /** Field description */
  private static boolean FINISH = false;

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  @Test
  public void queueListenerTest()
  {
    ABORT = false;
    START = false;
    FINISH = false;

    JobQueue<ExceptionJob> queue = new JobQueue<ExceptionJob>(1);
    ExceptionJobListener listener = new ExceptionJobListener();

    queue.addListener(listener);
    queue.start();
    queue.processs(new ExceptionJob());
    assertTrue(START);
    assertTrue(ABORT);
    assertFalse(FINISH);
    ABORT = false;
    START = false;
    FINISH = false;
    queue.processs(new ExceptionJob(false));
    assertTrue(START);
    assertFalse(ABORT);
    assertTrue(FINISH);
    queue.stop();
    queue.removeListener(listener);
    listener = null;
    queue = null;
  }

  /**
   * Method description
   *
   *
   * @throws InterruptedException
   */
  @Test
  public void simpleQueueTest() throws InterruptedException
  {
    COUNTER = 0;

    JobQueue<SleepJob> queue = new JobQueue<SleepJob>(1);

    assertTrue(queue.getHandlerCount() == 1);
    assertTrue(queue.getMaxJobs() == 0);
    assertFalse(queue.isRunning());
    queue.add(new SleepJob());
    queue.add(new SleepJob());
    queue.add(new SleepJob());
    assertTrue(queue.getJobs().size() == 3);
    assertTrue(COUNTER == 0);
    queue.start();
    assertTrue(queue.isRunning());
    assertTrue(COUNTER == 0);
    Thread.sleep(300l);
    assertTrue(COUNTER == 3);
    assertTrue(queue.isRunning());
    assertTrue(queue.getMaxJobs() == 3);

    long time = System.currentTimeMillis();

    queue.processs(new SleepJob());
    assertTrue((System.currentTimeMillis() - time) >= 50);
    assertTrue(queue.getMaxJobs() == 3);
    assertTrue(queue.count() == 0);
    queue.stop();
    assertFalse(queue.isRunning());
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/08/02
   * @author         Enter your name here...
   */
  private class ExceptionJob implements Job
  {

    /**
     * Constructs ...
     *
     */
    public ExceptionJob()
    {
      this.throwEx = true;
    }

    /**
     * Constructs ...
     *
     *
     * @param throwEx
     */
    public ExceptionJob(boolean throwEx)
    {
      this.throwEx = throwEx;
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
      if (throwEx)
      {
        throw new JobException("test");
      }
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private boolean throwEx;
  }


  /**
   * Class description
   *
   *
   * @version    Enter version here..., 09/02/14
   * @author     Enter your name here...
   */
  private class ExceptionJobListener implements JobListener
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
      ABORT = true;
    }

    /**
     * Method description
     *
     *
     * @param job
     */
    public void finished(Job job)
    {
      FINISH = true;
    }

    /**
     * Method description
     *
     *
     * @param job
     */
    public void started(Job job)
    {
      START = true;
    }
  }


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

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @throws JobException
     */
    public void excecute() throws JobException
    {
      try
      {
        Thread.sleep(50l);
        COUNTER++;
      }
      catch (InterruptedException ex)
      {
        throw new JobException(ex);
      }
    }
  }
}
