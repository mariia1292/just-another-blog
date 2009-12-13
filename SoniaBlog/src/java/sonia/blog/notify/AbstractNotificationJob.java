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



package sonia.blog.notify;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogJob;
import sonia.blog.api.app.Constants;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.PermaObject;
import sonia.blog.entity.User;

import sonia.jobqueue.JobException;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 *
 * @param <T>
 */
public abstract class AbstractNotificationJob<T extends PermaObject>
        implements BlogJob
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(AbstractNotificationJob.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param blog
   * @param object
   */
  public AbstractNotificationJob(Blog blog, T object)
  {
    this.blog = blog;
    this.permaObject = object;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param user
   * @param message
   */
  protected abstract void sendNotification(User user, Message message);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param locale
   * @return
   */
  protected abstract Message getMessage(Locale locale);

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract List<BlogMember> getNotificationMembers();

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract boolean isActive();

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws JobException
   */
  public void excecute() throws JobException
  {
    if (isActive())
    {
      List<BlogMember> members = getNotificationMembers();

      if (Util.hasContent(members))
      {
        Locale locale = blog.getLocale();

        if (locale == null)
        {
          locale = Constants.DEFAULT_LOCALE;
        }

        Message message = getMessage(locale);
        int counter = 0;

        for (BlogMember member : members)
        {
          if (logger.isLoggable(Level.FINEST))
          {
            StringBuffer msg = new StringBuffer();

            msg.append("send notification to user ");
            msg.append(member.getUser().getId());
            logger.finest(msg.toString());
          }

          sendNotification(member.getUser(), message);
          counter++;
        }

        if (logger.isLoggable(Level.FINE))
        {
          StringBuffer msg = new StringBuffer();

          msg.append("send ").append(counter).append(" notifications");
          logger.fine(msg.toString());
        }
      }
      else if (logger.isLoggable(Level.FINEST))
      {
        logger.finest("no user for notification found");
      }
    }
    else if (logger.isLoggable(Level.WARNING))
    {
      StringBuffer msg = new StringBuffer();

      msg.append("service of ").append(getClass().getName());
      msg.append(" is not active");
      logger.warning(msg.toString());
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
    return blog;
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/12/13
   * @author         Enter your name here...
   */
  protected class Message
  {

    /**
     * Constructs ...
     *
     *
     * @param subject
     * @param content
     */
    public Message(String subject, String content)
    {
      this.subject = subject;
      this.content = content;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public String getContent()
    {
      return content;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getSubject()
    {
      return subject;
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private String content;

    /** Field description */
    private String subject;
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected Blog blog;

  /** Field description */
  protected T permaObject;
}
