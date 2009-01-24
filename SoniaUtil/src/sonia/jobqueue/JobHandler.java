/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jobqueue;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
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
      logger.fine("JH" + handlerNumber + " started");
    }

    while (!stop)
    {
      Job job = queue.nextJob();

      if (job != null)
      {
        if (logger.isLoggable(Level.FINER))
        {
          logger.finer("JH" + handlerNumber + " start job ");
        }

        queue.fireStartedEvent(job);

        try
        {
          job.excecute();

          if (logger.isLoggable(Level.FINER))
          {
            logger.finer("JH" + handlerNumber + " finished job ");
          }

          queue.fireFinishedEvent(job);
        }
        catch (JobException ex)
        {
          if (logger.isLoggable(Level.FINER))
          {
            logger.finer("JH" + handlerNumber + " job exited with exception");
          }

          queue.fireFinishedEvent(job, ex);
        }
        job.setFinished(true);
      }
      else
      {
        try
        {
          if (logger.isLoggable(Level.FINEST))
          {
            logger.finest("JH" + handlerNumber + " is going in wait mode ");
          }
          synchronized ( queue.getQueue() )
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
      logger.fine("JH" + handlerNumber + " exiting");
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
      logger.fine("JH" + handlerNumber + " is going in stop mode");
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private int handlerNumber;

  /** Field description */
  private JobQueue queue;

  /** Field description */
  private boolean stop;
}
