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



package sonia.blog.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment.Type;
import sonia.blog.entity.Entry;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Sebastian Sdorra
 */
public class AutoTrackbackJob implements Runnable
{

  /** Field description */
  private static final Pattern INTERNAL_PATTERN =
    Pattern.compile("(?m)(?i)<a.*href=[\"'](http[^\"']*)[\"'].*>.*</a>");

  /** Field description */
  private static final long serialVersionUID = -3051850801915999862L;

  /** Field description */
  private static Logger logger =
    Logger.getLogger(AutoTrackbackJob.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   *
   * @param request
   * @param entry
   */
  public AutoTrackbackJob(BlogRequest request, Entry entry)
  {
    this.entry = entry;
    this.sender = new TrackbackSender(request, entry);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   */
  public void run()
  {
    if (BlogContext.getDAOFactory().getCommentDAO().count(entry,
            Type.TRACKBACK_SEND) == 0)
    {
      String content = entry.getContent();

      if (Util.hasContent(content))
      {
        List<URL> urls = buildUrls(content);

        for (URL url : urls)
        {
          try
          {
            sender.sendPing(url);
          }
          catch (Exception ex)
          {
            logger.log(Level.SEVERE, null, ex);
          }
        }
      }
    }
    else if (logger.isLoggable(Level.FINE))
    {
      StringBuffer msg = new StringBuffer("trackback for entry ");

      msg.append(entry.getId()).append(" allready send");
      logger.fine(msg.toString());
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Blog getBlog()
  {
    return entry.getBlog();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDescription()
  {
    return "auto trackback job for " + entry.getId();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return AutoTrackbackJob.class.getName();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param content
   *
   * @return
   */
  private List<URL> buildUrls(String content)
  {
    List<URL> urls = new ArrayList<URL>();
    Matcher m = INTERNAL_PATTERN.matcher(content);

    while (m.find())
    {
      String href = m.group(1);

      try
      {
        if (Util.hasContent(href))
        {
          urls.add(new URL(href));
        }
      }
      catch (MalformedURLException ex)
      {
        logger.log(Level.FINEST, null, ex);
      }
    }

    return urls;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Entry entry;

  /** Field description */
  private TrackbackSender sender;
}
