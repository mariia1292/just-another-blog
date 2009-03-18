/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.spam;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseTag;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.component.UIComponent;

/**
 *
 * @author sdorra
 */
public class SpamLabelTag extends BaseTag
{

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String getComponentType()
  {
    return SpamLabelComponent.FAMILY;
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
    return SpamLabelComponent.RENDERER;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   *  Method description
   *
   *
   *  @param method
   */
  public void setMethod(ValueExpression method)
  {
    this.method = method;
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
    if (method != null)
    {
      component.setValueExpression("method", method);
    }

    super.setProperties(component);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ValueExpression method;
}
