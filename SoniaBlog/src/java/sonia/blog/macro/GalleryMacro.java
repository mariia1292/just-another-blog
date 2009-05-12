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
import sonia.blog.api.macro.LinkResource;
import sonia.blog.api.macro.ScriptResource;
import sonia.blog.api.macro.WebMacro;
import sonia.blog.api.macro.WebResource;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class GalleryMacro extends AbstractBlogMacro implements WebMacro
{

  /** Field description */
  private static Logger logger = Logger.getLogger(GalleryMacro.class.getName());

  //~--- get methods ----------------------------------------------------------

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

          resources = new ArrayList<WebResource>();

          ScriptResource jquery = new ScriptResource(20,
                                    res + "jquery/jquery.min.js");

          resources.add(jquery);

          ScriptResource jqueryLightbox = new ScriptResource(21,
                                            lRes + "js/jquery.lightbox-0.5.js");

          resources.add(jqueryLightbox);

          LinkResource lightboxCSS = new LinkResource(22);

          lightboxCSS.setRel(LinkResource.REL_STYLESHEET);
          lightboxCSS.setType(LinkResource.TYPE_STYLESHEET);
          lightboxCSS.setHref(lRes + "css/jquery.lightbox-0.5.css");
          resources.add(lightboxCSS);

          long time = System.nanoTime();

          result.append("<div id=\"gallery_").append(time);
          result.append("\">\n");

          for (int i = 0; i < images.size(); i++)
          {
            Attachment image = images.get(i);

            result.append("<a title=\"");
            result.append(image.getName()).append("\" rel=\"ligthbox[group_");
            result.append(time).append("]\" href=\"");
            result.append(linkBuilder.buildLink(request, image));
            result.append("\">\n").append("<img border=\"0\" alt=\"\" src=\"");
            result.append(linkBuilder.buildLink(request, image));
            result.append("?thumb\" />\n").append("</a>");
          }

          result.append("</div>\n");
          result.append("<script type=\"text/javascript\">\n");

          // on document ready
          result.append("$(document).ready(function() {\n");

          // configure gallery
          result.append("$(\"div#gallery_").append(time);
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

  /** Field description */
  private List<WebResource> resources;
}
