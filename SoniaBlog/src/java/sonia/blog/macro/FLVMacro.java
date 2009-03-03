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
   * @param height
   */
  public void setHeight(String height)
  {
    this.height = height;
  }

  /**
   * Method description
   *
   *
   * @param id
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Method description
   *
   *
   * @param width
   */
  public void setWidth(String width)
  {
    this.width = width;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param facesContext
   * @param linkBase
   * @param object
   * @param body
   *
   * @return
   */
  @Override
  protected String doBody(FacesContext facesContext, String linkBase,
                          ContentObject object, String body)
  {
    String result = null;

    if (isEntry(object))
    {
      if (id != null)
      {
        try
        {
          BlogRequest request = getRequest(facesContext);
          Blog blog = request.getCurrentBlog();
          Attachment attchment = findAttachment(blog, Long.parseLong(id));

          if (attchment != null)
          {
            int w = 480;
            int h = 360;

            if (width != null)
            {
              w = Integer.parseInt(width);
            }

            if (height != null)
            {
              h = Integer.parseInt(height);
            }

            result = renderPlayer(request, attchment, linkBase, w, h);
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String height;

  /** Field description */
  private String id;

  /** Field description */
  private String width;
}
