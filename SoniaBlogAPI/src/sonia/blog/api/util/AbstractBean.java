/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;

import sonia.jsf.util.MessageHandler;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
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

  /** Field description */
  private static Logger logger = Logger.getLogger(AbstractBean.class.getName());

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
    if (logger.isLoggable(Level.FINEST))
    {
      StringBuffer log = new StringBuffer();

      log.append("init, calling InjectionProvider for ");
      log.append(getClass().getName());
      logger.finest(log.toString());
    }

    BlogContext.getInstance().getInjectionProvider().inject(this);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   * @param clazz
   * @param name
   * @param <T>
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public <T> T getBean(Class<T> clazz, String name)
  {
    T result = null;
    String el = "#{" + name + "}";
    FacesContext context = FacesContext.getCurrentInstance();
    Object obj =
      context.getApplication().getELResolver().getValue(context.getELContext(),
        null, el);

    if (clazz.isInstance(obj))
    {
      result = (T) obj;
    }

    return result;
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
  protected Locale getLocale()
  {
    Locale l = null;
    FacesContext ctx = FacesContext.getCurrentInstance();
    UIViewRoot view = ctx.getViewRoot();

    if (view != null)
    {
      l = view.getLocale();
    }

    if (l == null)
    {
      l = ctx.getApplication().getDefaultLocale();
    }

    return l;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  protected MessageHandler getMessageHandler()
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
  protected BlogResponse getResponse()
  {
    BlogResponse blogResponse = null;
    Object response =
      FacesContext.getCurrentInstance().getExternalContext().getResponse();

    if (response instanceof BlogResponse)
    {
      blogResponse = (BlogResponse) response;
    }
    else if (response instanceof HttpServletResponse)
    {
      blogResponse = new BlogResponse((HttpServletResponse) response);
    }

    return blogResponse;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private MessageHandler messageHandler;
}
