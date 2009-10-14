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
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Sebastian Sdorra
 */
public class JpaAttachmentDAO extends JpaGenericDAO<Attachment>
        implements AttachmentDAO
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(JpaAttachmentDAO.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param entityManagerFactory
   */
  public JpaAttachmentDAO(EntityManagerFactory entityManagerFactory)
  {
    super(entityManagerFactory, Attachment.class,
          Constants.LISTENER_ATTACHMENT);
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
    return countQuery("Attachment.count");
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
    return countQuery("Attachment.countByBlog", blog);
  }

  /**
   * Method description
   *
   *
   * @param entry
   *
   * @return
   */
  public List<Attachment> findAllByEntry(Entry entry)
  {
    return findAllByEntry(entry, -1, -1);
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
  @SuppressWarnings("unchecked")
  public List<Attachment> findAllByEntry(Entry entry, int start, int max)
  {
    List<Attachment> attachments = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Attachment.findAllByEntry");

    q.setParameter("entry", entry);

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
      attachments = q.getResultList();
    }
    catch (NoResultException ex) {}
    finally
    {
      em.close();
    }

    return attachments;
  }

  /**
   * Method description
   *
   *
   * @param entry
   *
   * @return
   */
  public List<Attachment> findAllImagesByEntry(Entry entry)
  {
    return findAllImagesByEntry(entry, -1, -1);
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
  @SuppressWarnings("unchecked")
  public List<Attachment> findAllImagesByEntry(Entry entry, int start, int max)
  {
    List<Attachment> attachments = null;
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Attachment.findAllImagesByEntry");

    q.setParameter("entry", entry);

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
      attachments = q.getResultList();
    }
    catch (NoResultException ex) {}
    finally
    {
      em.close();
    }

    return attachments;
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param id
   *
   * @return
   */
  public Attachment findByBlogAndId(Blog blog, Long id)
  {
    Attachment attachment = get(id);

    if (attachment != null)
    {
      Entry e = attachment.getEntry();
      Page p = attachment.getPage();

      if (!(((e != null) && e.getBlog().equals(blog))
            || ((p != null) && p.getBlog().equals(blog))))
      {
        attachment = null;

        if (logger.isLoggable(Level.WARNING))
        {
          StringBuffer msg = new StringBuffer();

          msg.append("attachment ").append(id);
          msg.append(" is not an an attachment of ");
          msg.append(blog.getIdentifier());
          logger.warning(msg.toString());
        }
      }
    }
    else if (logger.isLoggable(Level.WARNING))
    {
      StringBuffer msg = new StringBuffer();

      msg.append("could not find attachment with id ").append(id);
      logger.warning(msg.toString());
    }

    return attachment;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Attachment> getAll()
  {
    return findList("Attachment.findAll");
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
  public List<Attachment> getAll(int start, int max)
  {
    return findList("Attachment.findAll", start, max);
  }

  /**
   * Method description
   *
   *
   * @param page
   *
   * @return
   */
  public List<Attachment> getAll(Page page)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Attachment.getAllByPage");

    q.setParameter("page", page);

    return excecuteListQuery(em, q);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param published
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Attachment> getAll(Blog blog, boolean published)
  {
    List<Attachment> result = new ArrayList<Attachment>();
    List<Attachment> pageResult = null;
    List<Attachment> entryResult = null;
    EntityManager em = createEntityManager();

    try
    {
      Query q =
        em.createNamedQuery("Attachment.entry.getAllByBlogAndPublished");

      q.setParameter("blog", blog);
      q.setParameter("published", published);
      entryResult = q.getResultList();

      if (entryResult != null)
      {
        result.addAll(entryResult);
      }

      q = em.createNamedQuery("Attachment.page.getAllByBlogAndPublished");
      q.setParameter("blog", blog);
      q.setParameter("published", published);
      pageResult = q.getResultList();

      if (pageResult != null)
      {
        result.addAll(pageResult);
      }
    }
    finally
    {
      em.close();
    }

    entryResult = null;
    pageResult = null;

    return result;
  }

  /**
   * Method description
   *
   *
   * @param page
   *
   * @return
   */
  public List<Attachment> getAllImages(Page page)
  {
    EntityManager em = createEntityManager();
    Query q = em.createNamedQuery("Attachment.getAllImagesByPage");

    q.setParameter("page", page);

    return excecuteListQuery(em, q);
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
