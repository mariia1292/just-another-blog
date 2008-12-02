/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.entity;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;

/**
 *
 * @author sdorra
 */
public interface ContentObject extends PermaObject
{

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean renderMacros();

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAuthorName();

  /**
   * Method description
   *
   *
   *
   * @return
   */
  public String getContent();

  /**
   * Method description
   *
   *
   * @return
   */
  public Date getCreationDate();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTeaser();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTitle();
}
