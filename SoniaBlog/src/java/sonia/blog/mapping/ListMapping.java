/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.entity.Blog;
import sonia.blog.wui.BlogBean;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.logging.Level;

import javax.faces.context.FacesContext;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class ListMapping extends AbstractMappingHandler
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
    System.out.println("handle listMapping");

    int position = 0;
    int max = blog.getEntriesPerPage();

    if ((args != null) && (args.length > 0))
    {
      try
      {
        position = Integer.parseInt(args[0]);
      }
      catch (NumberFormatException ex)
      {
        logger.log(Level.FINEST, null, ex);
      }
    }

    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Entry.overview");

    q.setParameter("blog", blog);

    List list = q.getResultList();
    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    BlogBean blogBean = getBlogBean(context);
    String prefix = linkBuilder.buildLink(getRequest(context), "/blog/list/");

    setEntries(blogBean, list, position, max, prefix);

    return "list.xhtml";
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
    return "list";
  }
}
