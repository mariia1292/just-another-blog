/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.office;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.api.util.AbstractBlogMacro;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;

import sonia.config.Config;

//~--- JDK imports ------------------------------------------------------------

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import java.util.logging.Level;

import javax.faces.context.FacesContext;

import javax.imageio.ImageIO;

/**
 *
 * @author sdorra
 */
public class PdfViewerMacro extends AbstractBlogMacro
{

  /** Field description */
  public static final String NAME = "pdfviewer";

  /** Field description */
  private static String PDFMIMETYPE = "application/pdf";

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param id
   */
  public void setId(String id)
  {
    this.id = id;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param facesContext
   * @param linkBase
   * @param object
   * @param body
   *
   * @return
   */
  @Override
  protected String doBody(FacesContext facesContext, String linkBase,
                          ContentObject object, String body)
  {
    String result = null;

    if (extension == null)
    {
      extension = Constants.DEFAULT_IMAGE_EXTENSION;
    }

    if (format == null)
    {
      format = Constants.DEFAULT_IMAGE_FORMAT;
    }

    Blog blog = getCurrentBlog(facesContext);
    BlogRequest request = getRequest(facesContext);
    BlogContext context = BlogContext.getInstance();

    if ((object != null) && (object instanceof Entry))
    {
      if (id != null)
      {
        Long attachmentId = Long.parseLong(id);
        AttachmentDAO attachmentDAO =
          BlogContext.getDAOFactory().getAttachmentDAO();
        Attachment attachment = attachmentDAO.findByBlogAndId(blog,
                                  attachmentId);

        if (attachment.getMimeType().equalsIgnoreCase(PDFMIMETYPE))
        {
          File attachmentFile =
            context.getResourceManager().getFile(attachment);

          if (attachmentFile.exists())
          {
            result = createPdfImageGallery(request, linkBase, attachmentId,
                                           attachmentFile, body);
          }
          else
          {
            result = "-- file not found --";
          }
        }
        else
        {
          result = "-- file is not a pdf --";
        }
      }
      else
      {
        result = "-- id param is required --";
      }
    }
    else
    {
      result = "-- entry not found --";
    }

    return result;
  }

  /**
   * Field description
   *
   * @param request
   * @param linkBase
   * @param id
   * @param attachmentFile
   * @param body
   *
   * @return
   */

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param linkBase
   * @param id
   * @param attachmentFile
   * @param body
   *
   * @return
   */
  private String createPdfImageGallery(BlogRequest request, String linkBase,
          long id, File attachmentFile, String body)
  {
    String result = null;
    File resDir = BlogContext.getInstance().getResourceManager().getDirectory(
                      Constants.RESOURCE_ATTACHMENT, request.getCurrentBlog());
    File pdfDir = new File(resDir, "pdfviewer" + File.separator + id);

    if (pdfDir.exists())
    {
      result = printPdfImageGallery(request, linkBase, pdfDir, body);
    }
    else
    {
      if (pdfDir.mkdirs())
      {
        try
        {
          createPdfImageGallery(pdfDir, attachmentFile);
          result = printPdfImageGallery(request, linkBase, pdfDir, body);
        }
        catch (Exception ex)
        {
          logger.log(Level.SEVERE, null, ex);
          result = "-- " + ex.getLocalizedMessage() + " --";
        }
      }
      else
      {
        result = "-- cant create pdf directory --";
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param pdfDir
   * @param attachmentFile
   *
   * @throws IOException
   */
  private void createPdfImageGallery(File pdfDir, File attachmentFile)
          throws IOException
  {
    RandomAccessFile raf = new RandomAccessFile(attachmentFile, "r");
    FileChannel channel = raf.getChannel();
    ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0,
                                 channel.size());
    PDFFile pdf = new PDFFile(buf);
    int pages = pdf.getNumPages();

    for (int i = 1; i < pages; i++)
    {
      PDFPage page = pdf.getPage(i);

      createPdfPage(page,
                    new File(pdfDir, String.valueOf(i) + "." + extension));
    }
  }

  /**
   * Method description
   *
   *
   * @param page
   * @param file
   *
   * @throws IOException
   */
  private void createPdfPage(PDFPage page, File file) throws IOException
  {
    Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(),
                                   (int) page.getBBox().getHeight());
    BufferedImage bufferedImage = new BufferedImage(rect.width, rect.height,
                                    BufferedImage.TYPE_INT_RGB);

    // generate the image
    Image image = page.getImage(rect.width, rect.height,    // width & height
                                rect,                       // clip rect
                                null,                       // null for the ImageObserver
                                true,                       // fill background with white
                                true                        // block until drawing is done
                                  );
    Graphics2D bufImageGraphics = bufferedImage.createGraphics();

    bufImageGraphics.drawImage(image, 0, 0, null);
    ImageIO.write(bufferedImage, format, file);
  }

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param linkBase
   * @param pdfDir
   * @param body
   *
   * @return
   */
  private String printPdfImageGallery(BlogRequest request, String linkBase,
          File pdfDir, String body)
  {
    String result = "";
    String res = linkBase + "resources/lightbox/";

    if (request.getAttribute("sonia.blog.macro.gallery") == null)
    {
      result += "<script type=\"text/javascript\" src=\"" + res
                + "js/jquery.js\"></script>\n";
      result += "<script type=\"text/javascript\" src=\"" + res
                + "js/jquery.lightbox-0.5.js\"></script>\n";
      result += "<link rel=\"stylesheet\" href=\"" + res
                + "css/jquery.lightbox-0.5.css\" "
                + "type=\"text/css\" media=\"screen\"></link>\n";
      request.setAttribute("sonia.blog.macro.gallery", Boolean.TRUE);
    }

    File[] files = pdfDir.listFiles(new FilenameFilter()
    {
      public boolean accept(File dir, String name)
      {
        return name.endsWith("." + extension);
      }
    });
    String name = "pdfgallery_" + pdfDir.getName();

    result += "<span id=\"" + name + "\">\n";

    String path = pdfDir.getName() + "/";
    String baseLink = linkBase + "macros/pdfviewer/" + path;

    if ((files != null) && (files.length > 0))
    {
      for (int i = 1; i < files.length; i++)
      {
        String link = baseLink + i + "." + extension;

        result += "<a id=\"" + name + "_" + i + "\" title=\"Page " + i + "\" ";
        result += "rel=\"[ligthbox[group_a" + i + "]\" href=\"" + link + "\"";

        if (i > 1)
        {
          result += " style=\"display: none;\">";
        }
        else
        {
          if ((body == null) || (body.trim().length() == 0))
          {
            body = "<img src=\"" + linkBase
                   + "resource/icon/document.gif\" alt=\"PDF\" />";
          }

          result += ">" + body;
        }

        result += "</a>\n";
      }
    }

    result += "</span>\n";
    result += "<script type=\"text/javascript\">\n";
    result += "$(document).ready(function() {\n";
    result += "$(\"span#" + name + " a\").lightBox({\n";
    result += "imageLoading: '" + res + "images/lightbox-ico-loading.gif',\n";
    result += "imageBtnPrev: '" + res + "images/lightbox-btn-prev.gif',\n";
    result += "imageBtnNext: '" + res + "images/lightbox-btn-next.gif',\n";
    result += "imageBtnClose: '" + res + "images/lightbox-btn-close.gif',\n";
    result += "imageBlank: '" + res + "images/lightbox-blank.gif'\n";
    result += "});\n";
    result += "});\n";
    result += "</script>\n";

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Config(Constants.CONFIG_IMAGEEXTENSION)
  private String extension;

  /** Field description */
  @Config(Constants.CONFIG_IMAGEFORMAT)
  private String format;

  /** Field description */
  private String id;
}
