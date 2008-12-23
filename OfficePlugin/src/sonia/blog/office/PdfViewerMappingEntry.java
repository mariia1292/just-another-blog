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
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingEntry;
import sonia.blog.entity.Blog;
import sonia.blog.entity.PermaObject;

import sonia.config.ConfigurationListener;
import sonia.config.ModifyableConfiguration;
import sonia.config.XmlConfiguration;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
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
public class PdfViewerMappingEntry
        implements MappingEntry, ConfigurationListener
{

  /** Field description */
  public static final String PATH = "macros/pdfviewer/";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   */
  public PdfViewerMappingEntry()
  {
    XmlConfiguration config = BlogContext.getInstance().getConfiguration();

    mimeType = config.getString(Constants.CONFIG_IMAGEMIMETYPE,
                                Constants.DEFAULT_IMAGE_MIMETYPE);
    config.addListener(this);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param config
   * @param key
   */
  public void configChanged(ModifyableConfiguration config, String key)
  {
    if (key.equals(Constants.CONFIG_IMAGEMIMETYPE))
    {
      mimeType = config.getString(Constants.CONFIG_IMAGEMIMETYPE,
                                  Constants.DEFAULT_IMAGE_MIMETYPE);
    }
  }

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
    boolean notFound = true;

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

              notFound = false;
            }
          }
        }
      }
    }

    if (notFound)
    {
      try
      {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
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

    File attachmentDir = resManager.getDirectory( Constants.RESOURCE_ATTACHMENT, blog, false );

    return new File( attachmentDir, "pdfviewer" + File.separator + directory );
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Logger logger =
    Logger.getLogger(PdfViewerMappingEntry.class.getName());

  /** Field description */
  private String mimeType;
}
