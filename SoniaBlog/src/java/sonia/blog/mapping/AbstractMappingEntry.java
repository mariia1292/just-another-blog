/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.mapping.MappingEntry;
import sonia.blog.wui.BlogBean;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

/**
 *
 * @author sdorra
 */
public abstract class AbstractMappingEntry implements MappingEntry
{

  /** Field description */
  public static final String VIEW_DETAIL = "detail.xhtml";

  /** Field description */
  public static final String VIEW_LIST = "list.xhtml";

  /** Field description */
  public static final String VIEW_NOTFOUND = "notFound.xhtml";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public AbstractMappingEntry()
  {
    logger = Logger.getLogger(getClass().getName());
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  public BlogBean getBlogBean(BlogRequest request)
  {
    HttpSession session = request.getSession(true);
    BlogBean blogBean = (BlogBean) session.getAttribute("BlogBean");

    if (blogBean == null)
    {
      blogBean = new BlogBean();
      session.setAttribute("BlogBean", blogBean);
    }

    return blogBean;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isNavigationRendered()
  {
    return true;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param view
   */
  public void setViewId(BlogRequest request, String view)
  {
    String template = request.getCurrentBlog().getTemplate();

    request.setViewId("/template/" + template + "/" + view);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected Logger logger;
}
