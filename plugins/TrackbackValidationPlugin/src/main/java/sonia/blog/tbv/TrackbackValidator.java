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



package sonia.blog.tbv;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.spam.SpamCheck;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class TrackbackValidator implements SpamCheck
{

  /** Field description */
  private static final int CONNECTION_TIMEOUT = 300;

  /** Field description */
  private static Logger logger =
    Logger.getLogger(TrackbackValidator.class.getName());

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param comment
   *
   * @return
   */
  public boolean isSpam(BlogRequest request, Comment comment)
  {
    boolean result = false;

    if (comment.getType() == Comment.Type.TRACKBACK_RECEIVE)
    {
      result = checkTrackback(request, comment);
    }
    else if (logger.isLoggable(Level.FINEST))
    {
      StringBuffer log = new StringBuffer("check only comments of type ");

      log.append(Comment.Type.TRACKBACK_RECEIVE);
      logger.finest(log.toString());
    }

    return result;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param comment
   *
   * @return
   */
  private boolean checkTrackback(BlogRequest request, Comment comment)
  {
    boolean result = true;
    String authorUrl = comment.getAuthorURL();

    if (Util.isNotEmpty(authorUrl))
    {
      Blog blog = request.getCurrentBlog();

      if (blog != null)
      {
        String blogUrl =
          BlogContext.getInstance().getLinkBuilder().buildLink(request, blog);

        result = validateTrackbackURL(authorUrl, blogUrl);
      }
      else
      {
        throw new IllegalStateException("no current blog found");
      }
    }
    else if (logger.isLoggable(Level.WARNING))
    {
      logger.warning("trackback has no authorURL, mark trackback as spam");
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param connection
   * @param blogUrl
   *
   * @return
   *
   * @throws IOException
   */
  private boolean searchBlogUrl(URLConnection connection, String blogUrl)
          throws IOException
  {
    boolean result = true;
    InputStream in = null;

    try
    {
      in = connection.getInputStream();

      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String line = reader.readLine();

      while (line != null)
      {
        if (line.contains(blogUrl))
        {
          result = false;

          break;
        }

        line = reader.readLine();
      }
    }
    finally
    {
      if (in != null)
      {
        in.close();
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param authorUrl
   * @param blogUrl
   *
   * @return
   */
  private boolean validateTrackbackURL(String authorUrl, String blogUrl)
  {
    boolean result = true;

    try
    {
      URL url = new URL(authorUrl);
      URLConnection connection = url.openConnection();

      connection.setConnectTimeout(CONNECTION_TIMEOUT);

      String contentType = connection.getContentType();

      if (isContentTypeValid(contentType))
      {
        if (logger.isLoggable(Level.FINEST))
        {
          StringBuffer log = new StringBuffer("check trackback url '");

          log.append(authorUrl).append("'");
          logger.finest(log.toString());
        }

        result = searchBlogUrl(connection, blogUrl);

        if (result && logger.isLoggable(Level.WARNING))
        {
          StringBuffer log = new StringBuffer();

          log.append("'").append(blogUrl).append(" not found in content of '");
          log.append(authorUrl).append("'");
          logger.warning(log.toString());
        }
      }
      else if (logger.isLoggable(Level.WARNING))
      {
        StringBuffer log = new StringBuffer();
        String ct = (contentType != null)
                    ? contentType
                    : "";

        log.append("content type '").append(ct).append("' is not valid, ");
        log.append("mark trackback as spam");
        logger.warning(log.toString());
      }
    }
    catch (IOException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param contentType
   *
   * @return
   */
  private boolean isContentTypeValid(String contentType)
  {
    return Util.isNotEmpty(contentType)
           && contentType.toLowerCase().startsWith("text");
  }
}
