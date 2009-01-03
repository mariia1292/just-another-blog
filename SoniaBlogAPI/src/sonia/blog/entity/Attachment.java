/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.entity;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.listener.AttachmentListener;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author sdorra
 */
@Entity @EntityListeners({ AttachmentListener.class }) @NamedQueries(
{
  @NamedQuery(name = "Attachment.entryOverview",
              query = "select a from Attachment a join a.entry e where e = :entry order by a.creationDate desc") ,
  @NamedQuery(name = "Attachment.findByBlogAndId",
              query = "select a from Attachment a join a.entry e join e.category c join c.blog b where b = :blog and a.id = :id") ,
  @NamedQuery(name = "Attachment.findAllImagesByEntry",
              query = "select a from Attachment a join a.entry e where e = :entry and a.mimeType like 'image/%'") ,
  @NamedQuery(name = "Attachment.count",
              query = "select count(a) from Attachment a") ,
  @NamedQuery(name = "Attachment.findAll",
              query = "select a from Attachment a") ,
  @NamedQuery(name = "Attachment.countFromBlog",
              query = "select count(a) from Attachment a join a.entry e join e.category c join c.blog b where b = :blog")
})
public class Attachment implements Serializable, PermaObject, FileObject
{

  /** Field description */
  private static final long serialVersionUID = 6433042381384640712L;

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
    if (!(object instanceof Attachment))
    {
      return false;
    }

    Attachment other = (Attachment) object;

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
    return "sonia.blop.entity.Attachment[id=" + id + "]";
  }

  //~--- get methods ----------------------------------------------------------

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
  public String getDescription()
  {
    return description;
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
  public String getFilePath()
  {
    return filePath;
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
  public String getMimeType()
  {
    return mimeType;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return name;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Page getPage()
  {
    return page;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getSize()
  {
    return fileSize;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param description
   */
  public void setDescription(String description)
  {
    this.description = description;
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
   * @param filePath
   */
  public void setFilePath(String filePath)
  {
    this.filePath = filePath;
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
   * @param mimeType
   */
  public void setMimeType(String mimeType)
  {
    this.mimeType = mimeType;
  }

  /**
   * Method description
   *
   *
   * @param name
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Method description
   *
   *
   * @param page
   */
  public void setPage(Page page)
  {
    this.page = page;
  }

  /**
   * Method description
   *
   *
   * @param size
   */
  public void setSize(long size)
  {
    this.fileSize = size;
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Temporal(TemporalType.TIMESTAMP)
  private Date creationDate;

  /** Field description */
  private String description;

  /** Field description */
  @ManyToOne
  private Entry entry;

  /** Field description */
  @Column(nullable = false)
  private String filePath;

  /** Field description */
  private long fileSize;

  /** Field description */
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Field description */
  private String mimeType;

  /** Field description */
  @Column(nullable = false)
  private String name;

  /** Field description */
  @ManyToOne
  private Page page;
}
