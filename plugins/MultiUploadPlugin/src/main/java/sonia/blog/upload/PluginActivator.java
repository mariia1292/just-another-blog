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



package sonia.blog.upload;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.Context;
import sonia.blog.api.editor.EditorPlugin;
import sonia.blog.api.mapping.MappingHandler;

import sonia.plugin.Activator;
import sonia.plugin.PluginContext;
import sonia.plugin.service.Service;
import sonia.plugin.service.ServiceReference;

/**
 *
 * @author Sebastian Sdorra
 */
public class PluginActivator implements Activator
{

  /**
   * Method description
   *
   *
   * @param context
   */
  public void start(PluginContext context)
  {
    if (pluginReference != null)
    {
      plugin = createEditorPlugin();
      pluginReference.add(plugin);
    }

    if (mappingHandler != null)
    {
      mappingHandler.add(MultiUploadMapping.class);
    }
  }

  /**
   * Method description
   *
   *
   * @param context
   */
  public void stop(PluginContext context)
  {
    if ((pluginReference != null) && (plugin != null))
    {
      pluginReference.remove(plugin);
      plugin = null;
    }

    if (mappingHandler != null)
    {
      mappingHandler.remove(MultiUploadMapping.class);
    }
  }

  /**
   * Method description
   *
   *
   * @return
   */
  private EditorPlugin createEditorPlugin()
  {
    String contextPath =
      BlogContext.getInstance().getServletContext().getContextPath();
    EditorPlugin uploadPlugin = new EditorPlugin("multiupload");

    uploadPlugin.setAuthor("Sebastian Sdorra");
    uploadPlugin.setAuthorUrl("http://www.just-another-blog.org");
    uploadPlugin.setDisplayName("Multiupload");
    uploadPlugin.setIcon(contextPath
                         + "/resource/mutliupload-icon.gif");
    uploadPlugin.setInfoUrl("http://www.plupload.com/");
    uploadPlugin.setUrl(contextPath + "/view/admin/plupload/plugin.jab");
    uploadPlugin.setVersion("1.0");

    return uploadPlugin;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Context
  private MappingHandler mappingHandler;

  /** Field description */
  private EditorPlugin plugin;

  /** Field description */
  @Service(Constants.SERVICE_EDITORPLUGIN)
  private ServiceReference<EditorPlugin> pluginReference;
}