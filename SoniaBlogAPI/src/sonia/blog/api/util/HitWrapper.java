/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.util;

/**
 *
 * @author sdorra
 */
public class HitWrapper
{

  /**
   * Constructs ...
   *
   *
   * @param year
   * @param month
   * @param count
   */
  public HitWrapper(int year, int month, long count)
  {
    this.year = year;
    this.month = month;
    this.count = count;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public long getCount()
  {
    return count;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getMonth()
  {
    return month;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getYear()
  {
    return year;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param count
   */
  public void setCount(long count)
  {
    this.count = count;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private long count;

  /** Field description */
  private int month;

  /** Field description */
  private int year;
}
