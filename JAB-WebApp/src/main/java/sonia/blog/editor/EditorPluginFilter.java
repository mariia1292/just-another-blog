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
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.editor.EditorPlugin;
import sonia.blog.macro.FreemarkerTemplateParser;
import sonia.blog.util.BlogUtil;

import sonia.plugin.service.ServiceReference;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
public class EditorPluginFilter implements Filter
{

  /** Field description */
  private static final String EDITORPLUGINURL = "/resources/tiny_mce/plugins/";

  /** Field description */
  private static final String TEMPALTE = "/sonia/blog/editor/editor_plugin.js";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  public void destroy() {}

  /**
   * Method description
   *
   *
   * @param req
   * @param resp
   * @param chain
   *
   * @throws IOException
   * @throws ServletException
   */
  public void doFilter(ServletRequest req, ServletResponse resp,
                       FilterChain chain)
          throws IOException, ServletException
  {
    BlogRequest request = BlogUtil.getBlogRequest(req);
    BlogResponse response = BlogUtil.getBlogResponse(resp);
    String uri = getRelativePluginUri(request);
    String name = getPluginName(uri);
    EditorPlugin plugin = getEditorPlugin(name);

    if (plugin != null)
    {
      String path = uri.substring(name.length() + 1);

      if (path.equals("editor_plugin.js"))
      {
        printPlugin(response, plugin);
      }
    }
    else
    {
      chain.doFilter(request, response);
    }
  }

  /**
   * Method description
   *
   *
   * @param config
   *
   * @throws ServletException
   */
  public void init(FilterConfig config) throws ServletException
  {
    templateParser = new FreemarkerTemplateParser();
  }

  /**
   * Method description
   *
   *
   * @param response
   * @param plugin
   *
   * @throws IOException
   */
  private void printPlugin(BlogResponse response, EditorPlugin plugin)
          throws IOException
  {
    Map<String, Object> params = new HashMap<String, Object>();

    params.put("plugin", plugin);
    templateParser.parseTemplate(response.getWriter(), params, TEMPALTE);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   */
  private EditorPlugin getEditorPlugin(String name)
  {
    EditorPlugin result = null;

    if (Util.isNotEmpty(name))
    {
      List<EditorPlugin> plugins = getEditorPlugins();

      if (plugins != null)
      {
        for (EditorPlugin plugin : plugins)
        {
          if (name.equals(plugin.getName()))
          {
            result = plugin;

            break;
          }
        }
      }
    }

    return result;
  }

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
   * @param relativePlugin
   *
   * @return
   */
  private String getPluginName(String relativePlugin)
  {
    String result = null;
    int index = relativePlugin.indexOf("/");

    if (index > 0)
    {
      result = relativePlugin.substring(0, index);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  private String getRelativePluginUri(BlogRequest request)
  {
    String uri = request.getRequestURI();

    uri = uri.substring(request.getContextPath().length()
                        + EDITORPLUGINURL.length());

    return uri;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private FreemarkerTemplateParser templateParser;
}
