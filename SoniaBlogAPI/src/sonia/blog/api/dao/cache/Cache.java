/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao.cache;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

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

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public int getHitPercentage();

  /**
   * Method description
   *
   *
   * @return
   */
  public List<CacheInformation> getInformation();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName();
}
