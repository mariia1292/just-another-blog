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



package sonia.blog.api.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.exception.BlogException;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URLConnection;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class AttachmentFacade
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(AttachmentFacade.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public AttachmentFacade()
  {
    BlogContext context = BlogContext.getInstance();

    this.resourceManager = context.getResourceManager();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param object
   * @param content
   * @param name
   *
   * @return
   */
  public Attachment createAttachment(ContentObject object, InputStream content,
                                     String name)
  {
    File resourceDirectory = resourceManager.getResourceDirectory();
    File attachmentDirectory =
      resourceManager.getDirectory(Constants.RESOURCE_ATTACHMENT,
                                   object.getBlog());
    File directory = new File(attachmentDirectory, createPath(object));

    if (!directory.exists())
    {
      if (!directory.mkdirs())
      {
        throw new BlogException("could not create directory");
      }
    }

    File attachmentFile = new File(directory, UUID.randomUUID().toString());
    Attachment attachment = null;

    try
    {
      createAttachmentFile(content, attachmentFile);
      attachment = new Attachment();
      attachment.setName(name);

      String relativePath = attachmentFile.getAbsolutePath().substring(
                                resourceDirectory.getAbsolutePath().length());

      attachment.setFilePath(relativePath);

      String contentType =
        URLConnection.getFileNameMap().getContentTypeFor(name);

      if (Util.isEmpty(contentType))
      {
        contentType = "application/octet-stream";
      }

      attachment.setMimeType(contentType);
      attachment.setSize(attachmentFile.length());
      setRelation(attachment, object);
    }
    catch (IOException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }

    return attachment;
  }

  /**
   * Method description
   *
   *
   * @param content
   * @param attachmentFile
   *
   * @throws IOException
   */
  private void createAttachmentFile(InputStream content, File attachmentFile)
          throws IOException
  {
    OutputStream out = null;

    try
    {
      out = new FileOutputStream(attachmentFile);
      Util.copy(content, out);
    }
    finally
    {
      content.close();
      out.close();
    }
  }

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  private String createPath(ContentObject object)
  {
    StringBuffer path = new StringBuffer();

    if (object instanceof Entry)
    {
      path.append(Constants.RESOURCE_ENTRIES);
    }
    else if (object instanceof Page)
    {
      path.append(Constants.RESOURCE_PAGES);
    }
    else
    {
      throw new IllegalArgumentException("page or entry is required");
    }

    return path.append(File.separator).append(object.getId()).toString();
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param attachment
   * @param object
   */
  private void setRelation(Attachment attachment, ContentObject object)
  {
    if (object instanceof Entry)
    {
      attachment.setEntry((Entry) object);
    }
    else if (object instanceof Page)
    {
      attachment.setPage((Page) object);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ResourceManager resourceManager;
}
