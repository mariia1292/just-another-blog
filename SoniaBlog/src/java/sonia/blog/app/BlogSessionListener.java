/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 * @author sdorra
 */
public class BlogSessionListener implements HttpSessionListener
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(BlogSessionListener.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param se
   */
  public void sessionCreated(HttpSessionEvent se)
  {
    if (logger.isLoggable(Level.FINE))
    {
      StringBuffer msg = new StringBuffer();

      msg.append("create new session ").append(se.getSession().getId());
      logger.info(msg.toString());
    }

    BlogContext.getInstance().getSessionInformation().addSession();
  }

  /**
   * Method description
   *
   *
   * @param se
   */
  public void sessionDestroyed(HttpSessionEvent se)
  {
    if (logger.isLoggable(Level.FINE))
    {
      StringBuffer msg = new StringBuffer();

      msg.append("destroy session ").append(se.getSession().getId());
      logger.info(msg.toString());
    }

    BlogContext.getInstance().getSessionInformation().removeSession();
  }
}
