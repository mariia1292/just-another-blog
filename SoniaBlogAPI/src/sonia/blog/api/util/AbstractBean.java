/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;

import sonia.jsf.util.MessageHandler;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class AbstractBean
{

  /** Field description */
  public static final String FAILURE = "failure";

  /** Field description */
  public static final String SUCCESS = "success";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public AbstractBean()
  {
    init();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  public void init()
  {
    this.logger = Logger.getLogger(getClass().getName());
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public MessageHandler getMessageHandler()
  {
    if (messageHandler == null)
    {
      FacesContext context = FacesContext.getCurrentInstance();
      ResourceBundle bundle =
        context.getApplication().getResourceBundle(context, "message");

      this.messageHandler = new MessageHandler(bundle, true);
    }

    return messageHandler;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param uri
   */
  protected void sendRedirect(String uri)
  {
    try
    {
      if (!uri.contains("://"))
      {
        String contextPath =
          BlogContext.getInstance().getServletContext().getContextPath();

        uri = contextPath + uri;
      }

      FacesContext context = FacesContext.getCurrentInstance();
      Iterator<FacesMessage> messages = context.getMessages();

      if ((messages != null) && messages.hasNext())
      {
        context.getExternalContext().getSessionMap().put("sonia.blog.messages",
                messages);
      }

      context.getExternalContext().redirect(uri);
      context.responseComplete();
    }
    catch (IOException ex)
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
  protected BlogRequest getRequest()
  {
    BlogRequest blogRequest = null;
    Object request =
      FacesContext.getCurrentInstance().getExternalContext().getRequest();

    if (request instanceof BlogRequest)
    {
      blogRequest = (BlogRequest) request;
    }
    else if (request instanceof HttpServletRequest)
    {
      blogRequest = new BlogRequest((HttpServletRequest) request);
    }

    return blogRequest;
  }

  /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   */
  protected ResourceBundle getResourceBundle(String name)
  {
    FacesContext context = FacesContext.getCurrentInstance();

    return context.getApplication().getResourceBundle(context, name);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  protected HttpServletResponse getResponse()
  {
    return (HttpServletResponse) FacesContext.getCurrentInstance()
      .getExternalContext().getResponse();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected Logger logger;

  /** Field description */
  private MessageHandler messageHandler;
}
