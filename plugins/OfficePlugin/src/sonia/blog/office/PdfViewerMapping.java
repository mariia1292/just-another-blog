/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://kenai.com/projects/jab
 *
 */



package sonia.blog.office;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogJob;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;

import sonia.config.Config;

import sonia.jobqueue.JobException;
import sonia.jobqueue.JobQueue;

import sonia.plugin.service.Service;

import sonia.util.Util;

import sonia.web.util.WebUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
public class PdfViewerMapping extends FinalMapping
{

  /** Field description */
  public static final String REGEX = "^/macros/pdfviewer/([0-9]+)/(.*)$";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(PdfViewerMapping.class.getName());

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
    if ((param != null) && (param.length > 1))
    {
      try
      {
        Blog blog = request.getCurrentBlog();
        Long attachmentId = Long.parseLong(param[0]);
        Attachment attachment = attachmentDAO.findByBlogAndId(blog,
                                  attachmentId);

        if (attachment != null)
        {
          if ("index.json".equals(param[1]) && (renderer != null))
          {
            handleListMapping(response, blog, attachment);
          }
          else
          {
            renderImage(request, response, blog, attachment, param[1]);
          }
        }
        else
        {
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
      }
      catch (NumberFormatException ex)
      {
        if (logger.isLoggable(Level.FINEST))
        {
          logger.log(Level.FINEST, null, ex);
        }

        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

  /**
   * Method description
   *
   *
   * @param response
   * @param blog
   * @param attachment
   *
   * @throws IOException
   */
  private void handleListMapping(BlogResponse response, Blog blog,
                                 Attachment attachment)
          throws IOException
  {
    PDFImageGallery imageGallery = renderer.renderPdf(blog,
                                     getDirectory(blog, attachment),
                                     attachment, format, extension);

    if ((imageGallery != null) && Util.hasContent(imageGallery.getImages()))
    {
      response.setContentType("application/x-javascript");

      PrintWriter writer = null;

      try
      {
        writer = response.getWriter();

        List<String> images = imageGallery.getImages();
        int size = images.size();

        writer.println("[");
        writer.append("{\"title\":\"").append(imageGallery.getTitle());
        writer.append("\", \"size\": ");
        writer.append(Integer.toString(size));
        writer.append(", \"pages\": [\n");

        for (int i = 0; i < size; i++)
        {
          writer.append("{ \"name\": \"").append(images.get(i));
          writer.append("\", \"nr\": ").append(Integer.toString(i + 1));
          writer.append(" }");

          if ((i + 1) < size)
          {
            writer.append(",\n");
          }
        }

        writer.println("]}]");
      }
      finally
      {
        writer.close();
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
   * @param request
   * @param response
   * @param image
   *
   * @throws IOException
   */
  private void printImage(BlogRequest request, BlogResponse response,
                          File image)
          throws IOException
  {
    if (WebUtil.isModified(request, image))
    {
      response.setContentType(mimeType);
      response.setContentLength((int) image.length());
      WebUtil.addETagHeader(response, image);

      FileInputStream in = null;
      ServletOutputStream out = null;

      try
      {
        in = new FileInputStream(image);
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
    else
    {
      response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param image
   *
   * @throws IOException
   */
  private void printThumbnail(BlogRequest request, BlogResponse response,
                              File image)
          throws IOException
  {
    String imageName = image.getName();
    StringBuffer nameBuffer = new StringBuffer();

    nameBuffer.append(imageName.substring(0,
            imageName.length() - extension.length()));
    nameBuffer.append("thumb.").append(extension);

    File thumbnail = new File(image.getParentFile(), nameBuffer.toString());

    if (thumbnail.exists())
    {
      printImage(request, response, thumbnail);
    }
    else
    {
      JobQueue<BlogJob> queue = BlogContext.getInstance().getJobQueue();

      queue.processs(new ImageResizeJob(request.getCurrentBlog(), image,
                                        thumbnail));

      if (thumbnail.exists())
      {
        printImage(request, response, thumbnail);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param blog
   * @param attachment
   * @param value
   *
   * @throws IOException
   */
  private void renderImage(BlogRequest request, BlogResponse response,
                           Blog blog, Attachment attachment, String value)
          throws IOException
  {
    boolean thumb = request.getParameter("thumb") != null;
    File image = new File(getDirectory(blog, attachment), value);

    if (image.exists())
    {
      if (thumb)
      {
        printThumbnail(request, response, image);
      }
      else
      {
        printImage(request, response, image);
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blog
   * @param attachment
   *
   * @return
   */
  private File getDirectory(Blog blog, Attachment attachment)
  {
    ResourceManager resManager = BlogContext.getInstance().getResourceManager();
    File attachmentDir = resManager.getDirectory(Constants.RESOURCE_ATTACHMENT,
                           blog, false);

    return new File(attachmentDir,
                    "pdfviewer" + File.separator + attachment.getId());
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/10/13
   * @author         Enter your name here...
   */
  private class ImageResizeJob implements BlogJob
  {

    /**
     * Constructs ...
     *
     *
     * @param blog
     * @param input
     * @param output
     */
    public ImageResizeJob(Blog blog, File input, File output)
    {
      this.blog = blog;
      this.input = input;
      this.output = output;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @throws JobException
     */
    public void excecute() throws JobException
    {
      int width = blog.getThumbnailWidth();
      int height = blog.getThumbnailHeight();

      try
      {
        BlogContext.getInstance().getImageFileHandler().scaleImage(input,
                output, format, width, height);
      }
      catch (IOException ex)
      {
        throw new JobException(ex);
      }
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public Blog getBlog()
    {
      return blog;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getDescription()
    {
      return "Resize an image for PDF-Gallery";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getName()
    {
      return "PDF-ImageResizeJob";
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private Blog blog;

    /** Field description */
    private File input;

    /** Field description */
    private File output;
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Dao
  private AttachmentDAO attachmentDAO;

  /** Field description */
  @Config(Constants.CONFIG_IMAGEMIMETYPE)
  private String mimeType = Constants.DEFAULT_IMAGE_MIMETYPE;

  /** Field description */
  @Config(Constants.CONFIG_IMAGEFORMAT)
  private String format = Constants.DEFAULT_IMAGE_FORMAT;

  /** Field description */
  @Config(Constants.CONFIG_IMAGEEXTENSION)
  private String extension = Constants.DEFAULT_IMAGE_EXTENSION;

  /** Field description */
  @Service(PDFRenderer.SERVICE)
  private PDFRenderer renderer;
}
