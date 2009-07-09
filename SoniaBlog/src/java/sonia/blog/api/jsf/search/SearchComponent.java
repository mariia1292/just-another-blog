/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.search;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseComponent;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class SearchComponent extends BaseComponent
{

  /** Field description */
  public static final String FAMILY = "sonia.blog.search";

  /** Field description */
  public static final String RENDERER = "sonia.blog.search.renderer";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public SearchComponent()
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
    target = (String) state[1];
    title = (String) state[2];
    type = (String) state[3];
    value = (String) state[4];
    label = (String) state[5];
    autoComplete = (Boolean) state[6];
  }

  private Boolean autoComplete;

  public Boolean isAutoComplete()
  {
         if (autoComplete != null)
    {
      return autoComplete;
    }

    ValueExpression ve = getValueExpression("autoComplete");

    return (ve != null)
           ? (Boolean) ve.getValue(getFacesContext().getELContext())
           : Boolean.FALSE;
  
  }

  public void setAutoComplete(Boolean autoComplete)
  {
    this.autoComplete = autoComplete;
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
    state[1] = target;
    state[2] = title;
    state[3] = type;
    state[4] = value;
    state[5] = label;
    state[6] = autoComplete;

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
  public String getLabel()
  {
    if (label != null)
    {
      return label;
    }

    ValueExpression ve = getValueExpression("label");

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
   * @param label
   */
  public void setLabel(String label)
  {
    this.label = label;
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
   * @param type
   */
  public void setType(String type)
  {
    this.type = type;
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
  private String label;

  /** Field description */
  private Object[] state;

  /** Field description */
  private String target;

  /** Field description */
  private String title;

  /** Field description */
  private String type;

  /** Field description */
  private String value;
}
