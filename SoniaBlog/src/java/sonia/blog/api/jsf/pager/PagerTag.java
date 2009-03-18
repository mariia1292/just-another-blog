/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.pager;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseTag;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.component.UIComponent;

/**
 *
 * @author sdorra
 */
public class PagerTag extends BaseTag
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
    return PagerComponent.FAMILY;
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
    return PagerComponent.RENDERER;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param forParam
   */
  public void setFor(ValueExpression forParam)
  {
    this.forParam = forParam;
  }

  /**
   * Method description
   *
   *
   * @param selectedStyle
   */
  public void setSelectedStyle(ValueExpression selectedStyle)
  {
    this.selectedStyle = selectedStyle;
  }

  /**
   * Method description
   *
   *
   * @param selectedStyleClass
   */
  public void setSelectedStyleClass(ValueExpression selectedStyleClass)
  {
    this.selectedStyleClass = selectedStyleClass;
  }

  /**
   * Method description
   *
   *
   * @param showPages
   */
  public void setShowPages(ValueExpression showPages)
  {
    this.showPages = showPages;
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

    if (forParam != null)
    {
      component.setValueExpression("for", forParam);
    }

    if (showPages != null)
    {
      component.setValueExpression("showPages", showPages);
    }

    if (selectedStyle != null)
    {
      component.setValueExpression("selectedStyle", selectedStyle);
    }

    if (selectedStyleClass != null)
    {
      component.setValueExpression("selectedStyleClass", selectedStyleClass);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ValueExpression forParam;

  /** Field description */
  private ValueExpression selectedStyle;

  /** Field description */
  private ValueExpression selectedStyleClass;

  /** Field description */
  private ValueExpression showPages;
}
