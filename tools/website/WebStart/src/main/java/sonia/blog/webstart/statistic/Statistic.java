/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webstart.statistic;

/**
 *
 * @author Sebastian Sdorra
 */
public class Statistic
{

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param counter
   */
  public Statistic(String name, int counter)
  {
    this.name = name;
    this.counter = counter;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public int getCounter()
  {
    return counter;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return name;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private int counter;

  /** Field description */
  private String name;
}
