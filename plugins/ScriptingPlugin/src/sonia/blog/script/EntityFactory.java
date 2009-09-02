/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.script;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.BlogParameter;
import sonia.blog.entity.Category;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;
import sonia.blog.entity.Role;
import sonia.blog.entity.Trackback;
import sonia.blog.entity.User;

/**
 *
 * @author Sebastian Sdorra
 */
public class EntityFactory
{

  /**
   * Method description
   *
   *
   * @return
   */
  public Attachment createAttachment()
  {
    return new Attachment();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Blog createBlog()
  {
    return new Blog();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public BlogMember createBlogMember()
  {
    return new BlogMember();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public BlogParameter createBlogParameter()
  {
    return new BlogParameter();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Category createCategory()
  {
    return new Category();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Comment createComment()
  {
    return new Comment();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Entry createEntry()
  {
    return new Entry();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Page createPage()
  {
    return new Page();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Trackback createTrackback()
  {
    return new Trackback();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public User createUser()
  {
    return new User();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   */
  public Role getRole(String name)
  {
    return Role.valueOf(name);
  }
}
