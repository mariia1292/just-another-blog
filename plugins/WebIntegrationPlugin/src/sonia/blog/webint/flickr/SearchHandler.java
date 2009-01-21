/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webint.flickr;

//~--- non-JDK imports --------------------------------------------------------

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public class SearchHandler extends DefaultHandler
{

  /**
   * Constructs ...
   *
   *
   * @param photos
   */
  public SearchHandler(List<Photo> photos)
  {
    this.photos = photos;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param uri
   * @param localName
   * @param qName
   * @param attributes
   *
   * @throws SAXException
   */
  @Override
  public void startElement(String uri, String localName, String qName,
                           Attributes attributes)
          throws SAXException
  {
    if (qName.equals("photo"))
    {
      photos.add(Photo.fromXml(attributes));
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<Photo> photos;
}
