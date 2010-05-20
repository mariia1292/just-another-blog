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



package sonia.blog.upload;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.Context;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.editor.EditorUtil;
import sonia.blog.api.exception.BlogException;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.api.mapping.MappingConfig;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URLConnection;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
@MappingConfig(regex = "^/editor/upload$")
public class MultiUploadMapping extends FinalMapping
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(MultiUploadMapping.class.getName());

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
    String name = request.getParameter("name");

    if (Util.isNotEmpty(name))
    {
      addAttachment(request, response, name);
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
   * @param request
   * @param response
   * @param name
   *
   * @throws IOException
   */
  private void addAttachment(BlogRequest request, BlogResponse response,
                             String name)
          throws IOException
  {
    ContentObject object = EditorUtil.getInstance().getCurrentObject(request);

    if (object != null)
    {
      if (object.getId() == null)
      {
        save(request, object);
      }

      Attachment attachement = createAttachment(request, object, name);

      if (attachement != null)
      {
        BlogContext.getDAOFactory().getAttachmentDAO().add(
            request.getBlogSession(), attachement);
        EditorUtil.getInstance().setCurrentObject(request, object);
      }
      else
      {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
    }
    else
    {
      throw new BlogException("no contentobject found");
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param object
   * @param name
   *
   * @return
   */
  private Attachment createAttachment(BlogRequest request,
          ContentObject object, String name)
  {
    File resourceDirectory = resourceManager.getResourceDirectory();
    File attachmentDirectory =
      resourceManager.getDirectory(Constants.RESOURCE_ATTACHMENT,
                                   request.getCurrentBlog());
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

    path.append(File.separator).append(object.getId());

    File directory = new File(attachmentDirectory, path.toString());

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
      createAttachmentFile(request, attachmentFile);
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
   * @param request
   * @param file
   *
   * @throws IOException
   */
  private void createAttachmentFile(BlogRequest request, File file)
          throws IOException
  {
    InputStream in = null;
    FileOutputStream out = null;

    try
    {
      in = request.getInputStream();
      out = new FileOutputStream(file);
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

  /**
   * Method description
   *
   *
   * @param request
   * @param object
   */
  private void save(BlogRequest request, ContentObject object)
  {
    if (object instanceof Entry)
    {
      Entry entry = (Entry) object;

      if (entry.getAuthor() == null)
      {
        entry.setAuthor(request.getUser());
      }

      if (Util.isEmpty(entry.getTitle()))
      {
        entry.setTitle("Autosave");
      }

      BlogContext.getDAOFactory().getEntryDAO().add(request.getBlogSession(),
              entry);
    }
    else if (object instanceof Page)
    {
      Page page = (Page) object;

      if (page.getAuthor() == null)
      {
        page.setAuthor(request.getUser());
      }

      if (Util.isEmpty(page.getTitle()))
      {
        page.setTitle("Autosave");
      }

      BlogContext.getDAOFactory().getPageDAO().add(request.getBlogSession(),
              page);
    }
    else
    {
      throw new IllegalArgumentException("page or entry is required");
    }
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
  @Context
  private ResourceManager resourceManager;
}
