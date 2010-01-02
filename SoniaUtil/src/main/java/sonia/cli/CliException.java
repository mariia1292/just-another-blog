/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.cli;

/**
 *
 * @author sdorra
 */
public class CliException extends Exception
{

  /**
   * Constructs ...
   *
   */
  public CliException() {}

  /**
   * Constructs ...
   *
   *
   * @param string
   */
  public CliException(String string)
  {
    super(string);
  }

  /**
   * Constructs ...
   *
   *
   * @param thrwbl
   */
  public CliException(Throwable thrwbl)
  {
    super(thrwbl);
  }

  /**
   * Constructs ...
   *
   *
   * @param string
   * @param thrwbl
   */
  public CliException(String string, Throwable thrwbl)
  {
    super(string, thrwbl);
  }
}
