/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

/**
 *
 * @author sdorra
 */
public class DAOException extends RuntimeException
{

  /** Field description */
  private static final long serialVersionUID = -5933842467933897502L;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public DAOException() {}

  /**
   * Constructs ...
   *
   *
   * @param message
   */
  public DAOException(String message)
  {
    super(message);
  }

  /**
   * Constructs ...
   *
   *
   * @param cause
   */
  public DAOException(Throwable cause)
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
  public DAOException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
