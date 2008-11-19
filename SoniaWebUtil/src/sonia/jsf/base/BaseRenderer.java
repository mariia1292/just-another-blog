/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.base;

//~--- JDK imports ------------------------------------------------------------

import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

/**
 *
 * @author sdorra
 */
public class BaseRenderer extends Renderer
{

  /**
   * Method description
   *
   *
   * @param context
   * @param link
   *
   * @return
   */
  public String buildRelativeLink(FacesContext context, String link)
  {
    if (link.startsWith("/"))
    {
      String contextPath = context.getExternalContext().getRequestContextPath();

      link = contextPath + link;
    }

    return link;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param value
   *
   * @return
   */
  public boolean isBlank(String value)
  {
    return (value == null) || (value.length() == 0);
  }

  /**
   * Method description
   *
   *
   * @param context
   * @param component
   *
   * @return
   */
  public boolean isRendered(FacesContext context, BaseComponent component)
  {
    boolean result = false;

    if (component.isRendered())
    {
      if (component.getRole() != null)
      {
        if (context.getExternalContext().isUserInRole(component.getRole()))
        {
          result = true;
        }
      }
      else
      {
        result = true;
      }
    }

    return result;
  }
}
