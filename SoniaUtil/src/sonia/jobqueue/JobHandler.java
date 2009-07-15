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

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class JobHandler extends Thread
{

  /** Field description */
  private static Logger logger = Logger.getLogger(JobHandler.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param queue
   * @param handlerNumber
   */
  public JobHandler(JobQueue queue, int handlerNumber)
  {
    this.queue = queue;
    this.handlerNumber = handlerNumber;
    this.stop = false;

    if (logger.isLoggable(Level.FINE))
    {
      logger.fine("JH" + handlerNumber + " initializing");
    }

    name = "JobHandler-" + handlerNumber;
    setName(name);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  @Override
  public void run()
  {
    if (logger.isLoggable(Level.FINE))
    {
      logger.fine(name + " started");
    }

    while (!stop)
    {
      Job job = queue.nextJob();

      if (job != null)
      {
        if (logger.isLoggable(Level.FINER))
        {
          logger.finer(name + " start job ");
        }

        queue.fireStartedEvent(job);

        try
        {
          job.excecute();

          if (logger.isLoggable(Level.FINER))
          {
            logger.finer(name + " finished job ");
          }

          queue.fireFinishedEvent(job);
        }
        catch (JobException ex)
        {
          if (logger.isLoggable(Level.FINER))
          {
            logger.log(Level.FINER, name + " job exited with exception", ex);
          }

          queue.fireFinishedEvent(job, ex);
        }

        synchronized (job)
        {
          job.notify();
        }
      }
      else
      {
        try
        {
          if (logger.isLoggable(Level.FINEST))
          {
            logger.finest(name + " is going in wait mode ");
          }

          synchronized (queue.getQueue())
          {
            queue.getQueue().wait();
          }
        }
        catch (InterruptedException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }

    if (logger.isLoggable(Level.FINE))
    {
      logger.fine(name + " exiting");
    }
  }

  /**
   * Method description
   *
   */
  void stopWork()
  {
    this.stop = true;

    if (logger.isLoggable(Level.FINE))
    {
      logger.fine(name + " is going in stop mode");
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private int handlerNumber;

  /** Field description */
  private String name;

  /** Field description */
  private JobQueue queue;

  /** Field description */
  private boolean stop;
}