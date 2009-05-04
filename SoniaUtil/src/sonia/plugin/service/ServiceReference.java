/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.plugin.service;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sdorra
 *
 * @param <T>
 */
public class ServiceReference<T>
{

  /**
   * Constructs ...
   *
   *
   * @param type
   * @param path
   */
  public ServiceReference(Class<T> type, String path)
  {
    this.type = type;
    this.path = path;
    this.implementations = new ArrayList<T>();
  }

  /**
   * Constructs ...
   *
   *
   * @param type
   * @param path
   * @param implementations
   */
  public ServiceReference(Class<T> type, String path, List<T> implementations)
  {
    this.type = type;
    this.path = path;
    this.implementations = implementations;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param implementation
   *
   * @return
   */
  public ServiceReference<T> add(T implementation)
  {
    implementations.add(implementation);

    return this;
  }

  /**
   * Method description
   *
   *
   * @param pos
   * @param implementation
   *
   * @return
   */
  public ServiceReference<T> add(int pos, T implementation)
  {
    implementations.add(pos, implementation);

    return this;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ServiceReference<T> clear()
  {
    implementations.clear();

    return this;
  }

  /**
   * Method description
   *
   *
   * @param implementation
   *
   * @return
   */
  public boolean contains(T implementation)
  {
    return implementations.contains(implementation);
  }

  /**
   * Method description
   *
   *
   * @param implementation
   *
   * @return
   */
  public ServiceReference<T> remove(T implementation)
  {
    implementations.remove(implementation);

    return this;
  }

  /**
   * Method description
   *
   *
   * @param pos
   *
   * @return
   */
  public ServiceReference<T> remove(int pos)
  {
    implementations.remove(pos);

    return this;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param pos
   *
   * @return
   */
  public T get(int pos)
  {
    return implementations.get(pos);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public T get()
  {
    return implementations.get(0);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<T> getAll()
  {
    return implementations;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getPath()
  {
    return path;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Class<T> getType()
  {
    return type;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<T> implementations;

  /** Field description */
  private String path;

  /** Field description */
  private Class<T> type;
}
