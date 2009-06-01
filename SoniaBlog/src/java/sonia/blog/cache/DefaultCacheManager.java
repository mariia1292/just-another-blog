/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.cache;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import sonia.blog.api.cache.Cache;
import sonia.blog.api.cache.CacheManager;

import sonia.util.Util;
import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author sdorra
 */
public class DefaultCacheManager implements CacheManager
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultCacheManager.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public DefaultCacheManager()
  {
    this.caches = new HashMap<String, Cache>();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  public void clearAll()
  {
    for (Cache cache : caches.values())
    {
      cache.clear();
    }
  }

  /**
   * Method description
   *
   *
   *
   * @param maxSize
   * @return
   */
  public Cache createCache(int maxSize)
  {
    return new DefaultCache(maxSize);
  }

  /**
   * Method description
   *
   *
   * @param config
   *
   * @throws IOException
   */
  public void load(InputStream config) throws IOException
  {
    try
    {
      Document doc = XmlUtil.buildDocument(config);
      NodeList cacheList =
        doc.getDocumentElement().getElementsByTagName("cache");

      if (XmlUtil.hasContent(cacheList))
      {
        for (int i = 0; i < cacheList.getLength(); i++)
        {
          Node node = cacheList.item(i);

          addCache(node);
        }
      }
    }
    catch (SAXException ex)
    {
      throw new IOException(ex.getMessage());
    }
    catch (ParserConfigurationException ex)
    {
      throw new IOException(ex.getMessage());
    }
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param cache
   */
  public void putCache(String name, Cache cache)
  {
    if ( logger.isLoggable( Level.INFO ) )
    {
      StringBuffer log = new StringBuffer();
      log.append( "add cache " ).append( name ).append( " to CacheManager" );
      logger.info( log.toString() );
    }
    caches.put(name, cache);
  }

  /**
   * Method description
   *
   *
   * @param name
   */
  public void removeCache(String name)
  {
    caches.remove(name);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   */
  public Cache getCache(String name)
  {
    return caches.get(name);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Collection<Cache> getCaches()
  {
    return caches.values();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param cacheNode
   */
  private void addCache(Node cacheNode)
  {
    NodeList children = cacheNode.getChildNodes();

    if (XmlUtil.hasContent(children))
    {
      String name = null;
      int size = 25;

      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);

        if (child.getNodeName().equals("name"))
        {
          name = child.getTextContent();
        }
        else if (child.getNodeName().equals("size"))
        {
          try
          {
            size = Integer.parseInt(child.getTextContent());
          }
          catch (NumberFormatException ex)
          {
            logger.log(Level.WARNING, null, ex);
          }
        }
      }

      if (Util.hasContent(name))
      {
        Cache cache = createCache(size);

        putCache(name, cache);
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Map<String, Cache> caches;
}
