/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

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
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.template.Template;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Tag;
import sonia.blog.entity.User;
import sonia.blog.util.TrackbackJob;
import sonia.blog.wui.model.EntryDataModel;

import sonia.config.Config;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

/**
 *
 * @author sdorra
 */
public class EntryBean extends AbstractEditorBean
{

  /** Field description */
  public static final String NAME = "EntryBean";

  /** Field description */
  private static final String DETAIL = "detail";

  /** Field description */
  private static final String EDITOR = "editor";

  /** Field description */
  private static Logger logger = Logger.getLogger(EntryBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public EntryBean()
  {
    super();
    entry = new Entry();
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
    Entry e = (Entry) entries.getRowData();

    setSessionVar();

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
  public String newEntry()
  {
    entry = new Entry();

    Blog blog = getRequest().getCurrentBlog();

    entry.setBlog(blog);
    tagString = "";
    setSessionVar();

    return EDITOR;
  }

  /**
   * Method description
   *
   *
   * @param event
   */
  @Override
  public void preview(ActionEvent event)
  {
    BlogRequest request = getRequest();
    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    String uri = linkBuilder.buildLink(request, "/entry-preview.jab");

    entry.setPublishingDate(new Date());
    entry.setAuthor(request.getUser());
    sendRedirect(uri);
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

      BlogRequest request = getRequest();

      if (entry.getId() == null)
      {
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
          doTrackback(request.getCurrentBlog());
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
          doTrackback(request.getCurrentBlog());
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

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   * @return
   */
  public List<Attachment> getAttachmentList()
  {
    AttachmentDAO attachmentDAO =
      BlogContext.getDAOFactory().getAttachmentDAO();

    return attachmentDAO.findAllByEntry(entry);
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
  public Integer getPageSize()
  {
    return pageSize;
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
  @Override
  public List<Attachment> getThumbnailList()
  {
    return BlogContext.getDAOFactory().getAttachmentDAO().findAllImagesByEntry(
        entry);
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
  @Override
  public boolean isNew()
  {
    return entry.getId() == null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public boolean isPublished()
  {
    return entry.isPublished();
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

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param parent
   *
   * @return
   */
  @Override
  protected File getAttachmentDirectory(File parent)
  {
    StringBuffer path = new StringBuffer();

    path.append(Constants.RESOURCE_ENTRIES).append(File.separator).append(
        entry.getId());

    return new File(parent, path.toString());
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param attachment
   */
  @Override
  protected void setRelation(Attachment attachment)
  {
    attachment.setEntry(entry);
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
   * @param blog
   */
  private void doTrackback(Blog blog)
  {
    if (entry.isPublished() && blog.isSendAutoPing())
    {
      ResourceBundle bundle = getResourceBundle("message");

      BlogContext.getInstance().getJobQueue().add(new TrackbackJob(entry,
              bundle));
    }
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

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   */
  private void setSessionVar()
  {
    getRequest().getSession().setAttribute("editor", "entry");
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private File directory;

  /** Field description */
  private DataModel entries;

  /** Field description */
  private Entry entry;

  /** Field description */
  @Config(Constants.CONFIG_ADMIN_PAGESIZE)
  private Integer pageSize = new Integer(20);

  /** Field description */
  private String tagString;

  /** Field description */
  private Tidy tidy;
}
