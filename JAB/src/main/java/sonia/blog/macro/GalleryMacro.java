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
import sonia.blog.api.macro.browse.StringInputWidget;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;

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
  name = "gallery",
  displayName = "macro.gallery.displayName",
  description = "macro.gallery.description",
  resourceBundle = "sonia.blog.resources.label"
)
public class GalleryMacro extends AbstractBlogMacro implements WebMacro
{

  /** Field description */
  private static final String TEMPLATE =
    "sonia/blog/macro/template/gallery.html";

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
   * @param descriptionExclude
   */
  @MacroInfoParameter(
    displayName = "macro.gallery.descriptionExclude.displayName",
    description = "macro.gallery.descriptionExclude.description",
    widget = StringInputWidget.class
  )
  public void setDescriptionExclude(String descriptionExclude)
  {
    this.descriptionExclude = descriptionExclude;
  }

  /**
   * Method description
   *
   *
   * @param descriptionInclude
   */
  @MacroInfoParameter(
    displayName = "macro.gallery.descriptionInclude.displayName",
    description = "macro.gallery.descriptionInclude.description",
    widget = StringInputWidget.class
  )
  public void setDescriptionInclude(String descriptionInclude)
  {
    this.descriptionInclude = descriptionInclude;
  }

  /**
   * Method description
   *
   *
   * @param exclude
   */
  @MacroInfoParameter(
    displayName = "macro.gallery.exclude.displayName",
    description = "macro.gallery.exclude.description",
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
   * @param include
   */
  @MacroInfoParameter(
    displayName = "macro.gallery.include.displayName",
    description = "macro.gallery.include.description",
    widget = StringInputWidget.class
  )
  public void setInclude(String include)
  {
    this.include = include;
  }

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
    String result = "";
    List<Attachment> images = getImages(object);

    if (Util.hasContent(images))
    {
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

      long time = System.nanoTime();
      Map<String, Object> parameters = new HashMap<String, Object>();

      parameters.put("linkBase", request.getContextPath());
      parameters.put("id", String.valueOf(time));
      parameters.put("theme", theme);
      parameters.put("images", images);
      result = parseTemplate(parameters, TEMPLATE);
    }

    return result;
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
      images = attachmentDAO.getAllImages((Entry) object);
    }
    else if (object instanceof Page)
    {
      images = attachmentDAO.getAllImages((Page) object);
    }

    if (Util.hasContent(images))
    {
      List<Attachment> includeList = null;

      if (Util.hasContent(include))
      {
        includeList = new ArrayList<Attachment>();

        for (Attachment image : images)
        {
          if (include.matches(image.getName()))
          {
            includeList.add(image);
          }
        }
      }

      if (Util.hasContent(descriptionInclude))
      {
        if (includeList == null)
        {
          includeList = new ArrayList<Attachment>();
        }

        for (Attachment image : images)
        {
          if (descriptionInclude.matches(image.getDescription()))
          {
            includeList.add(image);
          }
        }
      }

      if (includeList != null)
      {
        images = includeList;
      }

      List<Attachment> removeList = new ArrayList<Attachment>();

      if (Util.hasContent(exclude))
      {
        for (Attachment image : images)
        {
          if (exclude.matches(image.getName()))
          {
            removeList.add(image);
          }
        }
      }

      if (Util.hasContent(descriptionExclude))
      {
        for (Attachment image : images)
        {
          if (descriptionExclude.matches(image.getName()))
          {
            removeList.add(image);
          }
        }
      }
    }

    return images;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Dao
  private AttachmentDAO attachmentDAO;

  /** Field description */
  private String descriptionExclude;

  /** Field description */
  private String descriptionInclude;

  /** Field description */
  private String exclude;

  /** Field description */
  private String include;

  /** Field description */
  private List<WebResource> resources;

  /** Field description */
  private String theme = "dark_square";
}
