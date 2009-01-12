/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

/**
 *
 * @author sdorra
 */
public interface DAOListener
{

  /**
   * Enum description
   *
   */
  public enum Action
  {
    PREADD, POSTADD, PREUPDATE, POSTUPDATE, PREREMOVE, POSTREMOVE
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param action
   * @param item
   */
  public void handleEvent(Action action, Object item);
}
