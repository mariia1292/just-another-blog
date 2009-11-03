/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://kenai.com/projects/jab
 *
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.TrackbackDAO;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Trackback;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Sebastian Sdorra
 */
public class JpaTrackbackDAO extends JpaGenericDAO<Trackback>
        implements TrackbackDAO
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(JpaTrackbackDAO.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   *
   * @param strategy
   */
  public JpaTrackbackDAO(JpaStrategy strategy)
  {
    super(strategy, Trackback.class, Constants.LISTENER_TRACKBACK);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public long count()
  {
    return countQuery("Trackback.count");
  }

  /**
   * Method description
   *
   *
   * @param entry
   * @param type
   * @param url
   *
   * @return
   */
  public long count(Entry entry, int type, String url)
  {
    Query q = strategy.getNamedQuery("Trackback.countByEntryTypeAndUrl", false);

    q.setParameter("entry", entry);
    q.setParameter("type", type);
    q.setParameter("url", url);

    return (Long) q.getSingleResult();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Trackback> getAll()
  {
    return findList("Trackback.getAll");
  }

  /**
   * Method description
   *
   *
   * @param start
   * @param max
   *
   * @return
   */
  public List<Trackback> getAll(int start, int max)
  {
    return findList("Trackback.getAll", start, max);
  }

  /**
   * Method description
   *
   *
   * @param entry
   *
   * @return
   */
  public List<Trackback> getAll(Entry entry)
  {
    Query q = strategy.getNamedQuery("Trackback.getAllByEntry", false);

    q.setParameter("entry", entry);

    return excecuteListQuery(q);
  }

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
  public List<Trackback> getAll(Entry entry, int start, int max)
  {
    Query q = strategy.getNamedQuery("Trackback.getAllByEntry", false);

    q.setParameter("entry", entry);
    q.setFirstResult(start);
    q.setMaxResults(max);

    return excecuteListQuery(q);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected Logger getLogger()
  {
    return logger;
  }
}
