/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.link;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.Mapping;
import sonia.blog.api.mapping.MappingNavigation;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.ContentObject;
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
        prefix += blog.getIdentifier();
      }
      else if (BlogContext.getInstance().isInstalled())
      {
        prefix += request.getCurrentBlog().getIdentifier();
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
        Mapping mapping = request.getMapping();

        if (mapping != null)
        {
          MappingNavigation navigation = mapping.getMappingNavigation();

          if (navigation != null)
          {
            link = navigation.getDetailUri(object);
          }
        }
        else
        {
          link += "list/" + object.getId() + ".jab";
        }
      }
      else if (object instanceof Category)
      {
        link += "category/" + object.getId() + "/index.jab";
      }
      else if (object instanceof Tag)
      {
        link += "tag/" + ((Tag) object).getId() + "/index.jab";
      }
      else if (object instanceof Attachment)
      {
        link += "attachment/" + object.getId();
      }
    }

    return link;
  }
}
