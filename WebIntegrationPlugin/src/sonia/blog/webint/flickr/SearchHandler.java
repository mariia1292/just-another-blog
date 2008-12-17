/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sonia.blog.webint.flickr;

import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author sdorra
 */
public class SearchHandler extends DefaultHandler
{

  private List<Photo> photos;

  public SearchHandler(List<Photo> photos)
  {
    this.photos = photos;
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
  {
    if (qName.equals("photo") )
    {
      photos.add( Photo.fromXml(attributes) );
    }
  }


}