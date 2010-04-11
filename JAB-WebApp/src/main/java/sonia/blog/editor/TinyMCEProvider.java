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



package sonia.blog.editor;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.editor.EditorPlugin;
import sonia.blog.api.editor.EditorProvider;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.template.Style;
import sonia.blog.api.template.StyleAttribute;
import sonia.blog.api.template.Template;
import sonia.blog.entity.Blog;

import sonia.plugin.service.ServiceReference;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Sebastian Sdorra
 */
public class TinyMCEProvider implements EditorProvider
{

  /**
   * Method description
   *
   *
   * @param request
   * @param ids
   *
   * @return
   */
  public String renderEditor(BlogRequest request, String[] ids)
  {
    StringBuffer result = new StringBuffer();

    result.append("<script type=\"text/javascript\" src=\"");
    result.append(request.getContextPath());
    result.append("/resources/tiny_mce/jquery.tinymce.js\"></script>\n");
    result.append("<script type=\"text/javascript\">\n");
    result.append("$(document).ready(function(){\n");
    result.append(getJavaScript(request, ids, false));
    result.append("});\n");
    result.append("</script>\n");

    return result.toString();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param ids
   *
   * @return
   */
  public String getJavaScript(BlogRequest request, String[] ids)
  {
    return getJavaScript(request, ids, true);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param ids
   * @param loadScript
   *
   * @return
   */
  public String getJavaScript(BlogRequest request, String[] ids,
                              boolean loadScript)
  {
    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    StringBuffer result = new StringBuffer();

    if (loadScript)
    {
      result.append("$.getScript(\"").append(request.getContextPath());
      result.append("/resources/tiny_mce/jquery.tinymce.js\", function(){");
    }

    result.append("$(\"");

    for (int i = 0; i < ids.length; i++)
    {
      if (i > 0)
      {
        result.append(", ");
      }

      result.append("#").append(ids[i].replaceAll(":", "\\\\\\\\:"));
    }

    result.append("\").tinymce({");
    result.append("script_url : \"");
    result.append(request.getContextPath());
    result.append("/resources/tiny_mce/tiny_mce.js\",\n");
    result.append("theme : \"advanced\",\n");
    result.append("mode : \"textareas\",\n");
    result.append("skin : \"jab\",\n");
    result.append("dialog_type : \"modal\",");

    String ls = "en";
    Locale locale = request.getLocale();

    if ((locale != null)
        && (Locale.GERMANY.equals(locale) || Locale.GERMAN.equals(locale)))
    {
      ls = "de";
    }

    result.append("language: \"").append(ls).append("\",\n");
    result.append(
        "plugins : \"fullscreen,inlinepopups,emotions,imgbrowser,attachment,links,table,macro,imgresize");
    appendPlugins(result);
    result.append("\",\n");
    result.append(
        "theme_advanced_buttons3_add : \"|,table,emotions,fullscreen,|,imgbrowser,attachment,links,macro");
    appendPlugins(result);
    result.append("\",\n");
    result.append("theme_advanced_toolbar_location : \"top\",\n");
    result.append("theme_advanced_toolbar_align : \"left\",\n");

    Template template = getTemplate(request.getCurrentBlog());

    if (template != null)
    {
      String contentCSS = template.getContentCSS();

      if (Util.hasContent(contentCSS))
      {
        contentCSS = linkBuilder.getRelativeLink(request, contentCSS);
        result.append("content_css : \"").append(contentCSS).append("?_=\"");
        result.append(" + new Date().getTime(),\n");
      }

      List<Style> styles = template.getStyles();

      if (Util.isNotEmpty(styles))
      {
        appendStyles(result, styles);
      }
    }

    String baseUrl = request.getContextPath();

    result.append("convert_urls : false,\n");
    result.append("document_base_url : \"").append(baseUrl);
    result.append("\",\n");
    result.append("imageHandlerPattern : \"").append(baseUrl);
    result.append("/image/\",\n");
    result.append("fullscreen_new_window : false,\n");
    result.append("fullscreen_settings : {\n");
    result.append("theme_advanced_path_location : \"top\"\n");
    result.append("}\n");
    result.append("});\n");

    if (loadScript)
    {
      result.append("});\n");
    }

    return result.toString();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param result
   */
  private void appendPlugins(StringBuffer result)
  {
    List<EditorPlugin> plugins = getEditorPlugins();

    if (plugins != null)
    {
      for (EditorPlugin plugin : plugins)
      {
        result.append(",").append(plugin.getName());
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param buffer
   * @param styles
   *
   * @return
   */
  private StringBuffer appendStyles(StringBuffer buffer, List<Style> styles)
  {
    buffer.append("style_formats : [\n");

    Iterator<Style> styleIt = styles.iterator();

    while (styleIt.hasNext())
    {
      Style style = styleIt.next();

      buffer.append("{");

      boolean first = true;
      String title = style.getTitle();

      if (Util.isNotEmpty(title))
      {
        first = false;
        buffer.append("title: '").append(title).append("'");
      }

      List<String> selectors = style.getSelectors();

      if (Util.isNotEmpty(selectors))
      {
        if (first)
        {
          first = false;
        }
        else
        {
          buffer.append(",");
        }

        buffer.append("selector: '");

        Iterator<String> selectorIt = selectors.iterator();

        while (selectorIt.hasNext())
        {
          buffer.append(selectorIt.next());

          if (selectorIt.hasNext())
          {
            buffer.append(",");
          }
        }

        buffer.append("'");
      }

      String inline = style.getInline();

      if (Util.isNotEmpty(inline))
      {
        if (first)
        {
          first = false;
        }
        else
        {
          buffer.append(",");
        }

        buffer.append("inline: '").append(inline).append("'");
      }

      String block = style.getBlock();

      if (Util.isNotEmpty(block))
      {
        if (first)
        {
          first = false;
        }
        else
        {
          buffer.append(",");
        }

        buffer.append("block: '").append(block).append("'");
      }

      List<String> classes = style.getClasses();

      if (Util.isNotEmpty(classes))
      {
        if (first)
        {
          first = false;
        }
        else
        {
          buffer.append(",");
        }

        buffer.append("classes: '");

        Iterator<String> classesIt = classes.iterator();

        while (classesIt.hasNext())
        {
          buffer.append(classesIt.next());

          if (classesIt.hasNext())
          {
            buffer.append(",");
          }
        }

        buffer.append("'");
      }

      List<StyleAttribute> attributes = style.getAttributes();

      if (Util.isNotEmpty(attributes))
      {
        if (first)
        {
          first = false;
        }
        else
        {
          buffer.append(",");
        }

        buffer.append("styles: {");

        Iterator<StyleAttribute> attributeIt = attributes.iterator();

        while (attributeIt.hasNext())
        {
          StyleAttribute a = attributeIt.next();

          buffer.append("'").append(a.getName()).append("': '");
          buffer.append(a.getValue()).append("'");

          if (attributeIt.hasNext())
          {
            buffer.append(",");
          }
        }

        buffer.append("}");
      }

      buffer.append("}");

      if (styleIt.hasNext())
      {
        buffer.append(",");
      }

      buffer.append("\n");
    }

    return buffer.append("],\n");
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private List<EditorPlugin> getEditorPlugins()
  {
    List<EditorPlugin> plugins = null;
    ServiceReference<EditorPlugin> reference =
      BlogContext.getInstance().getServiceRegistry().get(EditorPlugin.class,
        Constants.SERVICE_EDITORPLUGIN);

    if (reference != null)
    {
      plugins = reference.getAll();
    }

    return plugins;
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  private Template getTemplate(Blog blog)
  {
    return BlogContext.getInstance().getTemplateManager().getTemplate(blog);
  }
}
