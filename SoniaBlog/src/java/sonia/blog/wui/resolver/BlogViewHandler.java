/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.resolver;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.entity.Blog;
import sonia.blog.util.BlogUtil;

//~--- JDK imports ------------------------------------------------------------

import com.sun.facelets.FaceletViewHandler;

import java.util.Locale;

import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class BlogViewHandler extends FaceletViewHandler
{

  /**
   * Constructs ...
   *
   *
   * @param orginal
   */
  public BlogViewHandler(ViewHandler orginal)
  {
    super(orginal);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   *
   * @return
   */
  @Override
  public Locale calculateLocale(FacesContext context)
  {
    Locale locale = null;
    Object object = context.getExternalContext().getRequest();
    BlogRequest request = BlogUtil.getBlogRequest(object);

    if (request != null)
    {
      Blog blog = request.getCurrentBlog();

      if (blog != null)
      {
        locale = blog.getLocale();
      }
    }

    if (locale == null)
    {
      locale = BlogContext.getInstance().getDefaultLocale();
    }

    return locale;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   * @param viewId
   *
   * @return
   */
  @Override
  public String getActionURL(FacesContext context, String viewId)
  {
    String result = super.getActionURL(context, viewId);

    if (result.endsWith(viewId))
    {
      result = result.substring(0, result.length() - viewId.length());
    }

    return result;
  }
}
