/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.entity.Attachment;

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
public abstract class AbstractAttachmentMapping extends FinalMapping
{

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract Logger getLogger();

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param response
   * @param name
   * @param mimeType
   * @param file
   */
  protected void printFile(BlogResponse response, String name, String mimeType,
                           File file)
  {
    response.setContentType(mimeType);
    response.setContentLength((int) file.length());
    response.setHeader("Content-Disposition", "filename=\"" + name + "\";");
    response.setDateHeader("Last-Modified", file.lastModified());

    OutputStream out = null;
    InputStream in = null;

    try
    {
      out = response.getOutputStream();
      in = new FileInputStream(file);
      Util.copy(in, out);
    }
    catch (IOException ex)
    {
      getLogger().log(Level.SEVERE, null, ex);
    }
    finally
    {
      try
      {
        if (out != null)
        {
          out.close();
        }

        if (in != null)
        {
          in.close();
        }
      }
      catch (IOException e)
      {
        getLogger().log(Level.SEVERE, null, e);
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param response
   * @param attachment
   *
   * @return
   *
   * @throws IOException
   */
  protected File getFile(BlogResponse response, Attachment attachment)
          throws IOException
  {
    File file =
      BlogContext.getInstance().getResourceManager().getFile(attachment);

    if ((file == null) ||!file.exists() || file.isDirectory())
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    return file;
  }
}
