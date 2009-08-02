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

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogSession;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Role;
import sonia.blog.entity.Tag;
import sonia.blog.entity.User;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import sonia.blog.api.dao.DAOListener.Action;

/**
 *
 * @author Sebastian Sdorra
 */
public class JpaEntryDAO extends JpaGenericDAO<Entry> implements EntryDAO
{

  /** Field description */
  private static Logger logger = Logger.getLogger(JpaEntryDAO.class.getName());

  //~--- constructors ---------------------------------------------------------

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
   * @param category
   *
   * @return
   */
  public long count(Category category)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Entry.countByCategory");

    q.setParameter("category", category);

    return excecuteQuery(Long.class, em, q);
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
   * @param session
   *
   * @return
   */
  public long countModifyAbleEntries(BlogSession session)
  {
    long result = 0;

    if (session.hasRole(Role.AUTHOR))
    {
      Query q = null;
      EntityManager em = createEntityManager();

      try
      {
        if (session.hasRole(Role.CONTENTMANAGER))
        {
          q = em.createNamedQuery("Entry.countByBlog");
        }
        else
        {
          q = em.createNamedQuery("Entry.countByBlogAndUser");
          q.setParameter("user", session.getUser());
        }

        q.setParameter("blog", session.getBlog());
        result = (Long) q.getSingleResult();
      }
      finally
      {
        if (em != null)
        {
          em.close();
        }
      }
    }

    return result;
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

  /**
   * Method description
   *
   *
   *
   * @param session
   * @param item
   *
   * @return
   */
  @Override
  public boolean remove(BlogSession session, Entry item)
  {
    fireEvent(Action.PREREMOVE, item);
    boolean result = false;
    EntityManager em = createEntityManager();

    try
    {
      em.getTransaction().begin();

      List<Attachment> attachments = item.getAttachments();

      if (Util.hasContent(attachments))
      {
        for (Attachment a : attachments)
        {
          em.remove(em.merge(a));
        }
      }

      List<Comment> comments = item.getComments();

      if (Util.hasContent(comments))
      {
        for (Comment c : comments)
        {
          em.remove(em.merge(c));
        }
      }

      em.remove(em.merge(item));
      em.getTransaction().commit();
      fireEvent(Action.POSTREMOVE, item);
      result = true;
    }
    catch (Exception ex)
    {
      if (em.getTransaction().isActive())
      {
        em.getTransaction().rollback();
      }

      logger.log(Level.SEVERE, null, ex);
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

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Entry> getAll()
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
  public List<Entry> getAll(int start, int max)
  {
    return findList("Entry.findAll", start, max);
  }

  /**
   * Method description
   *
   *
   * @param session
   * @param start
   * @param max
   *
   * @return
   */
  public List<Entry> getAllModifyAbleEntries(BlogSession session, int start,
          int max)
  {
    List<Entry> entries = null;

    if (session.hasRole(Role.AUTHOR))
    {
      Query q = null;
      EntityManager em = createEntityManager();

      if (session.hasRole(Role.CONTENTMANAGER))
      {
        q = em.createNamedQuery("Entry.findAllByBlog");
      }
      else
      {
        q = em.createNamedQuery("Entry.getAllByBlogAndUser");
        q.setParameter("user", session.getUser());
      }

      q.setParameter("blog", session.getBlog());
      q.setFirstResult(start);
      q.setMaxResults(max);
      entries = excecuteListQuery(em, q);
    }
    else
    {
      entries = new ArrayList<Entry>();
    }

    return entries;
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
   * @param startDate
   * @param endDate
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getNextEntry(Blog blog, Date startDate, Date endDate,
                            Entry entry, Boolean published)
  {
    Entry nextEntry = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Entry.dateNext");

    q.setParameter("start", startDate);
    q.setParameter("end", endDate);
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

  /**
   * Method description
   *
   *
   * @param blog
   * @param startDate
   * @param endDate
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getPreviousEntry(Blog blog, Date startDate, Date endDate,
                                Entry entry, Boolean published)
  {
    Entry prevEntry = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Entry.datePrev");

    q.setParameter("start", startDate);
    q.setParameter("end", endDate);
    q.setParameter("blog", blog);
    q.setParameter("published", published);
    q.setParameter("date", entry.getPublishingDate());

    try
    {
      prevEntry = (Entry) q.getSingleResult();
    }
    catch (NoResultException ex) {}

    return prevEntry;
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

  /**
   * Method description
   *
   *
   * @param session
   * @param item
   * @param action
   *
   * @return
   */
  @Override
  protected boolean isPrivileged(BlogSession session, Entry item, int action)
  {
    boolean result = false;

    if (!BlogContext.getInstance().isInstalled()
        && (action == JpaGenericDAO.ACTION_ADD) && session.hasRole(Role.SYSTEM))
    {
      result = true;
    }
    else
    {
      result = session.hasRole(Role.CONTENTMANAGER)
               || (session.hasRole(Role.AUTHOR)
                   && session.getUser().equals(item.getAuthor()));
    }

    return result;
  }
}
