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



package sonia.blog.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Context;
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.api.macro.ScriptResource;
import sonia.blog.api.macro.WebMacro;
import sonia.blog.api.macro.WebResource;
import sonia.blog.api.macro.WebResourceManager;
import sonia.blog.api.macro.browse.StringInputWidget;
import sonia.blog.entity.ContentObject;

import sonia.macro.browse.MacroInfo;
import sonia.macro.browse.MacroInfoParameter;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sebastian Sdorra
 */
@MacroInfo(
  name = "feed",
  displayName = "macro.feed.displayName",
  description = "macro.feed.description",
  resourceBundle = "sonia.blog.resources.label"
)
public class FeedMacro extends AbstractBlogMacro implements WebMacro
{

  /** Field description */
  private static final String TEMPLATE = "sonia/blog/macro/template/feed.html";

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<WebResource> getResources()
  {
    return resources;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param maxItems
   */
  @MacroInfoParameter(
    displayName = "macro.feed.maxItems.displayName",
    description = "macro.feed.maxItems.description",
    widget = StringInputWidget.class,
    order = 1
  )
  public void setMaxItems(Integer maxItems)
  {
    this.maxItems = maxItems;
  }

  /**
   * Method description
   *
   *
   * @param url
   */
  @MacroInfoParameter(
    displayName = "macro.feed.url.displayName",
    description = "macro.feed.url.description",
    widget = StringInputWidget.class,
    order = 0
  )
  public void setUrl(String url)
  {
    System.out.println("SETURL: " + url);
    this.url = url;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param linkBase
   * @param object
   * @param body
   *
   * @return
   */
  @Override
  protected String doBody(BlogRequest request, String linkBase,
                          ContentObject object, String body)
  {
    resources = new ArrayList<WebResource>();
    resources.add(
        new ScriptResource(
            101,
            webResourceManager.getResourceUri(
              "/resources/jquery/plugins/js/jquery.feeds.js"), "text/javascript"));

    Map<String, Object> parameter = new HashMap<String, Object>();
    StringBuffer urlBuffer = new StringBuffer(linkBase);

    urlBuffer.append("async/feed.json?url=").append(url);
    parameter.put("url", urlBuffer.toString());
    parameter.put("loadingImage", webResourceManager.getLoadingImage());
    parameter.put("id", object.getId());
    parameter.put("maxItems", maxItems);

    return parseTemplate(parameter, TEMPLATE);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Integer maxItems;

  /** Field description */
  private List<WebResource> resources;

  /** Field description */
  private String url;

  /** Field description */
  @Context
  private WebResourceManager webResourceManager;
}
