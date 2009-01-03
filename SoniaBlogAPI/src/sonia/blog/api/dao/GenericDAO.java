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
   * @param item
   */
  public boolean remove(T item);
}
