/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.link;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseTag;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.component.UIComponent;

/**
 *
 * @author sdorra
 */
public class LinkTag extends BaseTag
{

  /**
   * Method description
   *
   */
  @Override
  public void release()
  {
    value = null;
    href = null;
    title = null;
    target = null;
    disabled = null;
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
    return LinkComponent.FAMILY;
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
    return LinkComponent.RENDERER;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param disabled
   */
  public void setDisabled(ValueExpression disabled)
  {
    this.disabled = disabled;
  }

  /**
   * Method description
   *
   *
   * @param href
   */
  public void setHref(ValueExpression href)
  {
    this.href = href;
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
    if (value != null)
    {
      component.setValueExpression("value", value);
    }

    if (href != null)
    {
      component.setValueExpression("href", href);
    }

    if (title != null)
    {
      component.setValueExpression("title", title);
    }

    if (target != null)
    {
      component.setValueExpression("target", target);
    }

    if (object != null)
    {
      component.setValueExpression("object", object);
    }

    if (disabled != null)
    {
      component.setValueExpression("disabled", disabled);
    }

    super.setProperties(component);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ValueExpression disabled;

  /** Field description */
  private ValueExpression href;

  /** Field description */
  private ValueExpression object;

  /** Field description */
  private ValueExpression target;

  /** Field description */
  private ValueExpression title;

  /** Field description */
  private ValueExpression value;
}
