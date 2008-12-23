/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.entity;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.listener.BlogListener;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author sdorra
 */
@Entity @NamedQueries(
{
  @NamedQuery(name = "Blog.findByServername",
              query = "select b from Blog as b where b.active = true and b.servername = :servername") ,
  @NamedQuery(name = "Blog.findAll", query = "select b from Blog b") ,
  @NamedQuery(name = "Blog.findActive",
              query = "select b from Blog b where b.active = true") ,
  @NamedQuery(name = "Blog.countAll", query = "select count(b) from Blog b")
}) @EntityListeners({ BlogListener.class })
public class Blog implements Serializable, PermaObject
{

  /** Field description */
  private static final long serialVersionUID = 2936020289259434092L;

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
    if (!(object instanceof Blog))
    {
      return false;
    }

    Blog other = (Blog) object;

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
    return "sonia.blop.entity.Blog[id=" + id + "]";
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Category> getCategories()
  {
    if (categories == null)
    {
      categories = new ArrayList<Category>();
    }

    return categories;
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
  public String getDateFormat()
  {
    return dateFormat;
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
  public String getEmail()
  {
    return email;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getEntriesPerPage()
  {
    return entriesPerPage;
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
  public int getImageHeight()
  {
    return imageHeight;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getImageWidth()
  {
    return imageWidth;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<BlogMember> getMembers()
  {
    if (members == null)
    {
      members = new ArrayList<BlogMember>();
    }

    return members;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getServername()
  {
    return servername;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTemplate()
  {
    return template;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getThumbnailHeight()
  {
    return thumbnailHeight;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getThumbnailWidth()
  {
    return thumbnailWidth;
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
  public boolean isActive()
  {
    return active;
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
  public boolean isAllowHtmlComments()
  {
    return allowHtmlComments;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isAllowMacros()
  {
    return allowMacros;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param active
   */
  public void setActive(boolean active)
  {
    this.active = active;
  }

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
   * @param allowHtmlComments
   */
  public void setAllowHtmlComments(boolean allowHtmlComments)
  {
    this.allowHtmlComments = allowHtmlComments;
  }

  /**
   * Method description
   *
   *
   * @param allowMacros
   */
  public void setAllowMacros(boolean allowMacros)
  {
    this.allowMacros = allowMacros;
  }

  /**
   * Method description
   *
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
   * @param dateFormat
   */
  public void setDateFormat(String dateFormat)
  {
    this.dateFormat = dateFormat;
  }

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
   * @param email
   */
  public void setEmail(String email)
  {
    this.email = email;
  }

  /**
   * Method description
   *
   *
   * @param entriesPerPage
   */
  public void setEntriesPerPage(int entriesPerPage)
  {
    this.entriesPerPage = entriesPerPage;
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
   * @param imageHeight
   */
  public void setImageHeight(int imageHeight)
  {
    this.imageHeight = imageHeight;
  }

  /**
   * Method description
   *
   *
   * @param imageWidth
   */
  public void setImageWidth(int imageWidth)
  {
    this.imageWidth = imageWidth;
  }

  /**
   * Method description
   *
   *
   * @param members
   */
  public void setMembers(List<BlogMember> members)
  {
    this.members = members;
  }

  /**
   * Method description
   *
   *
   * @param servername
   */
  public void setServername(String servername)
  {
    this.servername = servername;
  }

  /**
   * Method description
   *
   *
   * @param template
   */
  public void setTemplate(String template)
  {
    this.template = template;
  }

  /**
   * Method description
   *
   *
   * @param thumbnailHeight
   */
  public void setThumbnailHeight(int thumbnailHeight)
  {
    this.thumbnailHeight = thumbnailHeight;
  }

  /**
   * Method description
   *
   *
   * @param thumbnailWidth
   */
  public void setThumbnailWidth(int thumbnailWidth)
  {
    this.thumbnailWidth = thumbnailWidth;
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private boolean active = true;

  /** Field description */
  private boolean allowComments = true;

  /** Field description */
  private boolean allowHtmlComments = false;

  /** Field description */
  private boolean allowMacros = true;

  /** Field description */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog",cascade=CascadeType.REMOVE)
  private List<Category> categories;

  /** Field description */
  @Temporal(TemporalType.TIMESTAMP)
  private Date creationDate;

  /** Field description */
  @Column(nullable = false, length = 50)
  private String dateFormat = "yyyy-MM-dd HH:mm";

  /** Field description */
  private String description;

  /** Field description */
  private String email;

  /** Field description */
  private int entriesPerPage = 20;

  /** Field description */
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Field description */
  private int imageHeight = 0;

  /** Field description */
  private int imageWidth = 640;

  /** Field description */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog",cascade=CascadeType.REMOVE)
  private List<BlogMember> members;

  /** Field description */
  @Column(nullable = false, unique = true)
  private String servername;

  /** Field description */
  @Column(nullable = false)
  private String template;

  /** Field description */
  private int thumbnailHeight = 0;

  /** Field description */
  private int thumbnailWidth = 200;

  /** Field description */
  private String title;
}
