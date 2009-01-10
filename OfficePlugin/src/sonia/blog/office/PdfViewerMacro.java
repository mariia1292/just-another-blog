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
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;

import sonia.config.ConfigurationListener;
import sonia.config.ModifyableConfiguration;
import sonia.config.XmlConfiguration;

import sonia.macro.Macro;

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

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 *
 * @author sdorra
 */
public class PdfViewerMacro implements Macro, ConfigurationListener
{

  /** Field description */
  public static final String NAME = "pdfviewer";

  /** Field description */
  private static String PDFMIMETYPE = "application/pdf";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(PdfViewerMacro.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   */
  public PdfViewerMacro()
  {
    XmlConfiguration config = BlogContext.getInstance().getConfiguration();

    config.addListener(this);
    loadConfiguration(config);
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
    if (key.startsWith("image"))
    {
      loadConfiguration(config);
    }
  }

  /**
   * Method description
   *
   *
   * @param environment
   * @param body
   * @param parameters
   *
   * @return
   */
  public String excecute(Map<String, ?> environment, String body,
                         Map<String, String> parameters)
  {
    String result = null;
    Object object = environment.get("object");
    Blog blog = (Blog) environment.get("blog");
    BlogRequest request = (BlogRequest) environment.get("request");
    String linkBase = (String) environment.get("linkBase");
    BlogContext context = BlogContext.getInstance();

    if ((object != null) && (object instanceof Entry))
    {
      if ((parameters != null) && (parameters.containsKey("id")))
      {
        Long id = Long.parseLong(parameters.get("id"));
        AttachmentDAO attachmentDAO =
          BlogContext.getDAOFactory().getAttachmentDAO();
        Attachment attachment = attachmentDAO.findByBlogAndId(blog, id);

        if (attachment.getMimeType().equalsIgnoreCase(PDFMIMETYPE))
        {
          File attachmentFile =
            context.getResourceManager().getFile(attachment);

          if (attachmentFile.exists())
          {
            result = createPdfImageGallery(request, linkBase, id,
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
   * @param config
   */
  private void loadConfiguration(ModifyableConfiguration config)
  {
    extension = config.getString(Constants.CONFIG_IMAGEEXTENSION,
                                 Constants.DEFAULT_IMAGE_EXTENSION);
    format = config.getString(Constants.CONFIG_IMAGEFORMAT,
                              Constants.DEFAULT_IMAGE_FORMAT);
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
    String baseLink = linkBase + PdfViewerMappingEntry.PATH + path;

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
  private String extension;

  /** Field description */
  private String format;
}
