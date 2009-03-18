/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.resource;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseComponent;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class ResourceComponent extends BaseComponent
{

  /** Field description */
  public static final String FAMILY = "sonia.blog.resource";

  /** Field description */
  public static final String RENDERER = "sonia.blog.resource.renderer";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public ResourceComponent()
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
    href = (String) state[0];
    type = (String) state[1];
    title = (String) state[2];
    iePatch = (Boolean) state[3];
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
      state = new Object[4];
    }

    state[0] = href;
    state[1] = type;
    state[2] = title;
    state[3] = iePatch;

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
  public Boolean getIePatch()
  {
    if (iePatch != null)
    {
      return iePatch;
    }

    ValueExpression ve = getValueExpression("iePatch");

    return (ve != null)
           ? (Boolean) ve.getValue(getFacesContext().getELContext())
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
  public String getType()
  {
    if (type != null)
    {
      return type;
    }

    ValueExpression ve = getValueExpression("type");

    return (ve != null)
           ? (String) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
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
   * @param iePatch
   */
  public void setIePatch(Boolean iePatch)
  {
    this.iePatch = iePatch;
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
   * @param type
   */
  public void setType(String type)
  {
    this.type = type;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String href;

  /** Field description */
  private Boolean iePatch;

  /** Field description */
  private Object[] state;

  /** Field description */
  private String title;

  /** Field description */
  private String type;
}
