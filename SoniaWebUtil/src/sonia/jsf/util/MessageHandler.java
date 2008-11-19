/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.util;

//~--- JDK imports ------------------------------------------------------------

import java.text.MessageFormat;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class MessageHandler
{

  /**
   * Constructs ...
   *
   *
   * @param bundle
   */
  public MessageHandler(ResourceBundle bundle)
  {
    this.bundle = bundle;
  }

  /**
   * Constructs ...
   *
   *
   * @param bundle
   * @param enableLogging
   */
  public MessageHandler(ResourceBundle bundle, boolean enableLogging)
  {
    this.bundle = bundle;

    if (enableLogging)
    {
      logger = Logger.getLogger(MessageHandler.class.getName());
    }
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   */
  public void error(String key)
  {
    send(null, FacesMessage.SEVERITY_ERROR, key, null);
  }

  /**
   * Method description
   *
   *
   * @param clientId
   * @param key
   */
  public void error(String clientId, String key)
  {
    send(clientId, FacesMessage.SEVERITY_ERROR, key, null);
  }

  /**
   * Method description
   *
   *
   * @param clientId
   * @param key
   * @param detailKey
   */
  public void error(String clientId, String key, String detailKey)
  {
    send(clientId, FacesMessage.SEVERITY_ERROR, key, detailKey);
  }

  /**
   * Method description
   *
   *
   * @param clientId
   * @param key
   * @param detailKey
   * @param params
   */
  public void error(String clientId, String key, String detailKey,
                    Object... params)
  {
    send(clientId, FacesMessage.SEVERITY_ERROR, key, detailKey, params);
  }

  /**
   * Method description
   *
   *
   * @param clientId
   * @param key
   * @param detailKey
   * @param params
   */
  public void fatal(String clientId, String key, String detailKey,
                    Object... params)
  {
    send(clientId, FacesMessage.SEVERITY_FATAL, key, detailKey, params);
  }

  /**
   * Method description
   *
   *
   * @param key
   */
  public void fatal(String key)
  {
    send(null, FacesMessage.SEVERITY_FATAL, key, null);
  }

  /**
   * Method description
   *
   *
   * @param clientId
   * @param key
   */
  public void fatal(String clientId, String key)
  {
    send(clientId, FacesMessage.SEVERITY_FATAL, key, null);
  }

  /**
   * Method description
   *
   *
   * @param clientId
   * @param key
   * @param detailKey
   */
  public void fatal(String clientId, String key, String detailKey)
  {
    send(clientId, FacesMessage.SEVERITY_FATAL, key, detailKey);
  }

  /**
   * Method description
   *
   *
   * @param clientId
   * @param key
   * @param detailKey
   * @param params
   */
  public void info(String clientId, String key, String detailKey,
                   Object... params)
  {
    send(clientId, FacesMessage.SEVERITY_INFO, key, detailKey, params);
  }

  /**
   * Method description
   *
   *
   * @param key
   */
  public void info(String key)
  {
    send(null, FacesMessage.SEVERITY_INFO, key, null);
  }

  /**
   * Method description
   *
   *
   * @param clientId
   * @param key
   */
  public void info(String clientId, String key)
  {
    send(clientId, FacesMessage.SEVERITY_INFO, key, null);
  }

  /**
   * Method description
   *
   *
   * @param clientId
   * @param key
   * @param detailKey
   */
  public void info(String clientId, String key, String detailKey)
  {
    send(clientId, FacesMessage.SEVERITY_INFO, key, detailKey);
  }

  /**
   * Method description
   *
   *
   * @param clientId
   * @param key
   * @param detailKey
   * @param params
   */
  public void warn(String clientId, String key, String detailKey,
                   Object... params)
  {
    send(clientId, FacesMessage.SEVERITY_WARN, key, detailKey, params);
  }

  /**
   * Method description
   *
   *
   * @param key
   */
  public void warn(String key)
  {
    send(null, FacesMessage.SEVERITY_WARN, key, null);
  }

  /**
   * Method description
   *
   *
   * @param clientId
   * @param key
   */
  public void warn(String clientId, String key)
  {
    send(clientId, FacesMessage.SEVERITY_WARN, key, null);
  }

  /**
   * Method description
   *
   *
   * @param clientId
   * @param key
   * @param detailKey
   */
  public void warn(String clientId, String key, String detailKey)
  {
    send(clientId, FacesMessage.SEVERITY_WARN, key, detailKey);
  }

  /**
   * Method description
   *
   *
   * @param clientId
   * @param severity
   * @param summaryKey
   * @param detailKey
   * @param params
   */
  private void send(String clientId, FacesMessage.Severity severity,
                    String summaryKey, String detailKey, Object... params)
  {
    String summary = null;

    if ((summaryKey != null) && (summaryKey.trim().length() > 0))
    {
      if ((params != null) && (params.length > 0))
      {
        summary = MessageFormat.format(bundle.getString(summaryKey), params);
      }
      else
      {
        summary = bundle.getString(summaryKey);
      }
    }

    String detail = null;

    if ((detailKey != null) && (detailKey.trim().length() > 0))
    {
      detail = bundle.getString(detailKey);
    }

    if (logger != null)
    {
      Level level = getLogLevel(severity);
      String message = summary;

      if (detail != null)
      {
        message += "(" + detail + ")";
      }

      logger.log(level, message);
    }

    FacesContext.getCurrentInstance().addMessage(clientId,
            new FacesMessage(severity, summary, detail));
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param severity
   *
   * @return
   */
  private Level getLogLevel(FacesMessage.Severity severity)
  {
    Level level = null;
    int sevOrdinal = severity.getOrdinal();

    if (sevOrdinal == FacesMessage.SEVERITY_INFO.getOrdinal())
    {
      level = Level.INFO;
    }
    else if (sevOrdinal == FacesMessage.SEVERITY_WARN.getOrdinal())
    {
      level = Level.WARNING;
    }
    else if ((sevOrdinal == FacesMessage.SEVERITY_ERROR.getOrdinal())
             || (sevOrdinal == FacesMessage.SEVERITY_FATAL.getOrdinal()))
    {
      level = Level.SEVERE;
    }

    return level;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ResourceBundle bundle;

  /** Field description */
  private Logger logger;
}
