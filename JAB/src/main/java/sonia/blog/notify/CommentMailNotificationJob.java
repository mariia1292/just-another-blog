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
import sonia.blog.entity.Comment;
import sonia.blog.entity.Role;
import sonia.blog.notify.AbstractNotificationJob.Message;

//~--- JDK imports ------------------------------------------------------------

import java.text.MessageFormat;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author sdorra
 */
public class CommentMailNotificationJob extends MailNotificationJob<Comment>
{

  /** Field description */
  private static final String MESSAGE_CONTENT = "mailCommentNotifyText";

  /** Field description */
  private static final String MESSAGE_SUBJECT = "mailCommentNotifySubject";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param blog
   * @param object
   */
  public CommentMailNotificationJob(Blog blog, Comment object)
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
    return "Comment E-Mail notification job";
  }

  /**
   * Method description
   *
   *
   *
   * @param locale
   * @return
   */
  @Override
  protected Message getMessage(Locale locale)
  {
    ResourceBundle bundle =
      ResourceBundle.getBundle("sonia.blog.resources.message", locale);
    String subjectTemplate = bundle.getString(MESSAGE_SUBJECT);
    String textTemplate = bundle.getString(MESSAGE_CONTENT);
    StringBuffer linkBuffer = new StringBuffer("/list/");

    linkBuffer.append(permaObject.getEntry().getId()).append(".jab#");
    linkBuffer.append(permaObject.getId());

    String link = BlogContext.getInstance().getLinkBuilder().buildLink(blog,
                    linkBuffer.toString());
    String subject = MessageFormat.format(subjectTemplate, blog.getTitle(),
                       permaObject.getEntry().getTitle(),
                       permaObject.getAuthorName());
    String text = MessageFormat.format(textTemplate,
                                       permaObject.getEntry().getTitle(),
                                       permaObject.getContent(),
                                       permaObject.getAuthorName(), link);

    return new Message(subject, text);
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
    List<BlogMember> members =
      BlogContext.getDAOFactory().getBlogDAO().getCommentNotifyMembers(blog,
        true, true);
    Iterator<BlogMember> memberIt = members.iterator();

    while (memberIt.hasNext())
    {
      BlogMember m = memberIt.next();

      if (m.getRole().getValue() < Role.AUTHOR.getValue())
      {
        memberIt.remove();
      }
    }

    return members;
  }
}
