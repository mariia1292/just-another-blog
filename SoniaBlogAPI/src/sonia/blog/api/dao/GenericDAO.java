/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public interface GenericDAO<T>
{

  /**
   * Method description
   *
   *
   * @param item
   *
   * @return
   */
  public boolean add(T item);

  /**
   * Method description
   *
   *
   * @return
   */
  public long count();

  /**
   * Method description
   *
   *
   * @param item
   *
   * @return
   */
  public boolean edit(T item);

  /**
   * Method description
   *
   *
   * @param id
   *
   * @return
   */
  public T find(Long id);

  /**
   * Method description
   *
   *
   * @return
   */
  public List<T> findAll();

  /**
   * Method description
   *
   *
   * @param start
   * @param max
   *
   * @return
   */
  public List<T> findAll(int start, int max);

  /**
   * Method description
   *
   *
   * @param item
   *
   * @return
   */
  public boolean remove(T item);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isEmpty();
}
