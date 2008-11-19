/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.resource;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseTag;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.component.UIComponent;

/**
 *
 * @author sdorra
 */
public class ResourceTag extends BaseTag
{

  /**
   * Method description
   *
   */
  @Override
  public void release()
  {
    href = null;
    type = null;
    title = null;
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
    return ResourceComponent.FAMILY;
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
    return ResourceComponent.RENDERER;
  }

  //~--- set methods ----------------------------------------------------------

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
   * @param iePatch
   */
  public void setIePatch(ValueExpression iePatch)
  {
    this.iePatch = iePatch;
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
   * @param component
   */
  @Override
  protected void setProperties(UIComponent component)
  {
    if (href != null)
    {
      component.setValueExpression("href", href);
    }

    if (type != null)
    {
      component.setValueExpression("type", type);
    }

    if (title != null)
    {
      component.setValueExpression("title", title);
    }

    if (iePatch != null)
    {
      component.setValueExpression("iePatch", iePatch);
    }

    super.setProperties(component);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ValueExpression href;

  /** Field description */
  private ValueExpression iePatch;

  /** Field description */
  private ValueExpression title;

  /** Field description */
  private ValueExpression type;
}
