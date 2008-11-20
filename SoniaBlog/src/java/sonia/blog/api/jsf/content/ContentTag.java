/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.content;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseTag;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.component.UIComponent;

/**
 *
 * @author sdorra
 */
public class ContentTag extends BaseTag
{

  /**
   * Method description
   *
   */
  @Override
  public void release()
  {
    entry = null;
    teaser = null;
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
    return ContentComponent.FAMILY;
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
    return ContentComponent.RENDERER;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param entry
   */
  public void setEntry(ValueExpression entry)
  {
    this.entry = entry;
  }

  /**
   * Method description
   *
   *
   * @param teaser
   */
  public void setTeaser(ValueExpression teaser)
  {
    this.teaser = teaser;
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
    if (entry != null)
    {
      component.setValueExpression("entry", entry);
    }

    if (teaser != null)
    {
      component.setValueExpression("teaser", teaser);
    }

    super.setProperties(component);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ValueExpression entry;

  /** Field description */
  private ValueExpression teaser;
}
