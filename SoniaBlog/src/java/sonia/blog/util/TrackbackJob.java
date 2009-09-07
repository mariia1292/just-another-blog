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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Sebastian Sdorra
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

            if (BlogUtil.sendTrackbackPing(entry, url))
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

              if (BlogContext.getDAOFactory().getTrackbackDAO().add(
                      BlogContext.getInstance().getSystemBlogSession(),
                      trackback))
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ResourceBundle bundle;

  /** Field description */
  private Entry entry;
}
