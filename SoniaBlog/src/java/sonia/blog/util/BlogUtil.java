/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogRuntimeException;
import sonia.blog.api.app.Constants;
import sonia.blog.api.util.AbstractBean;

import sonia.config.XmlConfiguration;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.servlet.http.HttpSession;

/**
 *
 * @author sdorra
 */
public class BlogUtil
{

  /**
   * Method description
   *
   *
   * @param year
   * @param month
   * @param day
   *
   * @return
   */
  public static Date createEndDate(Integer year, Integer month, Integer day)
  {
    if (year == null)
    {
      throw new IllegalArgumentException("year is null");
    }

    GregorianCalendar cal = new GregorianCalendar();

    cal.set(Calendar.YEAR, year);

    if (month != null)
    {
      cal.set(Calendar.MONTH, month);
    }
    else
    {
      cal.set(Calendar.MONTH, Calendar.DECEMBER);
    }

    if (day != null)
    {
      cal.set(Calendar.DAY_OF_MONTH, day);
    }
    else
    {
      cal.set(Calendar.DAY_OF_MONTH,
              cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);

    return cal.getTime();
  }

  /**
   * Method description
   *
   *
   * @param month
   * @param year
   *
   * @return
   */
  public static Date createEndDate(Integer year, Integer month)
  {
    return createEndDate(year, month, null);
  }

  /**
   * Method description
   *
   *
   * @param year
   *
   * @return
   */
  public static Date createEndDate(Integer year)
  {
    return createEndDate(year, null, null);
  }

  /**
   * Method description
   *
   *
   * @param month
   * @param year
   *
   * @return
   */
  public static Date createStartDate(Integer year, Integer month)
  {
    return createStartDate(year, month, null);
  }

  /**
   * Method description
   *
   *
   * @param year
   *
   * @return
   */
  public static Date createStartDate(Integer year)
  {
    return createStartDate(year, null, null);
  }

  /**
   * Method description
   *
   *
   * @param year
   * @param month
   * @param day
   *
   * @return
   */
  public static Date createStartDate(Integer year, Integer month, Integer day)
  {
    if (year == null)
    {
      throw new IllegalArgumentException("year is null");
    }

    GregorianCalendar cal = new GregorianCalendar();

    cal.set(Calendar.YEAR, year);

    if (month != null)
    {
      cal.set(Calendar.MONTH, month);
    }
    else
    {
      cal.set(Calendar.MONTH, Calendar.JANUARY);
    }

    if (day != null)
    {
      cal.set(Calendar.DAY_OF_MONTH, day);
    }
    else
    {
      cal.set(Calendar.DAY_OF_MONTH, 1);
    }

    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 1);

    return cal.getTime();
  }

  /**
   * Method description
   *
   *
   * @param from
   * @param to
   * @param subject
   * @param text
   *
   * @throws MessagingException
   */
  public static void sendMail(String from, String to, String subject,
                              String text)
          throws MessagingException
  {
    XmlConfiguration config = BlogContext.getInstance().getConfiguration();
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

    if (BlogContext.getInstance().getConfiguration().getBoolean(
            Constants.CONFIG_SMTPSTARTTLS, Boolean.FALSE))
    {
      props.put("mail.smtp.starttls.enable", Boolean.TRUE);
    }

    Authenticator auth = null;
    String user = config.getString(Constants.CONFIG_SMTPUSER);

    if (!Util.isBlank(user))
    {
      props.put("mail.smtp.user", user);

      String password = config.getString(Constants.CONFIG_SMTPPASSWORD);

      if (!Util.isBlank(password))
      {
        props.put("mail.smtp.auth", "true");
        auth = new SmtpAuthenticator(user, password);
      }
    }

    Session session = Session.getInstance(props, auth);

    session.setDebug(true);

    MimeMessage msg = new MimeMessage(session);

    msg.setFrom(new InternetAddress(from));
    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
    msg.setSubject(subject);
    msg.setText(text);
    Transport.send(msg);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param type
   * @param name
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T>T getSessionBean(BlogRequest request, Class<T> type,
                                    String name)
  {
    T result = null;
    HttpSession session = request.getSession(true);
    Object obj = session.getAttribute(name);

    if (obj != null)
    {
      if (!type.isInstance(obj))
      {
        throw new BlogRuntimeException("session object " + name
                                       + " is not an instance of "
                                       + type.getName());
      }
      else
      {
        result = (T) obj;
      }
    }
    else
    {
      try
      {
        result = type.newInstance();

        if (result instanceof AbstractBean)
        {
          ((AbstractBean) result).init();
        }

        session.setAttribute(name, result);
      }
      catch (Exception ex)
      {
        throw new BlogRuntimeException("could not create new SessionBean", ex);
      }
    }

    return result;
  }
}
