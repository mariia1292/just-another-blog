/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public interface AttachmentDAO extends GenericDAO<Attachment>
{

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public long countByBlog(Blog blog);

  /**
   * Method description
   *
   *
   * @param entry
   *
   * @return
   */
  public List<Attachment> findAllByEntry(Entry entry);

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
  public List<Attachment> findAllByEntry(Entry entry, int start, int max);

  /**
   * Method description
   *
   *
   * @param entry
   *
   * @return
   */
  public List<Attachment> findAllImagesByEntry(Entry entry);

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
  public List<Attachment> findAllImagesByEntry(Entry entry, int start, int max);

  /**
   * Method description
   *
   *
   * @param blog
   * @param id
   *
   * @return
   */
  public Attachment findByBlogAndId(Blog blog, Long id);
}
