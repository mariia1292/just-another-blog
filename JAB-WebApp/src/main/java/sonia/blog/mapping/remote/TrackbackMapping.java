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



package sonia.blog.mapping.remote;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.CommentDAO;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.api.spam.SpamCheck;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;

import sonia.plugin.service.Service;
import sonia.plugin.service.ServiceReference;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
public class TrackbackMapping extends FinalMapping
{

  /** Field description */
  public static final int CODE_ERROR = 3;

  /** Field description */
  public static final int CODE_NOPOST = 1;

  /** Field description */
  public static final int CODE_NOURL = 2;

  /** Field description */
  public static final int CODE_OK = 0;

  /** Field description */
  public static final String MSG_ERROR = "unknown error";

  /** Field description */
  public static final String MSG_NOPOST = "only post method is allowed";

  /** Field description */
  public static final String MSG_NOURL = "url parameter is missing";

  /** Field description */
  public static final String MSG_OK = "Trackback accepted";

  /** Field description */
  private static final String METHOD_POST = "post";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(TrackbackMapping.class.getName());

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
    if (request.getCurrentBlog().isAllowTrackbacks())
    {
      response.setContentType("text/xml");

      PrintWriter writer = response.getWriter();

      writer.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
      writer.println("<response>");

      if (request.getMethod().equalsIgnoreCase(METHOD_POST))
      {
        if ((param != null) && (param.length == 1))
        {
          try
          {
            Long entryId = Long.parseLong(param[0]);
            Entry entry = entryDAO.get(entryId);

            if (entry != null)
            {
              Comment trackback = null;
              String url = request.getParameter("url");

              if (Util.hasContent(url))
              {
                trackback = new Comment(Comment.Type.TRACKBACK_RECEIVE);
                trackback.setAuthorURL(url);

                StringBuffer content = new StringBuffer("[Trackback]");
                String title = request.getParameter("title");

                if (Util.hasContent(title))
                {
                  content.append(": ").append(title);
                }

                String blogname = request.getParameter("blog_name");

                if (Util.hasContent(blogname))
                {
                  trackback.setAuthorName(blogname);
                }

                trackback.setAuthorAddress(request.getRemoteAddr());
                trackback.setAuthorURL(url);
                trackback.setContent(content.toString());
                trackback.setEntry(entry);

                boolean spam = isSpam(request, trackback);

                if (!spam
                    || BlogContext.getInstance().getConfiguration().getBoolean(
                      Constants.CONFIG_STORE_TRACKBACKSPAM, Boolean.TRUE))
                {
                  trackback.setSpam(spam);

                  if (commentDAO.add(
                          BlogContext.getInstance().getSystemBlogSession(),
                          trackback))
                  {
                    writeResponse(writer, CODE_OK, MSG_OK);
                    response.setStatus(HttpServletResponse.SC_OK);
                  }
                  else
                  {
                    if (logger.isLoggable(Level.WARNING))
                    {
                      StringBuffer msg = new StringBuffer();

                      msg.append("error during trackback add from ").append(
                          request.getRemoteAddr()).append(" with url ").append(
                          trackback.getAuthorURL());
                      logger.warning(msg.toString());
                    }

                    writeResponse(writer, CODE_ERROR, MSG_ERROR);
                  }
                }
                else
                {
                  if (logger.isLoggable(Level.WARNING))
                  {
                    StringBuffer msg = new StringBuffer("reject spam from ");

                    msg.append(request.getRemoteAddr()).append(" with url ");
                    msg.append(trackback.getAuthorURL());
                    logger.warning(msg.toString());
                  }

                  writeResponse(writer, CODE_ERROR, MSG_ERROR);
                }
              }
              else
              {
                writeResponse(writer, CODE_NOURL, MSG_NOURL);
              }
            }
            else
            {
              response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
          }
          catch (NumberFormatException ex)
          {
            logger.log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
          }
        }
        else
        {
          response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
      }
      else
      {
        writeResponse(writer, CODE_NOPOST, MSG_NOPOST);
      }

      writer.println("</response>");
      writer.close();
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
   * @param writer
   * @param code
   * @param msg
   */
  private void writeResponse(PrintWriter writer, int code, String msg)
  {
    writer.write("<error>");
    writer.write(Integer.toString(code));
    writer.println("</error>");

    if (Util.hasContent(msg))
    {
      writer.write("<message>");
      writer.write(msg);
      writer.println("</message>\n");
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param comment
   *
   * @return
   */
  private boolean isSpam(BlogRequest request, Comment comment)
  {
    boolean result = false;

    if (spamCheckReference != null)
    {
      List<SpamCheck> list = spamCheckReference.getAll();

      if (Util.hasContent(list))
      {
        for (SpamCheck check : list)
        {
          if (check.isSpam(request, comment))
          {
            result = true;

            break;
          }
        }
      }
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Dao
  private CommentDAO commentDAO;

  /** Field description */
  @Dao
  private EntryDAO entryDAO;

  /** Field description */
  @Service(Constants.SERVICE_SPAMCHECK)
  private ServiceReference<SpamCheck> spamCheckReference;
}
