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
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.api.macro.WebMacro;
import sonia.blog.api.macro.WebResource;
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
    return BlogContext.getInstance().getWebResources().getFancybox();
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
    widget = StringInputWidget.class,
    order = 3
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
    widget = StringInputWidget.class,
    order = 1
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
    widget = StringInputWidget.class,
    order = 2
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
    widget = StringInputWidget.class,
    order = 0
  )
  public void setInclude(String include)
  {
    this.include = include;
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
      long time = System.nanoTime();
      Map<String, Object> parameters = new HashMap<String, Object>();

      parameters.put("linkBase", request.getContextPath());
      parameters.put("id", String.valueOf(time));
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
          String name = image.getName();

          if ((name != null) && name.matches(include))
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
          String description = image.getDescription();

          if ((description != null) && description.matches(descriptionInclude))
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
          String name = image.getName();

          if (name.matches(exclude))
          {
            removeList.add(image);
          }
        }
      }

      if (Util.hasContent(descriptionExclude))
      {
        for (Attachment image : images)
        {
          String description = image.getName();

          if ((description != null) && description.matches(descriptionExclude))
          {
            removeList.add(image);
          }
        }
      }

      if (Util.hasContent(removeList))
      {
        images.removeAll(removeList);
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
}
