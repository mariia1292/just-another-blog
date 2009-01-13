/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jobqueue;

/**
 *
 * @author sdorra
 */
public interface JobListener
{

  /**
   * Method description
   *
   *
   * @param job
   * @param ex
   */
  public void aborted(Job job, JobException ex);

  /**
   * Method description
   *
   *
   * @param job
   */
  public void finished(Job job);

  /**
   * Method description
   *
   *
   * @param job
   */
  public void started(Job job);
}
