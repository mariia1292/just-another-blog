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

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogSession;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.authentication.RequireRole;
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
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Role;
import sonia.blog.entity.Tag;
import sonia.blog.entity.User;
import sonia.blog.util.AutoTrackbackJob;
import sonia.blog.util.TrackbackJob;
import sonia.blog.wui.model.EntryDataModel;

import sonia.config.Config;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.net.URL;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

/**
 *
 * @author Sebastian Sdorra
 */
@RequireRole(Role.AUTHOR)
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
    String uri = linkBuilder.getRelativeLink(request, "/entry-preview.jab");

    if (entry.getPublishingDate() == null)
    {
      entry.setPublishingDate(new Date());
    }

    if (entry.getAuthor() == null)
    {
      entry.setAuthor(request.getUser());
    }

    sendRedirect(uri);
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
        if (entryDAO.remove(getBlogSession(), entry))
        {
          File attachmentDir = new File(getDirectory(), "" + id);

          if (attachmentDir.exists())
          {
            Util.delete(attachmentDir);
          }

          getMessageHandler().info(getRequest(), "removeEntrySuccess");
          result = newEntry();
        }
        else
        {
          getMessageHandler().error(getRequest(), "removeEntryFailure");
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
      BlogRequest request = getRequest();
      BlogSession session = request.getBlogSession();

      buildTagList(session);

      if (entry.getId() == null)
      {
        User author = session.getUser();

        if (entry.getBlog() == null){
          entry.setBlog( request.getCurrentBlog() );
        }

        if (entry.getTitle() == null)
        {
          entry.setTitle("NewEntry " + getDateString(request, new Date()));
        }

        // TODO check if needed
        if (entry.getCategories() == null)
        {
          CategoryDAO categoryDAO = DAOFactory.getInstance().getCategoryDAO();
          Category cat = categoryDAO.getFirst(getRequest().getCurrentBlog());

          if (cat != null)
          {
            entry.addCateogory(cat);
          }
        }

        entry.setAuthor(author);

        if (entryDAO.add(session, entry))
        {
          doTrackback(request);
          getMessageHandler().info(request, "createEntrySuccess");
        }
        else
        {
          result = FAILURE;
          getMessageHandler().error(request, "entryActionFailure");
        }
      }
      else
      {
        if (entryDAO.edit(session, entry))
        {
          doTrackback(request);
          getMessageHandler().info(request, "updateEntrySuccess");
        }
        else
        {
          result = FAILURE;
          getMessageHandler().error(request, "entryActionFailure");
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
   * @param event
   */
  public void sendTrackback(ActionEvent event)
  {
    try
    {
      BlogRequest request = getRequest();

      BlogContext.getInstance().getJobQueue().add(new TrackbackJob(request,
              entry, new URL(trackbackURL)));
      getMessageHandler().info(getRequest(), "sendTrackBackSuccess");
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
      getMessageHandler().warn(getRequest(), "sendTrackBackFailure");
    }
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

    return attachmentDAO.getAll(entry);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getBaseUrl()
  {
    return BlogContext.getInstance().getLinkBuilder().getRelativeLink(
        getRequest(), "/");
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

    return BlogContext.getInstance().getLinkBuilder().getRelativeLink(request,
            template.getContentCSS());
  }

  /**
   * Method description
   *
   * @return
   */
  public DataModel getEntries()
  {
    entries = new EntryDataModel(getBlogSession(), pageSize);

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
  @Override
  public ContentObject getObject()
  {
    return getEntry();
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
    return BlogContext.getDAOFactory().getAttachmentDAO().getAllImages(entry);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTrackbackURL()
  {
    return trackbackURL;
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
   */
  public void setSessionVar()
  {
    getRequest().getSession().setAttribute("editor", "entry");
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
   * @param trackbackURL
   */
  public void setTrackbackURL(String trackbackURL)
  {
    this.trackbackURL = trackbackURL;
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
   *
   * @param session
   */
  private void buildTagList(BlogSession session)
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
          t = tagDAO.get(tag);
        }

        if (t == null)
        {
          t = new Tag(tag);
          tagDAO.add(session, t);
        }

        tags.add(t);
      }
    }

    List<Tag> removeAbleTags = new ArrayList<Tag>();

    for (Tag t : oldTags)
    {
      if (!tags.contains(t))
      {
        t.getEntries().remove(entry);

        if (t.getEntries().isEmpty())
        {
          removeAbleTags.add(t);
        }
        else
        {
          tagDAO.edit(session, t);
        }
      }
    }

    entry.setTags(tags);

    if (entry.getId() != null)
    {
      BlogContext.getDAOFactory().getEntryDAO().edit(session, entry, false);
    }

    for (Tag rt : removeAbleTags)
    {
      tagDAO.remove(session, rt);
    }
  }

  /**
   * Method description
   *
   *
   *
   * @param request
   */
  private void doTrackback(BlogRequest request)
  {
    if (entry.isPublished() && request.getCurrentBlog().isSendAutoPing())
    {
      BlogContext.getInstance().getJobQueue().add(new AutoTrackbackJob(request,
              entry));
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private File directory;

  /** Field description */
  private DataModel entries;

  /** Field description */
  private Entry entry;

  /** Field description */
  @Config(Constants.CONFIG_ADMIN_PAGESIZE)
  private Integer pageSize = Integer.valueOf(20);

  /** Field description */
  private String tagString;

  /** Field description */
  private String trackbackURL;
}
