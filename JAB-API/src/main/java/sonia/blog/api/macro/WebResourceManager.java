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



package sonia.blog.api.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogVersion;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public class WebResourceManager
{

  /** Field description */
  public static final String JABICON_128 = "128.gif";

  /** Field description */
  public static final String JABICON_16 = "16.gif";

  /** Field description */
  public static final String JABICON_32 = "32.gif";

  /** Field description */
  public static final String JABICON_64 = "64.gif";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param contextPath
   * @param blogVersion
   */
  public WebResourceManager(String contextPath, BlogVersion blogVersion)
  {
    this.contextPath = contextPath;
    this.blogVersion = blogVersion;
    jQuery =
      new ScriptResource(1, getResourceUri("/resources/jquery/jquery.min.js"));
    fancybox = new ArrayList<WebResource>();
    fancybox.add(
        new ScriptResource(
            200, getResourceUri("/resources/fancybox/jquery.fancybox.min.js")));
    fancybox.add(
        new ScriptResource(
            201,
            getResourceUri("/resources/fancybox/jquery.mousewheel.min.js")));
    fancybox.add(
        new LinkResource(
            202, LinkResource.TYPE_STYLESHEET,
            getResourceUri("/resources/fancybox/jquery.fancybox.css"),
            LinkResource.REL_STYLESHEET, null, "user", false));
    flowplayerScript = new ScriptResource(10,
            getResourceUri("/resources/flowplayer/flowplayer.min.js"));
    flowplayerControlUri =
      getResourceUri("/resources/flowplayer/flowplayer.control.swf");
    flowplayerUri = getResourceUri("/resources/flowplayer/flowplayer.swf");
    fancyplayer = new ScriptResource(
        110,
        getResourceUri("/resources/jquery/plugins/js/jquery.fancyplayer.js"));
    loadingImage = getResourceUri("/resources/jquery/plugins/img/loading.gif");
    favicon = getResourceUri("/resources/images/favicon.ico");
    jabIcon = contextPath + "/resources/images/icons/jab/icon-";
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<WebResource> getFancybox()
  {
    return fancybox;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public WebResource getFancyplayer()
  {
    return fancyplayer;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getFavicon()
  {
    return favicon;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getFlowplayerControlUri()
  {
    return flowplayerControlUri;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public WebResource getFlowplayerScript()
  {
    return flowplayerScript;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getFlowplayerUri()
  {
    return flowplayerUri;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ScriptResource getJQuery()
  {
    return jQuery;
  }

  /**
   * Method description
   *
   *
   * @param size
   *
   * @return
   */
  public String getJabIcon(String size)
  {
    return getResourceUri(jabIcon + size);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getLoadingImage()
  {
    return loadingImage;
  }

  /**
   * Method description
   *
   *
   * @param uri
   *
   * @return
   */
  public String getResourceUri(String uri)
  {
    StringBuffer resourceUri = new StringBuffer();
    int index = uri.indexOf("resources/");

    if (index >= 0)
    {
      resourceUri.append(contextPath).append("/resources/");
      resourceUri.append("rev").append(blogVersion.getRevision());
      resourceUri.append(uri.substring(10));
    }

    return resourceUri.toString();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private BlogVersion blogVersion;

  /** Field description */
  private String contextPath;

  /** Field description */
  private List<WebResource> fancybox;

  /** Field description */
  private WebResource fancyplayer;

  /** Field description */
  private String favicon;

  /** Field description */
  private String flowplayerControlUri;

  /** Field description */
  private WebResource flowplayerScript;

  /** Field description */
  private String flowplayerUri;

  /** Field description */
  private ScriptResource jQuery;

  /** Field description */
  private String jabIcon;

  /** Field description */
  private String loadingImage;
}
