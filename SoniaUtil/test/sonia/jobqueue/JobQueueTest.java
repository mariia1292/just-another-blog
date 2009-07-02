/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jobqueue;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
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
