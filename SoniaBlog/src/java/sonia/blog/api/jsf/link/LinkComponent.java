/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.link;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.PermaObject;

import sonia.jsf.base.BaseComponent;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class LinkComponent extends BaseComponent
{

  /** Field description */
  public static final String FAMILY = "sonia.blog.link";

  /** Field description */
  public static final String RENDERER = "sonia.blog.link.renderer";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public LinkComponent()
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
    value = (String) state[1];
    href = (String) state[2];
    title = (String) state[3];
    target = (String) state[4];
    object = (PermaObject) state[5];
    disabled = (Boolean) state[6];
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
      state = new Object[7];
    }

    state[0] = super.saveState(context);
    state[1] = value;
    state[2] = href;
    state[3] = title;
    state[4] = target;
    state[5] = object;
    state[6] = disabled;

    return state;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Boolean getDisabled()
  {
    if (disabled != null)
    {
      return disabled;
    }

    ValueExpression ve = getValueExpression("disabled");

    return (ve != null)
           ? (Boolean) ve.getValue(getFacesContext().getELContext())
           : Boolean.FALSE;
  }

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
  public String getHref()
  {
    if (href != null)
    {
      return href;
    }

    ValueExpression ve = getValueExpression("href");

    return (ve != null)
           ? (String) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public PermaObject getObject()
  {
    if (object != null)
    {
      return object;
    }

    ValueExpression ve = getValueExpression("object");

    return (ve != null)
           ? (PermaObject) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTarget()
  {
    if (target != null)
    {
      return target;
    }

    ValueExpression ve = getValueExpression("target");

    return (ve != null)
           ? (String) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTitle()
  {
    if (title != null)
    {
      return title;
    }

    ValueExpression ve = getValueExpression("title");

    return (ve != null)
           ? (String) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getValue()
  {
    if (value != null)
    {
      return value;
    }

    ValueExpression ve = getValueExpression("value");

    return (ve != null)
           ? (String) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param disabled
   */
  public void setDisabled(Boolean disabled)
  {
    this.disabled = disabled;
  }

  /**
   * Method description
   *
   *
   *
   * @param href
   */
  public void setHref(String href)
  {
    this.href = href;
  }

  /**
   * Method description
   *
   *
   * @param object
   */
  public void setObject(PermaObject object)
  {
    this.object = object;
  }

  /**
   * Method description
   *
   *
   * @param target
   */
  public void setTarget(String target)
  {
    this.target = target;
  }

  /**
   * Method description
   *
   *
   * @param title
   */
  public void setTitle(String title)
  {
    this.title = title;
  }

  /**
   * Method description
   *
   *
   * @param value
   */
  public void setValue(String value)
  {
    this.value = value;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Boolean disabled;

  /** Field description */
  private String href;

  /** Field description */
  private PermaObject object;

  /** Field description */
  private Object[] state;

  /** Field description */
  private String target;

  /** Field description */
  private String title;

  /** Field description */
  private String value;
}
