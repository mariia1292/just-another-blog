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

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.api.macro.LinkResource;
import sonia.blog.api.macro.ScriptResource;
import sonia.blog.api.macro.WebMacro;
import sonia.blog.api.macro.WebResource;
import sonia.blog.api.macro.browse.SelectWidget;
import sonia.blog.api.macro.browse.StringInputWidget;
import sonia.blog.api.macro.browse.StringTextAreaWidget;
import sonia.blog.entity.ContentObject;

import sonia.macro.browse.MacroInfo;
import sonia.macro.browse.MacroInfoParameter;

import sonia.util.Util;

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
  name = "lightbox",
  displayName = "macro.lightbox.displayName",
  description = "macro.lightbox.description",
  resourceBundle = "sonia.blog.resources.label",
  bodyWidget = StringTextAreaWidget.class,
  widgetParam = "cols=110;rows=25"
)
public class LightboxMacro extends AbstractBlogMacro implements WebMacro
{

  /** Field description */
  private static final String TEMPLATE =
    "/sonia/blog/macro/template/lightbox.html";

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
   * @param theme
   */
  @MacroInfoParameter(
    displayName = "macro.lightbox.theme.displayName",
    description = "macro.lightbox.theme.description",
    widget = SelectWidget.class,
    widgetParam = "options=light_rounded|dark_rounded|light_square|dark_square;nullable=true"
  )
  public void setTheme(String theme)
  {
    this.theme = theme;
  }

  /**
   * Method description
   *
   *
   * @param title
   */
  @MacroInfoParameter(
    displayName = "macro.lightbox.title.displayName",
    description = "macro.lightbox.title.description",
    widget = StringInputWidget.class
  )
  public void setTitle(String title)
  {
    this.title = title;
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

    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    String res = linkBuilder.getRelativeLink(request, "/resources/");
    String lRes = res + "prettyPhoto/";

    resources = new ArrayList<WebResource>();

    ScriptResource jqueryPrettyPhoto = new ScriptResource(21,
                                         lRes + "js/jquery.prettyPhoto.js");

    resources.add(jqueryPrettyPhoto);

    LinkResource prettyPhotoCSS = new LinkResource(22);

    prettyPhotoCSS.setRel(LinkResource.REL_STYLESHEET);
    prettyPhotoCSS.setType(LinkResource.TYPE_STYLESHEET);
    prettyPhotoCSS.setHref(lRes + "css/prettyPhoto.css");
    resources.add(prettyPhotoCSS);

    if (Util.isBlank(title))
    {
      title = object.getTitle();
    }

    Map<String, Object> env = new HashMap<String, Object>();

    env.put("id", object.getId().toString());
    env.put("title", title);
    env.put("theme", theme);
    env.put("body", body);

    return parseTemplate(env, TEMPLATE);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<WebResource> resources;

  /** Field description */
  private String theme = "dark_square";

  /** Field description */
  private String title;
}
