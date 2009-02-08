/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

/**
 *
 * @author sdorra
 */
public class BlogRuntimeException extends RuntimeException
{

  /** Field description */
  private static final long serialVersionUID = -976238857249007399L;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public BlogRuntimeException()
  {
    super();
  }

  /**
   * Constructs ...
   *
   *
   * @param msg
   */
  public BlogRuntimeException(String msg)
  {
    super(msg);
  }

  /**
   * Constructs ...
   *
   *
   * @param cause
   */
  public BlogRuntimeException(Throwable cause)
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
  public BlogRuntimeException(String msg, Throwable cause)
  {
    super(msg, cause);
  }
}
