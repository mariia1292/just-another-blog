/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.BlogHitCountDAO;
import sonia.blog.api.util.BlogWrapper;
import sonia.blog.api.util.HitWrapper;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogHitCount;
import sonia.blog.util.BlogUtil;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class JpaBlogHitCountDAO extends JpaGenericDAO<BlogHitCount>
        implements BlogHitCountDAO
{

  /**
   * Constructs ...
   *
   *
   * @param entityManagerFactory
   */
  public JpaBlogHitCountDAO(EntityManagerFactory entityManagerFactory)
  {
    super(entityManagerFactory, BlogHitCount.class,
          Constants.LISTENER_BLOGHITCOUNT);
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
    return countQuery("BlogHitCount.count");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<BlogHitCount> findAll()
  {
    return findList("BlogHitCount.findAll");
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
  public List<BlogHitCount> findAll(int start, int max)
  {
    return findList("BlogHitCount.findAll", start, max);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param date
   *
   * @return
   */
  public BlogHitCount findByBlogAndDate(Blog blog, Date date)
  {
    BlogHitCount hitCount = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("BlogHitCount.findByBlogAndDate");

    q.setParameter("blog", blog);
    q.setParameter("date", date);

    try
    {
      hitCount = (BlogHitCount) q.getSingleResult();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return hitCount;
  }

  /**
   * Method description
   *
   *
   * @param month
   * @param year
   *
   * @return
   */
  public List<BlogWrapper> findByMonth(int month, int year)
  {
    Date startDate = BlogUtil.createStartDate(month, year);
    Date endDate = BlogUtil.createEndDate(month, year);

    return findBetween(startDate, endDate);
  }

  /**
   * Method description
   *
   *
   * @param year
   *
   * @return
   */
  public List<BlogWrapper> findByYear(int year)
  {
    Date startDate = BlogUtil.createStartDate(year);
    Date endDate = BlogUtil.createEndDate(year);

    return findBetween(startDate, endDate);
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public boolean increase(Blog blog)
  {
    boolean result = true;
    Date date = new Date();
    BlogHitCount blogHitCount = findByBlogAndDate(blog, date);

    if (blogHitCount == null)
    {
      blogHitCount = new BlogHitCount(blog, date);
      blogHitCount.inc();
      add(blogHitCount);
    }
    else
    {
      blogHitCount.inc();
      edit(blogHitCount);
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blog
   * @param month
   * @param year
   *
   * @return
   */
  public Long getHitsByBlogAndMonth(Blog blog, int month, int year)
  {
    Date startDate = BlogUtil.createStartDate(month, year);
    Date endDate = BlogUtil.createEndDate(month, year);

    return getHitsByBlogBetween(blog, startDate, endDate);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param year
   *
   * @return
   */
  public Long getHitsByBlogAndYear(Blog blog, int year)
  {
    Date startDate = BlogUtil.createStartDate(year);
    Date endDate = BlogUtil.createEndDate(year);

    return getHitsByBlogBetween(blog, startDate, endDate);
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<HitWrapper> getHitsPerMonthByBlog(Blog blog)
  {
    List<HitWrapper> result = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("BlogHitCount.findByBlog");

    q.setParameter("blog", blog);

    try
    {
      List<BlogHitCount> hitCounts = q.getResultList();

      result = buildHitWrapper(hitCounts);
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return result;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param hitCounts
   *
   * @return
   */
  private List<HitWrapper> buildHitWrapper(List<BlogHitCount> hitCounts)
  {
    List<HitWrapper> wrappers = new ArrayList<HitWrapper>();

    for (BlogHitCount hitCount : hitCounts)
    {
      Calendar cal = new GregorianCalendar();

      cal.setTime(hitCount.getDate());

      int year = cal.get(Calendar.YEAR);
      int month = cal.get(Calendar.MONTH) + 1;
      HitWrapper wrapper = null;

      for (HitWrapper w : wrappers)
      {
        if ((w.getYear() == year) && (w.getMonth() == month))
        {
          wrapper = w;

          break;
        }
      }

      if (wrapper == null)
      {
        wrapper = new HitWrapper(year, month, hitCount.getHitCount());
        wrappers.add(wrapper);
      }
      else
      {
        long count = wrapper.getCount();

        wrapper.setCount(count + hitCount.getHitCount());
      }
    }

    return wrappers;
  }

  /**
   * Method description
   *
   *
   * @param startDate
   * @param endDate
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  private List<BlogWrapper> findBetween(Date startDate, Date endDate)
  {
    List<BlogWrapper> result = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("BlogHitCount.findBetween");

    q.setParameter("start", startDate);
    q.setParameter("end", endDate);

    try
    {
      result = q.getResultList();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blog
   * @param startDate
   * @param endDate
   *
   * @return
   */
  private Long getHitsByBlogBetween(Blog blog, Date startDate, Date endDate)
  {
    Long result = 0l;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("BlogHitCount.getSumBetweenByBlog");

    q.setParameter("blog", blog);
    q.setParameter("start", startDate);
    q.setParameter("end", endDate);

    try
    {
      result = (Long) q.getSingleResult();
    }
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return result;
  }
}
