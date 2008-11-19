/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.search;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;

/**
 *
 * @author sdorra
 */
public interface SearchEntry
{

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAuthor();

  /**
   * Method description
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
  public Object getData();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSearchResult();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTitle();
}
