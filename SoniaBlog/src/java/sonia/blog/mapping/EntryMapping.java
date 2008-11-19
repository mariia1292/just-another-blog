/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;

import javax.faces.context.FacesContext;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class EntryMapping extends AbstractMappingHandler
{

  /**
   * Method description
   *
   *
   * @param context
   * @param blog
   * @param args
   *
   * @return
   */
  public String handleMapping(FacesContext context, Blog blog, String[] args)
  {
    String result = null;

    if ((args != null) && (args.length > 0))
    {
      try
      {
        Long id = Long.parseLong(args[0]);
        EntityManager em = BlogContext.getInstance().getEntityManager();
        Query q = em.createNamedQuery("Entry.findIdFromBlog");

        q.setParameter("id", id);
        q.setParameter("blog", blog);

        Entry entry = (Entry) q.getSingleResult();

        if (entry != null)
        {
          getBlogBean(context).setEntry(entry);
          result = "detail.xhtml";
        }

        em.close();
      }
      catch (NumberFormatException ex)
      {
        logger.log(Level.FINEST, null, ex);
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
  public String getMappingName()
  {
    return "entries";
  }
}
