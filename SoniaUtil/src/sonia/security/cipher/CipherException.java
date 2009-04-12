/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.security.cipher;

/**
 *
 * @author sdorra
 */
public class CipherException extends RuntimeException
{

  /** Field description */
  private static final long serialVersionUID = -3483127799497553612L;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public CipherException()
  {
    super();
  }

  /**
   * Constructs ...
   *
   *
   * @param message
   */
  public CipherException(String message)
  {
    super(message);
  }

  /**
   * Constructs ...
   *
   *
   * @param cause
   */
  public CipherException(Throwable cause)
  {
    super(cause);
  }

  /**
   * Constructs ...
   *
   *
   * @param message
   * @param cause
   */
  public CipherException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
