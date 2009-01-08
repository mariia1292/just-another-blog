/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class JpaAttachmentDAO extends JpaGenericDAO<Attachment>
        implements AttachmentDAO
{

  /**
   * Constructs ...
   *
   *
   * @param entityManagerFactory
   */
  public JpaAttachmentDAO(EntityManagerFactory entityManagerFactory)
  {
    super(entityManagerFactory, Attachment.class);
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
   * @return
   */
  public List<Attachment> findAll()
  {
    return findList("Attachment.findAll");
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
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Method description
   *
   *
   * @param entry
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Attachment> findAllImagesByEntry(Entry entry)
  {
    List<Attachment> attachments = null;
    EntityManager em = createEntityManager();

    try
    {
      Query q = em.createNamedQuery("Attachment.findAllImagesByEntry");

      q.setParameter("entry", entry);
      attachments = q.getResultList();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
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
    Attachment attachment = null;
    EntityManager em = createEntityManager();

    try
    {
      Query q = em.createNamedQuery("Attachment.findByBlogAndId");

      q.setParameter("blog", blog);
      q.setParameter("id", id);
      attachment = (Attachment) q.getSingleResult();
    }
    catch (NoResultException ex) {}
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return attachment;
  }
}
