/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Tag;
import sonia.blog.entity.User;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class JpaEntryDAO extends JpaGenericDAO<Entry> implements EntryDAO
{

  /**
   * Constructs ...
   *
   *
   * @param entityManagerFactory
   */
  public JpaEntryDAO(EntityManagerFactory entityManagerFactory)
  {
    super(entityManagerFactory, Entry.class, Constants.LISTENER_ENTRY);
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
    return countQuery("Entry.count");
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public long countByBlog(Blog blog)
  {
    return countQuery("Entry.countByBlog", blog);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Entry> findAll()
  {
    return findList("Entry.findAll");
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
  public List<Entry> findAll(int start, int max)
  {
    return findList("Entry.findAll", start, max);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Entry> findAllActives()
  {
    return findList("Entry.findAllActives");
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
  public List<Entry> findAllActives(int start, int max)
  {
    return findList("Entry.findAllActives", start, max);
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<Entry> findAllActivesByBlog(Blog blog)
  {
    return findList("Entry.findAllActivesByBlog", blog);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param start
   * @param max
   *
   * @return
   */
  public List<Entry> findAllActivesByBlog(Blog blog, int start, int max)
  {
    return findList("Entry.findAllActivesByBlog", blog, start, max);
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<Entry> findAllByBlog(Blog blog)
  {
    return findList("Entry.findAllByBlog", blog);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param start
   * @param max
   *
   * @return
   */
  public List<Entry> findAllByBlog(Blog blog, int start, int max)
  {
    return findList("Entry.findAllByBlog", blog, start, max);
  }

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
  public List<Entry> findAllByBlogAndDate(Blog blog, Date startDate,
          Date endDate)
  {
    return findAllByBlogAndDate(blog, startDate, endDate, -1, -1);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param startDate
   * @param endDate
   * @param start
   * @param max
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Entry> findAllByBlogAndDate(Blog blog, Date startDate,
          Date endDate, int start, int max)
  {
    List<Entry> entries = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Entry.findByBlogAndDate");

    q.setParameter("blog", blog);
    q.setParameter("start", startDate);
    q.setParameter("end", endDate);

    if (start > 0)
    {
      q.setFirstResult(start);
    }

    if (max > 0)
    {
      q.setMaxResults(max);
    }

    try
    {
      entries = q.getResultList();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return entries;
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param tag
   *
   * @return
   */
  public List<Entry> findAllByBlogAndTag(Blog blog, Tag tag)
  {
    return findAllByBlogAndTag(blog, tag, -1, -1);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param tag
   * @param start
   * @param max
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Entry> findAllByBlogAndTag(Blog blog, Tag tag, int start, int max)
  {
    List<Entry> entries = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Entry.findByBlogAndTag");

    q.setParameter("blog", blog);
    q.setParameter("tag", tag);

    if (start > 0)
    {
      q.setFirstResult(start);
    }

    if (max > 0)
    {
      q.setMaxResults(max);
    }

    try
    {
      entries = q.getResultList();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return entries;
  }

  /**
   * Method description
   *
   *
   * @param category
   *
   * @return
   */
  public List<Entry> findAllByCategory(Category category)
  {
    return findAllByCategory(category, -1, -1);
  }

  /**
   * Method description
   *
   *
   * @param category
   * @param start
   * @param max
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Entry> findAllByCategory(Category category, int start, int max)
  {
    List<Entry> entries = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Entry.findByCategory");

    q.setParameter("category", category);

    if (start > 0)
    {
      q.setFirstResult(start);
    }

    if (max > 0)
    {
      q.setMaxResults(max);
    }

    try
    {
      entries = q.getResultList();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return entries;
  }

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
  public List<Date> findAllCalendarDates(Blog blog, Date startDate,
          Date endDate)
  {
    return findAllCalendarDates(blog, startDate, endDate, -1, -1);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param startDate
   * @param endDate
   * @param start
   * @param max
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Date> findAllCalendarDates(Blog blog, Date startDate,
          Date endDate, int start, int max)
  {
    List<Date> dates = null;
    EntityManager em = createEntityManager();

    try
    {
      Query q = em.createNamedQuery("Entry.calendar");

      q.setParameter("blog", blog);
      q.setParameter("start", startDate);
      q.setParameter("end", endDate);
      dates = q.getResultList();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return dates;
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param user
   *
   * @return
   */
  public List<Entry> findAllDraftsByBlogAndUser(Blog blog, User user)
  {
    return findAllDraftsByBlogAndUser(blog, user, -1, -1);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param user
   * @param start
   * @param max
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Entry> findAllDraftsByBlogAndUser(Blog blog, User user,
          int start, int max)
  {
    List<Entry> entries = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Entry.findAllDraftsByBlogAndUser");

    q.setParameter("blog", blog);
    q.setParameter("user", user);

    try
    {
      entries = q.getResultList();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return entries;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blog
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getNextEntry(Blog blog, Entry entry, Boolean published)
  {
    Entry nextEntry = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Entry.next");

    q.setParameter("blog", blog);
    q.setParameter("published", published);
    q.setParameter("date", entry.getPublishingDate());

    try
    {
      nextEntry = (Entry) q.getSingleResult();
    }
    catch (NoResultException ex) {}

    return nextEntry;
  }

  /**
   * Method description
   *
   *
   * @param category
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getNextEntry(Category category, Entry entry, Boolean published)
  {
    Entry nextEntry = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Entry.categoryNext");

    q.setParameter("category", category);
    q.setParameter("published", published);
    q.setParameter("date", entry.getPublishingDate());

    try
    {
      nextEntry = (Entry) q.getSingleResult();
    }
    catch (NoResultException ex) {}

    return nextEntry;
  }

  /**
   * Method description
   *
   *
   *
   * @param blog
   * @param tag
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getNextEntry(Blog blog, Tag tag, Entry entry, Boolean published)
  {
    Entry nextEntry = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Entry.tagNext");

    q.setParameter("tag", tag);
    q.setParameter("blog", blog);
    q.setParameter("published", published);
    q.setParameter("date", entry.getPublishingDate());

    try
    {
      nextEntry = (Entry) q.getSingleResult();
    }
    catch (NoResultException ex) {}

    return nextEntry;
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getPreviousEntry(Blog blog, Entry entry, Boolean published)
  {
    Entry nextEntry = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Entry.prev");

    q.setParameter("blog", blog);
    q.setParameter("published", published);
    q.setParameter("date", entry.getPublishingDate());

    try
    {
      nextEntry = (Entry) q.getSingleResult();
    }
    catch (NoResultException ex) {}

    return nextEntry;
  }

  /**
   * Method description
   *
   *
   * @param category
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getPreviousEntry(Category category, Entry entry,
                                Boolean published)
  {
    Entry nextEntry = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Entry.categoryPrev");

    q.setParameter("category", category);
    q.setParameter("published", published);
    q.setParameter("date", entry.getPublishingDate());

    try
    {
      nextEntry = (Entry) q.getSingleResult();
    }
    catch (NoResultException ex) {}

    return nextEntry;
  }

  /**
   * Method description
   *
   *
   *
   * @param blog
   * @param tag
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getPreviousEntry(Blog blog, Tag tag, Entry entry,
                                Boolean published)
  {
    Entry nextEntry = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Entry.tagPrev");

    q.setParameter("tag", tag);
    q.setParameter("blog", blog);
    q.setParameter("published", published);
    q.setParameter("date", entry.getPublishingDate());

    try
    {
      nextEntry = (Entry) q.getSingleResult();
    }
    catch (NoResultException ex) {}

    return nextEntry;
  }
}
