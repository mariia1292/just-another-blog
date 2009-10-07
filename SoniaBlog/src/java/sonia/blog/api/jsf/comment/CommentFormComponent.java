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



package sonia.blog.api.jsf.comment;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Entry;

import sonia.jsf.base.BaseComponent;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

import javax.faces.context.FacesContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class CommentFormComponent extends BaseComponent
{

  /** Field description */
  public static final String FAMILY = "sonia.blog.commentForm";

  /** Field description */
  public static final String RENDERER = "sonia.blog.commentForm.renderer";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public CommentFormComponent()
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
    Object[] state = (Object[]) obj;

    super.restoreState(context, state[0]);
    entry = (Entry) state[1];
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
    Object[] state = new Object[2];

    state[0] = super.saveState(context);
    state[1] = entry;

    return state;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Entry getEntry()
  {
    if (entry != null)
    {
      return entry;
    }

    ValueExpression ve = getValueExpression("entry");

    return (ve != null)
           ? (Entry) ve.getValue(getFacesContext().getELContext())
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

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param entry
   */
  public void setEntry(Entry entry)
  {
    this.entry = entry;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Entry entry;
}
