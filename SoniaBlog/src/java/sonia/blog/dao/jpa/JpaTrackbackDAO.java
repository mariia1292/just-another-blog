/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.dao.TrackbackDAO;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Trackback;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import sonia.blog.api.app.Constants;

/**
 *
 * @author sdorra
 */
public class JpaTrackbackDAO extends JpaGenericDAO<Trackback>
        implements TrackbackDAO
{

  /**
   * Constructs ...
   *
   *
   * @param entityManagerFactory
   */
  public JpaTrackbackDAO(EntityManagerFactory entityManagerFactory)
  {
    super(entityManagerFactory, Trackback.class, Constants.LISTENER_TRACKBACK);
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
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Trackback.getAllByEntry");

    q.setParameter("entry", entry);

    return excecuteListQuery(em, q);
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
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Trackback.getAllByEntry");

    q.setParameter("entry", entry);
    q.setFirstResult(start);
    q.setMaxResults(max);

    return excecuteListQuery(em, q);
  }
}
