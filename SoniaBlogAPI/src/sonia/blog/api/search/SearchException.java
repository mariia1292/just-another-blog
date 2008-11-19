/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.search;

/**
 *
 * @author sdorra
 */
public class SearchException extends Exception
{

  /** Field description */
  private static final long serialVersionUID = 1897011191644392199L;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public SearchException()
  {
    super();
  }

  /**
   * Constructs ...
   *
   *
   * @param msg
   */
  public SearchException(String msg)
  {
    super(msg);
  }

  /**
   * Constructs ...
   *
   *
   * @param cause
   */
  public SearchException(Throwable cause)
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
  public SearchException(String msg, Throwable cause)
  {
    super(msg, cause);
  }
}
