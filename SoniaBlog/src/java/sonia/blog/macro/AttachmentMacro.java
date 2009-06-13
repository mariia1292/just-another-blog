/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
