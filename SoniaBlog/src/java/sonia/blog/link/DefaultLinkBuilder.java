/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.link;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Entry;
import sonia.blog.entity.PermaObject;
import sonia.blog.entity.Tag;

/**
 *
 * @author sdorra
 */
public class DefaultLinkBuilder implements LinkBuilder
{

  /**
   * Method description
   *
   *
   * @param request
   * @param link
   *
   * @return
   */
  public String buildLink(BlogRequest request, String link)
  {
    String prefix = "";

    if (!link.contains("://"))
    {
      if (request.isSecure())
      {
        prefix = "https://";
      }
      else
      {
        prefix = "http://";
      }

      if (BlogContext.getInstance().isInstalled())
      {
        prefix += request.getCurrentBlog().getServername();
      }
      else
      {
        prefix += request.getServerName();
      }

      prefix += ":" + request.getServerPort();
      prefix += request.getContextPath();

      if (!link.startsWith("/"))
      {
        prefix += "/";
      }
    }

    return prefix + link;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param object
   *
   * @return
   */
  public String buildLink(BlogRequest request, PermaObject object)
  {
    String link = null;

    if (object instanceof Blog) {}
    else
    {
      link = buildLink(request, "");

      if (object instanceof Entry)
      {
        link += "blog/entries/" + object.getId();
      }
      else if (object instanceof Category)
      {
        link += "blog/categories/" + object.getId();
      }
      else if (object instanceof Tag)
      {
        link += "blog/tag/" + ((Tag) object).getName();
      }
      else if (object instanceof Attachment)
      {
        link += "attachment/" + object.getId();
      }
    }

    return link;
  }
}
