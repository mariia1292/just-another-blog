/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.entity;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.listener.EntryListener;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author sdorra
 */
@Entity @EntityListeners({ EntryListener.class }) @NamedQueries(
{
  @NamedQuery(name = "Entry.overview",
              query = "select e from Entry e join e.category c join c.blog b where b = :blog and e.published = true order by e.creationDate") ,
  @NamedQuery(name = "Entry.findByCategory",
              query = "select e from Entry as e where e.category = :category and e.published = true order by e.creationDate") ,
  @NamedQuery(name = "Entry.findIdFromBlog",
              query = "select e from Entry e join e.category c join c.blog b where b = :blog and e.id = :id and e.published = true order by e.creationDate") ,
  @NamedQuery(name = "Entry.findFromBlog",
              query = "select e from Entry e join e.category c join c.blog b where b = :blog order by e.creationDate desc") ,
  @NamedQuery(name = "Entry.findByTag",
              query = "select e from Entry e join e.tags t where e.published = true and t = :tag order by e.creationDate") ,
  @NamedQuery(name = "Entry.findByTagName",
              query = "select e from Entry e join e.category c join c.blog b join e.tags t where b = :blog and e.published = true and t.name = :name order by e.creationDate") ,
  @NamedQuery(name = "Entry.findByCategoryId",
              query = "select e From Entry e join e.category c where c.id = :id and e.published =true order by e.creationDate") ,
  @NamedQuery(name = "Entry.findByDate",
              query = "select e from Entry e join e.category c join c.blog b where b = :blog and e.published = true and e.creationDate between :start and :end")
})
public class Entry implements Serializable, ContentObject, PermaObject
{

  /** Field description */
  private static final long serialVersionUID = 4400178922212522094L;

  //~--- methods --------------------------------------------------------------

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
  public Category getCategory()
  {
    return category;
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
   * @param shortVersion
   *
   * @return
   */
  public String getContent(boolean shortVersion)
  {
    String result = null;

    if (shortVersion && (teaser != null) && (teaser.trim().length() > 0))
    {
      result = teaser;
    }
    else
    {
      result = content;
    }

    return result;
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
   * @param category
   */
  public void setCategory(Category category)
  {
    this.category = category;
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
  @PrePersist
  void prePersists()
  {
    creationDate = new Date();
  }

  /**
   * Method description
   *
   */
  @PreUpdate
  void preUpdate()
  {
    lastUpdate = new Date();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @OneToMany(mappedBy = "entry", cascade = { CascadeType.REMOVE })
  private List<Attachment> attachments;

  /** Field description */
  @ManyToOne(optional = false)
  private User author;

  /** Field description */
  @ManyToOne
  private Category category;

  /** Field description */
  @OneToMany(mappedBy = "entry", cascade = { CascadeType.REMOVE })
  private List<Comment> comments;

  /** Field description */
  @Lob @Column(nullable = false)
  private String content;

  /** Field description */
  @Temporal(TemporalType.TIMESTAMP) @Column(nullable = false)
  private Date creationDate;

  /** Field description */
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Field description */
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastUpdate;

  /** Field description */
  private boolean published = true;

  /** Field description */
  @ManyToMany @OrderBy("name")
  private List<Tag> tags;

  /** Field description */
  @Column(length = 5000)
  private String teaser;

  /** Field description */
  @Column(nullable = false)
  private String title;
}
