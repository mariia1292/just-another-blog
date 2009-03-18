/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.rss;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.ServiceLocator;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.List;

/**
 *
 * @author sdorra
 */
public abstract class FeedParser
{

  /** Field description */
  private static List<FeedParser> parserList;

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param type
   *
   * @return
   */
  public static FeedParser getInstance(String type)
  {
    FeedParser parser = null;

    if (parserList == null)
    {
      parserList = ServiceLocator.locateServices(FeedParser.class);
    }

    for (FeedParser p : parserList)
    {
      if (p.getType().equalsIgnoreCase(type))
      {
        parser = p;
      }
    }

    return parser;
  }

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
  public abstract Channel load(InputStream in) throws IOException;

  /**
   * Method description
   *
   *
   * @param channel
   * @param out
   *
   * @throws IOException
   */
  public abstract void store(Channel channel, OutputStream out)
          throws IOException;

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract String getMimeType();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract String getType();
}
