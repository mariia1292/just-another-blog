/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.entity;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author sdorra
 */
@Entity
public class Page implements Serializable, ContentObject
{

  /** Field description */
  private static final long serialVersionUID = 7471131711796508969L;

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
    if (!(object instanceof Page))
    {
      return false;
    }

    Page other = (Page) object;

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
    return "sonia.blog.entity.Page[id=" + id + "]";
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
  public int getPosition()
  {
    return position;
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
   * @param creationDate
   */
  public void setCreationDate(Date creationDate)
  {
    this.creationDate = creationDate;
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
   * @param position
   */
  public void setPosition(int position)
  {
    this.position = position;
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
   * @param title
   */
  public void setTitle(String title)
  {
    this.title = title;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @OneToMany(mappedBy = "page")
  private List<Attachment> attachments;

  /** Field description */
  @ManyToOne
  private User author;

  /** Field description */
  @Lob
  private String content;

  /** Field description */
  @Temporal(TemporalType.TIMESTAMP)
  private Date creationDate;

  /** Field description */
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Field description */
  @ManyToOne(fetch = FetchType.LAZY)
  private Page parent;

  /** Field description */
  private int position;

  /** Field description */
  private boolean published = false;

  /** Field description */
  private String title;
}
