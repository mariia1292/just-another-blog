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
import sonia.blog.api.macro.browse.StringInputWidget;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;
import sonia.blog.util.BlogUtil;

import sonia.macro.Macro;
import sonia.macro.browse.MacroInfo;
import sonia.macro.browse.MacroInfoParameter;

//~--- JDK imports ------------------------------------------------------------

import java.text.DateFormat;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Sebastian Sdorra
 */
@MacroInfo(
  name = "attachments",
  displayName = "macro.attachments.displayName",
  description = "macro.attachments.description",
  resourceBundle = "sonia.blog.resources.label"
)
public class AttachmentMacro implements Macro
{

  /**
   * Method description
   *
   *
   * @param environment
   * @param body
   *
   * @return
   */
  public String doBody(Map<String, ?> environment, String body)
  {
    String result = "";
    BlogRequest request = (BlogRequest) environment.get("request");
    Blog blog = (Blog) environment.get("blog");
    ContentObject object = (ContentObject) environment.get("object");

    if ((blog != null) && (object != null))
    {
      List<Attachment> attachments = BlogUtil.getAttachments(object);

      if ((attachments != null) &&!attachments.isEmpty())
      {
        LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
        DateFormat dateFormat = blog.getDateFormatter();

        result += "<table";

        if (style != null)
        {
          result += " style=\"" + style + "\"";
        }

        if (styleClass != null)
        {
          result += " class=\"" + styleClass + "\"";
        }

        result += ">";
        result += "<thead>\n";
        result += "<th>Name</th>\n";
        result += "<th>Description</th>\n";
        result += "<th>Size</th>\n";
        result += "<th>Date</th>\n";
        result += "</thead>\n";
        result += "<tbody>\n";

        for (Attachment attachment : attachments)
        {
          result += "<tr>\n";
          result += "<td>";
          result += "<a href=\"" + linkBuilder.buildLink(request, attachment)
                    + "?orginal\" target=\"_blank\">";
          result += attachment.getName();
          result += "</a>";
          result += "</td>\n";

          String description = attachment.getDescription();

          if (description == null)
          {
            description = "";
          }

          result += "<td>" + description + "</td>\n";
          result += "<td>" + attachment.getSize() + "</td>\n";
          result += "<td>" + dateFormat.format(attachment.getCreationDate())
                    + "</td>\n";
          result += "</tr>\n";
        }

        result += "</tbody>\n";
        result += "</table>\n";
      }
    }

    return result;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param style
   */
  @MacroInfoParameter(
    displayName = "macro.attachments.style.displayName",
    description = "macro.attachments.style.description",
    widget = StringInputWidget.class
  )
  public void setStyle(String style)
  {
    this.style = style;
  }

  /**
   * Method description
   *
   *
   * @param styleClass
   */
  @MacroInfoParameter(
    displayName = "macro.attachments.styleClass.displayName",
    description = "macro.attachments.styleClass.description",
    widget = StringInputWidget.class
  )
  public void setStyleClass(String styleClass)
  {
    this.styleClass = styleClass;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String style;

  /** Field description */
  private String styleClass;
}