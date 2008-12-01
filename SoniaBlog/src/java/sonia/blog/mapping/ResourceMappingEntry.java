/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingEntry;
import sonia.blog.entity.PermaObject;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class ResourceMappingEntry implements MappingEntry
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ResourceMappingEntry.class.getName());

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
    if ((param != null) && (param.length > 0))
    {
      String uri = "/jab/resource";

      for (String p : param)
      {
        uri += "/" + p;
      }

      InputStream in = Util.findResource(uri);
      OutputStream out = null;

      try
      {
        if (in != null)
        {
          out = response.getOutputStream();
          Util.copy(in, out);
        }
        else
        {
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
      finally
      {
        try
        {
          if (in != null)
          {
            in.close();
          }

          if (out != null)
          {
            out.close();
          }
        }
        catch (IOException e)
        {
          logger.log(Level.SEVERE, null, e);
        }
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
}
