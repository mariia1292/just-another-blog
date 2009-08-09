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

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.exception.BlogException;
import sonia.blog.api.macro.browse.BlogMacroWidget;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Role;
import sonia.blog.wui.AbstractEditorBean;
import sonia.blog.wui.EntryBean;
import sonia.blog.wui.PageAuthorBean;

import sonia.macro.Macro;
import sonia.macro.MacroParser;
import sonia.macro.browse.MacroInformation;
import sonia.macro.browse.MacroInformationParameter;
import sonia.macro.browse.MacroInformationProvider;
import sonia.macro.browse.MacroWidget;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Sebastian Sdorra
 */
public class MacroBrowserAsyncMapping extends FinalMapping
{

  /** Field description */
  public static final String ACTION_DETAIL = "detail";

  /** Field description */
  public static final String ACTION_LIST = "list";

  /** Field description */
  public static final String ACTION_PREVIEW = "preview";

  /** Field description */
  public static final String ACTION_RESULT = "result";

  /** Field description */
  public static final String PARAMETER_ACTION = "action";

  /** Field description */
  public static final String PARAMETER_NAME = "name";

  /** Field description */
  public static final String WIDGET_BODY = "__body";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(MacroBrowserAsyncMapping.class.getName());

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
      MacroParser parser = BlogContext.getInstance().getMacroParser();
      Class<? extends Macro> macroClass = parser.getMacro(name);

      if (macroClass != null)
      {
        MacroInformationProvider provider = parser.getInformationProvider();
        MacroInformation info = provider.getInformation(macroClass,
                                  request.getLocale());

        if (info != null)
        {
          writer.append("  { ");
          printBasicInfo(writer, info);

          ContentObject co = getContentObject(request);
          Class<? extends MacroWidget> bodyWidget = info.getBodyWidget();

          if (bodyWidget != null)
          {
            String bodyEl = getFormElement(request, bodyWidget, co,
                                           WIDGET_BODY, info.getWidgetParam());

            if (Util.hasContent(bodyEl))
            {
              bodyEl = encodeField(bodyEl);
              writer.append(", \"body\":\"").append(bodyEl).append("\"");
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
              Class<? extends MacroWidget> paramWidgetClass = param.getWidget();

              if (paramWidgetClass != null)
              {
                field = getFormElement(request, paramWidgetClass, co, pName,
                                       param.getWidgetParam());

                if (Util.hasContent(field))
                {
                  field = encodeField(field);
                }
              }

              writer.append("    { ");
              writer.append("\"name\":\"").append(pName).append("\",");
              writer.append("\"label\":\"").append(label).append("\",");
              writer.append("\"description\":\"").append(description).append(
                  "\",");
              writer.append("\"field\":\"").append(field).append("\"");
              writer.append("}");
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
   *
   * @param request
   * @param response
   * @param writer
   */
  private void handleListAction(BlogRequest request, BlogResponse response,
                                PrintWriter writer)
  {
    response.setContentType("application/x-javascript");

    MacroParser parser = BlogContext.getInstance().getMacroParser();
    MacroInformationProvider provider = parser.getInformationProvider();
    Iterator<Class<? extends Macro>> macroIt = parser.getMacros();

    writer.println("[");

    boolean first = true;

    while (macroIt.hasNext())
    {
      Class<? extends Macro> macroClass = macroIt.next();
      MacroInformation info = provider.getInformation(macroClass,
                                request.getLocale());

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
                                   PrintWriter writer) {}

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
      MacroParser parser = BlogContext.getInstance().getMacroParser();
      Class<? extends Macro> macroClass = parser.getMacro(name);
      MacroInformation info =
        parser.getInformationProvider().getInformation(macroClass,
          request.getLocale());

      writer.append("{").append(name);

      List<MacroInformationParameter> parameters = info.getParameter();
      List<String> resultParamerters = new ArrayList<String>();

      for (MacroInformationParameter param : parameters)
      {
        String paramName = param.getName();
        String paramValue = request.getParameter(paramName);

        if (Util.hasContent(paramName) && Util.hasContent(paramValue))
        {
          resultParamerters.add(
              new StringBuffer(paramName).append("=").append(
                paramValue).toString());
        }
      }
      boolean first = true;
      for ( String param : resultParamerters )
      {
        if ( first )
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
      String body = request.getParameter( WIDGET_BODY );
      if ( body != null )
      {
        writer.append(escape(body));
      }
      writer.append( "{/" ).append(name).append("}");
    }
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
   *
   * @param request
   * @param widgetClazz
   * @param co
   * @param name
   * @param parameter
   *
   * @return
   */
  private String getFormElement(BlogRequest request,
                                Class<? extends MacroWidget> widgetClazz,
                                ContentObject co, String name, String parameter)
  {
    String result = "";
    BlogMacroWidget widget = getWidget(widgetClazz);

    if (widget != null)
    {
      result = widget.getFormElement(request, co, name, parameter);
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
    if ( logger.isLoggable(Level.FINEST) )
    {
      StringBuffer log = new StringBuffer();
      log.append( "create an instance of " ).append( clazz.getName() );
      logger.finest(log.toString());
    }
    BlogMacroWidget widget = null;

    try
    {
      widget = (BlogMacroWidget) clazz.newInstance();
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }

    return widget;
  }
}
