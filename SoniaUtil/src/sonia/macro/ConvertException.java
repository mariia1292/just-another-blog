/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro;

/**
 *
 * @author sdorra
 */
public class ConvertException extends Exception
{

  /** Field description */
  private static final long serialVersionUID = -3640543228967330257L;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public ConvertException() {}

  /**
   * Constructs ...
   *
   *
   * @param msg
   */
  public ConvertException(String msg)
  {
    super(msg);
  }

  /**
   * Constructs ...
   *
   *
   * @param throwable
   */
  public ConvertException(Throwable throwable)
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
  public ConvertException(String msg, Throwable throwable)
  {
    super(msg, throwable);
  }
}
