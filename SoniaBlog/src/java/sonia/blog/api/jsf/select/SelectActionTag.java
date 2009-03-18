/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.select;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseTag;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.component.UIComponent;

/**
 *
 * @author sdorra
 */
public class SelectActionTag extends BaseTag
{

  /**
   * Method description
   *
   */
  @Override
  public void release()
  {
    super.release();
    onblur = null;
    onchange = null;
    onclick = null;
    ondblclick = null;
    onfocus = null;
    onkeydown = null;
    onkeypress = null;
    onkeyup = null;
    onmousedown = null;
    onmousemove = null;
    onmouseout = null;
    onmouseover = null;
    onmouseup = null;
    value = null;
    actions = null;
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
    return SelectActionComponent.FAMILY;
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
    return SelectActionComponent.RENDERER;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param actions
   */
  public void setActions(ValueExpression actions)
  {
    this.actions = actions;
  }

  /**
   * Method description
   *
   *
   * @param onblur
   */
  public void setOnblur(ValueExpression onblur)
  {
    this.onblur = onblur;
  }

  /**
   * Method description
   *
   *
   * @param onchange
   */
  public void setOnchange(ValueExpression onchange)
  {
    this.onchange = onchange;
  }

  /**
   * Method description
   *
   *
   * @param onclick
   */
  public void setOnclick(ValueExpression onclick)
  {
    this.onclick = onclick;
  }

  /**
   * Method description
   *
   *
   * @param ondblclick
   */
  public void setOndblclick(ValueExpression ondblclick)
  {
    this.ondblclick = ondblclick;
  }

  /**
   * Method description
   *
   *
   * @param onfocus
   */
  public void setOnfocus(ValueExpression onfocus)
  {
    this.onfocus = onfocus;
  }

  /**
   * Method description
   *
   *
   * @param onkeydown
   */
  public void setOnkeydown(ValueExpression onkeydown)
  {
    this.onkeydown = onkeydown;
  }

  /**
   * Method description
   *
   *
   * @param onkeypress
   */
  public void setOnkeypress(ValueExpression onkeypress)
  {
    this.onkeypress = onkeypress;
  }

  /**
   * Method description
   *
   *
   * @param onkeyup
   */
  public void setOnkeyup(ValueExpression onkeyup)
  {
    this.onkeyup = onkeyup;
  }

  /**
   * Method description
   *
   *
   * @param onmousedown
   */
  public void setOnmousedown(ValueExpression onmousedown)
  {
    this.onmousedown = onmousedown;
  }

  /**
   * Method description
   *
   *
   * @param onmousemove
   */
  public void setOnmousemove(ValueExpression onmousemove)
  {
    this.onmousemove = onmousemove;
  }

  /**
   * Method description
   *
   *
   * @param onmouseout
   */
  public void setOnmouseout(ValueExpression onmouseout)
  {
    this.onmouseout = onmouseout;
  }

  /**
   * Method description
   *
   *
   * @param onmouseover
   */
  public void setOnmouseover(ValueExpression onmouseover)
  {
    this.onmouseover = onmouseover;
  }

  /**
   * Method description
   *
   *
   * @param onmouseup
   */
  public void setOnmouseup(ValueExpression onmouseup)
  {
    this.onmouseup = onmouseup;
  }

  /**
   * Method description
   *
   *
   * @param value
   */
  public void setValue(ValueExpression value)
  {
    this.value = value;
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
    if (onblur != null)
    {
      component.setValueExpression("onblur", onblur);
    }

    if (onchange != null)
    {
      component.setValueExpression("onchange", onchange);
    }

    if (onclick != null)
    {
      component.setValueExpression("onclick", onclick);
    }

    if (ondblclick != null)
    {
      component.setValueExpression("ondblclick", ondblclick);
    }

    if (onfocus != null)
    {
      component.setValueExpression("onfocus", onfocus);
    }

    if (onkeydown != null)
    {
      component.setValueExpression("onkeydown", onkeydown);
    }

    if (onkeypress != null)
    {
      component.setValueExpression("onkeypress", onkeypress);
    }

    if (onkeyup != null)
    {
      component.setValueExpression("onkeyup", onkeyup);
    }

    if (onmousedown != null)
    {
      component.setValueExpression("onmousedown", onmousedown);
    }

    if (onmousemove != null)
    {
      component.setValueExpression("onmousemove", onmousemove);
    }

    if (onmouseout != null)
    {
      component.setValueExpression("onmouseout", onmouseout);
    }

    if (onmouseup != null)
    {
      component.setValueExpression("onmouseup", onmouseup);
    }

    if (value != null)
    {
      component.setValueExpression("value", value);
    }

    if (actions != null)
    {
      component.setValueExpression("actions", actions);
    }

    super.setProperties(component);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ValueExpression actions;

  /** Field description */
  private ValueExpression onblur;

  /** Field description */
  private ValueExpression onchange;

  /** Field description */
  private ValueExpression onclick;

  /** Field description */
  private ValueExpression ondblclick;

  /** Field description */
  private ValueExpression onfocus;

  /** Field description */
  private ValueExpression onkeydown;

  /** Field description */
  private ValueExpression onkeypress;

  /** Field description */
  private ValueExpression onkeyup;

  /** Field description */
  private ValueExpression onmousedown;

  /** Field description */
  private ValueExpression onmousemove;

  /** Field description */
  private ValueExpression onmouseout;

  /** Field description */
  private ValueExpression onmouseover;

  /** Field description */
  private ValueExpression onmouseup;

  /** Field description */
  private ValueExpression value;
}
