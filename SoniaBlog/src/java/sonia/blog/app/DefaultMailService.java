/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.MailService;
import sonia.blog.util.SmtpAuthenticator;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author sdorra
 */
public class DefaultMailService implements MailService
{

  /**
   * Method description
   *
   *
   * @param to
   * @param from
   * @param subject
   * @param text
   *
   * @throws Exception
   */
  public void sendHtmlMail(String to, String from, String subject, String text)
          throws Exception
  {
    sendMail(to, from, subject, text, "text/html");
  }

  /**
   * Method description
   *
   *
   * @param to
   * @param from
   * @param subject
   * @param text
   *
   * @throws Exception
   */
  public void sendMail(String to, String from, String subject, String text)
          throws Exception
  {
    sendMail(to, from, subject, text, "text/plain");
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isConfigured()
  {
    BlogConfiguration config = BlogContext.getInstance().getConfiguration();

    return config.contains(Constants.CONFIG_SMTPSERVER)
           && config.contains(Constants.CONFIG_SMTPPORT);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private Session createSession()
  {
    BlogConfiguration config = BlogContext.getInstance().getConfiguration();
    Properties props = new Properties(System.getProperties());
    String server = config.getString(Constants.CONFIG_SMTPSERVER);

    if (Util.isBlank(server))
    {
      throw new IllegalStateException(Constants.CONFIG_SMTPSERVER
                                      + " is blank");
    }

    props.put("mail.smtp.host", server);
    props.put("mail.smtp.port",
              config.getInteger(Constants.CONFIG_SMTPPORT, 25));

    if (config.getBoolean(Constants.CONFIG_SMTPSTARTTLS, Boolean.FALSE))
    {
      props.put("mail.smtp.starttls.enable", Boolean.TRUE);
    }

    Authenticator auth = null;
    String user = config.getString(Constants.CONFIG_SMTPUSER);

    if (!Util.isBlank(user))
    {
      props.put("mail.smtp.user", user);

      String password = config.getSecureString(Constants.CONFIG_SMTPPASSWORD);

      if (!Util.isBlank(password))
      {
        props.put("mail.smtp.auth", "true");
        auth = new SmtpAuthenticator(user, password);
      }
    }

    Session session = Session.getInstance(props, auth);

    if (config.getBoolean(Constants.CONFIG_SMTPDEBUG, Boolean.FALSE))
    {
      session.setDebug(true);
    }

    return session;
  }

  /**
   * Method description
   *
   *
   * @param to
   * @param from
   * @param subject
   * @param text
   * @param contentType
   *
   * @throws MessagingException
   */
  private void sendMail(String to, String from, String subject, String text,
                        String contentType)
          throws MessagingException
  {
    Session session = createSession();
    MimeMessage msg = new MimeMessage(session);

    msg.setFrom(new InternetAddress(from));
    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
    msg.setSubject(subject);
    msg.setContent(text, contentType);
    Transport.send(msg);
  }
}
