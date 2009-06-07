/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.entity;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.Date;

/**
 *
 * @author sdorra
 */
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

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isNotify()
  {
    return notify;
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
   * @param notify
   */
  public void setNotify(boolean notify)
  {
    this.notify = notify;
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
  void prePersists()
  {
    registrationDate = new Date();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Blog blog;

  /** Field description */
  private Long id;

  /** Field description */
  private boolean notify = false;

  /** Field description */
  private Date registrationDate;

  /** Field description */
  private Role role;

  /** Field description */
  private User user;
}
