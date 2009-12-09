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



package sonia.blog.spam;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.spam.SpamCheck;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Trackback;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
public class BlacklistSpamCheck implements SpamCheck
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(BlacklistSpamCheck.class.getName());

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

    if (blacklist == null)
    {
      loadBlacklist(request);
    }

    String url = comment.getAuthorURL();

    if (Util.hasContent(url))
    {
      result = isSpam(url);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param trackback
   *
   * @return
   */
  public boolean isSpam(BlogRequest request, Trackback trackback)
  {
    boolean result = false;

    if (blacklist == null)
    {
      loadBlacklist(request);
    }

    String url = trackback.getUrl();

    if (Util.hasContent(url))
    {
      result = isSpam(url);
    }

    return result;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   */
  private void loadBlacklist(BlogRequest request)
  {
    blacklist = new ArrayList<String>();

    String path = request.getRealPath("/WEB-INF/config/blacklist.txt");
    File file = new File(path);

    if (file.isFile())
    {
      if (logger.isLoggable(Level.FINER))
      {
        StringBuffer log = new StringBuffer();

        log.append("load black from ").append(file.getAbsolutePath());
        logger.finer(log.toString());
      }

      readBlackList(file);
    }

    if (logger.isLoggable(Level.INFO))
    {
      StringBuffer log = new StringBuffer();

      log.append("loaded blacklist with ").append(blacklist.size());
      log.append(" entries");
      logger.info(log.toString());
    }
  }

  /**
   * Method description
   *
   *
   * @param file
   */
  private void readBlackList(File file)
  {
    BufferedReader reader = null;

    try
    {
      reader = new BufferedReader(new FileReader(file));

      String line = reader.readLine();

      while (line != null)
      {
        line = line.trim();

        if (!line.startsWith("#"))
        {
          int index = line.indexOf("#");

          if (index > 0)
          {
            line = line.substring(0, index).trim();
          }

          blacklist.add(line);
        }

        line = reader.readLine();
      }
    }
    catch (IOException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      try
      {
        reader.close();
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param url
   *
   * @return
   */
  private boolean isSpam(String url)
  {
    boolean result = false;

    for (String expression : blacklist)
    {
      Pattern p = Pattern.compile(expression);
      Matcher m = p.matcher(url);

      if (m.find())
      {
        if (logger.isLoggable(Level.FINER))
        {
          StringBuffer log = new StringBuffer();

          log.append("spam detected by url blacklist: ").append(url);
          log.append(" matched by ").append(expression);
          logger.finer(log.toString());
        }

        result = true;

        break;
      }
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<String> blacklist;
}
