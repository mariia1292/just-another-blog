/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.servlet;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Role;

import sonia.util.ImageUtil;
import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Dimension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class AttachmentServlet extends HttpServlet
{

  /** Field description */
  private static final long serialVersionUID = 7530419613458887701L;

  /** Field description */
  private static Logger logger =
    Logger.getLogger(AttachmentServlet.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param attachment
   * @param blog
   *
   * @return
   */
  public File buildImageFile(Attachment attachment, Blog blog)
  {
    int width = blog.getImageWidth();
    int height = blog.getImageHeight();
    File directory = new File(imageDir, width + "x" + height);

    if (!directory.exists())
    {
      directory.mkdirs();
    }

    return new File(directory, attachment.getId() + "." + format);
  }

  /**
   * Method description
   *
   *
   * @param attachment
   * @param blog
   *
   * @return
   */
  public File buildThumbnailFile(Attachment attachment, Blog blog)
  {
    int width = blog.getThumbnailWidth();
    int height = blog.getThumbnailHeight();
    File directory = new File(imageDir, width + "x" + height);

    if (!directory.exists())
    {
      directory.mkdirs();
    }

    return new File(directory, attachment.getId() + "." + format);
  }

  /**
   * Method description
   *
   *
   * @throws ServletException
   */
  @Override
  public void init() throws ServletException
  {
    format =
      BlogContext.getInstance().getConfiguration().getString("image.format",
        "jpg");
    mimeType =
      BlogContext.getInstance().getConfiguration().getString("image.mimeType",
        "image/jpeg");

    File resourceDir = BlogContext.getInstance().getResourceDirectory();

    attachmentDir = new File(resourceDir, "attachment");
    imageDir = new File(resourceDir, "images");
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Returns a short description of the servlet.
   *
   * @return
   */
  @Override
  public String getServletInfo()
  {
    return "Short description";
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Handles the HTTP <code>GET</code> method.
   * @param request servlet request
   * @param response servlet response
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException
  {
    processRequest(request, response);
  }

  /**
   * Handles the HTTP <code>POST</code> method.
   * @param request servlet request
   * @param response servlet response
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void doPost(HttpServletRequest request,
                        HttpServletResponse response)
          throws ServletException, IOException
  {
    processRequest(request, response);
  }

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * @param request servlet request
   * @param response servlet response
   *
   * @throws IOException
   * @throws ServletException
   */
  protected void processRequest(HttpServletRequest request,
                                HttpServletResponse response)
          throws ServletException, IOException
  {
    BlogRequest blogRequest = null;

    if (request instanceof BlogRequest)
    {
      blogRequest = (BlogRequest) request;
    }
    else
    {
      blogRequest = new BlogRequest(request);
    }

    String requestURI = blogRequest.getRequestURI();
    Blog blog = blogRequest.getCurrentBlog();
    String[] parts = requestURI.split("/");
    String idString = parts[parts.length - 1];
    Long id = Long.parseLong(idString);
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Attachment.findByBlogAndId");

    q.setParameter("id", id);
    q.setParameter("blog", blog);

    Attachment attachment = (Attachment) q.getSingleResult();

    if ((attachment != null)
        && (attachment.getEntry().isPublished()
            || blogRequest.isUserInRole(Role.ADMIN)
            || blogRequest.isUserInRole(Role.AUTHOR)))
    {
      InputStream in = null;
      OutputStream out = response.getOutputStream();
      File attachmentFile = new File(attachmentDir, attachment.getFilePath());

      response.setContentType(mimeType);
      response.setHeader("Content-Disposition",
                         "filename=\"" + attachment.getName() + "\";");

      if (attachment.getMimeType().startsWith("image")
          && (request.getParameter("thumb") != null))
      {
        File thumbnail = buildThumbnailFile(attachment, blog);

        if (thumbnail.exists())
        {
          in = new FileInputStream(thumbnail);
        }
        else
        {
          if ((blog.getThumbnailWidth() > 0) && (blog.getThumbnailHeight() > 0))
          {
            ImageUtil.resize(new FileInputStream(attachmentFile),
                             new FileOutputStream(thumbnail), format,
                             blog.getThumbnailWidth(),
                             blog.getThumbnailHeight());
            in = new FileInputStream(thumbnail);
          }
          else if (blog.getThumbnailWidth() > 0)
          {
            ImageUtil.resizeKeepAspectRatioByWidth(
                new FileInputStream(attachmentFile),
                new FileOutputStream(thumbnail), format,
                blog.getThumbnailWidth());
            in = new FileInputStream(thumbnail);
          }
          else if (blog.getThumbnailHeight() > 0)
          {
            ImageUtil.resizeKeepAspectRatioByHeight(
                new FileInputStream(attachmentFile),
                new FileOutputStream(thumbnail), format,
                blog.getThumbnailHeight());
            in = new FileInputStream(thumbnail);
          }
          else
          {
            in = new FileInputStream(attachmentFile);
          }
        }
      }
      else if (attachment.getMimeType().startsWith("image")
               && (request.getParameter("orginal") == null))
      {
        File imageFile = buildImageFile(attachment, blog);

        if (imageFile.exists())
        {
          in = new FileInputStream(imageFile);
        }
        else
        {
          if ((blog.getImageWidth() > 0) && (blog.getImageHeight() > 0))
          {
            Dimension d =
              ImageUtil.getDimension(new FileInputStream(attachmentFile));

            if ((d.getWidth() > blog.getImageWidth())
                || (d.getHeight() > blog.getImageHeight()))
            {
              ImageUtil.resize(new FileInputStream(attachmentFile),
                               new FileOutputStream(imageFile), format,
                               blog.getImageWidth(), blog.getImageHeight());
              in = new FileInputStream(imageFile);
            }
            else
            {
              in = new FileInputStream(attachmentFile);
            }
          }
          else if (blog.getImageWidth() > 0)
          {
            int width = ImageUtil.getWidth(new FileInputStream(attachmentFile));

            if (width > blog.getImageWidth())
            {
              ImageUtil.resizeKeepAspectRatioByWidth(
                  new FileInputStream(attachmentFile),
                  new FileOutputStream(imageFile), format,
                  blog.getImageWidth());
              in = new FileInputStream(imageFile);
            }
            else
            {
              in = new FileInputStream(attachmentFile);
            }
          }
          else if (blog.getImageHeight() > 0)
          {
            int height =
              ImageUtil.getHeight(new FileInputStream(attachmentFile));

            if (height > blog.getImageHeight())
            {
              ImageUtil.resizeKeepAspectRatioByHeight(
                  new FileInputStream(attachmentFile),
                  new FileOutputStream(imageFile), format,
                  blog.getImageHeight());
              in = new FileInputStream(imageFile);
            }
          }
          else
          {
            in = new FileInputStream(attachmentFile);
          }
        }
      }
      else
      {
        response.setContentType(attachment.getMimeType());
        in = new FileInputStream(attachmentFile);
      }

      try
      {
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
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private File attachmentDir;

  /** Field description */
  private String format;

  /** Field description */
  private File imageDir;

  /** Field description */
  private String mimeType;
}
