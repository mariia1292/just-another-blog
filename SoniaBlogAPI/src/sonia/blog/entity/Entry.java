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
public class Entry implements Serializable, ContentObject, CommentAble
{

  /** Field description */
  private static final long serialVersionUID = 4400178922212522094L;

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param category
   */
  public void addCateogory(Category category)
  {
    if (categories == null)
    {
      categories = new ArrayList<Category>();
    }

    categories.add(category);

    if (!category.getEntries().contains(this))
    {
      categories.remove(this);
    }
  }

  /**
   * Method description
   *
   *
   * @param comment
   */
  public void addComment(Comment comment)
  {
    getComments().add(comment);
    comment.setEntry(this);
  }

  /**
   * Method description
   *
   *
   * @param category
   *
   * @return
   */
  public boolean containsCategory(Category category)
  {
    boolean result = false;

    if (categories != null)
    {
      result = categories.contains(category);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  @Override
  public boolean equals(Object object)
  {

    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Entry))
    {
      return false;
    }

    Entry other = (Entry) object;

    if (((this.id == null) && (other.id != null))
        || ((this.id != null) &&!this.id.equals(other.id)))
    {
      return false;
    }

    return true;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public int hashCode()
  {
    int hash = 0;

    hash += ((id != null)
             ? id.hashCode()
             : 0);

    return hash;
  }

  /**
   * Method description
   *
   */
  public void publish()
  {
    published = true;
    publishingDate = new Date();
  }

  /**
   * Method description
   *
   *
   * @param category
   */
  public void removeCategory(Category category)
  {
    if (categories != null)
    {
      categories.remove(category);
    }

    category.getEntries().remove(this);
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

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String toString()
  {
    return "sonia.blop.entity.Entry[id=" + id + "]";
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public int getAttachmentCount()
  {
    int result = 0;

    if (attachments != null)
    {
      result = attachments.size();
    }

    return result;
  }

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
    String name = null;

    if (author != null)
    {
      name = author.getDisplayName();
    }

    return name;
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
  public List<Category> getCategories()
  {
    return categories;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getCommentCount()
  {
    int result = 0;

    if (comments != null)
    {
      result = comments.size();
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Comment> getComments()
  {
    if (comments == null)
    {
      comments = new ArrayList<Comment>();
    }

    return comments;
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
  public List<Tag> getTags()
  {
    if (tags == null)
    {
      tags = new ArrayList<Tag>();
    }

    return tags;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTeaser()
  {
    return teaser;
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
  public boolean isAllowComments()
  {
    return allowComments;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isOnlyContent()
  {
    return (teaser == null) || (teaser.trim().length() == 0);
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
   * @param allowComments
   */
  public void setAllowComments(boolean allowComments)
  {
    this.allowComments = allowComments;
  }

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
   * @param categories
   */
  public void setCategories(List<Category> categories)
  {
    this.categories = categories;
  }

  /**
   * Method description
   *
   *
   * @param comments
   */
  public void setComments(List<Comment> comments)
  {
    this.comments = comments;
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
   * @param tags
   */
  public void setTags(List<Tag> tags)
  {
    this.tags = tags;
  }

  /**
   * Method description
   *
   *
   * @param teaser
   */
  public void setTeaser(String teaser)
  {
    this.teaser = teaser;
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
  private List<Category> categories;

  /** Field description */
  private List<Comment> comments;

  /** Field description */
  private String content;

  /** Field description */
  private Date creationDate;

  /** Field description */
  private Long id;

  /** Field description */
  private Date lastUpdate;

  /** Field description */
  private boolean published = false;

  /** Field description */
  private boolean allowComments = true;

  /** Field description */
  private Date publishingDate;

  /** Field description */
  private List<Tag> tags;

  /** Field description */
  private String teaser;

  /** Field description */
  private String title;
}
