/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webint.flickr;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.util.AbstractBlogMacro;
import sonia.blog.entity.ContentObject;

//~--- JDK imports ------------------------------------------------------------

import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.faces.context.FacesContext;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author sdorra
 */
public class FlickrMacro extends AbstractBlogMacro implements FlickrAPI
{

  /** Field description */
  public static final String NAME = "flickr";

  //~--- methods --------------------------------------------------------------

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
    String restUrl = null;

    if (parameters.containsKey("tags"))
    {
      restUrl = API_URL + "?" + PARAM_METHOD + "=" + METHOD_SEARCH + "&"
                + PARAM_TAGS + "=" + parameters.get("tags");

      if (parameters.containsKey("perPage"))
      {
        restUrl += "&" + PARAM_PERPAGE + "=" + parameters.get("perPage");
      }
    }

    restUrl += "&" + PARAM_APIKEY + "=" + KEY;

    try
    {
      URL url = new URL(restUrl);
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser parser = factory.newSAXParser();
      List<Photo> photos = new ArrayList<Photo>();

      parser.parse(url.openStream(), new SearchHandler(photos));
      result = buildGallery(photos, linkBase, object.getId(),
                            getRequest(facesContext));
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param photos
   * @param linkBase
   * @param id
   * @param request
   *
   * @return
   */
  private String buildGallery(List<Photo> photos, String linkBase, long id,
                              BlogRequest request)
  {
    String result = "";
    String res = linkBase + "resources/lightbox/";

    if (request.getAttribute("sonia.blog.macro.gallery") == null)
    {
      result += "<script type=\"text/javascript\" src=\"" + res
                + "js/jquery.js\"></script>\n";
      result += "<script type=\"text/javascript\" src=\"" + res
                + "js/jquery.lightbox-0.5.js\"></script>\n";
      result += "<link rel=\"stylesheet\" href=\"" + res
                + "css/jquery.lightbox-0.5.css\" "
                + "type=\"text/css\" media=\"screen\"></link>\n";
      request.setAttribute("sonia.blog.macro.gallery", Boolean.TRUE);
    }

    result += "<div id=\"gallery_" + id + "\">\n";

    for (Photo p : photos)
    {
      result += "<a id=\"" + p.getId() + "\" title=\"" + p.getTitle()
                + "\" rel=\"ligthbox[group_" + id + "]\" href=\"" + p.getUrl()
                + "\">\n";
      result += "<img border=\"0\" alt=\"\" src=\"" + p.getThumbnailUrl()
                + "\" />\n";
      result += "</a>";
    }

    result += "</div>\n";
    result += "<script type=\"text/javascript\">\n";
    result += "$(document).ready(function() {\n";
    result += "$(\"div#gallery_" + id + " a\").lightBox({\n";
    result += "imageLoading: '" + res + "images/lightbox-ico-loading.gif',\n";
    result += "imageBtnPrev: '" + res + "images/lightbox-btn-prev.gif',\n";
    result += "imageBtnNext: '" + res + "images/lightbox-btn-next.gif',\n";
    result += "imageBtnClose: '" + res + "images/lightbox-btn-close.gif',\n";
    result += "imageBlank: '" + res + "images/lightbox-blank.gif'\n";
    result += "});\n";
    result += "});\n";
    result += "</script>\n";

    return result;
  }
}
