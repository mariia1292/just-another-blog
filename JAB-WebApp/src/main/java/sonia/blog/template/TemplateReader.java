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



package sonia.blog.template;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sonia.blog.api.template.Style;
import sonia.blog.api.template.Template;

import sonia.util.Util;
import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class TemplateReader
{

  /** Field description */
  private static final String TEMPLATE_DESCRIPTOR = "jab.template.xml";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(TemplateReader.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param directory
   * @param path
   *
   * @return
   */
  public Template readTemplate(File directory, String path)
  {
    Template template = null;

    if (directory.exists() && directory.isDirectory())
    {
      File file = new File(directory, TEMPLATE_DESCRIPTOR);

      if (file.exists() && file.isFile())
      {
        template = readTemplateDescriptor(file);

        if (template != null)
        {
          template.setPath(path);
        }
      }
      else if (logger.isLoggable(Level.WARNING))
      {
        StringBuffer log = new StringBuffer();

        log.append("could not find a valid template descriptor at ");
        log.append(file.getPath());
        logger.warning(log.toString());
      }
    }
    else if (logger.isLoggable(Level.FINE))
    {
      StringBuffer log = new StringBuffer(directory.getPath());

      log.append(" is not an template directory");
      logger.finer(log.toString());
    }

    return template;
  }

  /**
   * Method description
   *
   *
   * @param style
   * @param node
   */
  private void parseAttributes(Style style, Node node)
  {
    NodeList children = node.getChildNodes();

    for (int i = 0; i < children.getLength(); i++)
    {
      Node child = children.item(i);
      String name = child.getNodeName();

      if ("attribute".equalsIgnoreCase(name))
      {
        String attrName = null;
        String attrValue = null;
        NodeList attrChildren = child.getChildNodes();

        for (int j = 0; j < attrChildren.getLength(); j++)
        {
          Node attrChild = attrChildren.item(j);

          if ("name".equalsIgnoreCase(attrChild.getNodeName()))
          {
            attrName = attrChild.getTextContent();
          }
          else if ("value".equalsIgnoreCase(attrChild.getNodeName()))
          {
            attrValue = attrChild.getTextContent();
          }
        }

        if (Util.isNotEmpty(attrName) && Util.isNotEmpty(attrValue))
        {
          style.addAttribute(attrName, attrValue);
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param style
   * @param node
   */
  private void parseClasses(Style style, Node node)
  {
    NodeList children = node.getChildNodes();

    for (int i = 0; i < children.getLength(); i++)
    {
      Node child = children.item(i);
      String name = child.getNodeName();

      if ("class".equalsIgnoreCase(name))
      {
        style.addClass(child.getTextContent());
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param style
   * @param node
   */
  private void parseSelectors(Style style, Node node)
  {
    NodeList children = node.getChildNodes();

    for (int i = 0; i < children.getLength(); i++)
    {
      Node child = children.item(i);
      String name = child.getNodeName();

      if ("selector".equalsIgnoreCase(name))
      {
        style.addSelector(child.getTextContent());
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param node
   *
   * @return
   */
  private Style parseStyle(Node node)
  {
    Style style = new Style();
    NodeList children = node.getChildNodes();

    for (int i = 0; i < children.getLength(); i++)
    {
      Node child = children.item(i);
      String name = child.getNodeName();

      if ("title".equalsIgnoreCase(name))
      {
        style.setTitle(child.getTextContent());
      }
      else if ("inline".equalsIgnoreCase(name))
      {
        style.setInline(child.getTextContent());
      }
      else if ("block".equalsIgnoreCase(name))
      {
        style.setBlock(child.getTextContent());
      }
      else if ("classes".equalsIgnoreCase(name))
      {
        parseClasses(style, child);
      }
      else if ("selectors".equalsIgnoreCase(name))
      {
        parseSelectors(style, child);
      }
      else if ("attributes".equalsIgnoreCase(name))
      {
        parseAttributes(style, child);
      }
    }

    return style;
  }

  /**
   * Method description
   *
   *
   * @param template
   * @param node
   */
  private void parseStyles(Template template, Node node)
  {
    NodeList children = node.getChildNodes();

    for (int i = 0; i < children.getLength(); i++)
    {
      Node child = children.item(i);
      String name = child.getNodeName();

      if ("style".equalsIgnoreCase(name))
      {
        template.addStyle(parseStyle(child));
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param file
   *
   * @return
   */
  private Template readTemplateDescriptor(File file)
  {
    Template template = null;

    try
    {
      Document doc = XmlUtil.buildDocument(file);

      template = new Template();

      NodeList children = doc.getDocumentElement().getChildNodes();

      for (int i = 0; i < children.getLength(); i++)
      {
        Node node = children.item(i);
        String name = node.getNodeName();

        if ("name".equalsIgnoreCase(name))
        {
          template.setName(node.getTextContent());
        }
        else if ("author".equalsIgnoreCase(name))
        {
          template.setAuthor(node.getTextContent());
        }
        else if ("url".equalsIgnoreCase(name))
        {
          template.setUrl(node.getTextContent());
        }
        else if ("email".equalsIgnoreCase(name))
        {
          template.setEmail(node.getTextContent());
        }
        else if ("version".equalsIgnoreCase(name))
        {
          template.setVersion(node.getTextContent());
        }
        else if ("description".equalsIgnoreCase(name))
        {
          template.setDescription(node.getTextContent());
        }
        else if ("content-css".equalsIgnoreCase(name))
        {
          template.setContentCSS(node.getTextContent());
        }
        else if ("styles".equalsIgnoreCase(name))
        {
          parseStyles(template, node);
        }
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }

    return template;
  }
}
