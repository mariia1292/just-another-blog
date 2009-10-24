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
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.api.macro.ScriptResource;
import sonia.blog.api.macro.WebMacro;
import sonia.blog.api.macro.WebResource;
import sonia.blog.api.macro.browse.CheckboxWidget;
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
  name = "toc",
  displayName = "macro.toc.displayName",
  description = "macro.toc.description",
  resourceBundle = "sonia.blog.resources.label",
  preview = false
)
public class TOCMacro extends AbstractBlogMacro implements WebMacro
{

  /** Field description */
  private static final String TEMPLATE = "sonia/blog/macro/template/toc.html";

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
   * @param container
   */
  @MacroInfoParameter(
    displayName = "macro.toc.container.displayName",
    description = "macro.toc.container.description",
    widget = StringInputWidget.class
  )
  public void setContainer(String container)
  {
    this.container = container;
  }

  /**
   * Method description
   *
   *
   * @param exclude
   */
  @MacroInfoParameter(
    displayName = "macro.toc.exclude.displayName",
    description = "macro.toc.exclude.description",
    widget = StringInputWidget.class
  )
  public void setExclude(String exclude)
  {
    this.exclude = exclude;
  }

  /**
   * Method description
   *
   *
   * @param id
   */
  @MacroInfoParameter(
    displayName = "macro.toc.id.displayName",
    description = "macro.toc.id.description",
    widget = StringInputWidget.class
  )
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Method description
   *
   *
   * @param orderedList
   */
  @MacroInfoParameter(
    displayName = "macro.toc.orderedList.displayName",
    description = "macro.toc.orderedList.description",
    widget = CheckboxWidget.class
  )
  public void setOrderedList(Boolean orderedList)
  {
    this.orderedList = orderedList;
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
    StringBuffer resource = new StringBuffer();

    resource.append(linkBase).append("resources/jquery/plugins/js/toc.js");

    ScriptResource tocScript = new ScriptResource(51, resource.toString());

    resources = new ArrayList<WebResource>();
    resources.add(tocScript);

    Map<String, Object> parameters = new HashMap<String, Object>();

    parameters.put("id", (id != null)
                         ? id
                         : object.getId().toString());
    parameters.put("exclude", exclude);
    parameters.put("orderedList", orderedList.toString());
    parameters.put("container", container);

    return parseTemplate(parameters, TEMPLATE);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String container;

  /** Field description */
  private String exclude = "h1";

  /** Field description */
  private String id;

  /** Field description */
  private Boolean orderedList = Boolean.FALSE;

  /** Field description */
  private List<WebResource> resources;
}
