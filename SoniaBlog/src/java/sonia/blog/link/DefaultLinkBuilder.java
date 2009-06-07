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
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;
import sonia.blog.entity.PermaObject;
import sonia.blog.entity.Tag;

//~--- JDK imports ------------------------------------------------------------

import java.text.MessageFormat;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class DefaultLinkBuilder implements LinkBuilder
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultLinkBuilder.class.getName());

  //~--- methods --------------------------------------------------------------

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

        if ((mapping != null) && (mapping.getMappingNavigation() != null))
        {
          MappingNavigation navigation = mapping.getMappingNavigation();

          if (navigation != null)
          {
            link = navigation.getDetailUri(object);
          }
        }
        else if (object instanceof Entry)
        {
          link += "list/" + object.getId() + ".jab";
        }
        else if (object instanceof Page)
        {
          link += "page/" + object.getId() + ".jab";
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

  /**
   * Method description
   *
   *
   * @param blog
   * @param link
   *
   * @return
   */
  public String buildLink(Blog blog, String link)
  {
    if (linkScheme == null)
    {
      throw new IllegalStateException();
    }

    if (!link.startsWith("/"))
    {
      link = "/" + link;
    }

    return MessageFormat.format(linkScheme, blog.getIdentifier(), link);
  }

  /**
   * Method description
   *
   *
   * @param request
   */
  public void init(BlogRequest request)
  {
    StringBuffer link = new StringBuffer();

    link.append(request.getScheme()).append("://").append("{0}").append(":");
    link.append(request.getServerPort());
    link.append(request.getContextPath()).append("{1}");
    linkScheme = link.toString();

    if (logger.isLoggable(Level.FINE))
    {
      StringBuffer log = new StringBuffer();

      log.append("init LinkBuilder with Link-Scheme: ").append(linkScheme);
      logger.fine(log.toString());
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isInit()
  {
    return linkScheme != null;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String linkScheme;
}
