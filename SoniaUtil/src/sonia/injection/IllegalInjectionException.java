/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.injection;

/**
 *
 * @author sdorra
 */
public class IllegalInjectionException extends RuntimeException
{

  /** Field description */
  private static final long serialVersionUID = -8886299900963209943L;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public IllegalInjectionException() {}

  /**
   * Constructs ...
   *
   *
   * @param msg
   */
  public IllegalInjectionException(String msg)
  {
    super(msg);
  }

  /**
   * Constructs ...
   *
   *
   * @param throwable
   */
  public IllegalInjectionException(Throwable throwable)
  {
    super(throwable);
  }

  /**
   * Constructs ...
   *
   *
   * @param msg
   * @param throwable
   */
  public IllegalInjectionException(String msg, Throwable throwable)
  {
    super(msg, throwable);
  }
}
