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

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.dao.PageDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.api.search.SearchCategory;
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.search.SearchEntry;
import sonia.blog.api.search.SearchException;
import sonia.blog.api.util.PageNavigation;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Page;
import sonia.blog.entity.Role;
import sonia.blog.util.BlogUtil;

import sonia.cache.ObjectCache;

import sonia.rss.AbstractBase;
import sonia.rss.Channel;
import sonia.rss.FeedParser;
import sonia.rss.Item;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.net.URL;

import java.text.DateFormat;
import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
public class AsyncMapping extends FinalMapping
{

  /** Field description */
  private static Logger logger = Logger.getLogger(AsyncMapping.class.getName());

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
        response.setContentType("application/x-javascript");

        if (provider.equals("search"))
        {
          search(request, response);
        }
        else if (provider.equals("feed"))
        {
          feed(request, response);
        }
        else if (provider.equals("navigation-options"))
        {
          navigationOptions(request, response);
        }
        else if (provider.equals("calendar"))
        {
          calendar(request, response);
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
  private void calendar(BlogRequest request, BlogResponse response)
          throws IOException
  {
    Calendar calendar = getCalendar(request);
    PrintWriter writer = null;
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    Date startDate = BlogUtil.createStartDate(year, month);
    Date endData = BlogUtil.createEndDate(year, month);
    List<Date> events = entryDAO.findAllCalendarDates(request.getCurrentBlog(),
                          startDate, endData);
    Calendar today = Calendar.getInstance();

    try
    {
      writer = response.getWriter();
      writer.print("{\"year\":");
      writer.print(year);
      writer.print(", \"month\":");
      writer.print(month + 1);
      writer.print(", \"weeks\":");
      writer.print(calendar.getActualMaximum(Calendar.WEEK_OF_MONTH));
      writer.print(", \"firstDay\":");
      writer.print(calendar.get(Calendar.DAY_OF_WEEK));
      writer.print(", \"daysOfMonth\":");
      writer.print(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

      if (today.get(Calendar.MONTH) == month)
      {
        writer.print(", \"today\":");
        writer.print(today.get(Calendar.DAY_OF_MONTH));
      }

      writer.print(", \"events\": \"");

      if (Util.hasContent(events))
      {
        List<Integer> days = getDays(events);
        Iterator<Integer> dayIt = days.iterator();

        while (dayIt.hasNext())
        {
          Integer day = dayIt.next();

          writer.print(day);

          if (dayIt.hasNext())
          {
            writer.print(",");
          }
        }
      }

      writer.println("\"}");
    }
    finally
    {
      if (writer != null)
      {
        writer.close();
      }
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
    String type = request.getParameter("type");

    if (Util.hasContent(urlParam) && Util.hasContent(type))
    {
      Channel channel = null;
      StringBuffer cacheKey = new StringBuffer();

      cacheKey.append(urlParam).append(":").append(type);

      ObjectCache cache =
        BlogContext.getInstance().getCacheManager().get(Constants.CACHE_FEED);

      if (cache != null)
      {
        channel = (Channel) cache.get(cacheKey.toString());
      }
      else if (logger.isLoggable(Level.WARNING))
      {
        StringBuffer msg = new StringBuffer();

        msg.append("cache ").append(Constants.CACHE_FEED).append(" not found");
        logger.warning(msg.toString());
      }

      if (channel == null)
      {
        FeedParser parser = FeedParser.getInstance(type);

        if (parser != null)
        {
          URL url = new URL(urlParam);
          InputStream in = null;

          try
          {
            in = url.openStream();
            channel = parser.load(in);

            if (channel != null)
            {
              printChannel(response, request.getCurrentBlog(), channel);
              cache.put(cacheKey.toString(), channel);
            }
            else
            {
              if (logger.isLoggable(Level.WARNING))
              {
                logger.warning("could not create channel object");
              }

              response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
        else
        {
          if (logger.isLoggable(Level.WARNING))
          {
            StringBuffer log = new StringBuffer();

            log.append("no parser for type ").append(type).append(" found");
            logger.warning(log.toString());
          }

          response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
      }
      else
      {
        printChannel(response, request.getCurrentBlog(), channel);
      }
    }
    else
    {
      if (logger.isLoggable(Level.WARNING))
      {
        logger.warning("url or feed is empty or null");
      }

      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
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

      printChildren(response, pages, exclude);
    }
  }

  /**
   * Method description
   *
   *
   * @param response
   * @param blog
   * @param channel
   *
   * @throws IOException
   */
  private void printChannel(BlogResponse response, Blog blog, Channel channel)
          throws IOException
  {
    PrintWriter writer = null;

    try
    {
      writer = response.getWriter();
      writer.println("[");

      DateFormat df = blog.getDateFormatter();

      writer.print("  {");
      printItem(writer, df, channel);
      writer.println(", \"items\": [");

      List<Item> items = channel.getItems();

      if (Util.hasContent(items))
      {
        Iterator<Item> itemIt = items.iterator();

        while (itemIt.hasNext())
        {
          writer.print("    {");
          printItem(writer, df, itemIt.next());
          writer.println(" }");

          if (itemIt.hasNext())
          {
            writer.print(", ");
          }
        }
      }

      writer.println("]}]");
    }
    finally
    {
      if (writer != null)
      {
        writer.close();
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param response
   * @param pages
   * @param exclude
   *
   * @throws IOException
   */
  private void printChildren(BlogResponse response,
                             List<? extends PageNavigation> pages, int exclude)
          throws IOException
  {
    PrintWriter writer = response.getWriter();

    writer.println("[");

    if (pages != null)
    {
      ResourceBundle bundle =
        ResourceBundle.getBundle("sonia.blog.resources.label");
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
   * @param df
   * @param item
   *
   * @throws IOException
   */
  private void printItem(PrintWriter writer, DateFormat df, AbstractBase item)
          throws IOException
  {
    writer.print(" \"title\": \"");
    writer.print((item.getTitle() != null)
                 ? item.getTitle()
                 : "");
    writer.print("\", \"date\": \"");

    if ((df != null) && (item.getPubDate() != null))
    {
      writer.print(df.format(item.getPubDate()));
    }

    writer.print("\", \"link\": \"");
    writer.print((item.getLink() != null)
                 ? item.getLink()
                 : "");
    writer.print("\"");
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
   * @param request
   * @param response
   *
   * @throws IOException
   */
  private void search(BlogRequest request, BlogResponse response)
          throws IOException
  {
    PrintWriter writer = response.getWriter();

    writer.println("[");

    String query = request.getParameter("query");

    if (Util.hasContent(query))
    {
      try
      {
        Blog blog = request.getCurrentBlog();
        SearchContext ctx = BlogContext.getInstance().getSearchContext();
        Collection<SearchCategory> categories = ctx.search(blog,
                                                  request.getLocale(), query);

        if (Util.hasContent(categories))
        {
          List<SearchEntry> entries = new ArrayList<SearchEntry>();

          for (SearchCategory cat : categories)
          {
            entries.addAll(cat.getEntries());
          }

          if (Util.hasContent(entries))
          {
            LinkBuilder linkBuilder =
              BlogContext.getInstance().getLinkBuilder();
            Iterator<SearchEntry> entryIt = entries.iterator();

            while (entryIt.hasNext())
            {
              SearchEntry e = entryIt.next();

              writer.print("  {");
              writer.print(" value : '");
              writer.print(e.getTitle());
              writer.print("',");
              writer.print(" url : '");
              writer.print(linkBuilder.buildLink(request, e.getData()));
              writer.print("'");

              if (entryIt.hasNext())
              {
                writer.println(" },");
              }
              else
              {
                writer.println(" }");
              }
            }
          }
        }
      }
      catch (SearchException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }

    writer.println("]");
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  private Calendar getCalendar(BlogRequest request)
  {
    Calendar calendar = null;
    int year = -1;
    int month = -1;

    try
    {
      String yearParam = request.getParameter("year");
      String monthParam = request.getParameter("month");

      if (Util.hasContent(yearParam))
      {
        year = Integer.parseInt(yearParam);
      }
      else
      {
        year = Calendar.getInstance().get(Calendar.YEAR);
      }

      if (Util.hasContent(monthParam))
      {
        month = Integer.parseInt(monthParam) - 1;
      }
      else
      {
        month = Calendar.getInstance().get(Calendar.MONTH);
      }

      calendar = Calendar.getInstance();
      calendar.set(Calendar.YEAR, year);
      calendar.set(Calendar.MONTH, month);
      calendar.set(Calendar.DAY_OF_MONTH, 1);
    }
    catch (NumberFormatException ex)
    {
      logger.log(Level.WARNING, null, ex);
    }

    return calendar;
  }

  /**
   * Method description
   *
   *
   * @param dates
   *
   * @return
   */
  private List<Integer> getDays(List<Date> dates)
  {
    List<Integer> days = new ArrayList<Integer>();
    Calendar cal = null;

    for (Date date : dates)
    {
      cal = Calendar.getInstance();
      cal.setTime(date);

      Integer day = new Integer(cal.get(Calendar.DAY_OF_MONTH));

      if (!days.contains(day))
      {
        days.add(day);
      }
    }

    return days;
  }
}
