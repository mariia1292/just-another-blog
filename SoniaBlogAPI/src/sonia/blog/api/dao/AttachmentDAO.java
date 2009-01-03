/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Attachment;
import sonia.blog.entity.Entry;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import sonia.blog.entity.Blog;

/**
 *
 * @author sdorra
 */
public interface AttachmentDAO extends GenericDAO<Attachment>
{

  public List<Attachment> findAllImagesByEntry( Entry entry );

  public Attachment findByBlogAndId( Blog blog, Long id );

  /**
   * Method description
   *
   *
   * @param entry
   *
   * @return
   */
  public List<Attachment> findAllByEntry(Entry entry);
}
