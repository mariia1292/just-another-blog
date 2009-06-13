/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.widget;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseTag;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.component.UIComponent;

/**
 *
 * @author sdorra
 */
public class WidgetTag extends BaseTag
{

  /**
   * Method description
   *
   */
  @Override
  public void release()
  {
    widget = null;
    param = null;
    object = null;
    name = null;
    result = null;
    super.release();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String getComponentType()
  {
    return WidgetComponent.FAMILY;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String getRendererType()
  {
    return WidgetComponent.RENDERER;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param name
   */
  public void setName(ValueExpression name)
  {
    this.name = name;
  }

  /**
   * Method description
   *
   *
   * @param object
   */
  public void setObject(ValueExpression object)
  {
    this.object = object;
  }

  /**
   * Method description
   *
   *
   * @param param
   */
  public void setParam(ValueExpression param)
  {
    this.param = param;
  }

  /**
   * Method description
   *
   *
   * @param result
   */
  public void setResult(ValueExpression result)
  {
    this.result = result;
  }

  /**
   * Method description
   *
   *
   * @param widget
   */
  public void setWidget(ValueExpression widget)
  {
    this.widget = widget;
  }

  /**
   * Method description
   *
   *
   * @param component
   */
  @Override
  protected void setProperties(UIComponent component)
  {
    super.setProperties(component);

    if (widget != null)
    {
      component.setValueExpression("widget", widget);
    }

    if (param != null)
    {
      component.setValueExpression("param", param);
    }

    if (object != null)
    {
      component.setValueExpression("object", object);
    }

    if (name != null)
    {
      component.setValueExpression("name", name);
    }

    if (result != null)
    {
      component.setValueExpression("result", result);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ValueExpression name;

  /** Field description */
  private ValueExpression object;

  /** Field description */
  private ValueExpression param;

  /** Field description */
  private ValueExpression result;

  /** Field description */
  private ValueExpression widget;
}
