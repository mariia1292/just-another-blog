/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.Context;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.mapping.FinalMapping;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class CustomTemplateMapping extends FinalMapping
{

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
    File file = null;

    if ((param != null) && (param.length > 0))
    {
      file = new File(resManager.getDirectory(Constants.RESOURCE_TEMPLATE),
                      param[0]);
    }

    if ((file != null) && file.exists())
    {
      sendResponse(response, file);
    }
    else
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  /**
   * Method description
   *
   *
   * @param response
   * @param file
   *
   * @throws IOException
   */
  private void sendResponse(BlogResponse response, File file) throws IOException
  {
    response.setContentLength((int) file.length());

    String mimeType =
      URLConnection.getFileNameMap().getContentTypeFor(file.getName());

    if (Util.hasContent(mimeType))
    {
      response.setContentType(mimeType);
    }

    FileInputStream in = null;
    ServletOutputStream out = null;

    try
    {
      in = new FileInputStream(file);
      out = response.getOutputStream();
      Util.copy(in, out);
    }
    finally
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
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Context
  private ResourceManager resManager;
}
