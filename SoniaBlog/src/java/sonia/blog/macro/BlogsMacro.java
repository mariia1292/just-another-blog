/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.entity.Blog;

import sonia.macro.Macro;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class BlogsMacro implements Macro
{

  /** Field description */
  private static Logger logger = Logger.getLogger(BlogsMacro.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param environment
   * @param body
   * @param parameters
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public String excecute(Map<String, ?> environment, String body,
                         Map<String, String> parameters)
  {
    String result = "";
    BlogRequest request = (BlogRequest) environment.get("request");
    EntityManager em = BlogContext.getInstance().getEntityManager();

    try
    {
      // TODO: replace with BlogDAO.findAllActives
      Query q = em.createNamedQuery("Blog.findAllActives");
      List<Blog> blogs = q.getResultList();

      if ((blogs != null) &&!blogs.isEmpty())
      {
        LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();

        result += "<ul>\n";

        for (Blog blog : blogs)
        {
          result += "<li>";
          result += "<a href=\"" + linkBuilder.buildLink(request, blog) + "\">";
          result += blog.getTitle();
          result += "</a>\n";
        }

        result += "</ul>\n";
      }
    }
    catch (Exception ex)
    {
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
}
