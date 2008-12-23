/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.entity;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.listener.CommentListener;

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
@Entity @EntityListeners({ CommentListener.class }) @NamedQueries(
{
  @NamedQuery(name = "Comment.findFromBlog",
              query = "select c from Comment c join c.entry e join e.category ca join ca.blog b where b = :blog and e.published = true order by c.creationDate desc") ,
  @NamedQuery(name = "Comment.entryOverview",
              query = "select c from Comment c join c.entry e where e = :entry and c.spam = false order by c.creationDate ") ,
  @NamedQuery(name = "Comment.countAll",
              query = "select count(c) from Comment c") ,
  @NamedQuery(name = "Comment.countFromBlog",
              query = "select count(c) from Comment c join c.entry e join e.category cat join cat.blog b where b = :blog")
})
public class Comment implements Serializable, PermaObject
{

  /** Field description */
  private static final long serialVersionUID = 4507923974114945766L;

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
    if (!(object instanceof Comment))
    {
      return false;
    }

    Comment other = (Comment) object;

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
    return "sonia.blop.entity.Comment[id=" + id + "]";
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAuthorAddress()
  {
    return authorAddress;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAuthorMail()
  {
    return authorMail;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAuthorName()
  {
    return authorName;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAuthorURL()
  {
    return authorURL;
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
  public boolean isSpam()
  {
    return spam;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param authorAddress
   */
  public void setAuthorAddress(String authorAddress)
  {
    this.authorAddress = authorAddress;
  }

  /**
   * Method description
   *
   *
   * @param authorMail
   */
  public void setAuthorMail(String authorMail)
  {
    this.authorMail = authorMail;
  }

  /**
   * Method description
   *
   *
   * @param authorName
   */
  public void setAuthorName(String authorName)
  {
    this.authorName = authorName;
  }

  /**
   * Method description
   *
   *
   * @param authorURL
   */
  public void setAuthorURL(String authorURL)
  {
    this.authorURL = authorURL;
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
   * @param spam
   */
  public void setSpam(boolean spam)
  {
    this.spam = spam;
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
  @Column(nullable = false)
  private String authorAddress;

  /** Field description */
  private String authorMail;

  /** Field description */
  @Column(nullable = false)
  private String authorName;

  /** Field description */
  private String authorURL;

  /** Field description */
  @Column(nullable = false, length = 2048)
  private String content;

  /** Field description */
  @Temporal(TemporalType.TIMESTAMP)
  private Date creationDate;

  /** Field description */
  @ManyToOne(optional = false)
  private Entry entry;

  /** Field description */
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Field description */
  private boolean spam = false;
}
