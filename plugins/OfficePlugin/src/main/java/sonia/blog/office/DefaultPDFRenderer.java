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
import sonia.blog.api.app.ResourceManager;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;

import sonia.jobqueue.JobException;

//~--- JDK imports ------------------------------------------------------------

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 *
 * @author Sebastian Sdorra
 */
public class DefaultPDFRenderer implements PDFRenderer
{

  /** Field description */
  public static final String RESOURCE = "pdf-gallery.xml";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blog
   * @param directory
   * @param attachment
   * @param format
   * @param extension
   *
   * @return
   *
   * @throws IOException
   */
  public PDFImageGallery renderPdf(Blog blog, File directory,
                                   Attachment attachment, String format,
                                   String extension)
          throws IOException
  {
    PDFImageGallery imageGallery = null;
    File file = new File(directory, RESOURCE);

    if (file.exists())
    {
      imageGallery = new PDFImageGallery(file);
    }
    else
    {
      ResourceManager manager = BlogContext.getInstance().getResourceManager();
      PDFRendererJob rendererJob = new PDFRendererJob(blog, directory,
                                     manager.getFile(attachment),
                                     attachment.getName(), format, extension);

      BlogContext.getInstance().getJobQueue().processs(rendererJob);
      imageGallery = rendererJob.getImageGallery();
    }

    return imageGallery;
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/10/12
   * @author         Enter your name here...
   */
  private class PDFRendererJob implements BlogJob
  {

    /**
     * Constructs ...
     *
     *
     *
     * @param blog
     * @param directory
     * @param pdfFile
     * @param title
     * @param format
     * @param extension
     */
    public PDFRendererJob(Blog blog, File directory, File pdfFile,
                          String title, String format, String extension)
    {
      this.blog = blog;
      this.directory = directory;
      this.pdfFile = pdfFile;
      this.title = title;
      this.format = format;
      this.extension = extension;
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
      try
      {
        if (!directory.mkdirs())
        {
          throw new JobException("could not create directory for pdf images");
        }

        List<String> images = new ArrayList<String>();
        RandomAccessFile raf = new RandomAccessFile(pdfFile, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0,
                                     channel.size());
        PDFFile pdf = new PDFFile(buf);
        int pages = pdf.getNumPages();

        for (int i = 1; i < pages; i++)
        {
          PDFPage page = pdf.getPage(i);
          String fileName = new StringBuffer(String.valueOf(i)).append(
                                ".").append(extension).toString();
          File pageFile = new File(directory, fileName);

          createPdfPage(page, pageFile);
          images.add(pageFile.getName());
        }

        imageGallery = new PDFImageGallery(title, images);
        imageGallery.toXML(new File(directory, RESOURCE));
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
      return "renders a pdf to images";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public PDFImageGallery getImageGallery()
    {
      return imageGallery;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getName()
    {
      return PDFRendererJob.class.getSimpleName();
    }

    //~--- methods ------------------------------------------------------------

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

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private Blog blog;

    /** Field description */
    private File directory;

    /** Field description */
    private String extension;

    /** Field description */
    private String format;

    /** Field description */
    private PDFImageGallery imageGallery;

    /** Field description */
    private File pdfFile;

    /** Field description */
    private String title;
  }
}
