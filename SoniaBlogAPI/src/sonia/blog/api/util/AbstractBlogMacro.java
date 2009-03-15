/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;

import sonia.macro.Macro;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sdorra
 */
public abstract class AbstractBlogMacro implements Macro
{

  /**
   * Constructs ...
   *
   */
  public AbstractBlogMacro()
  {
    logger = Logger.getLogger(getClass().getName());
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param facesContext
   * @param linkBase
   * @param object
   * @param body
   *
   * @return
   */
  protected abstract String doBody(FacesContext facesContext, String linkBase,
                                   ContentObject object, String body);

  /**
   * Method description
   *
   *
   * @param environment
   * @param body
   *
   * @return
   */
  public String doBody(Map<String, ?> environment, String body)
  {
    String result = null;
    ContentObject object = (ContentObject) environment.get("object");
    FacesContext facesContext = (FacesContext) environment.get("facesContext");
    String linkBase = (String) environment.get("linkBase");

    if ((object != null) && (facesContext != null))
    {
      result = doBody(facesContext, linkBase, object, body);
    }
    else
    {
      result = "-- object or facesContext is null --";
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   *
   * @return
   */
  protected Blog getCurrentBlog(FacesContext context)
  {
    return getRequest(context).getCurrentBlog();
  }

  /**
   * Method description
   *
   *
   * @param context
   *
   * @return
   */
  protected BlogRequest getRequest(FacesContext context)
  {
    BlogRequest request = null;
    Object object = context.getExternalContext().getRequest();

    if (object instanceof BlogRequest)
    {
      request = (BlogRequest) object;
    }
    else if (object instanceof HttpServletRequest)
    {
      request = new BlogRequest((HttpServletRequest) object);
    }
    else
    {
      throw new IllegalArgumentException(
          "object is not an instance of HttpServletRequest");
    }

    return request;
  }

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  protected boolean isEntry(ContentObject object)
  {
    return object instanceof Entry;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected Logger logger;
}
