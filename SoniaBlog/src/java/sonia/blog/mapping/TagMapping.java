/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.entity.Blog;
import sonia.blog.wui.BlogBean;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class TagMapping extends AbstractMappingHandler
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
      String tag = args[0].trim();

      if ((tag != null) && (tag.length() > 0))
      {
        int position = 0;
        int max = blog.getEntriesPerPage();

        if (args.length > 1)
        {
          try
          {
            position = Integer.parseInt(args[1]);
          }
          catch (NumberFormatException ex)
          {
            logger.log(Level.FINEST, null, ex);
          }
        }

        EntityManager em = BlogContext.getInstance().getEntityManager();
        Query q = em.createNamedQuery("Entry.findByTagName");

        q.setParameter("blog", blog);
        q.setParameter("name", tag);

        String prefix = BlogContext.getInstance().getLinkBuilder().buildLink(
                            getRequest(context), "/blog/tag/" + tag + "/");
        BlogBean blogBean = getBlogBean(context);
        List list = q.getResultList();

        setEntries(blogBean, list, position, max, prefix);
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
  public String getMappingName()
  {
    return "tag";
  }
}
