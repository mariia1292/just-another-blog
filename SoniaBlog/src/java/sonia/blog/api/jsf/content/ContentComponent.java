/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.content;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.ContentObject;

import sonia.jsf.base.BaseComponent;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class ContentComponent extends BaseComponent
{

  /** Field description */
  public static final String FAMILY = "sonia.blog.content";

  /** Field description */
  public static final String RENDERER = "sonia.blog.content.renderer";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public ContentComponent()
  {
    setRendererType(RENDERER);
  }

  //~--- methods --------------------------------------------------------------

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
    state = (Object[]) obj;
    super.restoreState(context, state[0]);
    object = (ContentObject) state[1];
    teaser = (Boolean) state[2];
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
      state = new Object[3];
    }

    state[0] = super.saveState(context);
    state[1] = object;
    state[2] = teaser;

    return state;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String getFamily()
  {
    return FAMILY;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ContentObject getObject()
  {
    if (object != null)
    {
      return object;
    }

    ValueExpression ve = getValueExpression("object");

    return (ve != null)
           ? (ContentObject) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Boolean getTeaser()
  {
    if (teaser != null)
    {
      return teaser;
    }

    ValueExpression ve = getValueExpression("teaser");

    return (ve != null)
           ? (Boolean) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   *
   * @param object
   */
  public void setEntry(ContentObject object)
  {
    this.object = object;
  }

  /**
   * Method description
   *
   *
   * @param teaser
   */
  public void setTeaser(Boolean teaser)
  {
    this.teaser = teaser;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ContentObject object;

  /** Field description */
  private Object[] state;

  /** Field description */
  private Boolean teaser;
}
