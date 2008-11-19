/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.rss;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.plugin.AbstractPluginServlet;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;

import sonia.rss.Channel;
import sonia.rss.FeedParser;
import sonia.rss.Item;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class RssServlet extends AbstractPluginServlet
{

  /** Field description */
  private static final long serialVersionUID = -4720876075414522568L;

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @throws IOException
   * @throws ServletException
   */
  @SuppressWarnings("unchecked")
  public void printRss(BlogRequest request, HttpServletResponse response)
          throws ServletException, IOException
  {
    response.setContentType("application/rss+xml");

    EntityManager em = BlogContext.getInstance().getEntityManager();

    try
    {
      Blog blog = request.getCurrentBlog();
      String url;

      if (request.isSecure())
      {
        url = "https://";
      }
      else
      {
        url = "http://";
      }

      url += request.getServerName();
      url += "/" + request.getContextPath();

      Channel channel = new Channel(blog.getTitle(), new URL(url),
                                    blog.getDescription());

      channel.setPubDate(blog.getCreationDate());

      LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
      List<Item> items = new ArrayList<Item>();

      if (request.getRequestURI().endsWith("/entries.rss"))
      {
        Query q = em.createNamedQuery("Entry.overview");

        q.setParameter("blog", blog);

        List<Entry> entries = q.getResultList();

        if (entries != null)
        {
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
      else if (request.getRequestURI().endsWith("/comments.rss"))
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

      channel.setItems(items);

      FeedParser parser = FeedParser.getInstance("rss2");

      parser.store(channel, response.getOutputStream());
    }
    finally
    {
      em.close();
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String getMapping()
  {
    return ".*.rss";
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException
  {
    printRss((BlogRequest) request, response);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void doPost(HttpServletRequest request,
                        HttpServletResponse response)
          throws ServletException, IOException
  {
    printRss((BlogRequest) request, response);
  }
}
