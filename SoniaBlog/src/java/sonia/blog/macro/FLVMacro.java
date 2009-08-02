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
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.api.macro.ScriptResource;
import sonia.blog.api.macro.WebMacro;
import sonia.blog.api.macro.WebResource;
import sonia.blog.api.macro.browse.AttachmentWidget;
import sonia.blog.api.macro.browse.CheckboxWidget;
import sonia.blog.api.macro.browse.StringInputWidget;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
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
  name = "flvviewer",
  displayName = "macro.flv.displayName",
  description = "macro.flv.description",
  resourceBundle = "sonia.blog.resources.label"
)
public class FLVMacro extends AbstractBlogMacro implements WebMacro
{

  /** Field description */
  private static final String TEMPLATE =
    "sonia/blog/macro/template/flowplayer.html";

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
   * @param autoBuffering
   */
  @MacroInfoParameter(
    displayName = "macro.flv.autoBuffering.displayName",
    description = "macro.flv.autoBuffering.description",
    widget = CheckboxWidget.class
  )
  public void setAutoBuffering(Boolean autoBuffering)
  {
    this.autoBuffering = autoBuffering;
  }

  /**
   * Method description
   *
   *
   * @param autoPlay
   */
  @MacroInfoParameter(
    displayName = "macro.flv.autoPlay.displayName",
    description = "macro.flv.autoPlay.description",
    widget = CheckboxWidget.class
  )
  public void setAutoPlay(Boolean autoPlay)
  {
    this.autoPlay = autoPlay;
  }

  /**
   * Method description
   *
   *
   * @param height
   */
  @MacroInfoParameter(
    displayName = "macro.flv.height.displayName",
    description = "macro.flv.height.description",
    widget = StringInputWidget.class,
    widgetParam = "regex=\\d+;value=360"
  )
  public void setHeight(Integer height)
  {
    this.height = height;
  }

  /**
   * Method description
   *
   *
   * @param id
   */
  @MacroInfoParameter(
    displayName = "macro.flv.id.displayName",
    description = "macro.flv.id.description",
    widget = AttachmentWidget.class,
    widgetParam = "filter=.*\\.(flv|f4v|avi)"
  )
  public void setId(Long id)
  {
    this.id = id;
  }

  /**
   * Method description
   *
   *
   * @param width
   */
  @MacroInfoParameter(
    displayName = "macro.flv.width.displayName",
    description = "macro.flv.width.description",
    widget = StringInputWidget.class,
    widgetParam = "regex=\\d+;value=480"
  )
  public void setWidth(Integer width)
  {
    this.width = width;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
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
    String result = null;

    if (id != null)
    {
      Blog blog = request.getCurrentBlog();
      Attachment attchment = attachmentDAO.get(id);

      if ((attchment != null) && attchment.isBlog(blog))
      {
        result = renderPlayer(request, attchment, linkBase, width, height);
      }
      else
      {
        result = "-- cant find attachment --";
      }
    }
    else
    {
      result = "-- id parameter not found --";
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param attchment
   * @param linkBase
   * @param width
   * @param height
   *
   * @return
   */
  private String renderPlayer(BlogRequest request, Attachment attchment,
                              String linkBase, int width, int height)
  {
    String playerPath = linkBase + "resources/flowplayer/";
    String attachmentLink = linkBuilder.buildLink(request, attchment);

    resources = new ArrayList<WebResource>();

    ScriptResource sr = new ScriptResource(10,
                          playerPath + "flowplayer.min.js");

    resources.add(sr);

    Map<String, Object> params = new HashMap<String, Object>();

    params.put("attachment", attchment);
    params.put("playerPath", playerPath);
    params.put("attachmentLink", attachmentLink);
    params.put("width", width);
    params.put("height", height);
    params.put("autoPlay", autoPlay.toString());
    params.put("autoBuffering", autoBuffering.toString());

    return parseTemplate(params, TEMPLATE);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Dao
  private AttachmentDAO attachmentDAO;

  /** Field description */
  private Boolean autoBuffering = false;

  /** Field description */
  private Boolean autoPlay = false;

  /** Field description */
  private Integer height = 360;

  /** Field description */
  private Long id;

  /** Field description */
  @Context
  private LinkBuilder linkBuilder;

  /** Field description */
  private List<WebResource> resources;

  /** Field description */
  private Integer width = 480;
}
