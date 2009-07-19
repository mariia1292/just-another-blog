/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://kenai.com/projects/jab
 *
 */



package sonia.blog.webint.flickr;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.entity.ContentObject;

//~--- JDK imports ------------------------------------------------------------

import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author Sebastian Sdorra
 */
public class FlickrMacro extends AbstractBlogMacro implements FlickrAPI
{

  /** Field description */
  public static final String NAME = "flickr";

  /** Field description */
  private static Logger logger = Logger.getLogger(FlickrMacro.class.getName());

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getPerPage()
  {
    return perPage;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTags()
  {
    return tags;
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
    String restUrl = null;

    if (tags != null)
    {
      restUrl = API_URL + "?" + PARAM_METHOD + "=" + METHOD_SEARCH + "&"
                + PARAM_TAGS + "=" + tags;

      if (perPage != null)
      {
        restUrl += "&" + PARAM_PERPAGE + "=" + perPage;
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
      result = buildGallery(photos, linkBase, object.getId(), request);
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String perPage;

  /** Field description */
  private String tags;
}
