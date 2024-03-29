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



package sonia.blog.webint;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.macro.WebResource;
import sonia.blog.webint.flickr.FlickrMacro;
import sonia.blog.webint.google.GoogleAnalyticsWebResource;

import sonia.macro.MacroParser;

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
    MacroParser parser = MacroParser.getInstance();

    parser.putMacro("flickr", FlickrMacro.class);
    BlogContext.getInstance().getMappingHandler().add(GatewayMapping.class);

    if (providerReference != null)
    {
      providerReference.add("/view/admin/webint/config.xhtml");
    }

    if (webResourceReference != null)
    {
      webResourceReference.add(gaResource);
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
    MacroParser parser = MacroParser.getInstance();

    parser.removeMacroFactory("flickr");
    BlogContext.getInstance().getMappingHandler().remove(GatewayMapping.class);

    if (providerReference != null)
    {
      providerReference.remove("/view/admin/webint/config.xhtml");
    }

    if (webResourceReference != null)
    {
      webResourceReference.add(gaResource);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private WebResource gaResource = new GoogleAnalyticsWebResource(2500);

  /** Field description */
  @Service(Constants.SERVICE_BLOGCONFIGPROVIDER)
  private ServiceReference<String> providerReference;

  /** Field description */
  @Service(Constants.SERVICE_FOOTRESOURCES)
  private ServiceReference<WebResource> webResourceReference;
}
