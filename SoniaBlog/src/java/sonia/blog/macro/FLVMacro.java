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
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.api.macro.ScriptResource;
import sonia.blog.api.macro.WebMacro;
import sonia.blog.api.macro.WebResource;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sdorra
 */
public class FLVMacro extends AbstractBlogMacro implements WebMacro
{

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
    String result = null;

    if (id != null)
    {
      Blog blog = request.getCurrentBlog();
      Attachment attchment = attachmentDAO.get(id);

      if ((attchment != null) && attchment.isBlog(blog))
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

    resources = new ArrayList<WebResource>();

    ScriptResource sr = new ScriptResource(10,
                          playerPath + "flowplayer.min.js");

    resources.add(sr);

    // player block
    result.append("<a id=\"flvplayer_").append(attchment.getId());
    result.append("\" href=\"").append(attachmentLink);
    result.append("\" style=\"display: block; width: ").append(width);
    result.append("px; height: ").append(height).append("px\"></a>");

    // load player
    result.append("<script type=\"text/javascript\">\n");

    // load flowplayer.min.js
    // result.append("addScript(\"").append(playerPath);
    // result.append("flowplayer.min.js\");\n");
    // load flowplayer.swf
    result.append("$f(\"flvplayer_").append(attchment.getId());
    result.append("\", \"").append(playerPath);
    result.append("flowplayer.swf\", {\n");

    // configure player
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
  private List<WebResource> resources;

  /** Field description */
  private Integer width = 480;
}
