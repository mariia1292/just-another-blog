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



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.myfaces.custom.fileupload.UploadedFile;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.editor.EditorProvider;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;
import sonia.blog.util.AttachmentWrapper;

import sonia.macro.Macro;
import sonia.macro.MacroParser;
import sonia.macro.browse.MacroInformation;
import sonia.macro.browse.MacroInformationProvider;

import sonia.plugin.service.ServiceReference;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.net.URLConnection;
import java.net.URLDecoder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Sebastian Sdorra
 */
public abstract class AbstractEditorBean extends AbstractBean
{

  /** Field description */
  private static final String UPLOAD_FAILURE = "upload-failure";

  /** Field description */
  private static final String UPLOAD_SUCCESS = "upload-success";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(AbstractEditorBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public AbstractEditorBean()
  {
    super();
    resourceDirectory =
      BlogContext.getInstance().getResourceManager().getResourceDirectory();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param event
   */
  public abstract void preview(ActionEvent event);

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract String save();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract String saveDraft();

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract ContentObject getObject();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract boolean isNew();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract boolean isPublished();

  /**
   * Method description
   *
   *
   * @param parent
   *
   * @return
   */
  protected abstract File getAttachmentDirectory(File parent);

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract List<Attachment> getAttachmentList();

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract List<Attachment> getThumbnailList();

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param attachment
   */
  protected abstract void setRelation(Attachment attachment);

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String abortAttachmentEdit()
  {
    return SUCCESS;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String editAttachment()
  {
    String result = FAILURE;
    AttachmentWrapper wrapper = (AttachmentWrapper) attachments.getRowData();

    if (wrapper != null)
    {
      attachment = wrapper.getAttachment();
      result = SUCCESS;
    }

    return result;
  }

  /**
   * Method description
   *
   * @param event
   */
  public void removeAttachment(ActionEvent event)
  {
    AttachmentWrapper wrapper = (AttachmentWrapper) attachments.getRowData();
    Attachment a = wrapper.getAttachment();
    File file = new File(getDirectory(), a.getFilePath());

    if (BlogContext.getDAOFactory().getAttachmentDAO().remove(getBlogSession(),
            a))
    {
      if (!file.delete())
      {
        logger.warning("could not remove file " + file.getAbsolutePath());
      }

      getMessageHandler().info(getRequest(), "removeAttachmentSuccess");
    }
    else
    {
      getMessageHandler().error(getRequest(), "removeAttachmentFailure");
    }
  }

  /**
   * Method description
   *
   * @return
   */
  public String saveAttachment()
  {
    String result = SUCCESS;

    if (!BlogContext.getDAOFactory().getAttachmentDAO().edit(getBlogSession(),
            attachment))
    {
      result = FAILURE;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   *
   * @return
   */
  public String upload()
  {
    String result = UPLOAD_SUCCESS;

    if (isPublished() &&!isNew())
    {
      save();
    }
    else
    {
      saveDraft();
    }

    if (unzipFiles && uploadedFile.getName().endsWith(".zip"))
    {
      result = unzip();
    }
    else
    {
      Attachment attachment = new Attachment();

      attachment.setMimeType(uploadedFile.getContentType());
      attachment.setSize(uploadedFile.getSize());
      attachment.setName(getUploadedFilename());
      attachment.setDescription(uploadDescription);
      setRelation(attachment);

      InputStream in = null;
      OutputStream out = null;

      try
      {
        in = uploadedFile.getInputStream();

        File rootDir = getDirectory();
        File dir = getAttachmentDirectory(rootDir);

        if (!dir.exists())
        {
          dir.mkdirs();
        }

        File file = new File(dir, "" + System.currentTimeMillis());

        out = new FileOutputStream(file);
        Util.copy(in, out);

        String path = file.getAbsolutePath().substring(
                          resourceDirectory.getAbsolutePath().length());

        attachment.setFilePath(path);

        if (!BlogContext.getDAOFactory().getAttachmentDAO().add(
                getBlogSession(), attachment))
        {
          result = UPLOAD_FAILURE;
        }
        else
        {
          uploadDescription = null;
        }
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
        result = UPLOAD_FAILURE;
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

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Attachment getAttachment()
  {
    return attachment;
  }

  /**
   * Method description
   *
   * @return
   *
   * @throws UnsupportedEncodingException
   */
  public DataModel getAttachments() throws UnsupportedEncodingException
  {
    attachments = new ListDataModel();

    if (!isNew())
    {
      List<Attachment> attachmentList = getAttachmentList();
      String content = getRequest().getParameter("content");

      if (!Util.isBlank(content))
      {
        try
        {
          content = URLDecoder.decode(content, "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }

      if ((attachmentList != null) &&!attachmentList.isEmpty())
      {
        List<AttachmentWrapper> wrapperList =
          new ArrayList<AttachmentWrapper>();

        for (Attachment a : attachmentList)
        {
          wrapperList.add(new AttachmentWrapper(a, content));
        }

        attachments.setWrappedData(wrapperList);
      }
    }

    return attachments;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getEditor()
  {
    ServiceReference<EditorProvider> reference =
      BlogContext.getInstance().getServiceRegistry().get(EditorProvider.class,
        Constants.SERVICE_EDITORPROVIDER);

    if (reference != null)
    {
      editorProvider = reference.get();
    }

    String result = "";

    if (editorProvider != null)
    {
      result = editorProvider.renderEditor(getRequest(),
              new String[] { "editorForm:content",
                             "editorForm:teaser" });
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getImageHandlerUri()
  {
    BlogRequest request = getRequest();
    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    String result = linkBuilder.buildLink(request.getCurrentBlog(), "/image/");

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getImageSize()
  {
    return imageSize;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public MacroInformation getMacroInformation()
  {
    MacroInformation info = null;
    BlogRequest request = getRequest();
    String name = request.getParameter("macro");

    if (Util.hasContent(name))
    {
      MacroParser parser = BlogContext.getInstance().getMacroParser();
      Class<? extends Macro> macro = parser.getMacro(name);

      if (macro != null)
      {
        info = parser.getInformationProvider().getInformation(macro,
                getLocale());
      }
    }

    return info;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getMacroInformations()
  {
    Locale locale = getLocale();

    if (macroInformations == null)
    {
      List<MacroInformation> informations = new ArrayList<MacroInformation>();
      MacroParser parser = BlogContext.getInstance().getMacroParser();
      MacroInformationProvider provider = parser.getInformationProvider();

      if (provider != null)
      {
        Iterator<Class<? extends Macro>> macroIterator = parser.getMacros();

        while (macroIterator.hasNext())
        {
          Class<? extends Macro> macro = macroIterator.next();
          MacroInformation info = provider.getInformation(macro, locale);

          if (info != null)
          {
            informations.add(info);
          }
        }

        macroInformations = new ListDataModel(informations);
      }
    }

    return macroInformations;
  }

  /**
   * Method description
   *
   * @return
   */
  public DataModel getThumbnails()
  {
    DataModel images = new ListDataModel();

    if (!isNew())
    {
      List<Attachment> attachmentList = getThumbnailList();

      if ((attachmentList != null) &&!attachmentList.isEmpty())
      {
        images.setWrappedData(attachmentList);
      }
    }

    return images;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getUploadDescription()
  {
    return uploadDescription;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public UploadedFile getUploadedFile()
  {
    return uploadedFile;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isUnzipFiles()
  {
    return unzipFiles;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param attachment
   */
  public void setAttachment(Attachment attachment)
  {
    this.attachment = attachment;
  }

  /**
   * Method description
   *
   *
   * @param imageSize
   */
  public void setImageSize(String imageSize)
  {
    this.imageSize = imageSize;
  }

  /**
   * Method description
   *
   *
   * @param unzipFiles
   */
  public void setUnzipFiles(boolean unzipFiles)
  {
    this.unzipFiles = unzipFiles;
  }

  /**
   * Method description
   *
   *
   * @param uploadDescription
   */
  public void setUploadDescription(String uploadDescription)
  {
    this.uploadDescription = uploadDescription;
  }

  /**
   * Method description
   *
   *
   * @param uploadedFile
   */
  public void setUploadedFile(UploadedFile uploadedFile)
  {
    this.uploadedFile = uploadedFile;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private String unzip()
  {
    InputStream in = null;
    String result = UPLOAD_SUCCESS;

    try
    {
      in = uploadedFile.getInputStream();

      if (in != null)
      {
        ZipInputStream zis = new ZipInputStream(in);
        ZipEntry ze = zis.getNextEntry();
        File root = getDirectory();
        File dir = getAttachmentDirectory(root);

        if (!dir.exists())
        {
          dir.mkdirs();
        }

        while (ze != null)
        {
          if (!ze.isDirectory())
          {
            try
            {
              File file = new File(dir, "" + System.currentTimeMillis());

              Util.copy(zis, new FileOutputStream(file));

              String name = ze.getName();

              name = name.replaceAll("/", "-");

              Attachment attachment = new Attachment();
              String path = file.getAbsolutePath().substring(
                                resourceDirectory.getAbsolutePath().length());

              attachment.setFilePath(path);
              attachment.setMimeType(
                  URLConnection.getFileNameMap().getContentTypeFor(name));
              attachment.setName(name);
              attachment.setSize(file.length());
              setRelation(attachment);

              if (!Util.isBlank(uploadDescription))
              {
                attachment.setDescription(uploadDescription);
              }
              else
              {
                attachment.setDescription(uploadedFile.getName());
              }

              if (!BlogContext.getDAOFactory().getAttachmentDAO().add(
                      getBlogSession(), attachment))
              {
                result = UPLOAD_FAILURE;
              }
            }
            catch (IOException ex)
            {
              logger.log(Level.SEVERE, null, ex);
            }
          }

          ze = zis.getNextEntry();
        }

        uploadDescription = null;
      }
      else
      {
        result = UPLOAD_FAILURE;
      }
    }
    catch (IOException ex)
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
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private File getDirectory()
  {
    if (directory == null)
    {
      Blog blog = getRequest().getCurrentBlog();
      ResourceManager manager = BlogContext.getInstance().getResourceManager();

      directory = manager.getDirectory(Constants.RESOURCE_ATTACHMENT, blog);
    }

    return directory;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  private String getUploadedFilename()
  {
    String name = uploadedFile.getName();

    if (Util.hasContent(name))
    {
      int index = name.lastIndexOf("\\");

      if ((index > 0) && (name.length() > index + 1))
      {
        name = name.substring(index + 1);
      }
    }

    return name;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Attachment attachment;

  /** Field description */
  private DataModel attachments;

  /** Field description */
  private File directory;

  /** Field description */
  private EditorProvider editorProvider;

  /** Field description */
  private String imageSize = "";

  /** Field description */
  private DataModel macroInformations;

  /** Field description */
  private File resourceDirectory;

  /** Field description */
  private boolean unzipFiles = false;

  /** Field description */
  private String uploadDescription;

  /** Field description */
  private UploadedFile uploadedFile;
}
