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
import sonia.blog.entity.ContentObject;

import sonia.macro.MacroParser;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sebastian Sdorra
 */
public class TabsMacro extends AbstractBlogMacro
{

  /** Field description */
  public static final String ENV_TABCONTAINER = "sonia.blog.tab-container";

  /** Field description */
  private static final String TEMPLATE = "/sonia/blog/macro/template/tabs.html";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param tab
   */
  public void addTab(TabMacro tab)
  {
    tabs.add(tab);
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param height
   */
  public void setHeight(String height)
  {
    this.height = height;
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
    tabs = new ArrayList<TabMacro>();
    environment.put(ENV_TABCONTAINER, this);
    parser.parseText(environment, body);
    environment.remove(ENV_TABCONTAINER);

    Map<String, Object> env = new HashMap<String, Object>();

    env.put("id", object.getId());
    env.put("height", height);
    env.put("tabs", tabs);

    return parseTemplate(env, TEMPLATE);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String height = "250px";

  /** Field description */
  @Context
  private MacroParser parser;

  /** Field description */
  private List<TabMacro> tabs;
}
