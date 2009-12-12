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



package sonia.blog.util;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogSession;
import sonia.blog.api.app.Constants;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;

import sonia.util.Util;
import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

import java.text.MessageFormat;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author sdorra
 */
public class TrackbackSender
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(TrackbackSender.class.getName());

  /** Field description */
  private static final Pattern RDF_TRACKBACK_REGEX =
    Pattern.compile("(?s)(?i)(<rdf:RDF .*</rdf:RDF>)");

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param request
   * @param entry
   */
  public TrackbackSender(BlogRequest request, Entry entry)
  {
    this.session = request.getBlogSession();
    this.address = request.getRemoteAddr();
    this.locale = request.getLocale();
    this.entry = entry;
  }

  /**
   * Constructs ...
   *
   *
   *
   * @param session
   * @param address
   * @param locale
   * @param entry
   */
  public TrackbackSender(BlogSession session, String address, Locale locale,
                         Entry entry)
  {
    this.session = session;
    this.address = address;
    this.locale = locale;
    this.entry = entry;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param url
   *
   * @throws IOException
   */
  public void sendPing(String url) throws IOException
  {
    sendPing(new URL(url));
  }

  /**
   * Method description
   *
   *
   * @param target
   *
   * @throws IOException
   */
  public void sendPing(URL target) throws IOException
  {
    if (logger.isLoggable(Level.FINE))
    {
      StringBuffer log = new StringBuffer();

      log.append("send ping to ").append(target);
      logger.fine(log.toString());
    }

    try
    {
      sendPing(target, null, 0);
    }
    catch (SAXException ex)
    {
      logger.log(Level.WARNING, null, ex);

      throw new IOException(ex.getMessage());
    }
    catch (ParserConfigurationException ex)
    {
      logger.log(Level.WARNING, null, ex);

      throw new IOException(ex.getMessage());
    }
  }

  /**
   * Method description
   *
   *
   *
   * @param message
   */
  private void addTrackbackComment(String message)
  {
    Comment comment = new Comment(Comment.Type.TRACKBACK_SEND);

    comment.setEntry(entry);
    comment.setContent(message);
    comment.setAuthor(session.getUser());
    comment.setAuthorAddress(address);
    BlogContext.getDAOFactory().getCommentDAO().add(session, comment);
  }

  /**
   * Method description
   *
   *
   * @param conn
   *
   * @return
   *
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  private boolean checkResponse(URLConnection conn)
          throws IOException, SAXException, ParserConfigurationException
  {
    boolean result = false;
    Document doc = XmlUtil.buildDocument(conn.getInputStream());
    NodeList list = doc.getElementsByTagName("error");

    if (XmlUtil.hasContent(list))
    {
      Node node = list.item(0);
      String errorCode = node.getTextContent();

      if (errorCode.trim().equals("0"))
      {
        result = true;
      }
      else
      {
        StringBuffer log = new StringBuffer();

        log.append("trackback ").append(conn.getURL());
        log.append(" returned errorcode ").append(errorCode);
        logger.warning(log.toString());
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param rdfContent
   *
   *
   * @return
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  private RDFContent parseRdfContent(String rdfContent)
          throws IOException, SAXException, ParserConfigurationException
  {
    RDFContent content = null;
    Document doc =
      XmlUtil.buildDocument(new ByteArrayInputStream(rdfContent.getBytes()));
    NodeList nodeList = doc.getElementsByTagName("rdf:Description");

    if (XmlUtil.hasContent(nodeList))
    {
      for (int i = 0; i < nodeList.getLength(); i++)
      {
        content = new RDFContent();

        Node node = nodeList.item(i);
        NamedNodeMap attributes = node.getAttributes();
        String ping = XmlUtil.getAttributeValue(attributes, "trackback:ping");

        content.url = new URL(ping);

        String identifier = XmlUtil.getAttributeValue(attributes,
                              "dc:identifier");

        if (Util.hasContent(identifier))
        {
          identifier = URLDecoder.decode(identifier, Constants.ENCODING);
        }

        String title = XmlUtil.getAttributeValue(attributes, "dc:title");

        identifier = Util.hasContent(identifier)
                     ? identifier
                     : ping;
        title = Util.hasContent(title)
                ? title
                : identifier;
        content.identifier = identifier;
        content.title = title;

        break;
      }
    }

    return content;
  }

  /**
   * Method description
   *
   *
   *
   *
   * @param url
   * @param message
   * @param counter
   *
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  private void sendPing(URL url, String message, int counter)
          throws IOException, SAXException, ParserConfigurationException
  {
    if (counter > 3)
    {
      throw new IOException("seems to be a loop");
    }

    Blog blog = entry.getBlog();
    URLConnection conn = url.openConnection();

    conn.setDoOutput(true);

    BufferedWriter writer = null;

    try
    {
      writer =
        new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

      String title = URLEncoder.encode(entry.getTitle(), Constants.ENCODING);

      writer.write("title=");
      writer.write(title);

      StringBuffer link = new StringBuffer();

      link.append("/list/").append(entry.getId()).append(".jab");

      String urlString =
        BlogContext.getInstance().getLinkBuilder().buildLink(blog,
          link.toString());

      writer.write("&url=");
      writer.write(urlString);

      String content = getContent(entry);

      writer.write("&excerpt=");
      writer.write(content);

      String blogName = URLEncoder.encode(blog.getTitle(), Constants.ENCODING);

      writer.write("&blog_name=");
      writer.write(blogName);
    }
    finally
    {
      if (writer != null)
      {
        writer.close();
      }
    }

    String contentType = conn.getContentType();

    if (contentType.contains("xml"))
    {
      if (checkResponse(conn))
      {
        if (message == null)
        {
          String u = url.toString();

          message = getMessage(locale, u, u);
        }

        addTrackbackComment(message);
      }
      else
      {
        throw new IOException("trackback return a failure");
      }
    }
    else
    {
      RDFContent content = getRDFContent(Util.getContent(conn));

      if (content != null)
      {
        counter++;

        if (logger.isLoggable(Level.FINER))
        {
          StringBuffer msg = new StringBuffer("found new trackback url ");

          msg.append(content.url).append(", counter ").append(counter);
          logger.finer(msg.toString());
        }

        sendPing(content.url,
                 getMessage(locale, content.title, content.identifier),
                 counter);
      }
      else if (logger.isLoggable(Level.WARNING))
      {
        logger.warning("no rdfcontent found");
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param entry
   *
   * @return
   *
   * @throws UnsupportedEncodingException
   */
  private String getContent(Entry entry) throws UnsupportedEncodingException
  {
    String content = entry.getTeaser();

    if (Util.isBlank(content))
    {
      content = entry.getContent();
    }

    content = Util.extractHTMLText(content);

    if (content.length() > 255)
    {
      content = content.substring(0, 255);
    }

    content = URLEncoder.encode(content, Constants.ENCODING);

    return content;
  }

  /**
   * Method description
   *
   *
   * @param locale
   * @param label
   * @param value
   *
   * @return
   */
  private String getMessage(Locale locale, String label, String value)
  {
    ResourceBundle bundle =
      ResourceBundle.getBundle("sonia.blog.resources.message", locale);

    return MessageFormat.format(bundle.getString("sendTrackbackExcerpt"),
                                label, value);
  }

  /**
   * Method description
   *
   *
   * @param content
   *
   * @return
   *
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  private RDFContent getRDFContent(String content)
          throws IOException, SAXException, ParserConfigurationException
  {
    RDFContent result = null;

    if (Util.hasContent(content))
    {
      Matcher m = RDF_TRACKBACK_REGEX.matcher(content);

      while (m.find())
      {
        String rdfContent = m.group(1);

        if (Util.hasContent(rdfContent))
        {
          result = parseRdfContent(rdfContent);
        }
      }
    }

    return result;
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/12/12
   * @author         Enter your name here...
   */
  private static class RDFContent
  {

    /** Field description */
    private String identifier;

    /** Field description */
    private String title;

    /** Field description */
    private URL url;
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String address;

  /** Field description */
  private Entry entry;

  /** Field description */
  private Locale locale;

  /** Field description */
  private BlogSession session;
}
