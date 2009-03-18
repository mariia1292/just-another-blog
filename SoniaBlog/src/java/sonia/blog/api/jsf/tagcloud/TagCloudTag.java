/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.tagcloud;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseTag;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.component.UIComponent;

/**
 *
 * @author sdorra
 */
public class TagCloudTag extends BaseTag
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
    return TagCloudComponent.FAMILY;
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
    return TagCloudComponent.RENDERER;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param maxItems
   */
  public void setMaxItems(ValueExpression maxItems)
  {
    this.maxItems = maxItems;
  }

  /**
   * Method description
   *
   *
   * @param maxPercentage
   */
  public void setMaxPercentage(ValueExpression maxPercentage)
  {
    this.maxPercentage = maxPercentage;
  }

  /**
   * Method description
   *
   *
   * @param minPercentage
   */
  public void setMinPercentage(ValueExpression minPercentage)
  {
    this.minPercentage = minPercentage;
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
    if (minPercentage != null)
    {
      component.setValueExpression("minPercentage", minPercentage);
    }

    if (maxPercentage != null)
    {
      component.setValueExpression("maxPercentage", maxPercentage);
    }

    if (maxItems != null)
    {
      component.setValueExpression("maxItems", maxItems);
    }

    super.setProperties(component);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ValueExpression maxItems;

  /** Field description */
  private ValueExpression maxPercentage;

  /** Field description */
  private ValueExpression minPercentage;
}
