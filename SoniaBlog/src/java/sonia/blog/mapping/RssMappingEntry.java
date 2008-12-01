/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingEntry;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;
import sonia.blog.entity.PermaObject;

import sonia.rss.Channel;
import sonia.rss.FeedParser;
import sonia.rss.Item;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class RssMappingEntry implements MappingEntry
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(RssMappingEntry.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public boolean handleMapping(BlogRequest request, BlogResponse response,
                               String[] param)
  {
    boolean found = true;

    if ((param != null) && (param.length > 0))
    {
      LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
      Blog blog = request.getCurrentBlog();
      String url = linkBuilder.buildLink(request, blog);
      EntityManager em = BlogContext.getInstance().getEntityManager();

      try
      {
        List<Item> items = new ArrayList<Item>();

        if (param[0].equals("entries"))
        {
          Query q = em.createNamedQuery("Entry.overview");

          q.setParameter("blog", blog);

          List<Entry> entries = q.getResultList();

          if (entries != null)
          {
            Collections.reverse(entries);

            for (Entry entry : entries)
            {
              String link = linkBuilder.buildLink(request, entry);
              Item item = new Item(entry.getTitle(), new URL(link),
                                   entry.getContent());

              item.setAuthor(entry.getAuthor().getDisplayName());
              item.setPubDate(entry.getCreationDate());
              items.add(item);
            }
          }
        }
        else if (param[0].equals("comments"))
        {
          Query q = em.createNamedQuery("Comment.findFromBlog");

          q.setParameter("blog", blog);

          List<Comment> comments = q.getResultList();

          if (comments != null)
          {
            for (Comment comment : comments)
            {
              String link = linkBuilder.buildLink(request, comment.getEntry());
              String title = "RE: " + comment.getEntry().getTitle();
              Item item = new Item(title, new URL(link), comment.getContent());

              item.setAuthor(comment.getAuthorName());
              item.setPubDate(comment.getCreationDate());
              items.add(item);
            }
          }
        }
        else
        {
          found = false;
        }

        if (found)
        {
          Channel channel = new Channel(blog.getTitle(), new URL(url),
                                        blog.getDescription());

          channel.setPubDate(blog.getCreationDate());
          channel.setItems(items);

          FeedParser parser = FeedParser.getInstance("rss2");

          parser.store(channel, response.getOutputStream());
        }
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
      finally
      {
        em.close();
      }
    }
    else
    {
      found = false;
    }

    if (!found)
    {
      try
      {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }

    return false;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param linkBuilder
   * @param object
   *
   * @return
   */
  public String getUri(BlogRequest request, LinkBuilder linkBuilder,
                       PermaObject object)
  {
    return null;
  }
}
