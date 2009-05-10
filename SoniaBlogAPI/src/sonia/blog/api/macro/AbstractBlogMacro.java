/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;

import sonia.macro.Macro;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

/**
 *
 * @author sdorra
 */
public abstract class AbstractBlogMacro implements Macro
{

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param linkBase - the blog link base
   * @param object - the current ContentObject (Entry)
   * @param body - the body of the macro
   *
   * @return
   */
  protected abstract String doBody(BlogRequest request, String linkBase,
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
    BlogRequest request = (BlogRequest) environment.get("request");
    String linkBase = (String) environment.get("linkBase");

    if ((object != null) && (request != null))
    {
      result = doBody(request, linkBase, object, body);
    }
    else
    {
      result = "-- object or request is null --";
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

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

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  protected boolean isPage(ContentObject object)
  {
    return object instanceof Page;
  }
}
