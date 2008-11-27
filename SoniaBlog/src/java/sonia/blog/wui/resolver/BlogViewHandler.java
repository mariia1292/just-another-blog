/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.resolver;

//~--- JDK imports ------------------------------------------------------------

import com.sun.facelets.FaceletViewHandler;

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
