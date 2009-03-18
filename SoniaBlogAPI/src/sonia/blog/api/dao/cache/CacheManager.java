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
public interface CacheManager
{

  /**
   * Method description
   *
   */
  public void clearAll();

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Cache> getCaches();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getInformation();
}
