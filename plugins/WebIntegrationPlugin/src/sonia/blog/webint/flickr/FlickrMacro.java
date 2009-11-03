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



package sonia.blog.webint.flickr;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.api.macro.LinkResource;
import sonia.blog.api.macro.ScriptResource;
import sonia.blog.api.macro.WebMacro;
import sonia.blog.api.macro.WebResource;
import sonia.blog.entity.ContentObject;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sebastian Sdorra
 */
public class FlickrMacro extends AbstractBlogMacro implements WebMacro
{

  /** Field description */
  public static final String TEMPLATE = "/sonia/blog/webint/flickr/flickr.html";

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
   * @param username
   */
  public void setUsername(String username)
  {
    this.username = username;
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

    StringBuffer path = new StringBuffer(linkBase);

    path.append("resource/script/jquery.flickr.js");
    resources.add(new ScriptResource(201, path.toString()));

    ScriptResource jqueryPrettyPhoto =
      new ScriptResource(21,
                         linkBase
                         + "resources/prettyPhoto/js/jquery.prettyPhoto.js");

    resources.add(jqueryPrettyPhoto);

    LinkResource prettyPhotoCSS = new LinkResource(22);

    prettyPhotoCSS.setRel(LinkResource.REL_STYLESHEET);
    prettyPhotoCSS.setType(LinkResource.TYPE_STYLESHEET);
    prettyPhotoCSS.setHref(linkBase
                           + "resources/prettyPhoto/css/prettyPhoto.css");
    resources.add(prettyPhotoCSS);

    Map<String, Object> env = new HashMap<String, Object>();

    env.put("username", username);
    env.put("url",
            new StringBuffer(linkBase).append("gateway/flickr").toString());
    env.put(
        "loadingImage",
        new StringBuffer(linkBase).append(
          "resources/jquery/plugins/img/loading.gif").toString());

    return parseTemplate(env, TEMPLATE);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<WebResource> resources;

  /** Field description */
  private String username;
}
