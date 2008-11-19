/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.listener;

/**
 *
 * @author sdorra
 */
public abstract class EntityListener
{

  /**
   * Method description
   *
   *
   * @param object
   */
  public void postLoad(Object object) {}

  /**
   * Method description
   *
   *
   * @param object
   */
  public void postPersists(Object object) {}

  /**
   * Method description
   *
   *
   * @param object
   */
  public void postRemove(Object object) {}

  /**
   * Method description
   *
   *
   * @param object
   */
  public void postUpdate(Object object) {}

  /**
   * Method description
   *
   *
   * @param object
   */
  public void prePersists(Object object) {}

  ;
  ;

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param object
   */
  public void preRemove(Object object) {}

  ;
  ;

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param object
   */
  public void preUpdate(Object object) {}

  ;
  ;
  ;
}
