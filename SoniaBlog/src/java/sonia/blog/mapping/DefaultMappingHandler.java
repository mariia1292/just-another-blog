/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.mapping.MappingEntry;
import sonia.blog.api.mapping.MappingHandler;

//~--- JDK imports ------------------------------------------------------------

import java.io.UnsupportedEncodingException;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class DefaultMappingHandler implements MappingHandler
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultMappingHandler.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public DefaultMappingHandler()
  {
    mappgins = new HashMap<String, MappingEntry>();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param path
   * @param entry
   */
  public void addMappging(String path, MappingEntry entry)
  {
    mappgins.put(path, entry);
  }

  /**
   * Method description
   *
   *
   * @param path
   *
   * @return
   */
  public boolean containsPath(String path)
  {
    return mappgins.containsKey(path);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @return
   */
  public boolean handleMapping(BlogRequest request, BlogResponse response)
  {
    boolean result = true;
    String uri = request.getRequestURI();

    uri = uri.substring(request.getContextPath().length());

    String path = null;

    for (String key : mappgins.keySet())
    {
      if (uri.startsWith(key))
      {
        path = key;

        break;
      }
    }

    if (path != null)
    {
      MappingEntry mapping = mappgins.get(path);

      setCharacterEncoding(request);
      uri = uri.substring(path.length());

      if (uri.startsWith("/"))
      {
        uri = uri.substring(1);
      }

      String[] parts = uri.split("/");

      result = mapping.handleMapping(request, response, parts);
      request.setMapping(mapping);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param path
   */
  public void removeMapping(String path)
  {
    mappgins.remove(path);
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Workaround
   *
   *
   * @param request
   */
  private void setCharacterEncoding(BlogRequest request)
  {
    try
    {
      request.getRequest().setCharacterEncoding(request.getCharacterEncoding());
    }
    catch (UnsupportedEncodingException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Map<String, MappingEntry> mappgins;
}
