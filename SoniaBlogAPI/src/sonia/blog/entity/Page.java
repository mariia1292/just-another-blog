/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.entity;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author sdorra
 */
public class Page implements Serializable, PermaObject, ContentObject
{

  /** Field description */
  private static final long serialVersionUID = 7113784206225853548L;

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param attachment
   */
  public void addAttachment(Attachment attachment)
  {
    getAttachments().add(attachment);
    attachment.setPage(this);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean renderMacros()
  {
    return true;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Attachment> getAttachments()
  {
    if (attachments == null)
    {
      attachments = new ArrayList<Attachment>();
    }

    return attachments;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public User getAuthor()
  {
    return author;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAuthorName()
  {
    return author.getDisplayName();
  }

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
  public String getContent()
  {
    return content;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Date getCreationDate()
  {
    return creationDate;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Long getId()
  {
    return id;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Date getLastUpdate()
  {
    return lastUpdate;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getNavigationPosition()
  {
    return navigationPosition;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getNavigationTitle()
  {
    return navigationTitle;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Page getParent()
  {
    return parent;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Date getPublishingDate()
  {
    return publishingDate;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTeaser()
  {
    return content;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTitle()
  {
    return title;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isPublished()
  {
    return published;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param attachments
   */
  public void setAttachments(List<Attachment> attachments)
  {
    this.attachments = attachments;
  }

  /**
   * Method description
   *
   *
   * @param author
   */
  public void setAuthor(User author)
  {
    this.author = author;
  }

  /**
   * Method description
   *
   *
   * @param blog
   */
  public void setBlog(Blog blog)
  {
    this.blog = blog;
  }

  /**
   * Method description
   *
   *
   * @param content
   */
  public void setContent(String content)
  {
    this.content = content;
  }

  /**
   * Method description
   *
   *
   * @param id
   */
  public void setId(Long id)
  {
    this.id = id;
  }

  /**
   * Method description
   *
   *
   * @param navigationPosition
   */
  public void setNavigationPosition(int navigationPosition)
  {
    this.navigationPosition = navigationPosition;
  }

  /**
   * Method description
   *
   *
   * @param navigationTitle
   */
  public void setNavigationTitle(String navigationTitle)
  {
    this.navigationTitle = navigationTitle;
  }

  /**
   * Method description
   *
   *
   * @param parent
   */
  public void setParent(Page parent)
  {
    this.parent = parent;
  }

  /**
   * Method description
   *
   *
   * @param published
   */
  public void setPublished(boolean published)
  {
    this.published = published;
  }

  /**
   * Method description
   *
   *
   * @param publishingDate
   */
  public void setPublishingDate(Date publishingDate)
  {
    this.publishingDate = publishingDate;
  }

  /**
   * Method description
   *
   *
   * @param title
   */
  public void setTitle(String title)
  {
    this.title = title;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  void prePersists()
  {
    creationDate = new Date();
  }

  /**
   * Method description
   *
   */
  void preUpdate()
  {
    lastUpdate = new Date();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<Attachment> attachments;

  /** Field description */
  private User author;

  /** Field description */
  private Blog blog;

  /** Field description */
  private String content;

  /** Field description */
  private Date creationDate;

  /** Field description */
  private Long id;

  /** Field description */
  private Date lastUpdate;

  /** Field description */
  private int navigationPosition;

  /** Field description */
  private String navigationTitle;

  /** Field description */
  private Page parent;

  /** Field description */
  private boolean published;

  /** Field description */
  private Date publishingDate;

  /** Field description */
  private String title;
}
