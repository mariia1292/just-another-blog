/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping.remote;

//~--- non-JDK imports --------------------------------------------------------

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.webserver.XmlRpcServletServer;

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingEntry;
import sonia.blog.entity.PermaObject;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
import sonia.util.Util;

/**
 *
 * @author sdorra
 */
public class XMLRPCMappingEntry implements MappingEntry
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(XMLRPCMappingEntry.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public XMLRPCMappingEntry()
  {
    server = new XmlRpcServletServer();

    try
    {
      PropertyHandlerMapping mapping = new PropertyHandlerMapping();

      mapping.addHandler(Blogger.BLOGGER_KEY, Blogger.class);
      mapping.addHandler(MetaWeblog.METAWEBLOG_KEY, MetaWeblog.class);
      server.setHandlerMapping(mapping);
    }
    catch (XmlRpcException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @return
   */
  public boolean handleMapping(BlogRequest request, BlogResponse response,
                               String[] param)
  {
    try
    {
      server.execute(request, response);
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);

      try
      {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
      catch (IOException ex1)
      {
        logger.log(Level.SEVERE, null, ex1);
      }
    }

    return false;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param linkBuilder
   * @param object
   *
   * @return
   */
  public String getUri(BlogRequest request, LinkBuilder linkBuilder,
                       PermaObject object)
  {
    return null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isNavigationRendered()
  {
    return false;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private XmlRpcServletServer server;
}
