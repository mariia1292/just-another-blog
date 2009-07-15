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
import sonia.blog.api.app.BlogJob;
import sonia.blog.api.app.MailService;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Entry;
import sonia.blog.entity.User;

import sonia.jobqueue.JobException;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.text.MessageFormat;

import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class NotificationJob implements BlogJob
{

  /** Field description */
  public static final String DESCRIPTION = "Notification Job";

  /*
   * # 0 - Entry title
   * # 1 - Entry teaser
   * # 2 - Entry content
   * # 3 - Author displayName
   * # 4 - Entry link
   */

  /** Field description */
  private static final String MESSAGE_SUBJECT = "mailNotifySubject";

  /** Field description */
  private static final String MESSAGE_TEXT = "mailNotifyText";

  /** Field description */
  private static final long serialVersionUID = -2475497490029616458L;

  /** Field description */
  private static Logger logger =
    Logger.getLogger(NotificationJob.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   *
   * @param bundle
   * @param blog
   * @param entry
   */
  public NotificationJob(ResourceBundle bundle, Blog blog, Entry entry)
  {
    this.bundle = bundle;
    this.blog = blog;
    this.entry = entry;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws JobException
   */
  public void excecute() throws JobException
  {
    MailService mailService = BlogContext.getInstance().getMailService();

    if (mailService.isConfigured())
    {
      List<BlogMember> members = getMembers();

      if (Util.hasContent(members))
      {
        User author = entry.getAuthor();
        StringBuffer linkPath = new StringBuffer();

        linkPath.append("/list/").append(entry.getId()).append(".jab");

        String link =
          BlogContext.getInstance().getLinkBuilder().buildLink(blog,
            linkPath.toString());
        String entryText = Util.hasContent(entry.getTeaser())
                           ? entry.getTeaser()
                           : entry.getContent();
        String subject =
          MessageFormat.format(bundle.getString(MESSAGE_SUBJECT),
                               blog.getTitle(), entry.getTitle(),
                               author.getDisplayName());
        String text = MessageFormat.format(bundle.getString(MESSAGE_TEXT),
                                           entry.getTitle(), entryText,
                                           entry.getTeaser(),
                                           entry.getContent(),
                                           author.getDisplayName(), link);

        for (BlogMember member : members)
        {
          sendNotification(mailService, member.getUser(), subject, text);
        }
      }
    }
    else
    {
      logger.warning("mail service is not configured");
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

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDescription()
  {
    return DESCRIPTION;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return getClass().getSimpleName();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param mailService
   * @param user
   * @param subject
   * @param text
   */
  private void sendNotification(MailService mailService, User user,
                                String subject, String text)
  {
    if (logger.isLoggable(Level.FINE))
    {
      StringBuffer log = new StringBuffer();

      log.append("send notification to ").append(user.getEmail());
      logger.fine(log.toString());
    }

    try
    {
      mailService.sendHtmlMail(user.getEmail(), blog.getEmail(), subject, text);
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private List<BlogMember> getMembers()
  {
    return BlogContext.getDAOFactory().getBlogDAO().getMembers(blog, true,
            true);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Blog blog;

  /** Field description */
  private ResourceBundle bundle;

  /** Field description */
  private Entry entry;
}