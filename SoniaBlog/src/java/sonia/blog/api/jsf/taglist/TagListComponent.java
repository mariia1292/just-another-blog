/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.taglist;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseComponent;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class TagListComponent extends BaseComponent
{

  /** Field description */
  public static final int DEFAULT_MAXPERCENTAGE = 75;

  /** Field description */
  public static final int DEFAULT_MINPERCENTAGE = 150;

  /** Field description */
  public static final String FAMILY = "sonia.blog.taglist";

  /** Field description */
  public static final String RENDERER = "sonia.blog.taglist.renderer";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public TagListComponent()
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
    minPercentage = (Integer) state[1];
    maxPercentage = (Integer) state[2];
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
    state[1] = minPercentage;
    state[2] = maxPercentage;

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
  public Integer getMaxPercentage()
  {
    if (maxPercentage != null)
    {
      return maxPercentage;
    }

    ValueExpression ve = getValueExpression("maxPercentage");

    return (ve != null)
           ? (Integer) ve.getValue(getFacesContext().getELContext())
           : DEFAULT_MAXPERCENTAGE;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Integer getMinPercentage()
  {
    if (minPercentage != null)
    {
      return minPercentage;
    }

    ValueExpression ve = getValueExpression("minPercentage");

    return (ve != null)
           ? (Integer) ve.getValue(getFacesContext().getELContext())
           : DEFAULT_MINPERCENTAGE;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param maxPercentage
   */
  public void setMaxPercentage(Integer maxPercentage)
  {
    this.maxPercentage = maxPercentage;
  }

  /**
   * Method description
   *
   *
   * @param minPercentage
   */
  public void setMinPercentage(Integer minPercentage)
  {
    this.minPercentage = minPercentage;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Integer maxPercentage;

  /** Field description */
  private Integer minPercentage;

  /** Field description */
  private Object[] state;
}
