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
public class AvatarMapping extends FinalMapping
{

  /** Field description */
  private static final String MAIN_AVATARDIR = "/WEB-INF/avatar";

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
      File avatarDir = resManager.getDirectory(Constants.RESOURCE_AVATAR);
      File avatar = new File(avatarDir, param[0]);

      if (!avatar.exists())
      {
        avatar = new File(request.getRealPath(MAIN_AVATARDIR), param[0]);
      }

      if (avatar.exists())
      {
        printAvatar(response, avatar);
      }
      else
      {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
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
   * @param avatar
   *
   * @throws IOException
   */
  private void printAvatar(BlogResponse response, File avatar)
          throws IOException
  {
    String contentType =
      URLConnection.getFileNameMap().getContentTypeFor(avatar.getName());

    response.setContentType(contentType);
    response.setContentLength((int) avatar.length());

    FileInputStream in = null;
    ServletOutputStream out = null;

    try
    {
      in = new FileInputStream(avatar);
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
