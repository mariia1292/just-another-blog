/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Context;
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.link.LinkBuilder;
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
  public void setWidth(Integer width)
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
        BlogRequest request = getRequest(facesContext);
        Blog blog = request.getCurrentBlog();
        Attachment attchment = attachmentDAO.findByBlogAndId(blog, id);

        if (attchment != null)
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
    StringBuffer result = new StringBuffer();

    if (request.getAttribute("sonia.blog.flvplayer") == null)
    {
      result.append("<script type=\"text/javascript\" src=\"");
      result.append(playerPath);
      result.append("flowplayer-3.0.1.min.js\"></script>");
      request.setAttribute("sonia.blog.flvplayer", Boolean.TRUE);
    }

    result.append("<a id=\"flvplayer_").append(attchment.getId());
    result.append("\" href=\"").append(attachmentLink);
    result.append("\" style=\"display: block; width: ").append(width);
    result.append("px; height: ").append(height).append("px\"></a>");
    result.append("<script type=\"text/javascript\">\n");
    result.append("$f(\"flvplayer_").append(+attchment.getId());
    result.append("\", \"").append(playerPath);
    result.append("flowplayer-3.0.1.swf\", {\n");
    result.append("clip: {\n");
    result.append("url: '").append(attachmentLink).append("',\n");
    result.append("autoPlay: false\n");
    result.append("}\n");
    result.append("});\n");
    result.append("</script>\n");

    return result.toString();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Dao
  private AttachmentDAO attachmentDAO;

  /** Field description */
  private Integer height = 360;

  /** Field description */
  private Long id;

  /** Field description */
  @Context
  private LinkBuilder linkBuilder;

  /** Field description */
  private Integer width = 480;
}
