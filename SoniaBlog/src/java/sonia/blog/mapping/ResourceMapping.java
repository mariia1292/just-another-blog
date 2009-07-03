/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.mapping.FinalMapping;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URLConnection;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class ResourceMapping extends FinalMapping
{

  /** Field description */
  private static final String PATHPREFIX = "/jab/resource/";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ResourceMapping.class.getName());

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
    if ((param != null) && (param.length > 0))
    {
      String path = param[0];
      String mimeType = URLConnection.getFileNameMap().getContentTypeFor(path);

      response.setContentType(mimeType);

      InputStream in = null;

      if (request.getParameter("temp") != null)
      {
        File tempDirectory =
          BlogContext.getInstance().getResourceManager().getDirectory(
              Constants.RESOURCE_TEMP);

        in = new FileInputStream(new File(tempDirectory, path));
      }
      else
      {
        String uri = PATHPREFIX + path;

        in = Util.findResource(uri);
      }

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
  }
}
