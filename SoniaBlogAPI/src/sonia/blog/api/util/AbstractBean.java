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



package sonia.blog.api.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.BlogSession;

import sonia.jsf.util.FacesMessageHandler;

import sonia.web.msg.MessageHandler;

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
 * @author Sebastian Sdorra
 */
public class AbstractBean
{

  /** Field description */
  public static final String FAILURE = "failure";

  /** Field description */
  public static final String SUCCESS = "success";

  /** Field description */
  public static final int TYPE_BACKEND = 0;

  /** Field description */
  public static final int TYPE_FRONTEND = 1;

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
  protected BlogSession getBlogSession()
  {
    return (BlogSession) FacesContext.getCurrentInstance().getExternalContext()
      .getSessionMap().get(BlogSession.SESSIONVAR);
  }

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

      if (getType() == TYPE_BACKEND)
      {
        this.messageHandler = new FacesMessageHandler(bundle, true);
      }
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

  /**
   * Method description
   *
   *
   * @return
   */
  protected int getType()
  {
    return TYPE_BACKEND;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private FacesMessageHandler messageHandler;
}
