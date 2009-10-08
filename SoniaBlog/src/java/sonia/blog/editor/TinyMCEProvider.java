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
import sonia.blog.api.editor.EditorProvider;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.template.Template;
import sonia.blog.entity.Blog;

import sonia.util.Util;

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
    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    StringBuffer result = new StringBuffer();

    result.append("<script type=\"text/javascript\" src=\"");
    result.append(request.getContextPath());
    result.append("/resources/tiny_mce/tiny_mce.js\"></script>\n");
    result.append("<script type=\"text/javascript\">\n");
    result.append("tinyMCE.init({\n");
    result.append("theme : \"advanced\",\n");
    result.append("mode : \"textareas\",\n");
    result.append("skin : \"jab\",\n");

    /*
     * result.append("language: \"").append(request.getLocale().toString()).append(
     *   "\",\n");
     */
    result.append(
        "plugins : \"fullscreen,safari,emotions,imgbrowser,attachment,links,table,macro,imgresize\",\n");
    result.append(
        "theme_advanced_buttons3_add : \"|,table,emotions,fullscreen,|,imgbrowser,attachment,links,macro\",\n");
    result.append("theme_advanced_toolbar_location : \"top\",\n");
    result.append("theme_advanced_toolbar_align : \"left\",\n");

    String contentCSS = getContentCSS(request.getCurrentBlog());

    if (Util.hasContent(contentCSS))
    {
      contentCSS = linkBuilder.getRelativeLink(request, contentCSS);
      result.append("content_css : \"").append(contentCSS).append("\"");
      result.append(" + new Date().getTime(),\n");
    }

    String baseUrl = linkBuilder.buildLink(request, "/");

    result.append("convert_urls : false,\n");
    result.append("document_base_url : \"").append(baseUrl);
    result.append("\",\n");
    result.append("imageHandlerPattern : \"").append(baseUrl);
    result.append("image/\",\n");
    result.append("fullscreen_new_window : true,\n");
    result.append("fullscreen_settings : {\n");
    result.append("theme_advanced_path_location : \"top\"\n");
    result.append("}\n");
    result.append("});\n");
    result.append("</script>\n");

    return result.toString();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  private String getContentCSS(Blog blog)
  {
    String contentCSS = null;
    Template template =
      BlogContext.getInstance().getTemplateManager().getTemplate(blog);

    if (template != null)
    {
      contentCSS = template.getContentCSS();
    }

    return contentCSS;
  }
}