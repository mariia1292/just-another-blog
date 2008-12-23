/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.entity;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
  @NamedQuery(name = "Category.findAllFromBlog",
              query = "select c from Category c where c.blog = :blog") ,
  @NamedQuery(name = "Category.findIdFromBlog",
              query = "select c from Category c where c.id = :id and c.blog = :blog") ,
  @NamedQuery(name = "Category.countAll",
              query = "select count(c) from Category c") ,
  @NamedQuery(name = "Category.findFirstFromBlog",
              query = "select c from Category c where c.blog = :blog and c.id = ( select min(cat.id) from Category cat where cat.blog = :blog )")
})
public class Category implements Serializable, PermaObject
{

  /** Field description */
  private static final long serialVersionUID = 7471132711766508949L;

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
    if (!(object instanceof Category))
    {
      return false;
    }

    Category other = (Category) object;

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
    return "sonia.blog.entity.Category[id=" + id + "]";
  }

  //~--- get methods ----------------------------------------------------------

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
  public List<Entry> getEntries()
  {
    return entries;
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
  public String getName()
  {
    return name;
  }

  //~--- set methods ----------------------------------------------------------

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
   * @param entries
   */
  public void setEntries(List<Entry> entries)
  {
    this.entries = entries;
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
   * @param name
   */
  public void setName(String name)
  {
    this.name = name;
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
  @ManyToOne(optional = false)
  private Blog blog;

  /** Field description */
  @Temporal(TemporalType.TIMESTAMP) @Column(nullable = false)
  private Date creationDate;

  /** Field description */
  private String description;

  /** Field description */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "category",cascade=CascadeType.REMOVE)
  private List<Entry> entries;

  /** Field description */
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Field description */
  @Column(nullable = false)
  private String name;
}
