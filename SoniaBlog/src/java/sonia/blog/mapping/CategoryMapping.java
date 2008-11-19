/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.entity.Blog;

import java.util.List;
import java.util.logging.Level;

import javax.faces.context.FacesContext;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class CategoryMapping extends AbstractMappingHandler
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
    String result = "list.xhtml";

    if ((args != null) && (args.length > 0))
    {
      int position = 0;
      int max = blog.getEntriesPerPage();

      try
      {
        Long id = Long.parseLong(args[0]);

        if (args.length > 1)
        {
          position = Integer.parseInt(args[1]);
        }

        EntityManager em = BlogContext.getInstance().getEntityManager();
        Query q = em.createNamedQuery("Entry.findByCategoryId");

        q.setParameter("id", id);

        List list = q.getResultList();
        String prefix = BlogContext.getInstance().getLinkBuilder().buildLink(
                            getRequest(context), "/blog/categories/" + id + "/");

        setEntries(getBlogBean(context), list, position, max, prefix);
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
    return "categories";
  }
}
