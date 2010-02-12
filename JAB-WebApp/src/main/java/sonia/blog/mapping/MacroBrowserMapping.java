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



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.commons.lang.StringEscapeUtils;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.Context;
import sonia.blog.api.exception.BlogException;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.macro.LinkResource;
import sonia.blog.api.macro.WebMacro;
import sonia.blog.api.macro.WebResource;
import sonia.blog.api.macro.browse.BlogMacroWidget;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.api.mapping.MappingConfig;
import sonia.blog.api.template.Template;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Role;
import sonia.blog.wui.AbstractEditorBean;
import sonia.blog.wui.EntryBean;
import sonia.blog.wui.PageAuthorBean;

import sonia.cache.Cache;
import sonia.cache.ObjectCache;

import sonia.macro.Macro;
import sonia.macro.MacroFactory;
import sonia.macro.MacroParser;
import sonia.macro.MacroResult;
import sonia.macro.browse.MacroInformation;
import sonia.macro.browse.MacroInformationParameter;
import sonia.macro.browse.MacroWidget;

import sonia.plugin.service.Service;
import sonia.plugin.service.ServiceReference;

import sonia.security.encryption.Encryption;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
@MappingConfig(compressable = true)
public class MacroBrowserMapping extends FinalMapping
{

  /** Field description */
  public static final String ACTION_DETAIL = "detail";

  /** Field description */
  public static final String ACTION_GETPREVIEW = "getPreview";

  /** Field description */
  public static final String ACTION_LIST = "list";

  /** Field description */
  public static final String ACTION_PREVIEW = "preview";

  /** Field description */
  public static final String ACTION_RESULT = "result";

  /** Field description */
  public static final String PARAMETER_ACTION = "action";

  /** Field description */
  public static final String PARAMETER_KEY = "key";

  /** Field description */
  public static final String PARAMETER_NAME = "name";

  /** Field description */
  public static final String WIDGET_BODY = "__body";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(MacroBrowserMapping.class.getName());

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param name
   *
   * @return
   */
  public String getMacroString(BlogRequest request, String name)
  {
    ContentObject co = getContentObject(request);
    StringBuffer writer = new StringBuffer();
    MacroInformation info =
      parser.getMacroFactory(name).getInformation(request.getLocale());

    writer.append("{").append(name);

    List<MacroInformationParameter> parameters = info.getParameter();
    List<String> resultParamerters = new ArrayList<String>();

    for (MacroInformationParameter param : parameters)
    {
      String paramName = param.getName();

      if (Util.hasContent(paramName))
      {
        Class<? extends MacroWidget> widgetClass = param.getWidget();
        String result = getWidgetResult(widgetClass, request, co, paramName,
                                        param.getWidgetParam());

        if (Util.hasContent(result))
        {
          resultParamerters.add(
              new StringBuffer(paramName).append("=").append(
                result).toString());
        }
      }
    }

    boolean first = true;

    for (String param : resultParamerters)
    {
      if (first)
      {
        writer.append(":");
        first = false;
      }
      else
      {
        writer.append(";");
      }

      writer.append(param);
    }

    writer.append("}");

    Class<? extends MacroWidget> widgetClass = info.getBodyWidget();
    String result = getWidgetResult(widgetClass, request, co, WIDGET_BODY,
                                    info.getWidgetParam());

    if (Util.hasContent(result))
    {
      writer.append(escape(result));
    }

    writer.append("{/").append(name).append("}");

    return writer.toString();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void handleFinalMapping(BlogRequest request, BlogResponse response,
                                    String[] param)
          throws IOException, ServletException
  {
    if (request.getBlogSession().hasRole(Role.AUTHOR))
    {
      String action = request.getParameter(PARAMETER_ACTION);

      if (Util.hasContent(action))
      {
        PrintWriter writer = response.getWriter();

        try
        {
          if (action.equalsIgnoreCase(ACTION_LIST))
          {
            handleListAction(request, response, writer);
          }
          else if (action.equalsIgnoreCase(ACTION_DETAIL))
          {
            handleDetailAction(request, response, writer);
          }
          else if (action.equalsIgnoreCase(ACTION_GETPREVIEW))
          {
            handleGetPreviewAction(request, response, writer);
          }
          else if (action.equalsIgnoreCase(ACTION_PREVIEW))
          {
            handlePreviewAction(request, response, writer);
          }
          else if (action.equalsIgnoreCase(ACTION_RESULT))
          {
            handleResultAction(request, response, writer);
          }
          else
          {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
          }
        }
        finally
        {
          writer.close();
        }
      }
      else
      {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
  }

  /**
   * Method description
   *
   *
   * @param field
   *
   * @return
   */
  private String encodeField(String field)
  {
    return field.replaceAll("\"", "&quot;").replaceAll("\n",
                            "").replaceAll("\r", "");
  }

  /**
   * Method description
   *
   *
   * @param result
   *
   * @return
   */
  private String escape(String result)
  {
    return StringEscapeUtils.escapeHtml(result).replace(" ",
            "&nbsp;").replace("'", "&#039;").replace("\n",
                              "<br />").replace("\r", "");
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param writer
   */
  private void handleDetailAction(BlogRequest request, BlogResponse response,
                                  PrintWriter writer)
  {
    response.setContentType("application/x-javascript");
    writer.println("[");

    String name = request.getParameter(PARAMETER_NAME);

    if (Util.hasContent(name))
    {
      MacroFactory factory = parser.getMacroFactory(name);

      if (factory != null)
      {
        MacroInformation info = factory.getInformation(request.getLocale());

        if (info != null)
        {
          writer.append("  { ");
          printBasicInfo(writer, info);
          writer.append(", \"preview\":").append(
              Boolean.toString(info.isPreview()));

          ContentObject co = getContentObject(request);
          Class<? extends MacroWidget> bodyWidget = info.getBodyWidget();

          if ((bodyWidget != null) &&!MacroWidget.class.equals(bodyWidget))
          {
            BlogMacroWidget widget = getWidget(bodyWidget);

            if (widget != null)
            {
              String bodyEl = widget.getFormElement(request, co, WIDGET_BODY,
                                info.getWidgetParam());

              if (Util.hasContent(bodyEl))
              {
                bodyEl = encodeField(bodyEl);
                writer.append(", \"body\":\"").append(bodyEl).append("\"");

                String bodyJS = widget.getJavaScript(request, co, WIDGET_BODY,
                                  info.getWidgetParam());

                writer.append(", \"js\":\"");

                if (Util.hasContent(bodyJS))
                {
                  bodyJS = encodeField(bodyJS);
                  writer.append(bodyJS);
                }

                writer.append("\"");
              }
            }
          }

          writer.println(",\"parameters\": [");

          List<MacroInformationParameter> parameters = info.getParameter();

          if (Util.hasContent(parameters))
          {
            boolean first = true;

            for (MacroInformationParameter param : parameters)
            {
              if (first)
              {
                first = false;
              }
              else
              {
                writer.println(", ");
              }

              String pName = param.getName();
              String label = (param.getLabel() != null)
                             ? param.getLabel()
                             : pName;
              String description = (param.getDescription() != null)
                                   ? param.getDescription()
                                   : "";
              String field = "";
              String fieldJS = "";
              Class<? extends MacroWidget> paramWidgetClass = param.getWidget();

              if (paramWidgetClass != null)
              {
                BlogMacroWidget widget = getWidget(paramWidgetClass);

                field = widget.getFormElement(request, co, pName,
                                              param.getWidgetParam());

                if (field != null)
                {
                  field = encodeField(field);
                }
                else
                {
                  field = "";
                }

                fieldJS = widget.getJavaScript(request, co, name, pName);

                if (fieldJS != null)
                {
                  fieldJS = encodeField(fieldJS);
                }
                else
                {
                  fieldJS = "";
                }
              }

              writer.append("    { ");
              writer.append("\"name\":\"").append(pName).append("\",");
              writer.append("\"label\":\"").append(label).append("\",");
              writer.append("\"description\":\"").append(description).append(
                  "\",");
              writer.append("\"field\":\"").append(field).append("\", ");
              writer.append("\"js\":\"").append(fieldJS).append("\"}");
            }
          }

          writer.println();
          writer.append("  ]}");
        }
      }
    }

    writer.println();
    writer.println("]");
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param writer
   */
  private void handleGetPreviewAction(BlogRequest request,
          BlogResponse response, PrintWriter writer)
  {
    response.setContentType("text/plain");

    String name = request.getParameter(PARAMETER_NAME);

    if (Util.hasContent(name))
    {
      StringBuffer cacheKey = new StringBuffer();
      StringBuffer cacheValue = new StringBuffer();
      String macroString = getMacroString(request, name);

      cacheKey.append(macroString);

      Map<String, Object> env = new HashMap<String, Object>();
      ContentObject co = getContentObject(request);

      if (co != null)
      {
        cacheKey.append("|").append(co.getId());
      }

      env.put("object", co);
      env.put("request", request);
      env.put("blog", request.getCurrentBlog());

      LinkBuilder builder = BlogContext.getInstance().getLinkBuilder();

      env.put("linkBase", builder.getRelativeLink(request, "/"));

      MacroResult result = parser.parseText(env, macroString);
      List<WebResource> resources = getResources(result);
      List<WebResource> serviceResources = getServiceResources();

      if (Util.hasContent(serviceResources))
      {
        resources.addAll(serviceResources);
      }

      WebResource contentCSS = getContentCSS(request);

      if (contentCSS != null)
      {
        resources.add(contentCSS);
      }

      Collections.sort(resources);
      cacheValue.append("<html><head><title>Preview</title>");

      for (WebResource resource : Util.unique(resources))
      {
        cacheValue.append(resource.toHTML(request));
      }

      cacheValue.append("</head><body>");
      cacheValue.append(result.getText());
      cacheValue.append("</body></html>");

      String key = null;

      if (encryption != null)
      {
        key = encryption.encrypt(cacheKey.toString());
      }
      else
      {
        key = cacheKey.toString();
      }

      if (previewCache != null)
      {
        previewCache.put(key, cacheValue.toString());
      }

      writer.println(key);
    }
  }

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param response
   * @param writer
   */
  private void handleListAction(BlogRequest request, BlogResponse response,
                                PrintWriter writer)
  {
    response.setContentType("application/x-javascript");

    Iterator<MacroFactory> macroIt = parser.getMacroFactories();

    writer.println("[");

    boolean first = true;
    List<MacroInformation> informations = new ArrayList<MacroInformation>();

    while (macroIt.hasNext())
    {
      MacroFactory factory = macroIt.next();
      MacroInformation info = factory.getInformation(request.getLocale());

      if (info != null)
      {
        informations.add(info);
      }
    }

    if (Util.hasContent(informations))
    {
      Collections.sort(informations, new MacroInformationComparator());

      for (MacroInformation info : informations)
      {
        if (info != null)
        {
          if (first)
          {
            first = false;
          }
          else
          {
            writer.println(",");
          }

          writer.append("  {");
          printBasicInfo(writer, info);
          writer.append(" }");
        }
      }
    }

    writer.println();
    writer.println("]");
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param writer
   */
  private void handlePreviewAction(BlogRequest request, BlogResponse response,
                                   PrintWriter writer)
  {
    String key = request.getParameter(PARAMETER_KEY);

    if (previewCache != null)
    {
      String result = (String) previewCache.get(key);

      if (result != null)
      {
        response.setContentType("text/html");
        writer.write(result);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param writer
   */
  private void handleResultAction(BlogRequest request, BlogResponse response,
                                  PrintWriter writer)
  {
    response.setContentType("text/plain");

    String name = request.getParameter(PARAMETER_NAME);

    if (Util.hasContent(name))
    {
      writer.write(getMacroString(request, name));
    }
  }

  /**
   * Method description
   *
   *
   * @param writer
   * @param info
   */
  private void printBasicInfo(PrintWriter writer, MacroInformation info)
  {
    String displayName = (info.getDisplayName() != null)
                         ? info.getDisplayName()
                         : "";
    String description = (info.getDescription() != null)
                         ? info.getDescription()
                         : "";
    String icon = (info.getIcon() != null)
                  ? info.getIcon()
                  : "";

    writer.append("\"name\":\"").append(info.getName()).append("\",");
    writer.append("\"label\":\"").append(displayName).append("\",");
    writer.append("\"description\":\"").append(description).append("\",");
    writer.append("\"icon\":\"").append(icon).append("\"");
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  private WebResource getContentCSS(BlogRequest request)
  {
    WebResource css = null;
    Template template =
      BlogContext.getInstance().getTemplateManager().getTemplate(
          request.getCurrentBlog());
    String templateCSS = template.getContentCSS();

    if (Util.hasContent(templateCSS))
    {
      templateCSS =
        BlogContext.getInstance().getLinkBuilder().buildLink(request,
          templateCSS);
      css = new LinkResource(99, LinkResource.TYPE_STYLESHEET, templateCSS,
                             LinkResource.REL_STYLESHEET, null, "user", false);
    }

    return css;
  }

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  private ContentObject getContentObject(BlogRequest request)
  {
    String editor = (String) request.getSession().getAttribute("editor");
    AbstractEditorBean editorBean = null;

    if (editor.equalsIgnoreCase("entry"))
    {
      editorBean =
        (AbstractEditorBean) request.getSession().getAttribute(EntryBean.NAME);
    }
    else if (editor.equalsIgnoreCase("page"))
    {
      editorBean = (AbstractEditorBean) request.getSession().getAttribute(
        PageAuthorBean.NAME);
    }

    if (editorBean == null)
    {
      throw new BlogException("could not find the EditorBean");
    }

    return editorBean.getObject();
  }

  /**
   * Method description
   *
   *
   * @param result
   *
   * @return
   */
  private List<WebResource> getResources(MacroResult result)
  {
    List<WebResource> resources = new ArrayList<WebResource>();
    List<Macro> macros = result.getMacros();

    if (Util.hasContent(macros))
    {
      for (Macro macro : macros)
      {
        if (macro instanceof WebMacro)
        {
          WebMacro webMacro = (WebMacro) macro;
          List<WebResource> wr = webMacro.getResources();

          if (Util.hasContent(wr))
          {
            resources.addAll(wr);
          }
        }
      }
    }

    return resources;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  private List<WebResource> getServiceResources()
  {
    List<WebResource> result = null;
    ServiceReference<WebResource> reference =
      BlogContext.getInstance().getServiceRegistry().get(WebResource.class,
        Constants.SERVICE_HEADRESOURCES);

    if (reference != null)
    {
      result = reference.getAll();
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param clazz
   *
   * @return
   */
  private BlogMacroWidget getWidget(Class<? extends MacroWidget> clazz)
  {
    BlogMacroWidget widget = null;

    if (!clazz.equals(MacroWidget.class))
    {
      if (logger.isLoggable(Level.FINEST))
      {
        StringBuffer log = new StringBuffer();

        log.append("create an instance of ").append(clazz.getName());
        logger.finest(log.toString());
      }

      try
      {
        widget = (BlogMacroWidget) clazz.newInstance();
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }

    return widget;
  }

  /**
   * Method description
   *
   *
   * @param widgetClass
   * @param request
   * @param co
   * @param paramName
   * @param widgetParam
   *
   * @return
   */
  private String getWidgetResult(Class<? extends MacroWidget> widgetClass,
                                 BlogRequest request, ContentObject co,
                                 String paramName, String widgetParam)
  {
    String result = null;

    if (widgetClass != null)
    {
      BlogMacroWidget widget = getWidget(widgetClass);

      if (widget != null)
      {
        result = widget.getResult(request, co, paramName, widgetParam);
      }
    }

    return result;
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/09/05
   * @author         Enter your name here...
   */
  private static class MacroInformationComparator
          implements Comparator<MacroInformation>, Serializable
  {

    /**
     * Method description
     *
     *
     * @param o1
     * @param o2
     *
     * @return
     */
    public int compare(MacroInformation o1, MacroInformation o2)
    {
      return o1.getDisplayName().compareTo(o2.getDisplayName());
    }
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Service(Constants.SERVCIE_ENCRYPTION)
  private Encryption encryption;

  /** Field description */
  @Context
  private MacroParser parser;

  /** Field description */
  @Cache(Constants.CACHE_MBPREVIEW)
  private ObjectCache previewCache;
}
