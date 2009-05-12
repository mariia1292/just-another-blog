/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.PageDAO;
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.api.util.PageNavigation;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Page;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public class ChildrenMacro extends AbstractBlogMacro
{

  /**
   * Method description
   *
   *
   * @param request
   * @param linkBase
   * @param object
   * @param body
   *
   * @return
   */
  @Override
  protected String doBody(BlogRequest request, String linkBase,
                          ContentObject object, String body)
  {
    StringBuffer result = new StringBuffer();

    if (isPage(object))
    {
      Page page = (Page) object;
      List<? extends PageNavigation> children = pageDAO.getChildren(page);

      result.append("<ul>\n");

      for (PageNavigation child : children)
      {
        result.append("<li><a href=\"").append(linkBase).append("page/");
        result.append(child.getId()).append(".jab\">");
        result.append(child.getNavigationTitle()).append("</a></li>\n");
      }

      result.append("</ul>\n");
    }
    else
    {
      result.append("-- object is not a page --");
    }

    return result.toString();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Dao
  private PageDAO pageDAO;
}
