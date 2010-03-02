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



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.authentication.RequireRole;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Role;

import sonia.plugin.Plugin;
import sonia.plugin.PluginContext;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Logger;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Sebastian Sdorra
 */
@RequireRole(Role.GLOBALADMIN)
public class PluginBean extends AbstractBean
{

  /**
   * Constructs ...
   *
   */
  public PluginBean()
  {
    init();
    context = BlogContext.getInstance().getPluginContext();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String start()
  {
    Plugin plugin = (Plugin) plugins.getRowData();

    if (plugin != null)
    {
      context.start(plugin);
      getMessageHandler().info(getRequest(), "pluginStarted");
    }

    return SUCCESS;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String stop()
  {
    Plugin plugin = (Plugin) plugins.getRowData();

    if (plugin != null)
    {
      context.stop(plugin);
      getMessageHandler().info(getRequest(), "pluginStopped");
    }

    return SUCCESS;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getPlugins()
  {
    plugins = new ListDataModel(context.getPlugins());

    return plugins;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private PluginContext context;

  /** Field description */
  private DataModel plugins;
}
