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

import sonia.macro.browse.MacroInformation;
import sonia.macro.browse.MacroInformationParameter;
import sonia.macro.browse.MacroWidget;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author Sebastian Sdorra
 */
public class MacroScript extends Script
{

  /** Field description */
  private static Logger logger = Logger.getLogger(MacroScript.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public MacroScript()
  {
    super();
  }

  /**
   * Constructs ...
   *
   *
   *
   * @param session
   * @param file
   *
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  MacroScript(BlogSession session, File file)
          throws IOException, SAXException, ParserConfigurationException
  {
    super(session, file);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public MacroInformation createMacroInformation()
  {
    information = new MacroInformation(getName(), null, getDescription());

    return information;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public MacroInformation getInformation()
  {
    return information;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param node
   */
  @Override
  protected void parseNode(Node node)
  {
    if ("macro-information".equals(node.getNodeName()))
    {
      parseMacroInformationNode(node);
    }
  }

  /**
   * Method description
   *
   *
   * @param directory
   * @param document
   * @param rootEl
   */
  @Override
  protected void storeScript(File directory, Document document, Element rootEl)
  {
    if (information != null)
    {
      storeMacroInformation(document, rootEl);
    }
  }

  /**
   * Method description
   *
   *
   * @param node
   */
  private void parseMacroInformationNode(Node node)
  {
    information = new MacroInformation(getName(), null, getDescription());

    List<MacroInformationParameter> parameters =
      new ArrayList<MacroInformationParameter>();
    NodeList childList = node.getChildNodes();

    for (int i = 0; i < childList.getLength(); i++)
    {
      Node childNode = childList.item(i);
      String childName = childNode.getNodeName();

      if ("display-name".equals(childName))
      {
        information.setDisplayName(childNode.getTextContent());
      }
      else if ("icon".equals(childName))
      {
        information.setIcon(childNode.getTextContent());
      }
      else if ("preview".equals(childName))
      {
        information.setPreview(
            Boolean.parseBoolean(childNode.getTextContent()));
      }
      else if ("body-widget".equals(childName))
      {
        try
        {
          information.setBodyWidget(
              (Class<? extends MacroWidget>) Class.forName(
                childNode.getTextContent()));
        }
        catch (Exception ex)
        {
          logger.log(Level.WARNING, null, ex);
        }
      }
      else if ("widget-param".equals(childName))
      {
        information.setWidgetParam(childNode.getTextContent());
      }
      else if ("parameter".equals(childName))
      {
        MacroInformationParameter parameter = parseParameterNode(childNode);

        if (parameter != null)
        {
          parameters.add(parameter);
        }
      }
    }

    information.setParameter(parameters);
  }

  /**
   * Method description
   *
   *
   * @param node
   *
   * @return
   */
  private MacroInformationParameter parseParameterNode(Node node)
  {
    MacroInformationParameter parameter = null;
    String name = null;
    String label = null;
    String description = null;
    Class<? extends MacroWidget> widget = null;
    String widgetParam = null;
    NodeList childList = node.getChildNodes();

    for (int i = 0; i < childList.getLength(); i++)
    {
      Node childNode = childList.item(i);
      String childName = childNode.getNodeName();
      String childValue = childNode.getTextContent();

      if ("name".equals(childName))
      {
        name = childValue;
      }
      else if ("label".equals(childName))
      {
        label = childValue;
      }
      else if ("description".equals(childName))
      {
        description = childValue;
      }
      else if ("widget".equals(childName))
      {
        try
        {
          widget = (Class<? extends MacroWidget>) Class.forName(childValue);
        }
        catch (Exception ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
      else if ("widget-param".equals(childName))
      {
        widgetParam = childValue;
      }
    }

    if (name != null)
    {
      parameter = new MacroInformationParameter(name, label, description);
      parameter.setWidget(widget);
      parameter.setWidgetParam(widgetParam);
    }

    return parameter;
  }

  /**
   * Method description
   *
   *
   * @param document
   * @param rootEl
   */
  private void storeMacroInformation(Document document, Element rootEl)
  {
    Element infoEl = document.createElement("macro-information");

    rootEl.appendChild(infoEl);

    if (Util.isNotEmpty(information.getDisplayName()))
    {
      Element displayNameEl = document.createElement("display-name");

      displayNameEl.setTextContent(information.getDisplayName());
      infoEl.appendChild(displayNameEl);
    }

    if (Util.isNotEmpty(information.getIcon()))
    {
      Element iconEl = document.createElement("icon");

      iconEl.setTextContent(information.getIcon());
      infoEl.appendChild(iconEl);
    }

    Element previewEl = document.createElement("preview");

    previewEl.setTextContent(Boolean.toString(information.isPreview()));
    infoEl.appendChild(previewEl);

    if (information.getBodyWidget() != null)
    {
      Element bodyWidgetEl = document.createElement("body-widget");

      bodyWidgetEl.setTextContent(information.getBodyWidget().getName());
      infoEl.appendChild(bodyWidgetEl);
    }

    if (Util.isNotEmpty(information.getWidgetParam()))
    {
      Element widgetParam = document.createElement("widget-param");

      widgetParam.setTextContent(information.getWidgetParam());
      infoEl.appendChild(widgetParam);
    }

    List<MacroInformationParameter> parameters = information.getParameter();

    if (Util.isNotEmpty(parameters))
    {
      for (MacroInformationParameter parameter : parameters)
      {
        storeParameter(document, infoEl, parameter);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param document
   * @param parent
   * @param parameter
   */
  private void storeParameter(Document document, Element parent,
                              MacroInformationParameter parameter)
  {
    Element parameterEl = document.createElement("parameter");

    parent.appendChild(parameterEl);

    Element nameEl = document.createElement("name");

    nameEl.setTextContent(parameter.getName());
    parameterEl.appendChild(nameEl);

    if (Util.isNotEmpty(parameter.getLabel()))
    {
      Element labelEl = document.createElement("label");

      labelEl.setTextContent(parameter.getLabel());
      parameterEl.appendChild(labelEl);
    }

    if (Util.isNotEmpty(parameter.getDescription()))
    {
      Element descriptionEl = document.createElement("description");

      descriptionEl.setTextContent(parameter.getDescription());
      parameterEl.appendChild(descriptionEl);
    }

    if (parameter.getWidget() != null)
    {
      Element widgetEl = document.createElement("widget");

      widgetEl.setTextContent(parameter.getWidget().getName());
      parameterEl.appendChild(widgetEl);
    }

    if (Util.isNotEmpty(parameter.getWidgetParam()))
    {
      Element widgetParamEl = document.createElement("widget-param");

      widgetParamEl.setTextContent(parameter.getWidgetParam());
      parameterEl.appendChild(widgetParamEl);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private MacroInformation information;
}
