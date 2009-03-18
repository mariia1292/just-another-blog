/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.spam;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.spam.SpamInputProtection;

import sonia.jsf.base.BaseComponent;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class SpamLabelComponent extends BaseComponent
{

  /** Field description */
  public static final String FAMILY = "sonia.blog.spamLabel";

  /** Field description */
  public static final String RENDERER = "sonia.blog.spamLabel.renderer";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public SpamLabelComponent()
  {
    setRendererType(RENDERER);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   * @param state
   */
  @Override
  public void restoreState(FacesContext context, Object state)
  {
    this.state = (Object[]) state;
    super.restoreState(context, this.state[0]);
    method = (SpamInputProtection) this.state[1];
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
      state = new Object[2];
    }

    state[0] = super.saveState(context);
    state[1] = method;

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
  public SpamInputProtection getMethod()
  {
    if (method != null)
    {
      return method;
    }

    ValueExpression ve = getValueExpression("method");

    return (ve != null)
           ? (SpamInputProtection) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param method
   */
  public void setMethod(SpamInputProtection method)
  {
    this.method = method;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private SpamInputProtection method;

  /** Field description */
  private Object[] state;
}
