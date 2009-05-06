/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
  private boolean stop;

  /** Field description */
  private int timeoutLimit;
}
