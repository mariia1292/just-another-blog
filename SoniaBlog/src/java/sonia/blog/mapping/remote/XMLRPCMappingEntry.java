/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping.remote;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.webserver.XmlRpcServletServer;

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.mapping.FinalMapping;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class XMLRPCMappingEntry extends FinalMapping
{

  /**
   * Constructs ...
   *
   */
  public XMLRPCMappingEntry()
  {
    super();
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
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void handleFinalMapping(BlogRequest request, BlogResponse response,
                                    String[] param)
          throws IOException, ServletException
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
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private XmlRpcServletServer server;
}
