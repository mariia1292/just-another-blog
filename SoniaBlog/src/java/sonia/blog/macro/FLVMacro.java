/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.util.AbstractBlogMacro;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class FLVMacro extends AbstractBlogMacro
{

  /**
   * Method description
   *
   *
   * @param facesContext
   * @param linkBase
   * @param object
   * @param body
   * @param parameters
   *
   * @return
   */
  @Override
  protected String excecute(FacesContext facesContext, String linkBase,
                            ContentObject object, String body,
                            Map<String, String> parameters)
  {
    String result = null;

    if (isEntry(object))
    {
      if (parameters.containsKey("id"))
      {
        String idString = parameters.get("id");

        try
        {
          long id = Long.parseLong(idString);
          BlogRequest request = getRequest(facesContext);
          Blog blog = request.getCurrentBlog();
          Attachment attchment = findAttachment(blog, id);

          if (attchment != null)
          {
            int width = 480;
            int height = 360;

            if (parameters.containsKey("width"))
            {
              width = Integer.parseInt(parameters.get("width"));
            }

            if (parameters.containsKey("height"))
            {
              height = Integer.parseInt(parameters.get("height"));
            }

            result = renderPlayer(request, attchment, linkBase, width, height);
          }
          else
          {
            result = "-- cant find attachment --";
          }
        }
        catch (NumberFormatException ex)
        {
          result = "-- id, width or height is no number --";
        }
      }
      else
      {
        result = "-- id parameter not found --";
      }
    }
    else
    {
      result = "-- object is not an instance of Entry --";
    }

    return result;
  }

  /**
   * Method description
   *
   * @param blog
   * @param id
   *
   * @return
   */
  private Attachment findAttachment(Blog blog, long id)
  {
    return BlogContext.getDAOFactory().getAttachmentDAO().findByBlogAndId(blog,
            id);
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
    String attachmentLink =
      BlogContext.getInstance().getLinkBuilder().buildLink(request, attchment);
    String result = "";

    if (request.getAttribute("sonia.blog.flvplayer") == null)
    {
      result = "<script type=\"text/javascript\" src=\"" + playerPath
               + "flowplayer-3.0.1.min.js\"></script>";
      request.setAttribute("sonia.blog.flvplayer", Boolean.TRUE);
    }

    result += "<a id=\"flvplayer_" + attchment.getId() + "\" href=\""
              + attachmentLink + "\" style=\"display: block; width: " + width
              + "px; height: " + height + "px\"></a>";
    result += "<script type=\"text/javascript\">\n";

    /*
     * result += "flowplayer(\"flvplayer_" + attchment.getId() + "\", \""
     *         + playerPath + "flowplayer-3.0.1.swf\");\n";
     */
    result += "$f(\"flvplayer_" + attchment.getId() + "\", \"" + playerPath
              + "flowplayer-3.0.1.swf\", {\n";
    result += "clip: {\n";
    result += "url: '" + attachmentLink + "',\n";
    result += "autoPlay: false\n";
    result += "}\n";
    result += "});\n";
    result += "</script>\n";

    return result;
  }
}
