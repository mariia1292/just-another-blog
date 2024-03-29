/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://kenai.com/projects/jab
 *
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.commons.lang.StringEscapeUtils;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.dao.PageDAO;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.api.msg.BlogMessage;
import sonia.blog.api.spam.SpamInputProtection;
import sonia.blog.api.util.PageNavigation;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Page;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.cache.ObjectCache;

import sonia.io.TeeWriter;

import sonia.plugin.service.ServiceReference;

import sonia.util.Util;

import sonia.web.io.JSONWriter;

//~--- JDK imports ------------------------------------------------------------

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import java.net.URL;

import java.text.DateFormat;
import java.text.MessageFormat;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Sebastian Sdorra
 */
public class AsyncMapping extends FinalMapping
{

  /** Field description */
  private static Logger logger = Logger.getLogger(AsyncMapping.class.getName());

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public SpamInputProtection getSpamInputMethod()
  {
    SpamInputProtection method = null;
    String configString =
      BlogContext.getInstance().getConfiguration().getString(
          Constants.CONFIG_SPAMMETHOD);
    ServiceReference<SpamInputProtection> spamServiceReference =
      BlogContext.getInstance().getServiceRegistry().get(
          SpamInputProtection.class, Constants.SERVICE_SPAMPROTECTIONMETHOD);

    if (!Util.isBlank(configString))
    {
      if (!configString.equalsIgnoreCase("none"))
      {
        List<SpamInputProtection> list = spamServiceReference.getAll();

        for (SpamInputProtection sp : list)
        {
          if (sp.getClass().getName().equals(configString))
          {
            method = sp;
          }
        }

        if (method == null)
        {
          logger.warning("method " + configString
                         + " not found, using default");
          method = list.get(0);
        }
      }
    }
    else
    {
      method = spamServiceReference.get();
    }

    return method;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void handleFinalMapping(BlogRequest request, BlogResponse response,
                                    String[] param)
          throws IOException, ServletException
  {
    if ((param != null) && (param.length > 0))
    {
      String provider = param[0];

      if (Util.hasContent(provider))
      {
        String type = param[1];

        if (Util.hasContent(type))
        {
          if (type.equals("json"))
          {
            response.setContentType("application/x-javascript");
          }
          else if (type.equals("plain") || type.equals("txt"))
          {
            response.setContentType("text/plain");
          }
          else if (type.equals("xml"))
          {
            response.setContentType("text/xml");
          }
          else if (type.equals("html"))
          {
            response.setContentType("text/html");
          }
          else
          {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
          }

          if (provider.equals("feed"))
          {
            feed(request, response);
          }
          else if (provider.equals("navigation-options"))
          {
            navigationOptions(request, response);
          }
          else if (provider.equals("messages"))
          {
            messages(request, response);
          }
          else if (provider.equals("user"))
          {
            user(request, response);
          }
          else if (provider.equals("blog"))
          {
            blog(request, response);
          }
          else if (provider.equals("spam"))
          {
            spam(request, response);
          }
        }
        else
        {
          response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
      }
      else
      {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @throws IOException
   */
  private void blog(BlogRequest request, BlogResponse response)
          throws IOException
  {
    if (request.isUserInRole(Role.GLOBALADMIN))
    {
      String filter = request.getParameter("filter");

      if (filter == null)
      {
        filter = "";
      }

      JSONWriter writer = new JSONWriter(response.getWriter());

      try
      {
        writer.startArray();

        BlogDAO blogDAO = BlogContext.getDAOFactory().getBlogDAO();
        List<Blog> blogs = blogDAO.getAll(filter, 0, 10);

        if (Util.hasContent(blogs))
        {
          Iterator<Blog> it = blogs.iterator();

          while (it.hasNext())
          {
            Blog blog = it.next();

            writer.startObject();
            writer.write("identifier", blog.getIdentifier(), false);
            writer.write("title", blog.getTitle(), false);
            writer.write("active", blog.isActive(), true);
            writer.endObject(!it.hasNext());
          }
        }

        writer.endArray(true);
      }
      finally
      {
        writer.close();
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
  }

  /**
   * Method description
   *
   * @param request
   * @param response
   *
   * @throws IOException
   */
  private void feed(BlogRequest request, BlogResponse response)
          throws IOException
  {
    String urlParam = request.getParameter("url");

    if (Util.hasContent(urlParam))
    {
      PrintWriter writer = response.getWriter();
      ObjectCache feedCache =
        BlogContext.getInstance().getCacheManager().get(Constants.CACHE_FEED);

      if (feedCache != null)
      {
        String cacheKey =
          new StringBuffer(urlParam).append(":").append(
              request.getCurrentBlog().getDateFormat()).toString();
        String result = (String) feedCache.get(cacheKey);

        if (Util.hasContent(result))
        {
          writer.write(result);
          writer.close();
          writer = null;
        }
        else
        {
          StringWriter cacheWriter = new StringWriter();
          TeeWriter w = new TeeWriter(writer, cacheWriter);

          processFeed(request, response, w, urlParam);
          w.flush();
          feedCache.put(cacheKey, cacheWriter.toString());
          writer.close();
        }
      }
      else if (writer != null)
      {
        processFeed(request, response, writer, urlParam);
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  private void messages(BlogRequest request, BlogResponse response)
          throws IOException
  {
    JSONWriter writer = new JSONWriter(response.getWriter());

    try
    {
      writer.startArray();

      HttpSession session = request.getSession();

      if (session != null)
      {
        List<BlogMessage> messages =
          (List<BlogMessage>) session.getAttribute(BlogMessage.SESSION_VAR);

        if (Util.hasContent(messages))
        {
          Iterator<BlogMessage> messageIt = messages.iterator();

          while (messageIt.hasNext())
          {
            BlogMessage msg = messageIt.next();
            String clientId = (msg.getClientId() != null)
                              ? msg.getClientId()
                              : "";

            writer.startObject();
            writer.write("level", msg.getLevel(), false);
            writer.write("clientId", clientId, false);
            writer.write("summary", msg.getSummary(), false);
            writer.write("detail", msg.getDetail(), true);
            writer.endObject(!messageIt.hasNext());
          }

          messages.clear();
        }
      }

      writer.endArray(true);
    }
    finally
    {
      writer.close();
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @throws IOException
   */
  private void navigationOptions(BlogRequest request, BlogResponse response)
          throws IOException
  {
    if (!request.getBlogSession().hasRole(Role.AUTHOR))
    {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
    else
    {
      String idString = request.getParameter("parent");
      String excludeString = request.getParameter("exclude");

      // ugly workaround
      if (Util.isBlank(excludeString))
      {
        excludeString = request.getParameter("amp;exclude");
      }

      Page page = null;
      int exclude = -1;
      PageDAO pageDAO = BlogContext.getDAOFactory().getPageDAO();

      try
      {
        if (Util.hasContent(idString))
        {
          long id = Long.parseLong(idString);

          page = pageDAO.get(id);
        }

        if (Util.hasContent(excludeString))
        {
          exclude = Integer.parseInt(excludeString);
        }
      }
      catch (NumberFormatException ex)
      {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      }

      List<? extends PageNavigation> pages = null;

      if (page != null)
      {
        pages = pageDAO.getChildren(page);
      }
      else
      {
        pages = pageDAO.getAllRoot(request.getCurrentBlog());
      }

      printChildren(request, response, pages, exclude);
    }
  }

  /**
   * Method description
   *
   *
   * @param writer
   * @param key
   * @param value
   * @param last
   *
   * @throws IOException
   */
  private void printBoolean(Writer writer, String key, boolean value,
                            boolean last)
          throws IOException
  {
    if (Util.hasContent(key))
    {
      writer.append("\"").append(key).append("\": ");
      writer.append(Boolean.toString(value));

      if (!last)
      {
        writer.append(", ");
      }
    }
  }

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param response
   * @param pages
   * @param exclude
   *
   * @throws IOException
   */
  private void printChildren(BlogRequest request, BlogResponse response,
                             List<? extends PageNavigation> pages, int exclude)
          throws IOException
  {
    PrintWriter writer = response.getWriter();

    writer.println("[");

    if (pages != null)
    {
      ResourceBundle bundle =
        ResourceBundle.getBundle("sonia.blog.resources.label",
                                 request.getLocale());
      int size = pages.size();
      int firstPos = 1000;

      if (size > 0)
      {
        firstPos = pages.get(0).getNavigationPosition() / 2;
      }

      String label = bundle.getString("pagePostion_first");

      printOption(writer, label, firstPos);
      label = bundle.getString("pagePostion_after");

      for (int i = 0; i < size; i++)
      {
        PageNavigation page = pages.get(i);

        if (exclude != page.getNavigationPosition())
        {
          writer.println(",");

          int nextPos = 0;

          try
          {
            if (i + 1 < size)
            {
              nextPos = ((pages.get(i + 1).getNavigationPosition()
                          - page.getNavigationPosition()) / 2) + page
                            .getNavigationPosition();
            }
            else
            {
              nextPos = page.getNavigationPosition() + 1000;
            }

            printOption(writer,
                        MessageFormat.format(label, page.getNavigationTitle()),
                        nextPos);
          }
          catch (Exception ex)
          {
            logger.log(Level.WARNING, null, ex);
          }
        }
      }
    }

    writer.println();
    writer.println("]");
  }

  /**
   * Method description
   *
   *
   * @param writer
   * @param format
   * @param key
   * @param value
   * @param last
   *
   * @throws IOException
   */
  private void printDate(Writer writer, DateFormat format, String key,
                         Date value, boolean last)
          throws IOException
  {
    if (format != null)
    {
      printString(writer, key, (value != null)
                               ? format.format(value)
                               : null, last);
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param writer
   * @param feed
   *
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  private void printFeed(BlogRequest request, Writer writer, SyndFeed feed)
          throws IOException
  {
    DateFormat format = request.getCurrentBlog().getDateFormatter();

    writer.append("{\n");
    printString(writer, "title", feed.getTitle(), false);
    printDate(writer, format, "date", feed.getPublishedDate(), false);
    printString(writer, "link", feed.getLink(), false);
    writer.append("\"items\": [\n");

    List<SyndEntry> entries = feed.getEntries();

    if (Util.hasContent(entries))
    {
      Iterator<SyndEntry> entryIt = entries.iterator();

      while (entryIt.hasNext())
      {
        SyndEntry entry = entryIt.next();

        writer.append("{");
        printString(writer, "title", entry.getTitle(), false);
        printDate(writer, format, "date", entry.getPublishedDate(), false);
        printString(writer, "link", entry.getLink(), true);
        writer.append("}");

        if (entryIt.hasNext())
        {
          writer.append(",\n");
        }
      }
    }

    writer.append("]}");
  }

  /**
   * Method description
   *
   *
   * @param writer
   * @param label
   * @param value
   */
  private void printOption(PrintWriter writer, String label, int value)
  {
    writer.print("  {\"label\": \"");
    writer.print(label);
    writer.print("\", \"value\": \"");
    writer.print(value);
    writer.print("\"}");
  }

  /**
   * Method description
   *
   *
   * @param writer
   * @param key
   * @param value
   * @param last
   *
   * @throws IOException
   */
  private void printString(Writer writer, String key, String value,
                           boolean last)
          throws IOException
  {
    if (Util.hasContent(key))
    {
      writer.append("\"").append(key).append("\": ").append("\"");

      if (value != null)
      {
        StringEscapeUtils.escapeHtml(writer, value);
      }

      writer.append("\"");

      if (!last)
      {
        writer.append(", ");
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param writer
   * @param url
   *
   * @throws IOException
   */
  private void processFeed(BlogRequest request, BlogResponse response,
                           Writer writer, String url)
          throws IOException
  {
    URL feedUrl = new URL(url);
    InputStream in = null;

    try
    {
      in = feedUrl.openConnection().getInputStream();

      SyndFeed feed = getFeed(in);

      if (feed != null)
      {
        printFeed(request, writer, feed);
      }
      else
      {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      }
    }
    finally
    {
      if (in != null)
      {
        in.close();
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @throws IOException
   */
  private void spam(BlogRequest request, BlogResponse response)
          throws IOException
  {
    PrintWriter writer = response.getWriter();

    try
    {
      String answer = getSpamInputMethod().renderInput(request, writer);

      request.getSession().setAttribute(SpamInputProtection.REQUESTKEY, answer);
    }
    finally
    {
      writer.close();
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @throws IOException
   */
  private void user(BlogRequest request, BlogResponse response)
          throws IOException
  {
    if (request.isUserInRole(Role.GLOBALADMIN))
    {
      String filter = request.getParameter("filter");

      if (filter == null)
      {
        filter = "";
      }

      PrintWriter writer = response.getWriter();

      try
      {
        writer.println("[");

        UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();
        List<User> users = userDAO.getAll(filter, 0, 10);

        if (Util.hasContent(users))
        {
          Iterator<User> it = users.iterator();

          while (it.hasNext())
          {
            User user = it.next();

            writer.append("{");
            printString(writer, "name", user.getName(), false);
            printString(writer, "displayName", user.getDisplayName(), false);
            printBoolean(writer, "active", user.isActive(), true);
            writer.append("}");

            if (it.hasNext())
            {
              writer.append(",");
            }
          }
        }

        writer.println("]");
      }
      finally
      {
        writer.close();
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param in
   *
   * @return
   */
  private SyndFeed getFeed(InputStream in)
  {
    SyndFeed result = null;

    try
    {

      // TODO replace UTF-8
      result = new SyndFeedInput().build(new InputStreamReader(in, "UTF-8"));
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }

    return result;
  }
}
