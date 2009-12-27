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
import sonia.blog.api.dao.DAOListener.Action;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.exception.BlogSecurityException;
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

import javax.persistence.NoResultException;
import javax.persistence.Query;

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
   *
   * @param strategy
   */
  public JpaEntryDAO(JpaStrategy strategy)
  {
    super(strategy, Entry.class, Constants.LISTENER_ENTRY);
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
    Query q = strategy.getNamedQuery("Entry.countByCategory", false);

    q.setParameter("category", category);

    return excecuteQuery(Long.class, q);
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public long count(Blog blog)
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

      if (session.hasRole(Role.CONTENTMANAGER))
      {
        q = strategy.getNamedQuery("Entry.countByBlog", false);
      }
      else
      {
        q = strategy.getNamedQuery("Entry.countByBlogAndUser", false);
        q.setParameter("user", session.getUser());
      }

      q.setParameter("blog", session.getBlog());
      result = (Long) q.getSingleResult();
    }

    return result;
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
  public List<Date> getAllCalendarDates(Blog blog, Date startDate,
          Date endDate)
  {
    return getAllCalendarDates(blog, startDate, endDate, -1, -1);
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
  public List<Date> getAllCalendarDates(Blog blog, Date startDate,
          Date endDate, int start, int max)
  {
    List<Date> dates = null;

    try
    {
      Query q = strategy.getNamedQuery("Entry.calendar", false);

      q.setParameter("blog", blog);
      q.setParameter("start", startDate);
      q.setParameter("end", endDate);
      dates = q.getResultList();
    }
    catch (NoResultException ex) {}

    return dates;
  }

  /**
   * Method description
   *
   *
   *
   * @param session
   * @param item
   * @param notifyListener
   *
   * @return
   */
  @Override
  public boolean remove(BlogSession session, Entry item, boolean notifyListener)
  {
    if (!isPrivileged(session, item, ACTION_REMOVE))
    {
      logUnprivilegedMessage(session, item, ACTION_REMOVE);

      throw new BlogSecurityException("Author session is required");
    }

    if (notifyListener)
    {
      fireEvent(Action.PREREMOVE, item);
    }

    boolean result = false;

    try
    {
      List<Attachment> attachments = item.getAttachments();

      if (Util.hasContent(attachments))
      {
        for (Attachment a : attachments)
        {
          strategy.remove(a);
        }
      }

      List<Comment> comments = item.getComments();

      if (Util.hasContent(comments))
      {
        for (Comment c : comments)
        {
          strategy.remove(c);
        }
      }

      strategy.remove(item);
      strategy.flush();

      if (notifyListener)
      {
        fireEvent(Action.POSTREMOVE, item);
      }

      result = true;
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blog
   * @param tag
   * @param active
   *
   * @return
   */
  public List<Entry> getAll(Blog blog, Tag tag, boolean active)
  {
    return getAll(blog, tag, active, -1, -1);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param tag
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Entry> getAll(Blog blog, Tag tag, boolean active, int start,
                            int max)
  {
    List<Entry> entries = null;
    Query q = strategy.getNamedQuery("Entry.getAllByBlogAndTag", false);

    q.setParameter("blog", blog);
    q.setParameter("tag", tag);
    q.setParameter("active", active);

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
  public List<Entry> getAll(Blog blog, Date startDate, Date endDate)
  {
    return getAll(blog, startDate, endDate, -1, -1);
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
  public List<Entry> getAll(Blog blog, Date startDate, Date endDate, int start,
                            int max)
  {
    List<Entry> entries = null;
    Query q = strategy.getNamedQuery("Entry.findByBlogAndDate", false);

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

    return entries;
  }

  /**
   * Method description
   *
   *
   *
   * @param active
   * @return
   */
  public List<Entry> getAll(boolean active)
  {
    return getAll(active, -1, -1);
  }

  /**
   * Method description
   *
   *
   *
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  public List<Entry> getAll(boolean active, int start, int max)
  {
    Query q = strategy.getNamedQuery("Entry.getAllByActive", active);

    q.setParameter("active", active);

    if (start >= 0)
    {
      q.setFirstResult(start);
    }

    if (max >= 0)
    {
      q.setMaxResults(max);
    }

    return excecuteListQuery(q);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param active
   *
   * @return
   */
  public List<Entry> getAll(Blog blog, boolean active)
  {
    return getAll(blog, active, -1, -1);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  public List<Entry> getAll(Blog blog, boolean active, int start, int max)
  {
    Query q = strategy.getNamedQuery("Entry.getAllByBlogAndActive", false);

    q.setParameter("blog", blog);
    q.setParameter("active", active);

    if (start >= 0)
    {
      q.setFirstResult(start);
    }

    if (max >= 0)
    {
      q.setMaxResults(max);
    }

    return excecuteListQuery(q);
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<Entry> getAll(Blog blog)
  {
    return getAll(blog, -1, -1);
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
  public List<Entry> getAll(Blog blog, int start, int max)
  {
    Query q = strategy.getNamedQuery("Entry.getAllByBlog", false);

    q.setParameter("blog", blog);

    if (start >= 0)
    {
      q.setFirstResult(start);
    }

    if (max >= 0)
    {
      q.setMaxResults(max);
    }

    return excecuteListQuery(q);
  }

  /**
   * Method description
   *
   *
   * @param category
   *
   * @return
   */
  public List<Entry> getAll(Category category)
  {
    return getAll(category, -1, -1);
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
  public List<Entry> getAll(Category category, int start, int max)
  {
    List<Entry> entries = null;
    Query q = strategy.getNamedQuery("Entry.getByCategory", false);

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

    return entries;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Entry> getAll()
  {
    return findList("Entry.getAll");
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
    return findList("Entry.getAll", start, max);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param author
   * @param published
   * @param start
   * @param max
   *
   * @return
   */
  public List<Entry> getAll(Blog blog, User author, Boolean published,
                            int start, int max)
  {
    Query q = strategy.getNamedQuery("Entry.getAllByAuthor", false);

    q.setParameter("blog", blog);
    q.setParameter("author", author);
    q.setParameter("published", published);

    if (start > 0)
    {
      q.setFirstResult(start);
    }

    if (max > 0)
    {
      q.setMaxResults(max);
    }

    return excecuteListQuery(q);
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

      if (session.hasRole(Role.CONTENTMANAGER))
      {
        q = strategy.getNamedQuery("Entry.getAllByBlog", false);
      }
      else
      {
        q = strategy.getNamedQuery("Entry.getAllByBlogAndUser", false);
        q.setParameter("user", session.getUser());
      }

      q.setParameter("blog", session.getBlog());
      q.setFirstResult(start);
      q.setMaxResults(max);
      entries = excecuteListQuery(q);
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
    Query q = strategy.getNamedQuery("Entry.next", false);

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
    Query q = strategy.getNamedQuery("Entry.categoryNext", false);

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
    Query q = strategy.getNamedQuery("Entry.tagNext", false);

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
    Query q = strategy.getNamedQuery("Entry.dateNext", false);

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
   * @param author
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getNextEntry(Blog blog, User author, Entry entry,
                            Boolean published)
  {
    Query q = strategy.getNamedQuery("Entry.authorNext", false);

    q.setParameter("blog", blog);
    q.setParameter("author", author);
    q.setParameter("date", entry.getPublishingDate());
    q.setParameter("published", published);

    return excecuteQuery(q);
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
    Query q = strategy.getNamedQuery("Entry.prev", false);

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
    Query q = strategy.getNamedQuery("Entry.categoryPrev", false);

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
    Query q = strategy.getNamedQuery("Entry.tagPrev", false);

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
    Query q = strategy.getNamedQuery("Entry.datePrev", false);

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
   * @param blog
   * @param author
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getPreviousEntry(Blog blog, User author, Entry entry,
                                Boolean published)
  {
    Query q = strategy.getNamedQuery("Entry.authorPrev", false);

    q.setParameter("blog", blog);
    q.setParameter("author", author);
    q.setParameter("date", entry.getPublishingDate());
    q.setParameter("published", published);

    return excecuteQuery(q);
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
