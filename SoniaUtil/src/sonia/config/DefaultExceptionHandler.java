/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

/**
 *
 * @author sdorra
 */
public class DefaultExceptionHandler implements ExceptionHandler
{

  /**
   * Method description
   *
   *
   * @param ex
   */
  public void handleException(Throwable ex)
  {
    if (ex instanceof RuntimeException)
    {
      throw(RuntimeException) ex;
    }
    else
    {
      throw new RuntimeException(ex);
    }
  }
}
