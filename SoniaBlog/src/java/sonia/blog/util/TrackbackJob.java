/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.util;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogJob;
import sonia.blog.api.dao.TrackbackDAO;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Trackback;

import sonia.jobqueue.JobException;

import sonia.util.Util;
import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.List;
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
public class TrackbackJob implements BlogJob
{

  /** Field description */
  private static final Pattern EXTERNAL_REGEX =
    Pattern.compile("(?s)(?i)(<rdf:RDF .*</rdf:RDF>)");

  /** Field description */
  private static final Pattern INTERNAL_PATTERN =
    Pattern.compile("(?m)(?i)<a.*href=[\"'](http[^\"']*)[\"'].*>.*</a>");

  /** Field description */
  private static final long serialVersionUID = -3051850801915999862L;

  /** Field description */
  private static Logger logger = Logger.getLogger(TrackbackJob.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param entry
   * @param bundle
   */
  public TrackbackJob(Entry entry, ResourceBundle bundle)
  {
    this.entry = entry;
    this.bundle = bundle;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws JobException
   */
  public void excecute() throws JobException
  {
    String content = entry.getContent();

    if (Util.hasContent(content))
    {
      List<URL> urls = buildUrls(content);

      for (URL url : urls)
      {
        parse(url);
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Blog getBlog()
  {
    return entry.getBlog();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDescription()
  {
    return "trackback job for " + entry.getId();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return TrackbackJob.class.getName();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param content
   *
   * @return
   */
  private List<URL> buildUrls(String content)
  {
    List<URL> urls = new ArrayList<URL>();
    Matcher m = INTERNAL_PATTERN.matcher(content);

    while (m.find())
    {
      String href = m.group(1);

      try
      {
        if (Util.hasContent(href))
        {
          urls.add(new URL(href));
        }
      }
      catch (MalformedURLException ex)
      {
        logger.log(Level.FINEST, null, ex);
      }
    }

    return urls;
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
   * @param url
   */
  private void parse(URL url)
  {
    try
    {
      String content = Util.getContent(url);

      if (Util.hasContent(content))
      {
        Matcher m = EXTERNAL_REGEX.matcher(content);

        while (m.find())
        {
          String rdfContent = m.group(1);

          if (Util.hasContent(rdfContent))
          {
            parseRdfContent(rdfContent);
          }
        }
      }
    }
    catch (IOException ex)
    {
      logger.log(Level.FINEST, null, ex);
    }
  }

  /**
   * Method description
   *
   *
   * @param rdfContent
   */
  private void parseRdfContent(String rdfContent)
  {
    try
    {
      Document doc =
        XmlUtil.buildDocument(new ByteArrayInputStream(rdfContent.getBytes()));
      NodeList nodeList = doc.getElementsByTagName("rdf:Description");

      if (XmlUtil.hasContent(nodeList))
      {
        for (int i = 0; i < nodeList.getLength(); i++)
        {
          Node node = nodeList.item(i);
          NamedNodeMap attributes = node.getAttributes();
          String ping = XmlUtil.getAttributeValue(attributes, "trackback:ping");
          URL url = new URL(ping);
          TrackbackDAO trackbackDAO =
            BlogContext.getDAOFactory().getTrackbackDAO();

          if (trackbackDAO.count(entry, Trackback.TYPE_SEND, ping) == 0)
          {
            String identifier = XmlUtil.getAttributeValue(attributes,
                                  "dc:identifier");
            String title = XmlUtil.getAttributeValue(attributes, "dc:title");

            if (sendPing(url))
            {
              Trackback trackback = new Trackback(Trackback.TYPE_SEND, ping);

              trackback.setEntry(entry);

              String excerpt = bundle.getString("sendTrackbackExcerpt");

              identifier = Util.hasContent(identifier)
                           ? identifier
                           : ping;
              title = Util.hasContent(title)
                      ? title
                      : identifier;
              trackback.setTitle(title);
              excerpt = MessageFormat.format(excerpt, title, identifier);
              trackback.setExcerpt(excerpt);

              if (BlogContext.getDAOFactory().getTrackbackDAO().add(trackback))
              {
                if (logger.isLoggable(Level.INFO))
                {
                  StringBuffer log = new StringBuffer();

                  log.append("trackback send to ").append(ping);
                  log.append(" success");
                  logger.info(log.toString());
                }
              }
              else
              {
                logger.severe("error occured during trackback save");
              }
            }
          }
          else if (logger.isLoggable(Level.FINE))
          {
            StringBuffer log = new StringBuffer();

            log.append("trackback to ").append(ping);
            log.append(" allready send");
            logger.fine(log.toString());
          }
        }
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.FINER, null, ex);
    }
  }

  /**
   * Method description
   *
   *
   * @param url
   *
   *
   * @return
   *
   * @throws Exception
   */
  private boolean sendPing(URL url) throws Exception
  {
    if (logger.isLoggable(Level.INFO))
    {
      StringBuffer log = new StringBuffer();

      log.append("send ping to ").append(url);
      logger.info(log.toString());
    }

    Blog blog = entry.getBlog();
    URLConnection conn = url.openConnection();

    conn.setDoOutput(true);

    BufferedWriter writer = null;

    try
    {
      writer =
        new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

      String title = URLEncoder.encode(entry.getTitle(), "UTF-8");

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

      String blogName = URLEncoder.encode(blog.getTitle(), "UTF-8");

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

    return checkResponse(conn);
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

    content = URLEncoder.encode(content, "UTF-8");

    return content;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ResourceBundle bundle;

  /** Field description */
  private Entry entry;
}
