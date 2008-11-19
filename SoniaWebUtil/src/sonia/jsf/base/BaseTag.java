/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.base;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;

/**
 *
 * @author sdorra
 */
public abstract class BaseTag extends UIComponentELTag
{

  /**
   * Method description
   *
   */
  @Override
  public void release()
  {
    style = null;
    styleClass = null;
    role = null;
    super.release();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public ValueExpression getRole()
  {
    return role;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ValueExpression getStyle()
  {
    return style;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ValueExpression getStyleClass()
  {
    return styleClass;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param component
   */
  @Override
  protected void setProperties(UIComponent component)
  {
    if (!(component instanceof BaseComponent))
    {
      throw new IllegalArgumentException("wrong type");
    }

    BaseComponent base = (BaseComponent) component;

    if (style != null)
    {
      base.setValueExpression("style", style);
    }

    if (styleClass != null)
    {
      base.setValueExpression("styleClass", styleClass);
    }

    if (role != null)
    {
      base.setValueExpression("role", role);
    }

    super.setProperties(component);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected ValueExpression role;

  /** Field description */
  protected ValueExpression style;

  /** Field description */
  protected ValueExpression styleClass;
}
