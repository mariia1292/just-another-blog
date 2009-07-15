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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 *
 * @param <T>
 */
public class JobQueue<T extends Job>
{

  /** Field description */
  private static Logger logger = Logger.getLogger(JobQueue.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public JobQueue()
  {
    this(Runtime.getRuntime().availableProcessors() * 2);
  }

  /**
   * Constructs ...
   *
   *
   * @param handlerCount
   */
  public JobQueue(int handlerCount)
  {
    this.listeners = new ArrayList<JobListener>();
    this.jobs = new LinkedList<T>();
    this.stop = true;
    this.timeoutLimit = 60;
    this.maxJobs = 0;
    this.handlerCount = handlerCount;
    this.handlers = new ArrayList<JobHandler>();

    if (logger.isLoggable(Level.FINE))
    {
      logger.fine("init JobQueue with " + handlerCount + " handlers");
    }
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param job
   */
  public void add(T job)
  {
    if (logger.isLoggable(Level.FINEST))
    {
      logger.finest("adding job to jobqueue");
    }

    synchronized (jobs)
    {
      jobs.offer(job);

      int size = jobs.size();

      if (size > maxJobs)
      {
        maxJobs = size;
      }

      jobs.notify();
    }
  }

  /**
   * Method description
   *
   *
   * @param listener
   */
  public void addListener(JobListener listener)
  {
    synchronized (listeners)
    {
      this.listeners.add(listener);
    }
  }

  /**
   * Method description
   *
   */
  public void clear()
  {
    synchronized (jobs)
    {
      jobs.clear();
    }
  }

  /**
   * Method description
   *
   *
   * @param job
   *
   * @return
   */
  public boolean contains(T job)
  {
    return jobs.contains(job);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int count()
  {
    return this.jobs.size();
  }

  /**
   * Method description
   *
   *
   * @param job
   *
   */
  public void processs(T job)
  {
    add(job);

    try
    {
      synchronized (job)
      {
        job.wait(1000l * timeoutLimit);
      }
    }
    catch (InterruptedException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Method description
   *
   *
   * @param listener
   */
  public void removeListener(JobListener listener)
  {
    synchronized (listeners)
    {
      this.listeners.remove(listener);
    }
  }

  /**
   * Method description
   *
   */
  public void start()
  {

    // TODO: check if allready runninng
    this.stop = false;
    handlers.clear();

    for (int i = 0; i < handlerCount; i++)
    {
      JobHandler handler = new JobHandler(this, i);

      handler.start();
      handlers.add(handler);
    }
  }

  /**
   * Method description
   *
   */
  public void stop()
  {
    this.stop = true;

    for (JobHandler handler : handlers)
    {
      handler.stopWork();
    }

    synchronized (jobs)
    {
      jobs.notifyAll();
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public int getHandlerCount()
  {
    return handlerCount;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Collection<T> getJobs()
  {
    return jobs;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getMaxJobs()
  {
    return maxJobs;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getTimeoutLimit()
  {
    return timeoutLimit;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isRunning()
  {
    return !stop;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param handlerCount
   */
  public void setHandlerCount(int handlerCount)
  {
    this.handlerCount = handlerCount;
  }

  /**
   * Method description
   *
   *
   * @param timeoutLimit
   */
  public void setTimeoutLimit(int timeoutLimit)
  {
    this.timeoutLimit = timeoutLimit;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param job
   */
  void fireFinishedEvent(Job job)
  {
    for (JobListener listener : listeners)
    {
      listener.finished(job);
    }
  }

  /**
   * Method description
   *
   *
   * @param job
   * @param ex
   */
  void fireFinishedEvent(Job job, JobException ex)
  {
    for (JobListener listener : listeners)
    {
      listener.aborted(job, ex);
    }
  }

  /**
   * Method description
   *
   *
   * @param job
   */
  void fireStartedEvent(Job job)
  {
    for (JobListener listener : listeners)
    {
      listener.started(job);
    }
  }

  /**
   * Method description
   *
   *
   * @return
   */
  Job nextJob()
  {
    Job job = null;

    synchronized (jobs)
    {
      job = jobs.poll();
    }

    return job;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  LinkedList<T> getQueue()
  {
    return jobs;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final LinkedList<T> jobs;

  /** Field description */
  private final List<JobListener> listeners;

  /** Field description */
  private int handlerCount;

  /** Field description */
  private List<JobHandler> handlers;

  /** Field description */
  private int maxJobs;

  /** Field description */
  private boolean stop;

  /** Field description */
  private int timeoutLimit;
}