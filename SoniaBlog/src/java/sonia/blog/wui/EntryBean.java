/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.myfaces.custom.fileupload.UploadedFile;

import org.w3c.tidy.Tidy;
import org.w3c.tidy.TidyMessage;
import org.w3c.tidy.TidyMessageListener;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.api.dao.CategoryDAO;
import sonia.blog.api.dao.DAOFactory;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.dao.TagDAO;
import sonia.blog.api.template.Template;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Tag;
import sonia.blog.entity.User;
import sonia.blog.util.AttachmentWrapper;
import sonia.blog.wui.model.EntryDataModel;
import sonia.blog.wui.model.GenericDataModel;

import sonia.config.Config;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.net.URLConnection;
import java.net.URLDecoder;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

/**
 *
 * @author sdorra
 */
public class EntryBean extends AbstractBean
{

  /** Field description */
  private static final String DETAIL = "detail";

  /** Field description */
  private static final String EDITOR = "editor";

  /** Field description */
  private static final String UPLOAD_FAILURE = "upload-failure";

  /** Field description */
  private static final String UPLOAD_SUCCESS = "upload-success";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public EntryBean()
  {
    super();
    entry = new Entry();
    resourceDirectory =
      BlogContext.getInstance().getResourceManager().getResourceDirectory();
  }

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
  public String edit()
  {
    Entry e = (Entry) entries.getRowData();

    return edit(e);
  }

  /**
   * Method description
   *
   *
   * @param entry
   *
   * @return
   */
  public String edit(Entry entry)
  {
    this.entry = entry;
    tagString = "";

    List<Tag> tags = entry.getTags();
    Iterator<Tag> it = tags.iterator();

    while (it.hasNext())
    {
      Tag t = it.next();

      tagString += t.getName();

      if (it.hasNext())
      {
        tagString += ", ";
      }
    }

    return EDITOR;
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
   *
   * @return
   */
  public String newEntry()
  {
    entry = new Entry();

    Blog blog = getRequest().getCurrentBlog();

    entry.setBlog(blog);
    tagString = "";

    return EDITOR;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String publish()
  {
    entry.publish();

    return save();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String remove()
  {
    String result = SUCCESS;
    Long id = entry.getId();

    if (id != null)
    {
      EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();

      try
      {
        if (entryDAO.remove(entry))
        {
          File attachmentDir = new File(getDirectory(), "" + id);

          if (attachmentDir.exists())
          {
            Util.delete(attachmentDir);
          }

          getMessageHandler().info("removeEntrySuccess");
          result = newEntry();
        }
        else
        {
          getMessageHandler().error("removeEntryFailure");
        }
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
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
    Attachment attachment = (Attachment) attachments.getRowData();
    File file = new File(getDirectory(), attachment.getFilePath());
    AttachmentDAO attachmentDAO =
      BlogContext.getDAOFactory().getAttachmentDAO();

    if (attachmentDAO.remove(attachment))
    {
      if (!file.delete())
      {
        logger.warning("could not remove file " + file.getAbsolutePath());
      }

      getMessageHandler().info("removeAttachmentSuccess");
    }
    else
    {
      getMessageHandler().error("removeAttachmentFailure");
    }
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String save()
  {
    String result = SUCCESS;
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();

    try
    {
      buildTagList();
      cleanupContent();

      if (entry.getId() == null)
      {
        BlogRequest request = getRequest();

        // entry.setBlog(blog);
        User author = request.getUser();

        if (entry.getTitle() == null)
        {
          entry.setTitle("NewEntry " + getDateString(request, new Date()));
        }

        if (entry.getCategories() == null)
        {
          CategoryDAO categoryDAO = DAOFactory.getInstance().getCategoryDAO();
          Category cat = categoryDAO.getFirst(getRequest().getCurrentBlog());

          entry.addCateogory(cat);
        }

        entry.setAuthor(author);

        if (entryDAO.add(entry))
        {
          getMessageHandler().info("createEntrySuccess");
        }
        else
        {
          result = FAILURE;
          getMessageHandler().error("entryActionFailure");
        }
      }
      else
      {
        if (entryDAO.edit(entry))
        {
          getMessageHandler().info("updateEntrySuccess");
        }
        else
        {
          result = FAILURE;
          getMessageHandler().error("entryActionFailure");
        }
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.WARNING, null, ex);
    }

    return result;
  }

  /**
   * Method description
   *
   * @return
   */
  public String saveAttachment()
  {
    String result = SUCCESS;
    AttachmentDAO attachmentDAO =
      BlogContext.getDAOFactory().getAttachmentDAO();

    if (!attachmentDAO.edit(attachment))
    {
      result = FAILURE;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String saveDraft()
  {
    entry.setPublished(false);

    return save();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String showDetail()
  {

    // detailEntry = (Entry) entries.getRowData();
    return DETAIL;
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

    if (entry.isPublished() && (entry.getId() != null))
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

      attachment.setEntry(entry);
      attachment.setMimeType(uploadedFile.getContentType());
      attachment.setSize(uploadedFile.getSize());
      attachment.setName(uploadedFile.getName());
      attachment.setDescription(uploadDescription);

      InputStream in = null;
      OutputStream out = null;

      try
      {
        in = uploadedFile.getInputStream();

        File rootDir = getDirectory();
        File dir = new File(rootDir,
                            Constants.RESOURCE_ENTRIES + File.separator
                            + entry.getId());

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

        if (!BlogContext.getDAOFactory().getAttachmentDAO().add(attachment))
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
   */
  public DataModel getAttachments()
  {
    attachments = new ListDataModel();

    if ((entry != null) && (entry.getId() != null))
    {
      AttachmentDAO attachmentDAO =
        BlogContext.getDAOFactory().getAttachmentDAO();
      List<Attachment> attachmentList = attachmentDAO.findAllByEntry(entry);
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
  public String getBaseUrl()
  {
    return BlogContext.getInstance().getLinkBuilder().buildLink(getRequest(),
            "/");
  }

  /**
   * Method description
   *
   * @return
   */
  public SelectItem[] getCategoryItems()
  {
    SelectItem[] items = null;
    CategoryDAO categoryDAO = BlogContext.getDAOFactory().getCategoryDAO();
    Blog blog = getRequest().getCurrentBlog();
    List<Category> categoryList = categoryDAO.getAll(blog);

    if ((categoryList != null) &&!categoryList.isEmpty())
    {
      int size = categoryList.size();

      if (categoryList != null)
      {
        items = new SelectItem[size];

        for (int i = 0; i < size; i++)
        {
          Category c = categoryList.get(i);

          items[i] = new SelectItem(c, c.getName(), c.getDescription());
        }
      }
    }
    else
    {
      items = new SelectItem[0];
    }

    return items;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getContentCSS()
  {
    BlogRequest request = getRequest();
    Template template =
      BlogContext.getInstance().getTemplateManager().getTemplate(
          request.getCurrentBlog());

    return BlogContext.getInstance().getLinkBuilder().buildLink(request,
            template.getContentCSS());
  }

  /**
   * Method description
   *
   * @return
   */
  public DataModel getEntries()
  {
    Blog blog = getRequest().getCurrentBlog();

    entries = new EntryDataModel(blog, pageSize);

    return entries;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Entry getEntry()
  {
    return entry;
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
  public String getTagString()
  {
    return tagString;
  }

  /**
   * Method description
   *
   * @return
   */
  public DataModel getThumbnails()
  {
    DataModel images = new ListDataModel();

    if (entry != null)
    {
      AttachmentDAO attachmentDAO =
        BlogContext.getDAOFactory().getAttachmentDAO();
      List<Attachment> attachmentList =
        attachmentDAO.findAllImagesByEntry(entry);

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
  public Tidy getTitdy()
  {
    if (tidy == null)
    {
      tidy = new Tidy();
      tidy.setQuiet(true);
      tidy.setMessageListener(new TidyMessageListener()
      {
        public void messageReceived(TidyMessage message)
        {
          String msg = message.getLine() + " : " + message.getColumn() + " "
                       + message.getMessage();

          if ((message.getLevel() == TidyMessage.Level.INFO)
              || (message.getLevel() == TidyMessage.Level.SUMMARY))
          {
            logger.info(msg);
          }
          else if (message.getLevel() == TidyMessage.Level.WARNING)
          {
            logger.warning(msg);
          }
          else if (message.getLevel() == TidyMessage.Level.ERROR)
          {
            logger.severe(msg);
          }
        }
      });
      tidy.setPrintBodyOnly(true);

      File configDir =
        BlogContext.getInstance().getConfigFile().getParentFile();
      String path = new File(configDir, "tidy.properties").getPath();

      if (logger.isLoggable(Level.INFO))
      {
        logger.info("load tidy configuration " + path);
      }

      tidy.setConfigurationFromFile(path);
    }

    return tidy;
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
   * @param entry
   */
  public void setEntry(Entry entry)
  {
    this.entry = entry;
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
   * @param tagString
   */
  public void setTagString(String tagString)
  {
    this.tagString = tagString;
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
   */
  private void buildTagList()
  {
    List<Tag> tags = new ArrayList<Tag>();
    List<Tag> oldTags = entry.getTags();
    TagDAO tagDAO = BlogContext.getDAOFactory().getTagDAO();

    if (!Util.isBlank(tagString))
    {
      Set<String> tagSet = Util.createSet(tagString, ",");

      for (String tag : tagSet)
      {
        tag = tag.trim();

        Tag t = null;

        for (Tag otag : oldTags)
        {
          if (otag.getName().equals(tag))
          {
            t = otag;
          }
        }

        if (t == null)
        {
          t = tagDAO.findByName(tag);
        }

        if (t == null)
        {
          t = new Tag(tag);
          tagDAO.add(t);
        }

        tags.add(t);
      }
    }

    for (Tag t : oldTags)
    {
      if (!tags.contains(t))
      {
        t.getEntries().remove(entry);

        if (t.getEntries().isEmpty())
        {
          tagDAO.remove(t);
        }
        else
        {
          tagDAO.edit(t);
        }
      }
    }

    entry.setTags(tags);
  }

  /**
   * Method description
   *
   */
  private void cleanupContent()
  {
    if (BlogContext.getInstance().getConfiguration().getBoolean(
            Constants.CONFIG_CLEANUPCODE, Boolean.FALSE))
    {
      getTitdy();

      ByteArrayInputStream bais = null;
      ByteArrayOutputStream baos = null;
      String content = entry.getContent();

      if ((content != null) && (content.length() > 0))
      {
        bais = new ByteArrayInputStream(content.getBytes());
        baos = new ByteArrayOutputStream();
        tidy.parse(bais, baos);
        entry.setContent(baos.toString());
      }

      String teaser = entry.getTeaser();

      if ((teaser != null) && (teaser.length() > 0))
      {
        bais = new ByteArrayInputStream(teaser.getBytes());
        baos = new ByteArrayOutputStream();
        tidy.parse(bais, baos);
        entry.setTeaser(baos.toString());
      }
    }
  }

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
        File dir = new File(root,
                            Constants.RESOURCE_ENTRIES + File.separator
                            + entry.getId());

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

              attachment.setEntry(entry);
              attachment.setFilePath(path);
              attachment.setMimeType(
                  URLConnection.getFileNameMap().getContentTypeFor(name));
              attachment.setName(name);
              attachment.setSize(file.length());

              if (!Util.isBlank(uploadDescription))
              {
                attachment.setDescription(uploadDescription);
              }
              else
              {
                attachment.setDescription(uploadedFile.getName());
              }

              if (!BlogContext.getDAOFactory().getAttachmentDAO().add(
                      attachment))
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
   * @param request
   * @param date
   *
   * @return
   */
  private String getDateString(BlogRequest request, Date date)
  {
    SimpleDateFormat sdf =
      new SimpleDateFormat(request.getCurrentBlog().getDateFormat());

    return sdf.format(date);
  }

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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Attachment attachment;

  /** Field description */
  private DataModel attachments;

  /** Field description */
  private File directory;

  /** Field description */
  private DataModel entries;

  /** Field description */
  private Entry entry;

  /** Field description */
  private String imageSize = "";

  /** Field description */
  @Config(Constants.CONFIG_ADMIN_PAGESIZE)
  private Integer pageSize = new Integer(20);

  /** Field description */
  private File resourceDirectory;

  /** Field description */
  private String tagString;

  /** Field description */
  private Tidy tidy;

  /** Field description */
  private boolean unzipFiles = false;

  /** Field description */
  private String uploadDescription;

  /** Field description */
  private UploadedFile uploadedFile;
}
