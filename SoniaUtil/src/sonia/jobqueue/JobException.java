/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jobqueue;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

/**
 *
 * @author sdorra
 */
public class JobException extends IOException
{

  /** Field description */
  private static final long serialVersionUID = -976238857249007399L;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public JobException()
  {
    super();
  }

  /**
   * Constructs ...
   *
   *
   * @param msg
   */
  public JobException(String msg)
  {
    super(msg);
  }

  /**
   * Constructs ...
   *
   *
   * @param cause
   */
  public JobException(Throwable cause)
  {
    super(cause);
  }

  /**
   * Constructs ...
   *
   *
   * @param msg
   * @param cause
   */
  public JobException(String msg, Throwable cause)
  {
    super(msg, cause);
  }
}
