/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.base;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
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
  protected String buildRelativeLink(FacesContext context, String link)
  {
    if (link.startsWith("/"))
    {
      String contextPath = context.getExternalContext().getRequestContextPath();

      link = contextPath + link;
    }

    return link;
  }

  /**
   * Method description
   *
   *
   * @param context
   * @param value
   *
   * @return
   */
  protected UIOutput createHtmlOutputComponent(FacesContext context,
          String value)
  {
    Boolean escape = Boolean.FALSE;
    ValueExpression ve =
      context.getApplication().getExpressionFactory().createValueExpression(
          escape, Boolean.class);
    UIOutput out = new UIOutput();

    out.setValueExpression("escape", ve);
    out.setValue(value);

    return out;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param component
   *
   * @return
   */
  protected Map<String, ?> getParameters(UIComponent component)
  {
    Map<String, Object> map = new HashMap<String, Object>();
    List<UIComponent> children = component.getChildren();

    if (children != null)
    {
      for (UIComponent child : children)
      {
        if (child instanceof UIParameter)
        {
          UIParameter param = (UIParameter) child;

          map.put(param.getName(), param.getValue());
        }
      }
    }

    return map;
  }

  /**
   * Method description
   *
   *
   * @param value
   *
   * @return
   */
  protected boolean isBlank(String value)
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
  protected boolean isRendered(FacesContext context, BaseComponent component)
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
