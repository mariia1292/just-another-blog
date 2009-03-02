/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.office;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.entity.Blog;

import sonia.config.Config;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class PdfViewerMapping extends FinalMapping
{

  /** Field description */
  public static final String REGEX = "^/macros/pdfviewer/([0-9]+)/(.*)$";

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
    if (Util.isBlank(mimeType))
    {
      mimeType = Constants.DEFAULT_IMAGE_MIMETYPE;
    }

    if ((param != null) && (param.length > 1))
    {
      String directory = param[0];

      if (!Util.isBlank(directory))
      {
        File pageDir = getDirectory(directory, request.getCurrentBlog());

        if (pageDir.exists())
        {
          String page = param[1];

          if (!Util.isBlank(page))
          {
            File file = new File(pageDir, page);

            if (file.exists())
            {
              OutputStream out = null;
              InputStream in = null;

              try
              {
                response.setContentType(mimeType);
                out = response.getOutputStream();
                in = new FileInputStream(file);
                Util.copy(in, out);
              }
              catch (Exception ex)
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
                catch (IOException ex)
                {
                  logger.log(Level.SEVERE, null, ex);
                }
              }
            }
            else
            {
              response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
          }
          else
          {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
          }
        }
        else
        {
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
      }
      else
      {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param directory
   * @param blog
   *
   * @return
   */
  private File getDirectory(String directory, Blog blog)
  {
    ResourceManager resManager = BlogContext.getInstance().getResourceManager();
    File attachmentDir = resManager.getDirectory(Constants.RESOURCE_ATTACHMENT,
                           blog, false);

    return new File(attachmentDir, "pdfviewer" + File.separator + directory);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Config(Constants.CONFIG_IMAGEMIMETYPE)
  private String mimeType;
}
