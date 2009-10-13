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



package sonia.blog.office;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.api.macro.LinkResource;
import sonia.blog.api.macro.ScriptResource;
import sonia.blog.api.macro.WebMacro;
import sonia.blog.api.macro.WebResource;
import sonia.blog.api.macro.browse.AttachmentWidget;
import sonia.blog.api.macro.browse.SelectWidget;
import sonia.blog.api.macro.browse.StringTextAreaWidget;
import sonia.blog.entity.Attachment;
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
  name = "pdfviewer",
  displayName = "macro.pdfviewer.displayName",
  description = "macro.pdfviewer.description",
  resourceBundle = "sonia.blog.office.label",
  bodyWidget = StringTextAreaWidget.class
)
public class PdfViewerMacro extends AbstractBlogMacro implements WebMacro
{

  /** Field description */
  public static final String NAME = "pdfviewer";

  /** Field description */
  private static final String PDFMIMETYPE = "application/pdf";

  /** Field description */
  private static final String TEMPLATE =
    "/sonia/blog/office/template/pdfgallery.html";

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
   * @param id
   */
  @MacroInfoParameter(
    displayName = "macro.pdfviewer.id.displayName",
    description = "macro.pdfviewer.id.description",
    widget = AttachmentWidget.class,
    widgetParam = ".*\\.pdf"
  )
  public void setId(Long id)
  {
    this.id = id;
  }

  /**
   * Method description
   *
   *
   * @param theme
   */
  @MacroInfoParameter(
    displayName = "macro.pdfviewer.theme.displayName",
    description = "macro.pdfviewer.theme.description",
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
   * @param type
   */
  @MacroInfoParameter(
    displayName = "macro.pdfviewer.type.displayName",
    description = "macro.pdfviewer.type.description",
    widget = SelectWidget.class,
    widgetParam = "options=hidden|thumb|linklist"
  )
  public void setType(String type)
  {
    this.type = type;
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
    String result = "";

    if (id != null)
    {
      Attachment attachment =
        attachmentDAO.findByBlogAndId(request.getCurrentBlog(), id);

      if ((attachment != null)
          && attachment.getMimeType().equalsIgnoreCase(PDFMIMETYPE))
      {
        result = printGallery(linkBase, attachment, object.getId(), body);
      }
      else
      {
        result = "-- pdf attachment not found --";
      }
    }
    else
    {
      result = "-- parameter id is requiered --";
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param linkBase
   * @param attachment
   * @param galleryId
   * @param body
   *
   * @return
   */
  private String printGallery(String linkBase, Attachment attachment,
                              Long galleryId, String body)
  {
    resources = new ArrayList<WebResource>();
    resources.add(
        new ScriptResource(
            21, linkBase + "resources/prettyPhoto/js/jquery.prettyPhoto.js"));

    LinkResource prettyPhotoCSS = new LinkResource(22);

    prettyPhotoCSS.setRel(LinkResource.REL_STYLESHEET);
    prettyPhotoCSS.setType(LinkResource.TYPE_STYLESHEET);
    prettyPhotoCSS.setHref(linkBase
                           + "resources/prettyPhoto/css/prettyPhoto.css");
    resources.add(prettyPhotoCSS);
    resources.add(new ScriptResource(23,
                                     linkBase
                                     + "resource/script/jquery.pdfgallery.js"));

    Map<String, Object> parameter = new HashMap<String, Object>();

    parameter.put("id", galleryId.toString());
    parameter.put("theme", theme);

    StringBuffer url = new StringBuffer(linkBase).append("macros/pdfviewer/");

    url.append(attachment.getId()).append("/index.json");
    parameter.put("url", url.toString());
    parameter.put("loadingImage",
                  linkBase + "resources/jquery/plugins/img/loading.gif");
    parameter.put("body", body);
    parameter.put("type", type);

    return parseTemplate(parameter, TEMPLATE);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Dao
  private AttachmentDAO attachmentDAO;

  /** Field description */
  private Long id;

  /** Field description */
  private List<WebResource> resources;

  /** Field description */
  private String theme = "dark_square";

  /** Field description */
  private String type = "hidden";
}
