/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
