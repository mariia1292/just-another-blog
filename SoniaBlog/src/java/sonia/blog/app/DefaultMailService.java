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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Sebastian Sdorra
 */
public class DefaultMailService implements MailService
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultMailService.class.getName());

  //~--- methods --------------------------------------------------------------

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
    if (logger.isLoggable(Level.FINEST))
    {
      StringBuffer log = new StringBuffer();

      log.append("try to send message: \n");
      log.append("\tto: ").append(from).append("\n");
      log.append("\tfrom: ").append(from).append("\n");
      log.append("\tsubject: ").append(subject).append("\n");
      log.append("\tcontent: ").append(text).append("\n");
      logger.finest(log.toString());
    }

    Session session = createSession();
    MimeMessage msg = new MimeMessage(session);

    msg.setFrom(new InternetAddress(from));
    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
    msg.setSubject(subject);
    msg.setContent(text, contentType);
    Transport.send(msg);
  }
}
