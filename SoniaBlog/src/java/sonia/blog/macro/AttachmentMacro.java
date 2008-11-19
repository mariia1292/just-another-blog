/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;

import sonia.macro.Macro;

//~--- JDK imports ------------------------------------------------------------

import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Map;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;

/**
 *
 * @author sdorra
 */
public class AttachmentMacro implements Macro
{

  /**
   * Method description
   *
   *
   * @param environment
   * @param body
   * @param parameters
   *
   * @return
   */
  public String excecute(Map<String, ?> environment, String body,
                         Map<String, String> parameters)
  {
    String result = "";
    BlogRequest request = (BlogRequest) environment.get("request");
    Blog blog = (Blog) environment.get("blog");
    Entry entry = (Entry) environment.get("entry");

    if ((blog != null) && (entry != null))
    {
      List<Attachment> attachments = entry.getAttachments();

      if ((attachments != null) &&!attachments.isEmpty())
      {
        LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
        SimpleDateFormat sdf = null;

        if (blog.getDateFormat() != null)
        {
          sdf = new SimpleDateFormat(blog.getDateFormat());
        }
        else
        {
          sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }

        result += "<table";

        String style = parameters.get("style");

        if (style != null)
        {
          result += " style=\"" + style + "\"";
        }

        String clazz = parameters.get("class");

        if (clazz != null)
        {
          result += " class=\"" + clazz + "\"";
        }

        result += ">";
        result += "<thead>\n";
        result += "<th>Name</th>\n";
        result += "<th>MimeType</th>\n";
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
          result += "<td>" + attachment.getMimeType() + "</td>\n";
          result += "<td>" + attachment.getSize() + "</td>\n";
          result += "<td>" + sdf.format(attachment.getCreationDate())
                    + "</td>\n";
          result += "</tr>\n";
        }

        result += "</tbody>\n";
        result += "</table>\n";
      }
    }

    return result;
  }
}
