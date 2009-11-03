/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://kenai.com/projects/jab
 *
 */



package sonia.blog.api.jsf.select;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.util.SelectAction;

import sonia.jsf.base.BaseComponent;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.el.ValueExpression;

import javax.faces.context.FacesContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class SelectActionComponent extends BaseComponent
{

  /** Field description */
  public static final String FAMILY = "sonia.blog.selectAction";

  /** Field description */
  public static final String RENDERER = "sonia.blog.selectAction.renderer";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public SelectActionComponent()
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
    Object[] state = (Object[]) obj;

    super.restoreState(context, state[0]);
    onblur = (String) state[1];
    onchange = (String) state[2];
    onclick = (String) state[3];
    ondblclick = (String) state[4];
    onfocus = (String) state[5];
    onkeydown = (String) state[6];
    onkeypress = (String) state[7];
    onkeyup = (String) state[8];
    onmousedown = (String) state[9];
    onmousemove = (String) state[10];
    onmouseout = (String) state[11];
    onmouseover = (String) state[12];
    onmouseup = (String) state[13];
    actions = (List<SelectAction>) state[14];
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
    Object[] state = new Object[15];

    state[0] = super.saveState(context);
    state[1] = onblur;
    state[2] = onchange;
    state[3] = onclick;
    state[4] = ondblclick;
    state[5] = onfocus;
    state[6] = onkeydown;
    state[7] = onkeypress;
    state[8] = onkeyup;
    state[9] = onmousedown;
    state[10] = onmousemove;
    state[11] = onmouseout;
    state[12] = onmouseover;
    state[13] = onmouseup;
    state[14] = actions;

    return state;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SelectAction> getActions()
  {
    if (actions != null)
    {
      return actions;
    }

    ValueExpression ve = getValueExpression("actions");

    return (ve != null)
           ? (List<SelectAction>) ve.getValue(getFacesContext().getELContext())
           : null;
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
  public String getOnblur()
  {
    if (onblur != null)
    {
      return onblur;
    }

    ValueExpression ve = getValueExpression("onblur");

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
  public String getOnchange()
  {
    if (onchange != null)
    {
      return onchange;
    }

    ValueExpression ve = getValueExpression("onchange");

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
  public String getOnclick()
  {
    if (onclick != null)
    {
      return onclick;
    }

    ValueExpression ve = getValueExpression("onclick");

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
  public String getOndblclick()
  {
    if (ondblclick != null)
    {
      return ondblclick;
    }

    ValueExpression ve = getValueExpression("ondblclick");

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
  public String getOnfocus()
  {
    if (onfocus != null)
    {
      return onfocus;
    }

    ValueExpression ve = getValueExpression("onfocus");

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
  public String getOnkeydown()
  {
    if (onkeydown != null)
    {
      return onkeydown;
    }

    ValueExpression ve = getValueExpression("onkeydown");

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
  public String getOnkeypress()
  {
    if (onkeypress != null)
    {
      return onkeypress;
    }

    ValueExpression ve = getValueExpression("onkeypress");

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
  public String getOnkeyup()
  {
    if (onkeyup != null)
    {
      return onkeyup;
    }

    ValueExpression ve = getValueExpression("onkeyup");

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
  public String getOnmousedown()
  {
    if (onmousedown != null)
    {
      return onmousedown;
    }

    ValueExpression ve = getValueExpression("onmousedown");

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
  public String getOnmousemove()
  {
    if (onmousemove != null)
    {
      return onmousemove;
    }

    ValueExpression ve = getValueExpression("onmousemove");

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
  public String getOnmouseout()
  {
    if (onmouseout != null)
    {
      return onmouseout;
    }

    ValueExpression ve = getValueExpression("onmouseout");

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
  public String getOnmouseover()
  {
    if (onmouseover != null)
    {
      return onmouseover;
    }

    ValueExpression ve = getValueExpression("onmouseover");

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
  public String getOnmouseup()
  {
    if (onmouseup != null)
    {
      return onmouseup;
    }

    ValueExpression ve = getValueExpression("onmouseup");

    return (ve != null)
           ? (String) ve.getValue(getFacesContext().getELContext())
           : null;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param actions
   */
  public void setActions(List<SelectAction> actions)
  {
    this.actions = actions;
  }

  /**
   * Method description
   *
   *
   * @param onblur
   */
  public void setOnblur(String onblur)
  {
    this.onblur = onblur;
  }

  /**
   * Method description
   *
   *
   * @param onchange
   */
  public void setOnchange(String onchange)
  {
    this.onchange = onchange;
  }

  /**
   * Method description
   *
   *
   * @param onclick
   */
  public void setOnclick(String onclick)
  {
    this.onclick = onclick;
  }

  /**
   * Method description
   *
   *
   * @param ondblclick
   */
  public void setOndblclick(String ondblclick)
  {
    this.ondblclick = ondblclick;
  }

  /**
   * Method description
   *
   *
   * @param onfocus
   */
  public void setOnfocus(String onfocus)
  {
    this.onfocus = onfocus;
  }

  /**
   * Method description
   *
   *
   * @param onkeydown
   */
  public void setOnkeydown(String onkeydown)
  {
    this.onkeydown = onkeydown;
  }

  /**
   * Method description
   *
   *
   * @param onkeypress
   */
  public void setOnkeypress(String onkeypress)
  {
    this.onkeypress = onkeypress;
  }

  /**
   * Method description
   *
   *
   * @param onkeyup
   */
  public void setOnkeyup(String onkeyup)
  {
    this.onkeyup = onkeyup;
  }

  /**
   * Method description
   *
   *
   * @param onmousedown
   */
  public void setOnmousedown(String onmousedown)
  {
    this.onmousedown = onmousedown;
  }

  /**
   * Method description
   *
   *
   * @param onmousemove
   */
  public void setOnmousemove(String onmousemove)
  {
    this.onmousemove = onmousemove;
  }

  /**
   * Method description
   *
   *
   * @param onmouseout
   */
  public void setOnmouseout(String onmouseout)
  {
    this.onmouseout = onmouseout;
  }

  /**
   * Method description
   *
   *
   * @param onmouseover
   */
  public void setOnmouseover(String onmouseover)
  {
    this.onmouseover = onmouseover;
  }

  /**
   * Method description
   *
   *
   * @param onmouseup
   */
  public void setOnmouseup(String onmouseup)
  {
    this.onmouseup = onmouseup;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<SelectAction> actions;

  /** Field description */
  private String onblur;

  /** Field description */
  private String onchange;

  /** Field description */
  private String onclick;

  /** Field description */
  private String ondblclick;

  /** Field description */
  private String onfocus;

  /** Field description */
  private String onkeydown;

  /** Field description */
  private String onkeypress;

  /** Field description */
  private String onkeyup;

  /** Field description */
  private String onmousedown;

  /** Field description */
  private String onmousemove;

  /** Field description */
  private String onmouseout;

  /** Field description */
  private String onmouseover;

  /** Field description */
  private String onmouseup;
}
