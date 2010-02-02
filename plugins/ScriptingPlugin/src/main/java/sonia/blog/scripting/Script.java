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



package sonia.blog.scripting;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import sonia.blog.api.app.BlogSession;
import sonia.blog.api.exception.BlogException;
import sonia.blog.api.exception.BlogSecurityException;
import sonia.blog.entity.Role;

import sonia.util.Util;
import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author Sebastian Sdorra
 */
public class Script
{

  /** Field description */
  public static final String FILE_CONTROLLER = "controller";

  /** Field description */
  public static final String FILE_SCRIPT = "script.xml";

  /** Field description */
  public static final String FILE_TEMPLATE = "template";

  /** Field description */
  private static Logger logger = Logger.getLogger(Script.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public Script() {}

  /**
   * Constructs ...
   *
   *
   *
   * @param session
   * @param directory
   *
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  Script(BlogSession session, File directory)
          throws IOException, SAXException, ParserConfigurationException
  {
    if (!directory.exists() || directory.isFile())
    {
      throw new IllegalArgumentException("file must be a directory");
    }

    if (!session.hasRole(Role.GLOBALADMIN))
    {
      throw new BlogSecurityException("user is not an GlobalAdmin");
    }

    File file = new File(directory, "script.xml");
    Document document = XmlUtil.buildDocument(file);
    NodeList nodeList = document.getDocumentElement().getChildNodes();

    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Node node = nodeList.item(i);
      String nodeName = node.getNodeName();

      if ("name".equals(nodeName))
      {
        name = node.getTextContent();
      }
      else if ("description".equals(nodeName))
      {
        description = node.getTextContent();
      }
      else if ("creation-date".equals(nodeName))
      {
        creationDate = parseDate(node.getTextContent());
      }
      else if ("last-modified".equals(nodeName))
      {
        lastModified = parseDate(node.getTextContent());
      }
      else if ("controller".equals(nodeName))
      {
        controllerContent = parseScriptContentNode(node, directory,
                FILE_CONTROLLER);
      }
      else if ("template".equals(nodeName))
      {
        templateContent = parseScriptContentNode(node, directory,
                FILE_TEMPLATE);
      }
      else
      {
        parseNode(node);
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public ScriptContent getControllerContent()
  {
    if (controllerContent == null)
    {
      controllerContent = new ScriptContent();
    }

    return controllerContent;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Date getCreationDate()
  {
    return creationDate;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Date getLastModified()
  {
    return lastModified;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return name;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ScriptContent getTemplateContent()
  {
    if (templateContent == null)
    {
      templateContent = new ScriptContent();
    }

    return templateContent;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isValid()
  {
    return Util.hasContent(name);
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param controllerContent
   */
  public void setControllerContent(ScriptContent controllerContent)
  {
    this.controllerContent = controllerContent;
  }

  /**
   * Method description
   *
   *
   * @param description
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Method description
   *
   *
   * @param name
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Method description
   *
   *
   * @param templateContent
   */
  public void setTemplateContent(ScriptContent templateContent)
  {
    this.templateContent = templateContent;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param session
   * @param directory
   *
   *
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws TransformerException
   */
  void store(BlogSession session, File directory)
          throws IOException, ParserConfigurationException, TransformerException
  {
    if (!session.hasRole(Role.GLOBALADMIN))
    {
      throw new BlogSecurityException("user is not an GlobalAdmin");
    }

    if (!directory.exists())
    {
      if (!directory.mkdirs())
      {
        throw new BlogException("could not create directory "
                                + directory.getPath());
      }
    }

    Document document = XmlUtil.createDocument();
    Element rootEl = document.createElement("script");

    document.appendChild(rootEl);

    Element nameEl = document.createElement("name");

    nameEl.setTextContent(name);
    rootEl.appendChild(nameEl);

    if (Util.isNotEmpty(description))
    {
      Element descriptionEl = document.createElement("description");

      descriptionEl.setTextContent(description);
      rootEl.appendChild(descriptionEl);
    }

    if (creationDate == null)
    {
      creationDate = new Date();
    }
    else
    {
      lastModified = new Date();
    }

    Element creationDateEl = document.createElement("creation-date");

    creationDateEl.setTextContent(formatDate(creationDate));
    rootEl.appendChild(creationDateEl);

    if (lastModified != null)
    {
      Element lastModifiedEl = document.createElement("last-modified");

      lastModifiedEl.setTextContent(formatDate(lastModified));
      rootEl.appendChild(lastModifiedEl);
    }

    if (controllerContent != null)
    {
      Element controllerEl = document.createElement("controller");

      rootEl.appendChild(controllerEl);
      storeScriptContent(document, controllerEl, controllerContent, directory,
                         FILE_CONTROLLER);
    }

    if (templateContent != null)
    {
      Element templateEl = document.createElement("template");

      rootEl.appendChild(templateEl);
      storeScriptContent(document, templateEl, templateContent, directory,
                         FILE_TEMPLATE);
    }

    storeScript(directory, document, rootEl);

    File file = new File(directory, FILE_SCRIPT);

    if (file.exists() &&!file.delete())
    {
      throw new BlogException("could not delete file " + file.getPath());
    }

    XmlUtil.writeDocument(document, file);
  }

  /**
   * Method description
   *
   *
   * @param value
   *
   * @return
   */
  protected String formatDate(Date value)
  {
    String result = null;

    try
    {
      result = getDateFormat().format(value);
    }
    catch (Exception ex)
    {
      logger.log(Level.WARNING, null, ex);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param value
   *
   * @return
   */
  protected Date parseDate(String value)
  {
    Date result = null;

    try
    {
      result = getDateFormat().parse(value);
    }
    catch (Exception ex)
    {
      logger.log(Level.WARNING, null, ex);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param node
   */
  protected void parseNode(Node node) {}

  /**
   * Method description
   *
   *
   * @param directory
   * @param document
   * @param node
   */
  protected void storeScript(File directory, Document document, Element node) {}

  /**
   * Method description
   *
   *
   * @param node
   * @param directory
   * @param filename
   *
   * @return
   *
   * @throws IOException
   */
  private ScriptContent parseScriptContentNode(Node node, File directory,
          String filename)
          throws IOException
  {
    String language = null;
    String content = null;
    NodeList childList = node.getChildNodes();

    for (int i = 0; i < childList.getLength(); i++)
    {
      Node childNode = childList.item(i);
      String childName = childNode.getNodeName();

      if ("language".equals(childName))
      {
        language = childNode.getTextContent();
      }
    }

    StringBuffer nameBuffer = new StringBuffer(filename);

    nameBuffer.append(".jsc");

    File scriptFile = new File(directory, nameBuffer.toString());

    if (scriptFile.exists() && scriptFile.isFile())
    {
      FileInputStream fis = null;

      try
      {
        fis = new FileInputStream(scriptFile);

        byte[] data = new byte[fis.available()];

        fis.read(data);
        content = new String(data);
      }
      finally
      {
        if (fis != null)
        {
          fis.close();
        }
      }
    }

    return new ScriptContent(language, content);
  }

  /**
   * Method description
   *
   *
   * @param document
   * @param parent
   * @param content
   * @param directory
   * @param filename
   *
   * @throws IOException
   */
  private void storeScriptContent(Document document, Element parent,
                                  ScriptContent content, File directory,
                                  String filename)
          throws IOException
  {
    String language = content.getLanguage();

    if (Util.isEmpty(language))
    {
      throw new IllegalArgumentException("language is empty");
    }

    Element languageEl = document.createElement("language");

    languageEl.setTextContent(language);
    parent.appendChild(languageEl);

    StringBuffer nameBuffer = new StringBuffer(filename);

    nameBuffer.append(".jsc");

    File file = new File(directory, nameBuffer.toString());

    if (file.exists() &&!file.delete())
    {
      throw new BlogException("could not delete file " + file.getPath());
    }

    String scriptContent = content.getContent();

    if (Util.isNotEmpty(scriptContent))
    {
      FileOutputStream fos = null;

      try
      {
        fos = new FileOutputStream(file);
        fos.write(scriptContent.getBytes());
      }
      finally
      {
        if (fos != null)
        {
          fos.close();
        }
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private DateFormat getDateFormat()
  {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm");
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ScriptContent controllerContent;

  /** Field description */
  private Date creationDate;

  /** Field description */
  private String description;

  /** Field description */
  private Date lastModified;

  /** Field description */
  private String name;

  /** Field description */
  private ScriptContent templateContent;
}
