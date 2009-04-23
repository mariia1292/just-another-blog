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
import sonia.blog.api.util.AbstractBlogMacro;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;
import sonia.blog.util.BlogUtil;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

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
    String result = "";
    List<Attachment> images = getImages(object);

    if (Util.hasContent(images))
    {
      try
      {
        facesContext.getExternalContext().getRequestMap().put("test",
                "<h1>Hello</h1>");

        if ((images != null) &&!images.isEmpty())
        {
          LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
          Map<String, Object> requestMap =
            facesContext.getExternalContext().getRequestMap();
          BlogRequest request =
            BlogUtil.getBlogRequest(
                facesContext.getExternalContext().getRequest());
          String res = linkBuilder.buildLink(request, "/resources/lightbox/");

          if (!requestMap.containsKey("sonia.blog.macro.gallery"))
          {
            result += "<script type=\"text/javascript\" src=\"" + res
                      + "js/jquery.js\"></script>\n";
            result += "<script type=\"text/javascript\" src=\"" + res
                      + "js/jquery.lightbox-0.5.js\"></script>\n";
            result += "<link rel=\"stylesheet\" href=\"" + res
                      + "css/jquery.lightbox-0.5.css\" "
                      + "type=\"text/css\" media=\"screen\"></link>\n";
            requestMap.put("sonia.blog.macro.gallery", Boolean.TRUE);
          }

          result += "<div id=\"gallery_" + object.getId() + "\">\n";

          for (int i = 0; i < images.size(); i++)
          {
            Attachment image = images.get(i);

            result += "<a id=\"image_" + i + "\" title=\"" + image.getName()
                      + "\" rel=\"ligthbox[group_" + object.getId()
                      + "]\" href=\"" + linkBuilder.buildLink(request, image)
                      + "\">\n";
            result += "<img border=\"0\" alt=\"\" src=\""
                      + linkBuilder.buildLink(request, image) + "?thumb\" />\n";
            result += "</a>";
          }

          result += "</div>\n";
          result += "<script type=\"text/javascript\">\n";
          result += "$(document).ready(function() {\n";
          result += "$(\"div#gallery_" + object.getId() + " a\").lightBox({\n";
          result += "imageLoading: '" + res
                    + "images/lightbox-ico-loading.gif',\n";
          result += "imageBtnPrev: '" + res
                    + "images/lightbox-btn-prev.gif',\n";
          result += "imageBtnNext: '" + res
                    + "images/lightbox-btn-next.gif',\n";
          result += "imageBtnClose: '" + res
                    + "images/lightbox-btn-close.gif',\n";
          result += "imageBlank: '" + res + "images/lightbox-blank.gif'\n";
          result += "});\n";
          result += "});\n";
          result += "</script>\n";
        }
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
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
