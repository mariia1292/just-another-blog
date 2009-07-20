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
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.api.macro.LinkResource;
import sonia.blog.api.macro.ScriptResource;
import sonia.blog.api.macro.WebMacro;
import sonia.blog.api.macro.WebResource;
import sonia.blog.api.macro.browse.SelectWidget;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;

import sonia.macro.browse.MacroInfo;
import sonia.macro.browse.MacroInfoParameter;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
@MacroInfo(
  name = "gallery",
  displayName = "macro.gallery.displayName",
  description = "macro.gallery.description",
  resourceBundle = "sonia.blog.resources.label"
)
public class GalleryMacro extends AbstractBlogMacro implements WebMacro
{

  /** Field description */
  private static Logger logger = Logger.getLogger(GalleryMacro.class.getName());

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
    displayName = "macro.gallery.theme.displayName",
    description = "macro.gallery.theme.description",
    widget = SelectWidget.class,
    widgetParam = "options=light_rounded|dark_rounded|light_square|dark_square;nullable=true"
  )
  public void setTheme(String theme)
  {
    this.theme = theme;
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
    StringBuffer result = new StringBuffer();
    List<Attachment> images = getImages(object);

    if (Util.hasContent(images))
    {
      try
      {
        if ((images != null) &&!images.isEmpty())
        {
          LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
          String res = linkBuilder.buildLink(request, "/resources/");
          String lRes = res + "prettyPhoto/";

          resources = new ArrayList<WebResource>();

          ScriptResource jquery = new ScriptResource(20,
                                    res + "jquery/jquery.min.js");

          resources.add(jquery);

          ScriptResource jqueryPrettyPhoto = new ScriptResource(21,
                                               lRes
                                               + "js/jquery.prettyPhoto.js");

          resources.add(jqueryPrettyPhoto);

          LinkResource prettyPhotoCSS = new LinkResource(22);

          prettyPhotoCSS.setRel(LinkResource.REL_STYLESHEET);
          prettyPhotoCSS.setType(LinkResource.TYPE_STYLESHEET);
          prettyPhotoCSS.setHref(lRes + "css/prettyPhoto.css");
          resources.add(prettyPhotoCSS);

          long time = System.nanoTime();

          result.append("<div id=\"gallery_").append(time);
          result.append("\">\n");

          for (int i = 0; i < images.size(); i++)
          {
            Attachment image = images.get(i);

            result.append("<a title=\"");
            result.append(image.getName()).append(
                "\" rel=\"prettyPhoto[group_");
            result.append(time).append("]\" href=\"");
            result.append(linkBase).append("image/").append(image.getId());
            result.append("\">\n").append("<img border=\"0\" alt=\"\" src=\"");
            result.append(linkBase).append("image/").append(image.getId());
            result.append("?type=thumb\" />\n").append("</a>");
          }

          result.append("</div>\n");
          result.append(
              "<script type=\"text/javascript\" charset=\"utf-8\">\n");

          // on document ready
          result.append("$(document).ready(function() {\n");

          // configure gallery
          result.append("$(\"div#gallery_").append(time);
          result.append(" a\").prettyPhoto({\n");
          result.append("theme: '").append(theme).append("'\n");
          result.append("});\n");
          result.append("});\n");
          result.append("</script>\n");
        }
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }

    return result.toString();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  private List<Attachment> getImages(ContentObject object)
  {
    List<Attachment> images = null;

    if (object instanceof Entry)
    {
      images = attachmentDAO.findAllImagesByEntry((Entry) object);
    }
    else if (object instanceof Page)
    {
      images = attachmentDAO.getAllImages((Page) object);
    }

    return images;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Dao
  private AttachmentDAO attachmentDAO;

  /** Field description */
  private List<WebResource> resources;

  /** Field description */
  private String theme = "dark_square";
}
