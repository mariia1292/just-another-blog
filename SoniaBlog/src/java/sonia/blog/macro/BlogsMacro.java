/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.entity.Blog;

import sonia.macro.Macro;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Map;

/**
 *
 * @author sdorra
 */
public class BlogsMacro implements Macro
{

  /**
   * Method description
   *
   *
   * @param environment
   * @param body
   *
   * @return
   */
  public String doBody(Map<String, ?> environment, String body)
  {
    String result = "";
    BlogRequest request = (BlogRequest) environment.get("request");
    BlogDAO blogDAO = BlogContext.getDAOFactory().getBlogDAO();
    List<Blog> blogs = blogDAO.getAll(true, 0, 100);

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

    return result;
  }
}
