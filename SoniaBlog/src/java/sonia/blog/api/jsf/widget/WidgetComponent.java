/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.widget;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.ContentObject;

import sonia.jsf.base.BaseComponent;

import sonia.macro.browse.MacroWidget;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class WidgetComponent extends BaseComponent
{

  /** Field description */
  public static final String FAMILY = "sonia.blog.widget";

  /** Field description */
  public static final String RENDERER = "sonia.blog.widget.renderer";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public WidgetComponent()
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
  @SuppressWarnings("unchecked")
  public void restoreState(FacesContext context, Object obj)
  {
    state = (Object[]) obj;
    super.restoreState(context, state[0]);
    widget = (Class<? extends MacroWidget>) state[1];
    param = (String) state[2];
    object = (ContentObject) state[3];
    name = (String) state[4];
    result = (Boolean) state[5];
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
      state = new Object[6];
    }

    state[0] = super.saveState(context);
    state[1] = widget;
    state[2] = param;
    state[3] = object;
    state[4] = name;
    state[5] = result;

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
  public String getName()
  {
    if (name != null)
    {
      return name;
    }

    ValueExpression ve = getValueExpression("name");

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
  public String getParam()
  {
    if (param != null)
    {
      return param;
    }

    ValueExpression ve = getValueExpression("param");

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
  public Boolean getResult()
  {
    if (result != null)
    {
      System.out.println("result: " + result);

      return result;
    }

    ValueExpression ve = getValueExpression("result");
    Boolean re = (ve != null)
                 ? (Boolean) ve.getValue(getFacesContext().getELContext())
                 : Boolean.FALSE;

    System.out.println("re: " + re);

    return re;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public Class<? extends MacroWidget> getWidget()
  {
    if (widget != null)
    {
      return widget;
    }

    ValueExpression ve = getValueExpression("widget");

    return (ve != null)
           ? (Class<? extends MacroWidget>) ve.getValue(
               getFacesContext().getELContext())
           : null;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param name
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Method description
   *
   *
   * @param object
   */
  public void setObject(ContentObject object)
  {
    this.object = object;
  }

  /**
   * Method description
   *
   *
   * @param param
   */
  public void setParam(String param)
  {
    this.param = param;
  }

  /**
   * Method description
   *
   *
   * @param result
   */
  public void setResult(Boolean result)
  {
    this.result = result;
  }

  /**
   * Method description
   *
   *
   * @param widget
   */
  public void setWidget(Class<? extends MacroWidget> widget)
  {
    this.widget = widget;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String name;

  /** Field description */
  private ContentObject object;

  /** Field description */
  private String param;

  /** Field description */
  private Boolean result;

  /** Field description */
  private Object[] state;

  /** Field description */
  private Class<? extends MacroWidget> widget;
}
