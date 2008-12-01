/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Entry;

import sonia.macro.Macro;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class GalleryMacro implements Macro
{

  /** Field description */
  private static Logger logger = Logger.getLogger(GalleryMacro.class.getName());

  //~--- methods --------------------------------------------------------------

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

    if (environment != null)
    {
      FacesContext ctx = (FacesContext) environment.get("facesContext");
      Entry entry = (Entry) environment.get("entry");
      BlogRequest request = (BlogRequest) environment.get("request");

      if ((ctx != null) && (entry != null) && (request != null))
      {
        result = createImageGallery(ctx, request, entry);
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param ctx
   * @param request
   * @param entry
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  private String createImageGallery(FacesContext ctx, BlogRequest request,
                                    Entry entry)
  {
    String result = "";
    BlogContext context = BlogContext.getInstance();
    EntityManager em = context.getEntityManager();
    Query q = em.createNamedQuery("Attachment.findImagesFromEntry");

    q.setParameter("entry", entry);

    try
    {
      List<Attachment> images = q.getResultList();

      ctx.getExternalContext().getRequestMap().put("test", "<h1>Hello</h1>");

      if ((images != null) &&!images.isEmpty())
      {
        LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
        Map<String, Object> requestMap =
          ctx.getExternalContext().getRequestMap();
        String res = linkBuilder.buildLink(request, "/resources/lightbox/");

        if (request.getAttribute("sonia.blog.macro.gallery") == null)
        {
          result += "<script type=\"text/javascript\" src=\"" + res
                    + "js/jquery.js\"></script>\n";
          result += "<script type=\"text/javascript\" src=\"" + res
                    + "js/jquery.lightbox-0.5.js\"></script>\n";
          result += "<script type=\"text/javascript\" src=\"" + res
                    + "js/lightbox.js\"></script>\n";
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
        result += "imageBtnPrev: '" + res + "images/lightbox-btn-prev.gif',\n";
        result += "imageBtnNext: '" + res + "images/lightbox-btn-next.gif',\n";
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
    finally
    {
      em.close();
    }

    return result;
  }
}
