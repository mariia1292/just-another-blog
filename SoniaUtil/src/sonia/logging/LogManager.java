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


package sonia.logging;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sonia.util.Util;
import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Sebastian Sdorra
 */
public class LogManager
{

  /** Field description */
  private static LogManager instance;

  /** Field description */
  private static Logger logger = Logger.getLogger(LogManager.class.getName());

  /** Field description */
  private static Pattern variablePattern =
    Pattern.compile("\\$\\{([a-zA-Z0-9-]+)\\}");

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  private LogManager()
  {
    this.vars = new HashMap<String, String>();
    this.handlers = new HashMap<String, Handler>();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static LogManager getInstance()
  {
    if (instance == null)
    {
      instance = new LogManager();
    }

    return instance;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   *
   * @return
   */
  public String putVar(String key, String value)
  {
    return vars.put(key, value);
  }

  /**
   * Method description
   *
   *
   * @param logFile
   *
   * @throws IOException
   */
  public void readConfiguration(File logFile) throws IOException
  {
    FileInputStream fis = null;

    try
    {
      fis = new FileInputStream(logFile);
      readConfiguration(fis);
    }
    finally
    {
      if (fis != null)
      {
        fis.close();
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param in
   *
   * @throws IOException
   */
  public void readConfiguration(InputStream in) throws IOException
  {
    try
    {
      Document doc = XmlUtil.buildDocument(in);
      NodeList handlerList = doc.getElementsByTagName("handlers");

      if (XmlUtil.hasContent(handlerList))
      {
        for (int i = 0; i < handlerList.getLength(); i++)
        {
          Node node = handlerList.item(i);

          if (node.getNodeName().equals("handlers"))
          {
            parseHandlers(node);
          }
        }
      }

      NodeList loggerList = doc.getElementsByTagName("loggers");

      if (XmlUtil.hasContent(loggerList))
      {
        for (int i = 0; i < loggerList.getLength(); i++)
        {
          Node node = loggerList.item(i);

          if (node.getNodeName().equals("loggers"))
          {
            parseLoggers(node);
          }
        }
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);

      throw new IOException(ex.getMessage());
    }
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public String removeVar(Object key)
  {
    return vars.remove(key);
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param handlerClass
   * @param formatterClass
   * @param level
   * @param parameters
   *
   * @throws Exception
   */
  private void createHandler(String name,
                             Class<? extends Handler> handlerClass,
                             Class<? extends Formatter> formatterClass,
                             Level level, Map<String, String> parameters)
          throws Exception
  {
    Handler handler = null;

    if ((parameters == null) || parameters.isEmpty())
    {
      handler = handlerClass.newInstance();
    }
    else if (handlerClass.equals(FileHandler.class))
    {
      int limit = 50000;
      String limitString = parameters.get("limit");

      if (Util.hasContent(limitString))
      {
        try
        {
          limit = Integer.valueOf(limitString);
        }
        catch (NumberFormatException ex) {}
      }

      int count = 1;
      String countString = parameters.get("count");

      if (Util.hasContent(countString))
      {
        try
        {
          count = Integer.parseInt(countString);
        }
        catch (NumberFormatException ex) {}
      }

      boolean append = false;
      String appendString = parameters.get("append");

      if (Util.hasContent(appendString))
      {
        append = Boolean.parseBoolean(appendString);
      }

      String pattern = parameters.get("pattern");

      if (Util.isBlank(pattern))
      {
        pattern = "%h/java%u.log";
      }

      /** TODO: check */
      handler = new FileHandler(pattern, limit, count, append);
    }
    else if (ConfigureableHandler.class.isAssignableFrom(handlerClass))
    {
      ConfigureableHandler ch =
        (ConfigureableHandler) handlerClass.newInstance();

      ch.init(parameters);
      handler = ch;
    }
    else
    {
      handler = handlerClass.newInstance();
    }

    if (formatterClass != null)
    {
      handler.setFormatter(formatterClass.newInstance());
    }

    if (level != null)
    {
      handler.setLevel(level);
    }

    handlers.put(name, handler);
  }

  /**
   * Method description
   *
   *
   * @param handlerNode
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private void parseHandler(Node handlerNode) throws Exception
  {
    NodeList children = handlerNode.getChildNodes();

    if (XmlUtil.hasContent(children))
    {
      String name = null;
      Class<? extends Handler> handlerClass = null;
      Class<? extends Formatter> formatterClass = null;
      Level level = null;
      Map<String, String> parameters = null;

      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);
        String childName = child.getNodeName();

        if (childName.equals("name"))
        {
          name = getNodeValue(child);
        }
        else if (childName.equals("class"))
        {
          handlerClass =
            (Class<? extends Handler>) Class.forName(getNodeValue(child));
        }
        else if (childName.equals("formatter"))
        {
          formatterClass =
            (Class<? extends Formatter>) Class.forName(getNodeValue(child));
        }
        else if (childName.equals("level"))
        {
          level = Level.parse(getNodeValue(child));
        }
        else if (childName.equals("parameter"))
        {
          if (parameters == null)
          {
            parameters = new HashMap<String, String>();
          }

          parseParameter(parameters, child);
        }
      }

      if (Util.hasContent(name) && (handlerClass != null))
      {
        createHandler(name, handlerClass, formatterClass, level, parameters);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param node
   *
   * @throws Exception
   */
  private void parseHandlers(Node node) throws Exception
  {
    NodeList children = node.getChildNodes();

    if (XmlUtil.hasContent(children))
    {
      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);

        if (child.getNodeName().equals("handler"))
        {
          parseHandler(child);
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param node
   */
  private void parseLogger(Node node)
  {
    NodeList children = node.getChildNodes();

    if (XmlUtil.hasContent(children))
    {
      String name = null;
      Level level = null;
      boolean useParentHandlers = true;
      boolean removeOldHandlers = false;
      List<Handler> handlerList = null;

      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);
        String childName = child.getNodeName();

        if (childName.equals("name"))
        {
          name = getNodeValue(child);
        }
        else if (childName.equals("level"))
        {
          level = Level.parse(getNodeValue(child));
        }
        else if (childName.equals("handler"))
        {
          Handler handler = handlers.get(getNodeValue(child));

          if (handler != null)
          {
            if (handlerList == null)
            {
              handlerList = new ArrayList<Handler>();
            }

            handlerList.add(handler);
          }
        }
        else if (childName.equals("removeOldHandlers"))
        {
          removeOldHandlers = true;
        }
        else if (childName.equals("useParentHandlers"))
        {
          useParentHandlers = Boolean.parseBoolean(getNodeValue(node));
        }
      }

      if (Util.hasContent(name))
      {
        Logger l = Logger.getLogger(name);

        if (removeOldHandlers)
        {
          Handler[] oldHandlers = l.getHandlers();

          if (oldHandlers != null)
          {
            for (Handler h : oldHandlers)
            {
              l.removeHandler(h);
            }
          }
        }

        if (level != null)
        {
          l.setLevel(level);
        }

        l.setUseParentHandlers(useParentHandlers);

        if (Util.hasContent(handlerList))
        {
          for (Handler handler : handlerList)
          {
            l.addHandler(handler);
          }
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param node
   */
  private void parseLoggers(Node node)
  {
    NodeList children = node.getChildNodes();

    if (XmlUtil.hasContent(children))
    {
      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);

        if (child.getNodeName().equals("logger"))
        {
          parseLogger(child);
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param parameters
   * @param node
   */
  private void parseParameter(Map<String, String> parameters, Node node)
  {
    String name = null;
    String value = null;
    NodeList children = node.getChildNodes();

    if (XmlUtil.hasContent(children))
    {
      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);
        String childName = child.getNodeName();
        String childValue = getNodeValue(child);

        if (childName.equals("name"))
        {
          name = childValue;
        }
        else if (childName.equals("value"))
        {
          value = childValue;
        }
      }

      if (Util.hasContent(name) && Util.hasContent(value))
      {
        parameters.put(name, value);
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param node
   *
   * @return
   */
  private String getNodeValue(Node node)
  {
    String value = node.getTextContent();

    if (Util.hasContent(value))
    {
      Matcher m = variablePattern.matcher(value);

      while (m.find())
      {
        String replaceContent = vars.get(m.group(1));

        if (replaceContent == null)
        {
          replaceContent = "";
        }

        value = m.replaceFirst(replaceContent);
        m = variablePattern.matcher(value);
      }
    }

    return value;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Map<String, Handler> handlers;

  /** Field description */
  private Map<String, String> vars;
}