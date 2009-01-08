/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.entity;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Entity @NamedQueries(
{
  @NamedQuery(name = "BlogMember.findAll",
              query = "select m from BlogMember as m") ,
  @NamedQuery(name = "BlogMember.count",
              query = "select count(m) from BlogMember as m") ,
  @NamedQuery(name = "BlogMember.findByBlogAndUser",
              query = "select m from BlogMember as m where m.blog = :blog and m.user = :user") ,
  @NamedQuery(name = "BlogMember.findByUser",
              query = "select m from BlogMember m where m.user = :user") ,
  @NamedQuery(name = "BlogMember.findByBlog",
              query = "select m from BlogMember m where m.blog = :blog") ,
  @NamedQuery(name = "BlogMember.countByBlog",
              query = "select count(m) from BlogMember m where m.blog = :blog")
})
public class BlogMember implements Serializable
{

  /** Field description */
  private static final long serialVersionUID = -2042101383449661742L;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public BlogMember() {}

  /**
   * Constructs ...
   *
   *
   * @param blog
   * @param user
   * @param role
   */
  public BlogMember(Blog blog, User user, Role role)
  {
    this.blog = blog;
    this.role = role;
    this.user = user;
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
    if (!(object instanceof BlogMember))
    {
      return false;
    }

    BlogMember other = (BlogMember) object;

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
    return "sonia.blop.entity.BlogMember[id=" + id + "]";
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
  public Date getRegistrationDate()
  {
    return registrationDate;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Role getRole()
  {
    return role;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public User getUser()
  {
    return user;
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
   * @param role
   */
  public void setRole(Role role)
  {
    this.role = role;
  }

  /**
   * Method description
   *
   *
   * @param user
   */
  public void setUser(User user)
  {
    this.user = user;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  @PrePersist
  void prePersists()
  {
    registrationDate = new Date();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @ManyToOne(optional = false)
  private Blog blog;

  /** Field description */
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Field description */
  @Temporal(TemporalType.TIMESTAMP)
  private Date registrationDate;

  /** Field description */
  @Column(name = "BlogRole", nullable = false)
  private Role role;

  /** Field description */
  @ManyToOne(optional = false)
  private User user;
}
