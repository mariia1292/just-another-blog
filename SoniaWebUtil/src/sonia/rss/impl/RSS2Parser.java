/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.rss.impl;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import sonia.rss.AbstractBase;
import sonia.rss.Channel;
import sonia.rss.FeedParser;
import sonia.rss.Image;
import sonia.rss.Item;

import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.MalformedURLException;
import java.net.URL;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * Example:
 * <rss version="2.0">
 *   <channel>
 *     <title>Nitilon</title>
 *     <link>http://www.nitilon.de</link>
 *     <pubDate>Tue, 8 Jul 2008 2:43:19</pubDate>
 *     ...
 *     <image>
 *       <url>http://www.nitilon.de/bild_thumbnail.jpg</url>
 *       <description>Beschreibung</description>
 *       <link>http://www.nitilon.de/bild.jpg</link>
 *     </image>
 *     <item>
 *       <title>Test Eintrag</title>
 *       <link>http://www.nitilon.de/entries.xhtml?id=3</link>
 *       ...
 *     </item>
 *     <item>
 *       ...
 *     </item>
 *     ...
 *   </channel>
 * </rss>
 *
 *
 * @author sdorra
 */
public class RSS2Parser extends FeedParser
{

  /** Field description */
  public static final String ATTRIBUTE_VERSION = "version";

  /** Field description */
  public static final String ATTRIBUTE_VERSION_VALUE = "2.0";

  /** Field description */
  public static final String ELEMENT_AUTHOR = "author";

  /** Field description */
  public static final String ELEMENT_CHANNEL = "channel";

  /** Field description */
  public static final String ELEMENT_COPYRIGHT = "copyright";

  /** Field description */
  public static final String ELEMENT_DESCRIPTION = "description";

  /** Field description */
  public static final String ELEMENT_GUID = "guid";

  /** Field description */
  public static final String ELEMENT_IMAGE = "image";

  /** Field description */
  public static final String ELEMENT_ITEM = "item";

  /** Field description */
  public static final String ELEMENT_LANGUAGE = "language";

  /** Field description */
  public static final String ELEMENT_LINK = "link";

  /** Field description */
  public static final String ELEMENT_PUBDATE = "pubDate";

  /** Field description */
  public static final String ELEMENT_RSS = "rss";

  /** Field description */
  public static final String ELEMENT_TITLE = "title";

  /** Field description */
  public static final String ELEMENT_URL = "url";

  /** Field description */
  public static final String MIMETYPE = "application/rss+xml";

  /** Field description */
  public static final SimpleDateFormat HTTP_DATE_FORMAT =
    new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);

  /** Field description */
  public static final String TYPE = "RSS2";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param in
   *
   * @return
   *
   * @throws IOException
   */
  @Override
  public Channel load(InputStream in) throws IOException
  {
    Channel channel = null;

    try
    {
      Document doc = XmlUtil.buildDocument(in);
      Element rssEl = doc.getDocumentElement();

      channel = new Channel();

      NodeList children = rssEl.getChildNodes();

      if (children != null)
      {
        for (int i = 0; i < children.getLength(); i++)
        {
          Node child = children.item(i);

          if (child.getNodeName().equals(ELEMENT_CHANNEL))
          {
            parseChannel(channel, child);
          }
        }
      }
    }
    catch (SAXException ex)
    {
      ex.printStackTrace();

      throw new IOException(ex.getMessage());
    }
    catch (ParserConfigurationException ex)
    {
      ex.printStackTrace();

      throw new IOException(ex.getMessage());
    }
    catch (ParseException ex)
    {
      ex.printStackTrace();

      throw new IOException(ex.getMessage());
    }

    return channel;
  }

  /**
   * Method description
   *
   *
   * @param channel
   * @param out
   *
   * @throws IOException
   */
  @Override
  public void store(Channel channel, OutputStream out) throws IOException
  {
    try
    {
      Document doc = XmlUtil.createDocument();
      Element rssEl = doc.createElement(ELEMENT_RSS);

      rssEl.setAttribute(ATTRIBUTE_VERSION, ATTRIBUTE_VERSION_VALUE);
      doc.appendChild(rssEl);

      Element channelEl = doc.createElement(ELEMENT_CHANNEL);

      rssEl.appendChild(channelEl);
      appendBaseElements(channel, channelEl, doc);

      if (channel.getLanguage() != null)
      {
        Element channelLanguageEl = doc.createElement(ELEMENT_COPYRIGHT);

        channelLanguageEl.setTextContent(channel.getLanguage().toString());
        channelEl.appendChild(channelLanguageEl);
      }

      if (channel.getCopyright() != null)
      {
        Element channelCopyrightEl = doc.createElement(ELEMENT_COPYRIGHT);

        channelCopyrightEl.setTextContent(channel.getCopyright());
        channelEl.appendChild(channelCopyrightEl);
      }

      if (channel.getPubDate() != null)
      {
        Element channelPubDateEl = doc.createElement(ELEMENT_PUBDATE);

        channelPubDateEl.setTextContent(
            HTTP_DATE_FORMAT.format(channel.getPubDate()));
        channelEl.appendChild(channelPubDateEl);
      }

      if (channel.getImage() != null)
      {
        Element channelImageEl = createImageElement(channel.getImage(), doc);

        channelEl.appendChild(channelImageEl);
      }

      List<Item> items = channel.getItems();

      if ((items != null) &&!items.isEmpty())
      {
        for (Item item : items)
        {
          Element itemEl = doc.createElement(ELEMENT_ITEM);

          appendBaseElements(item, itemEl, doc);

          if (item.getAuthor() != null)
          {
            Element itemAuthorEl = doc.createElement(ELEMENT_AUTHOR);

            itemAuthorEl.setTextContent(item.getAuthor());
            itemEl.appendChild(itemAuthorEl);
          }

          if (item.getGuid() != null)
          {
            Element itemGuidEl = doc.createElement(ELEMENT_GUID);

            itemGuidEl.setTextContent(item.getGuid());
            itemEl.appendChild(itemGuidEl);
          }

          if (item.getPubDate() != null)
          {
            Element itemPubDateEl = doc.createElement(ELEMENT_PUBDATE);

            itemPubDateEl.setTextContent(
                HTTP_DATE_FORMAT.format(item.getPubDate()));
            itemEl.appendChild(itemPubDateEl);
          }

          channelEl.appendChild(itemEl);
        }
      }

      XmlUtil.writeDocument(doc, out, true);
    }
    catch (ParserConfigurationException ex)
    {
      ex.printStackTrace();

      throw new IOException(ex.getMessage());
    }
    catch (TransformerException ex)
    {
      ex.printStackTrace();

      throw new IOException(ex.getMessage());
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String getMimeType()
  {
    return MIMETYPE;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String getType()
  {
    return TYPE;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param item
   * @param parentEl
   * @param doc
   */
  private void appendBaseElements(AbstractBase item, Element parentEl,
                                  Document doc)
  {
    Element channelTitleEl = doc.createElement(ELEMENT_TITLE);

    channelTitleEl.setTextContent(item.getTitle());
    parentEl.appendChild(channelTitleEl);

    Element channelLinkEl = doc.createElement(ELEMENT_LINK);

    channelLinkEl.setTextContent(item.getLink().toString());
    parentEl.appendChild(channelLinkEl);

    Element channelDescriptionEl = doc.createElement(ELEMENT_DESCRIPTION);

    channelDescriptionEl.setTextContent(item.getDescription());
    parentEl.appendChild(channelDescriptionEl);
  }

  /**
   * Method description
   *
   *
   * @param image
   * @param doc
   *
   * @return
   */
  private Element createImageElement(Image image, Document doc)
  {
    Element imageEl = doc.createElement(ELEMENT_IMAGE);
    Element urlEl = doc.createElement(ELEMENT_URL);

    urlEl.setTextContent(image.getUrl().toString());
    imageEl.appendChild(urlEl);

    if (image.getTitle() != null)
    {
      Element titleEl = doc.createElement(ELEMENT_TITLE);

      titleEl.setTextContent(image.getTitle());
      imageEl.appendChild(titleEl);
    }

    if (image.getLink() != null)
    {
      Element linkEl = doc.createElement(ELEMENT_LINK);

      linkEl.setTextContent(image.getLink().toString());
      imageEl.appendChild(linkEl);
    }

    return imageEl;
  }

  /**
   * Method description
   *
   *
   * @param channel
   * @param node
   *
   * @throws MalformedURLException
   * @throws ParseException
   */
  private void parseChannel(Channel channel, Node node)
          throws MalformedURLException, ParseException
  {
    setBaseAttributes(channel, node);

    NodeList children = node.getChildNodes();

    if (children != null)
    {
      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);
        String name = child.getNodeName();
        String value = child.getTextContent();

        if (name.equals(ELEMENT_LANGUAGE))
        {
          channel.setLanguage(new Locale(value));
        }
        else if (name.equals(ELEMENT_COPYRIGHT))
        {
          channel.setCopyright(value);
        }
        else if (name.equals(ELEMENT_IMAGE))
        {
          Image image = parseImage(child);

          channel.setImage(image);
        }
        else if (name.equals(ELEMENT_ITEM))
        {
          Item item = parsetItem(child);

          if (item != null)
          {
            channel.getItems().add(item);
          }
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param node
   *
   * @return
   *
   * @throws MalformedURLException
   */
  private Image parseImage(Node node) throws MalformedURLException
  {
    Image image = null;
    NodeList children = node.getChildNodes();

    if (children != null)
    {
      image = new Image();

      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);
        String name = child.getNodeName();
        String value = child.getTextContent();

        if (name.equals(ELEMENT_URL))
        {
          image.setUrl(new URL(value));
        }
        else if (name.equals(ELEMENT_TITLE))
        {
          image.setTitle(value);
        }
        else if (name.equals(ELEMENT_LINK))
        {
          image.setLink(new URL(value));
        }
      }
    }

    return image;
  }

  /**
   * Method description
   *
   *
   *
   * @param node
   *
   * @return
   *
   * @throws MalformedURLException
   * @throws ParseException
   */
  private Item parsetItem(Node node)
          throws MalformedURLException, ParseException
  {
    Item item = new Item();

    setBaseAttributes(item, node);

    NodeList children = node.getChildNodes();

    for (int i = 0; i < children.getLength(); i++)
    {
      Node child = children.item(i);
      String name = child.getNodeName();
      String value = child.getNodeValue();

      if (name.equals(ELEMENT_AUTHOR))
      {
        item.setAuthor(value);
      }
      else if (name.equals(ELEMENT_GUID))
      {
        item.setGuid(value);
      }
    }

    return item;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param item
   * @param node
   *
   * @throws MalformedURLException
   * @throws ParseException
   */
  private void setBaseAttributes(AbstractBase item, Node node)
          throws MalformedURLException, ParseException
  {
    NodeList children = node.getChildNodes();

    if (children != null)
    {
      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);
        String name = child.getNodeName();
        String value = child.getTextContent();

        if (name.equals(ELEMENT_TITLE))
        {
          item.setTitle(value);
        }
        else if (name.equals(ELEMENT_LINK))
        {
          item.setLink(new URL(value));
        }
        else if (name.equals(ELEMENT_DESCRIPTION))
        {
          item.setDescription(value);
        }
        else if (name.equals(ELEMENT_PUBDATE))
        {
          item.setPubDate(HTTP_DATE_FORMAT.parse(value));
        }
      }
    }
  }
}
