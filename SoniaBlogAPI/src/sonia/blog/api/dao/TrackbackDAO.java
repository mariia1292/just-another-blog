/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Entry;
import sonia.blog.entity.Trackback;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public interface TrackbackDAO extends GenericDAO<Trackback>
{

  /**
   * Method description
   *
   *
   * @param entry
   *
   * @return
   */
  public List<Trackback> getAll(Entry entry);

  /**
   * Method description
   *
   *
   * @param entry
   * @param start
   * @param max
   *
   * @return
   */
  public List<Trackback> getAll(Entry entry, int start, int max);
}
