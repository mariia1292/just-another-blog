/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.link;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.search.SearchEntry;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.ContentObject;
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
    return buildLink(request, null, link);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param blog
   * @param link
   *
   * @return
   */
  public String buildLink(BlogRequest request, Blog blog, String link)
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

      if (blog != null)
      {
        prefix += blog.getServername();
      }
      else if (BlogContext.getInstance().isInstalled())
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

    if (object instanceof Blog)
    {
      link = buildLink(request, (Blog) object, "");
    }
    else
    {
      link = buildLink(request, "");

      if (object instanceof ContentObject)
      {
        if (request.getMapping() != null)
        {
          link = request.getMapping().getUri(request, this, object);
        }
        else
        {
          link += "list/" + object.getId() + ".jab";
        }
      }
      else if (object instanceof Category)
      {
        link += "categories/" + object.getId() + "/index.jab";
      }
      else if (object instanceof Tag)
      {
        link += "tags/" + ((Tag) object).getId() + "/index.jab";
      }
      else if (object instanceof Attachment)
      {
        link += "attachment/" + object.getId();
      }
    }

    return link;
  }
}
