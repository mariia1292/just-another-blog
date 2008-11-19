/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.myfaces.custom.fileupload.UploadedFile;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Tag;
import sonia.blog.entity.User;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

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

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public EntryBean()
  {
    entry = new Entry();

    File resourceDirectory = BlogContext.getInstance().getResourceDirectory();

    directory = new File(resourceDirectory, "attachment");
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String edit()
  {
    entry = (Entry) entries.getRowData();
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

    return SUCCESS;
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

    return SUCCESS;
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
    EntityManager em = BlogContext.getInstance().getEntityManager();

    em.getTransaction().begin();

    try
    {
      em.remove(em.merge(entry));
      newEntry();
      em.getTransaction().commit();
      messageHandler.info("removeEntrySuccess");
    }
    catch (Exception ex)
    {
      if (em.getTransaction().isActive())
      {
        em.getTransaction().rollback();
      }

      logger.log(Level.SEVERE, null, ex);
      messageHandler.error("removeEntryFailure");
    }
    finally
    {
      em.close();
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

      File file = new File(directory, attachment.getFilePath());

      file.delete();
      em.getTransaction().commit();
      messageHandler.info("removeAttachmentSuccess");
    }
    catch (Exception ex)
    {
      if (em.getTransaction().isActive())
      {
        em.getTransaction().rollback();
      }

      logger.log(Level.SEVERE, null, ex);
      messageHandler.error("removeAttachmentFailure");
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

      if (entry.getId() == null)
      {
        BlogRequest request = getRequest();

        // entry.setBlog(blog);
        User author = request.getUser();

        entry.setAuthor(author);
        em.persist(entry);
        messageHandler.info("createEntrySuccess");
      }
      else
      {
        em.merge(entry);
        messageHandler.info("updateEntrySuccess");
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
      messageHandler.error("entryActionFailure");
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
    String result = SUCCESS;

    if (entry.isPublished() && (entry.getId() != null))
    {
      save();
    }
    else
    {
      saveDraft();
    }

    System.out.println("UPLOAD");

    Attachment attachment = new Attachment();

    attachment.setEntry(entry);
    attachment.setMimeType(uploadedFile.getContentType());
    attachment.setSize(uploadedFile.getSize());
    attachment.setName(uploadedFile.getName());
    System.out.println("ATTACHMENT CREATED");

    EntityManager em = BlogContext.getInstance().getEntityManager();

    em.getTransaction().begin();

    InputStream in = null;
    OutputStream out = null;

    try
    {
      in = uploadedFile.getInputStream();

      File dir = new File(directory, "" + entry.getId());

      if (!dir.exists())
      {
        dir.mkdirs();
      }

      System.out.println("FILE: " + dir.getPath());

      File file = new File(dir, "" + System.currentTimeMillis());

      out = new FileOutputStream(file);
      Util.copy(in, out);

      String path = file.getPath().substring(directory.getPath().length());

      attachment.setFilePath(path);
      em.persist(attachment);
      em.getTransaction().commit();
    }
    catch (Exception ex)
    {
      if (em.getTransaction().isActive())
      {
        em.getTransaction().rollback();
      }

      logger.log(Level.SEVERE, null, ex);
      result = FAILURE;
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

    return result;
  }

  //~--- get methods ----------------------------------------------------------

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
  public UploadedFile getUploadedFile()
  {
    return uploadedFile;
  }

  //~--- set methods ----------------------------------------------------------

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
    Blog blog = getRequest().getCurrentBlog();
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
          Query q = em.createNamedQuery("Tag.findFromBlogAndName");

          q.setParameter("blog", blog);
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private DataModel attachments;

  /** Field description */
  private File directory;

  /** Field description */
  private DataModel entries;

  /** Field description */
  private Entry entry;

  /** Field description */
  private String tagString;

  /** Field description */
  private UploadedFile uploadedFile;
}
