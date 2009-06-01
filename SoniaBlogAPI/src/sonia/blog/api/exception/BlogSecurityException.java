/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.exception;

/**
 *
 * @author sdorra
 */
public class BlogSecurityException extends BlogException
{

  /** Field description */
  private static final long serialVersionUID = 6763017825251720042L;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public BlogSecurityException()
  {
    super();
  }

  /**
   * Constructs ...
   *
   *
   * @param msg
   */
  public BlogSecurityException(String msg)
  {
    super(msg);
  }

  /**
   * Constructs ...
   *
   *
   * @param cause
   */
  public BlogSecurityException(Throwable cause)
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
  public BlogSecurityException(String msg, Throwable cause)
  {
    super(msg, cause);
  }
}
