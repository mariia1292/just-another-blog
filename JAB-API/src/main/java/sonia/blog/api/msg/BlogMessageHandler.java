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



package sonia.blog.api.msg;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

import sonia.web.msg.MessageHandler;

//~--- JDK imports ------------------------------------------------------------

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Sebastian Sdorra
 */
public class BlogMessageHandler extends MessageHandler
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(BlogMessageHandler.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param bundle
   */
  public BlogMessageHandler(ResourceBundle bundle)
  {
    super(bundle);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param key
   */
  @Override
  public void error(HttpServletRequest request, String key)
  {
    sendMessage(request, BlogMessage.LEVEL_ERROR, null, key, null, null);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param clientId
   * @param key
   */
  @Override
  public void error(HttpServletRequest request, String clientId, String key)
  {
    sendMessage(request, BlogMessage.LEVEL_ERROR, clientId, key, null, null);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param clientId
   * @param key
   * @param detailKey
   */
  @Override
  public void error(HttpServletRequest request, String clientId, String key,
                    String detailKey)
  {
    sendMessage(request, BlogMessage.LEVEL_ERROR, clientId, key, detailKey,
                null);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param clientId
   * @param key
   * @param detailKey
   * @param params
   */
  @Override
  public void error(HttpServletRequest request, String clientId, String key,
                    String detailKey, Object... params)
  {
    sendMessage(request, BlogMessage.LEVEL_ERROR, clientId, key, detailKey,
                params);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param clientId
   * @param key
   * @param detailKey
   * @param params
   */
  @Override
  public void fatal(HttpServletRequest request, String clientId, String key,
                    String detailKey, Object... params)
  {
    sendMessage(request, BlogMessage.LEVEL_FATAL, clientId, key, detailKey,
                params);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param key
   */
  @Override
  public void fatal(HttpServletRequest request, String key)
  {
    sendMessage(request, BlogMessage.LEVEL_FATAL, null, key, null, null);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param clientId
   * @param key
   */
  @Override
  public void fatal(HttpServletRequest request, String clientId, String key)
  {
    sendMessage(request, BlogMessage.LEVEL_FATAL, clientId, key, null, null);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param clientId
   * @param key
   * @param detailKey
   */
  @Override
  public void fatal(HttpServletRequest request, String clientId, String key,
                    String detailKey)
  {
    sendMessage(request, BlogMessage.LEVEL_FATAL, clientId, key, detailKey,
                null);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param clientId
   * @param key
   * @param detailKey
   * @param params
   */
  @Override
  public void info(HttpServletRequest request, String clientId, String key,
                   String detailKey, Object... params)
  {
    sendMessage(request, BlogMessage.LEVEL_INFO, clientId, key, detailKey,
                params);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param key
   */
  @Override
  public void info(HttpServletRequest request, String key)
  {
    sendMessage(request, BlogMessage.LEVEL_INFO, null, key, null, null);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param clientId
   * @param key
   */
  @Override
  public void info(HttpServletRequest request, String clientId, String key)
  {
    sendMessage(request, BlogMessage.LEVEL_INFO, clientId, key, null, null);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param clientId
   * @param key
   * @param detailKey
   */
  @Override
  public void info(HttpServletRequest request, String clientId, String key,
                   String detailKey)
  {
    sendMessage(request, BlogMessage.LEVEL_INFO, clientId, key, detailKey,
                null);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param clientId
   * @param key
   * @param detailKey
   * @param params
   */
  @Override
  public void warn(HttpServletRequest request, String clientId, String key,
                   String detailKey, Object... params)
  {
    sendMessage(request, BlogMessage.LEVEL_WARN, clientId, key, detailKey,
                params);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param key
   */
  @Override
  public void warn(HttpServletRequest request, String key)
  {
    sendMessage(request, BlogMessage.LEVEL_WARN, null, key, null, null);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param clientId
   * @param key
   */
  @Override
  public void warn(HttpServletRequest request, String clientId, String key)
  {
    sendMessage(request, BlogMessage.LEVEL_WARN, clientId, key, null, null);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param clientId
   * @param key
   * @param detailKey
   */
  @Override
  public void warn(HttpServletRequest request, String clientId, String key,
                   String detailKey)
  {
    sendMessage(request, BlogMessage.LEVEL_WARN, clientId, key, detailKey,
                null);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param level
   * @param clientId
   * @param summary
   * @param detail
   * @param params
   */
  @SuppressWarnings("unchecked")
  private void sendMessage(HttpServletRequest request, int level,
                           String clientId, String summary, String detail,
                           Object[] params)
  {
    summary = getValue(summary, params);

    if (Util.hasContent(detail))
    {
      detail = getValue(detail, params);
    }

    BlogMessage msg = new BlogMessage(level, clientId, summary, detail);
    List<BlogMessage> messages = (List<BlogMessage>) request.getSession(
                                     true).getAttribute(
                                     BlogMessage.SESSION_VAR);

    if (messages == null)
    {
      messages = new ArrayList<BlogMessage>();
      request.getSession().setAttribute(BlogMessage.SESSION_VAR, messages);
    }

    messages.add(msg);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   * @param param
   *
   * @return
   */
  private String getValue(String key, Object[] param)
  {
    String value = key;

    try
    {
      value = bundle.getString(key);
    }
    catch (MissingResourceException ex)
    {
      if (logger.isLoggable(Level.WARNING))
      {
        StringBuffer msg = new StringBuffer();

        msg.append("no value found for key ").append(key);
        logger.warning(msg.toString());
      }
    }

    if (param != null)
    {
      value = MessageFormat.format(value, param);
    }

    return value;
  }
}
