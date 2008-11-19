/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.entity;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author sdorra
 */
@Entity @NamedQueries({ @NamedQuery(name = "Tag.findFromBlog",
        query = "select t from Tag t join t.entries e join e.category c join c.blog b where b = :blog group by t") ,
                        @NamedQuery(name = "Tag.findFromBlogAndName",
        query = "select t from Tag t join t.entries e join e.category c join c.blog b where b = :blog and t.name = :name group by t") })
public class Tag implements PermaObject, Serializable
{

  /** Field description */
  private static final long serialVersionUID = -1180901740008079076L;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public Tag() {}

  /**
   * Constructs ...
   *
   *
   * @param name
   */
  public Tag(String name)
  {
    this.name = name;
  }

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
    if (!(object instanceof Tag))
    {
      return false;
    }

    Tag other = (Tag) object;

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
    return "sonia.blog.entity.Tag[id=" + id + "]";
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Entry> getEntries()
  {
    if (entries == null)
    {
      entries = new ArrayList<Entry>();
    }

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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @ManyToMany(mappedBy = "tags")
  private List<Entry> entries;

  /** Field description */
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Field description */
  private String name;
}
