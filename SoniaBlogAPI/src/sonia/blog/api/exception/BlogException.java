/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.exception;

/**
 *
 * @author sdorra
 */
public class BlogException extends RuntimeException
{

  /** Field description */
  private static final long serialVersionUID = -976238857249007399L;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public BlogException()
  {
    super();
  }

  /**
   * Constructs ...
   *
   *
   * @param msg
   */
  public BlogException(String msg)
  {
    super(msg);
  }

  /**
   * Constructs ...
   *
   *
   * @param cause
   */
  public BlogException(Throwable cause)
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
  public BlogException(String msg, Throwable cause)
  {
    super(msg, cause);
  }
}
