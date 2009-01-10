/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;

/**
 *
 * @author sdorra
 */
public class ConfigBean extends AbstractBean
{

  /**
   * Method description
   *
   *
   * @return
   */
  public String reIndex()
  {
    BlogContext.getInstance().getSearchContext().reIndex(getBlog());

    return SUCCESS;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String save()
  {
    String result = SUCCESS;
    BlogDAO blogDAO = BlogContext.getDAOFactory().getBlogDAO();

    if (blogDAO.edit(blog))
    {
      getMessageHandler().info("unpdateConfigSuccess");
    }
    else
    {
      getMessageHandler().error("unpdateConfigFailure");
      result = FAILURE;
    }

    return result;
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
    blog = getRequest().getCurrentBlog();

    return blog;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isSearchContextLocked()
  {
    return BlogContext.getInstance().getSearchContext().isLocked();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isSearchReIndexAble()
  {
    return BlogContext.getInstance().getSearchContext().isReIndexable();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Blog blog;
}
