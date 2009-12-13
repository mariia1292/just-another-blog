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

import sonia.blog.api.app.BlogContext;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Entry;
import sonia.blog.entity.User;
import sonia.blog.notify.AbstractNotificationJob.Message;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.text.MessageFormat;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author sdorra
 */
public class EntryMailNotificationJob extends MailNotificationJob<Entry>
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

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param blog
   * @param object
   */
  public EntryMailNotificationJob(Blog blog, Entry object)
  {
    super(blog, object);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDescription()
  {
    return "Entry E-Mail notification job";
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected Message getMessage(Locale locale)
  {
    ResourceBundle bundle =
      ResourceBundle.getBundle("sonia.blog.resources.message", locale);
    User author = permaObject.getAuthor();
    StringBuffer linkPath = new StringBuffer();

    linkPath.append("/list/").append(permaObject.getId()).append(".jab");

    String link = BlogContext.getInstance().getLinkBuilder().buildLink(blog,
                    linkPath.toString());
    String entryText = Util.hasContent(permaObject.getTeaser())
                       ? permaObject.getTeaser()
                       : permaObject.getContent();
    String subject = MessageFormat.format(bundle.getString(MESSAGE_SUBJECT),
                       blog.getTitle(), permaObject.getTitle(),
                       author.getDisplayName());
    String content = MessageFormat.format(bundle.getString(MESSAGE_TEXT),
                       permaObject.getTitle(), entryText,
                       permaObject.getTeaser(), permaObject.getContent(),
                       author.getDisplayName(), link);

    return new Message(subject, content);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected List<BlogMember> getNotificationMembers()
  {
    return BlogContext.getDAOFactory().getBlogDAO().getEntryNotifyMembers(blog,
            true, true);
  }
}
