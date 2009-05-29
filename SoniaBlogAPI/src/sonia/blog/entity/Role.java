/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.entity;

/**
 *
 * @author sdorra
 */
public enum Role
{
  AUTHOR(1), READER(0), ADMIN(2), GLOBALADMIN(3), SYSTEM(-1);

  /**
   * Constructs ...
   *
   *
   * @param value
   */
  private Role(int value)
  {
    this.value = value;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public int getValue()
  {
    return value;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final int value;
}
