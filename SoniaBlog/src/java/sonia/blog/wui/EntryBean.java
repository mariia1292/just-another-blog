/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.myfaces.custom.fileupload.UploadedFile;

import org.w3c.tidy.Tidy;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.template.Template;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Tag;
import sonia.blog.entity.User;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URLConnection;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

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

    attachment = (Attachment) attachments.getRowData();

    if (attachment != null)
    {
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
    entry.setPublished(true);

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
      EntityManager em = BlogContext.getInstance().getEntityManager();

      em.getTransaction().begin();

      try
      {
        List<Attachment> attachmentList = entry.getAttachments();

        if (attachmentList != null)
        {
          for (Attachment a : attachmentList)
          {
            em.remove(em.merge(a));
          }
        }

        em.remove(em.merge(entry));
        newEntry();
        em.getTransaction().commit();

        File attachmentDir = new File(getDirectory(), "" + id);

        if (attachmentDir.exists())
        {
          Util.delete(attachmentDir);
        }

        getMessageHandler().info("removeEntrySuccess");
      }
      catch (Exception ex)
      {
        if (em.getTransaction().isActive())
        {
          em.getTransaction().rollback();
        }

        logger.log(Level.SEVERE, null, ex);
        getMessageHandler().error("removeEntryFailure");
      }
      finally
      {
        em.close();
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param event
   */
  public void removeAttachment(ActionEvent event)
  {
    Attachment attachment = (Attachment) attachments.getRowData();
    EntityManager em = BlogContext.getInstance().getEntityManager();

    em.getTransaction().begin();

    try
    {
      em.remove(em.merge(attachment));

      File file = new File(getDirectory(), attachment.getFilePath());

      file.delete();
      em.getTransaction().commit();
      getMessageHandler().info("removeAttachmentSuccess");
    }
    catch (Exception ex)
    {
      if (em.getTransaction().isActive())
      {
        em.getTransaction().rollback();
      }

      logger.log(Level.SEVERE, null, ex);
      getMessageHandler().error("removeAttachmentFailure");
    }
    finally
    {
      em.close();
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
    EntityManager em = BlogContext.getInstance().getEntityManager();

    em.getTransaction().begin();

    try
    {
      buildTagList(em);
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

        if (entry.getCategory() == null)
        {
          Category cat = findCategory(em, request.getCurrentBlog());

          entry.setCategory(cat);
        }

        entry.setAuthor(author);
        em.persist(entry);
        getMessageHandler().info("createEntrySuccess");
      }
      else
      {
        em.merge(entry);
        getMessageHandler().info("updateEntrySuccess");
      }

      em.getTransaction().commit();
    }
    catch (Exception ex)
    {
      logger.log(Level.WARNING, null, ex);

      if (em.getTransaction().isActive())
      {
        em.getTransaction().rollback();
      }

      result = FAILURE;
      getMessageHandler().error("entryActionFailure");
    }

    em.close();

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String saveAttachment()
  {
    EntityManager em = BlogContext.getInstance().getEntityManager();

    em.getTransaction().begin();

    try
    {
      attachment = em.merge(attachment);
      em.getTransaction().commit();
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);

      if (em.getTransaction().isActive())
      {
        em.getTransaction().rollback();
      }
    }
    finally
    {
      if (em != null)
      {
        em.close();
      }
    }

    return SUCCESS;
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

      EntityManager em = BlogContext.getInstance().getEntityManager();

      em.getTransaction().begin();

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
        em.persist(attachment);
        uploadDescription = null;
        em.getTransaction().commit();
      }
      catch (Exception ex)
      {
        if (em.getTransaction().isActive())
        {
          em.getTransaction().rollback();
        }

        logger.log(Level.SEVERE, null, ex);
        result = UPLOAD_FAILURE;
      }
      finally
      {
        em.close();

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
   *
   * @return
   */
  public DataModel getAttachments()
  {
    attachments = new ListDataModel();

    if ((entry != null) && (entry.getId() != null))
    {
      EntityManager em = BlogContext.getInstance().getEntityManager();
      Query q = em.createNamedQuery("Attachment.entryOverview");

      q.setParameter("entry", entry);

      List list = q.getResultList();

      if (list != null)
      {
        attachments.setWrappedData(list);
      }

      em.close();
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
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public SelectItem[] getCategoryItems()
  {
    SelectItem[] items = null;
    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("Category.findAllFromBlog");
    Blog blog = getRequest().getCurrentBlog();

    q.setParameter("blog", blog);

    List<Category> categoryList = q.getResultList();
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

    em.close();

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
   *
   * @return
   */
  public DataModel getEntries()
  {
    entries = new ListDataModel();

    EntityManager em = BlogContext.getInstance().getEntityManager();
    List list = null;
    Query q = em.createNamedQuery("Entry.findFromBlog");

    q.setParameter("blog", getRequest().getCurrentBlog());
    list = q.getResultList();

    if (list != null)
    {
      entries.setWrappedData(list);
    }

    em.close();

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
   *
   * @return
   */
  public DataModel getThumbnails()
  {
    DataModel images = new ListDataModel();

    if (entry != null)
    {
      EntityManager em = BlogContext.getInstance().getEntityManager();
      Query q = em.createNamedQuery("Attachment.findImagesFromEntry");

      q.setParameter("entry", entry);

      try
      {
        List list = q.getResultList();

        if (list != null)
        {
          images.setWrappedData(list);
        }
      }
      catch (NoResultException ex) {}

      em.close();
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
      tidy.setPrintBodyOnly(true);

      File configDir =
        BlogContext.getInstance().getConfigFile().getParentFile();

      tidy.setConfigurationFromFile(new File(configDir,
              "tidy.properties").getPath());
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
   * @param em
   *
   */
  private void buildTagList(EntityManager em)
  {
    List<Tag> tags = new ArrayList<Tag>();
    List<Tag> oldTags = entry.getTags();

    if ((tagString != null) && (tagString.length() > 0))
    {
      String[] tagArray = tagString.split(",");

      for (String tag : tagArray)
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
          Query q = em.createNamedQuery("Tag.findFromName");

          q.setParameter("name", tag);

          try
          {
            t = (Tag) q.getSingleResult();
          }
          catch (NoResultException ex) {}
          catch (Exception ex)
          {
            logger.log(Level.SEVERE, null, ex);

            continue;
          }
        }

        if (t == null)
        {
          t = new Tag(tag);
          em.persist(t);
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
          em.remove(em.merge(t));
        }
        else
        {
          em.merge(t);
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
   * @param em
   * @param blog
   *
   * @return
   */
  private Category findCategory(EntityManager em, Blog blog)
  {
    Query q = em.createNamedQuery("Category.findFirstFromBlog");

    q.setParameter("blog", blog);

    return (Category) q.getSingleResult();
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
        EntityManager em = BlogContext.getInstance().getEntityManager();
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
              em.getTransaction().begin();

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

              em.persist(attachment);
              em.getTransaction().commit();
            }
            catch (IOException ex)
            {
              if (em.getTransaction().isActive())
              {
                em.getTransaction().rollback();
              }

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
