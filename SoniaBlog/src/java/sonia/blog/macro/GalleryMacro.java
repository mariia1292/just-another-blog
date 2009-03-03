/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.util.AbstractBlogMacro;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.util.BlogUtil;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class GalleryMacro extends AbstractBlogMacro
{

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

    if (object instanceof Entry)
    {
      Entry entry = (Entry) object;

      try
      {
        List<Attachment> images =
          BlogContext.getDAOFactory().getAttachmentDAO().findAllImagesByEntry(
              entry);

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

          result += "<div id=\"gallery_" + entry.getId() + "\">\n";

          for (int i = 0; i < images.size(); i++)
          {
            Attachment image = images.get(i);

            result += "<a id=\"image_" + i + "\" title=\"" + image.getName()
                      + "\" rel=\"ligthbox[group_" + entry.getId()
                      + "]\" href=\"" + linkBuilder.buildLink(request, image)
                      + "\">\n";
            result += "<img border=\"0\" alt=\"\" src=\""
                      + linkBuilder.buildLink(request, image) + "?thumb\" />\n";
            result += "</a>";
          }

          result += "</div>\n";
          result += "<script type=\"text/javascript\">\n";
          result += "$(document).ready(function() {\n";
          result += "$(\"div#gallery_" + entry.getId() + " a\").lightBox({\n";
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
}
