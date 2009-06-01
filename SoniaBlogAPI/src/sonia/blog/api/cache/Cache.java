/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.cache;

/**
 *
 * @author sdorra
 */
public interface Cache
{

  /**
   * Method description
   *
   */
  public void clear();

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   */
  public void put(Object key, Object value);

  /**
   * Method description
   *
   *
   * @param key
   */
  public void remove(Object key);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public int getSize();
}
