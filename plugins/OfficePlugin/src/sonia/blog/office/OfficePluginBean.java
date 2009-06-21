/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.office;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogSession;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;

import sonia.util.Util;

/**
 *
 * @author sdorra
 */
public class OfficePluginBean extends AbstractBean
{

  /**
   * Method description
   *
   *
   * @return
   */
  public String save()
  {
    String result = SUCCESS;

    if (Util.hasContent(theme))
    {
      BlogSession session = getRequest().getBlogSession();

      blogDAO.setParameter(session, session.getBlog(), CodeMacro.CONFIG_THEME,
                           theme);
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
  public String getTheme()
  {
    if (theme == null)
    {
      Blog blog = getRequest().getCurrentBlog();

      theme = blogDAO.getParameter(blog, CodeMacro.CONFIG_THEME);

      if (Util.isBlank(theme))
      {
        theme = CodeMacro.THEME_DEFAULT;
      }
    }

    return theme;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param theme
   */
  public void setTheme(String theme)
  {
    this.theme = theme;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Dao
  private BlogDAO blogDAO;

  /** Field description */
  private String theme;
}
