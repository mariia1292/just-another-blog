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



package sonia.blog.webint;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.api.mapping.MappingConfig;
import sonia.blog.webint.flickr.FlickrAPI;

import sonia.util.Util;
import sonia.util.XmlUtil;

import sonia.web.io.JSONWriter;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author Sebastian Sdorra
 */
@MappingConfig(regex = "^/gateway/([a-zA-Z0-9]+)/([a-zA-Z0-9]+)$")
public class GatewayMapping extends FinalMapping implements FlickrAPI
{

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void handleFinalMapping(BlogRequest request, BlogResponse response,
                                    String[] param)
          throws IOException, ServletException
  {
    if ((param != null) && (param.length > 1))
    {
      String provider = param[0];
      String method = param[1];

      if ("flickr".equals(provider))
      {
        handleFlickrRequest(request, response, method);
      }
      else
      {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param method
   *
   * @throws IOException
   * @throws ServletException
   */
  private void handleFlickrRequest(BlogRequest request, BlogResponse response,
                                   String method)
          throws IOException, ServletException
  {
    if ("user".equals(method))
    {
      String nsid = getNsid(request);

      if (Util.hasContent(nsid))
      {
        handleFlickrUser(request, response, nsid);
      }
      else
      {
        throw new ServletException("no nsid");
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param nsid
   *
   * @throws IOException
   */
  private void handleFlickrUser(BlogRequest request, BlogResponse response,
                                String nsid)
          throws IOException
  {
    StringBuffer url = new StringBuffer(getBaseURL(METHOD_USERPUBLICPHOTOS));

    url.append("&").append(PARAM_NSID).append("=").append(nsid);

    String perPage = request.getParameter("perPage");

    if (Util.hasContent(perPage))
    {
      url.append("&per_page=").append(perPage);
    }

    Node photos = getResponseNode(url.toString(), "photos");

    if (photos != null)
    {
      response.setContentType("application/x-javascript");

      JSONWriter writer = new JSONWriter(response.getWriter());

      writer.startArray();

      try
      {
        NodeList children = photos.getChildNodes();
        int size = children.getLength();
        boolean first = true;

        for (int i = 0; i < size; i++)
        {
          Node child = children.item(i);

          if (child.getNodeName().equals("photo"))
          {
            if (!first)
            {
              writer.getWriter().append(",");
            }
            else
            {
              first = false;
            }

            writePhoto(writer, child);
          }
        }

        writer.endArray(true);
      }
      finally
      {
        writer.close();
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param writer
   * @param node
   *
   * @throws IOException
   */
  private void writePhoto(JSONWriter writer, Node node) throws IOException
  {
    NamedNodeMap attributes = node.getAttributes();
    String id = XmlUtil.getAttributeValue(attributes, "id");
    String secret = XmlUtil.getAttributeValue(attributes, "secret");
    String server = XmlUtil.getAttributeValue(attributes, "server");
    String farm = XmlUtil.getAttributeValue(attributes, "farm");
    String title = XmlUtil.getAttributeValue(attributes, "title");
    String thumbnail = MessageFormat.format(PHOTO_URL, farm, server, id,
                         secret, SIZE_THUMBNAIL);
    String picture = MessageFormat.format(PHOTO_URL, farm, server, id, secret,
                       SIZE_MIDDLE);

    writer.startObject();
    writer.write("title", title, false);
    writer.write("thumbnail", thumbnail, false);
    writer.write("picture", picture, true);
    writer.endObject(true);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param method
   *
   * @return
   */
  private String getBaseURL(String method)
  {
    StringBuffer url = new StringBuffer(API_URL);

    url.append("?").append(PARAM_APIKEY).append("=").append(KEY).append("&");

    return url.append(PARAM_METHOD).append("=").append(method).toString();
  }

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   *
   * @throws IOException
   */
  private String getNsid(BlogRequest request) throws IOException
  {
    String nsid = request.getParameter("nsid");

    if (Util.isBlank(nsid))
    {
      String username = request.getParameter("username");

      if (Util.hasContent(username))
      {
        nsid = getNsidByUsername(username);
      }
      else
      {
        String email = request.getParameter("email");

        if (Util.hasContent(email))
        {
          nsid = getNsidByEmail(email);
        }
      }
    }

    return nsid;
  }

  /**
   * Method description
   *
   *
   * @param url
   *
   * @return
   *
   * @throws IOException
   */
  private String getNsid(String url) throws IOException
  {
    String nsid = null;
    Node node = getResponseNode(url, "user");

    if (node != null)
    {
      nsid = XmlUtil.getAttributeValue(node, "nsid");
    }

    return nsid;
  }

  /**
   * Method description
   *
   *
   * @param email
   *
   * @return
   *
   * @throws IOException
   */
  private String getNsidByEmail(String email) throws IOException
  {
    StringBuffer url = new StringBuffer(getBaseURL(METHOD_NSIDBYEMAIL));

    url.append("&").append(PARAM_EMAIL).append("=").append(email);

    return getNsid(url.toString());
  }

  /**
   * Method description
   *
   *
   * @param username
   *
   * @return
   *
   * @throws IOException
   */
  private String getNsidByUsername(String username) throws IOException
  {
    StringBuffer url = new StringBuffer(getBaseURL(METHOD_NSIDBYUSERNAME));

    url.append("&").append(PARAM_USERNAME).append("=").append(username);

    return getNsid(url.toString());
  }

  /**
   * Method description
   *
   *
   * @param urlString
   * @param childName
   *
   * @return
   *
   * @throws IOException
   */
  private Node getResponseNode(String urlString, String childName)
          throws IOException
  {
    Node node = null;
    URL url = new URL(urlString);
    InputStream in = url.openStream();

    try
    {
      Document doc = XmlUtil.buildDocument(in);
      Element root = doc.getDocumentElement();
      String status = root.getAttribute("stat");

      if ("ok".equalsIgnoreCase(status))
      {
        NodeList children = root.getChildNodes();

        if (XmlUtil.hasContent(children))
        {
          for (int i = 0; i < children.getLength(); i++)
          {
            Node child = children.item(i);

            if (childName.equals(child.getNodeName()))
            {
              node = child;

              break;
            }
          }
        }
      }
      else
      {
        throw new IOException("reponse error");
      }
    }
    catch (SAXException ex)
    {
      throw new IOException(ex.getLocalizedMessage());
    }
    catch (ParserConfigurationException ex)
    {
      throw new IOException(ex.getLocalizedMessage());
    }
    finally
    {
      if (in != null)
      {
        in.close();
      }
    }

    return node;
  }
}
