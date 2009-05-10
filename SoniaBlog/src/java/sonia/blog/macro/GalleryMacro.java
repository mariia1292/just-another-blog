/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class GalleryMacro extends AbstractBlogMacro
{

  /** Field description */
  private static Logger logger = Logger.getLogger(GalleryMacro.class.getName());

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
          String lRes = res + "lightbox/";

          result.append("<div id=\"gallery_").append(object.getId());
          result.append("\">\n");

          for (int i = 0; i < images.size(); i++)
          {
            Attachment image = images.get(i);

            result.append("<a id=\"image_").append(i).append("\" title=\"");
            result.append(image.getName()).append("\" rel=\"ligthbox[group_");
            result.append(object.getId()).append("]\" href=\"");
            result.append(linkBuilder.buildLink(request, image));
            result.append("\">\n").append("<img border=\"0\" alt=\"\" src=\"");
            result.append(linkBuilder.buildLink(request, image));
            result.append("?thumb\" />\n").append("</a>");
          }

          result.append("</div>\n");
          result.append("<script type=\"text/javascript\">\n");

          // on document ready
          result.append("$(document).ready(function() {\n");

          // load lightbox js
          result.append("addScript(\"").append(lRes);
          result.append("js/jquery.lightbox-0.5.js").append("\");\n");

          // load lightbox css
          result.append("addCSS(\"").append(lRes);
          result.append("css/jquery.lightbox-0.5.css").append("\");\n");

          // configure gallery
          result.append("$(\"div#gallery_").append(object.getId());
          result.append(" a\").lightBox({\n");

          // load icon
          result.append("imageLoading: '").append(lRes);
          result.append("images/lightbox-ico-loading.gif',\n");

          // previous button
          result.append("imageBtnPrev: '").append(lRes);
          result.append("images/lightbox-btn-prev.gif',\n");

          // next button
          result.append("imageBtnNext: '").append(lRes);
          result.append("images/lightbox-btn-next.gif',\n");

          // close button
          result.append("imageBtnClose: '").append(lRes);
          result.append("images/lightbox-btn-close.gif',\n");

          // blank image
          result.append("imageBlank: '").append(lRes);
          result.append("images/lightbox-blank.gif'\n");
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
}
