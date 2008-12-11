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

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sdorra
 */
public abstract class AbstractBlogBeanMacro implements Macro
{

  /**
   * Method description
   *
   *
   * @param facesContext
   * @param linkBase
   * @param object
   * @param body
   * @param parameters
   *
   * @return
   */
  protected abstract String excecute(FacesContext facesContext,
                                     String linkBase, ContentObject object,
                                     String body,
                                     Map<String, String> parameters);

  /**
   * Method description
   *
   *
   * @param environment
   * @param body
   * @param parameters
   *
   * @return
   */
  public String excecute(Map<String, ?> environment, String body,
                         Map<String, String> parameters)
  {
    String result = null;
    ContentObject object = (ContentObject) environment.get("object");
    FacesContext facesContext = (FacesContext) environment.get("facesContext");
    String linkBase = (String) environment.get("linkBase");

    if ((object != null) && (facesContext != null))
    {
      result = excecute(facesContext, linkBase, object, body, parameters);
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
}
