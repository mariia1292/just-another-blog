/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.base;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public abstract class BaseComponent extends UIComponentBase
{

  /**
   * Method description
   *
   *
   * @param context
   * @param obj
   */
  @Override
  public void restoreState(FacesContext context, Object obj)
  {
    this.state = (Object[]) obj;
    super.restoreState(context, state[0]);
    style = (String) state[1];
    styleClass = (String) state[2];
    role = (String) state[3];
  }

  /**
   * Method description
   *
   *
   * @param context
   *
   * @return
   */
  @Override
  public Object saveState(FacesContext context)
  {
    if (state == null)
    {
      state = new Object[4];
    }

    state[0] = super.saveState(context);
    state[1] = style;
    state[2] = styleClass;
    state[3] = role;

    return state;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getRole()
  {
    if (role != null)
    {
      return role;
    }

    ValueExpression ve = getValueExpression("role");

    return (ve != null)
           ? (String) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getStyle()
  {
    if (style != null)
    {
      return style;
    }

    ValueExpression ve = getValueExpression("style");

    return (ve != null)
           ? (String) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getStyleClass()
  {
    if (styleClass != null)
    {
      return styleClass;
    }

    ValueExpression ve = getValueExpression("styleClass");

    return (ve != null)
           ? (String) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param role
   */
  public void setRole(String role)
  {
    this.role = role;
  }

  /**
   * Method description
   *
   *
   * @param style
   */
  public void setStyle(String style)
  {
    this.style = style;
  }

  /**
   * Method description
   *
   *
   * @param styleClass
   */
  public void setStyleClass(String styleClass)
  {
    this.styleClass = styleClass;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String role;

  /** Field description */
  private Object[] state;

  /** Field description */
  private String style;

  /** Field description */
  private String styleClass;
}
