/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.search;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseTag;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.component.UIComponent;

/**
 *
 * @author sdorra
 */
public class SearchTag extends BaseTag
{

  /**
   * Method description
   *
   */
  @Override
  public void release()
  {
    title = null;
    target = null;
    type = null;
    value = null;
    label = null;
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
    return SearchComponent.FAMILY;
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
    return SearchComponent.RENDERER;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param autoComplete
   */
  public void setAutoComplete(ValueExpression autoComplete)
  {
    this.autoComplete = autoComplete;
  }

  /**
   * Method description
   *
   *
   * @param label
   */
  public void setLabel(ValueExpression label)
  {
    this.label = label;
  }

  /**
   * Method description
   *
   *
   * @param target
   */
  public void setTarget(ValueExpression target)
  {
    this.target = target;
  }

  /**
   * Method description
   *
   *
   * @param title
   */
  public void setTitle(ValueExpression title)
  {
    this.title = title;
  }

  /**
   * Method description
   *
   *
   * @param type
   */
  public void setType(ValueExpression type)
  {
    this.type = type;
  }

  /**
   * Method description
   *
   *
   * @param value
   */
  public void setValue(ValueExpression value)
  {
    this.value = value;
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
    if (title != null)
    {
      component.setValueExpression("title", title);
    }

    if (target != null)
    {
      component.setValueExpression("target", target);
    }

    if (value != null)
    {
      component.setValueExpression("value", value);
    }

    if (type != null)
    {
      component.setValueExpression("type", type);
    }

    if (label != null)
    {
      component.setValueExpression("label", label);
    }

    if (autoComplete != null)
    {
      component.setValueExpression("autoComplete", autoComplete);
    }

    super.setProperties(component);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ValueExpression autoComplete;

  /** Field description */
  private ValueExpression label;

  /** Field description */
  private ValueExpression target;

  /** Field description */
  private ValueExpression title;

  /** Field description */
  private ValueExpression type;

  /** Field description */
  private ValueExpression value;
}
