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



package sonia.blog.api.jsf.header;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.macro.WebResource;
import sonia.blog.api.mapping.FilterMapping;
import sonia.blog.api.mapping.Mapping;
import sonia.blog.util.BlogUtil;

import sonia.jsf.base.BaseComponent;

import sonia.plugin.service.ServiceReference;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collections;
import java.util.List;

import javax.el.ValueExpression;

import javax.faces.context.FacesContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class HeaderComponent extends BaseComponent
{

  /** Field description */
  public static final String FAMILY = "sonia.blog.header";

  /** Field description */
  public static final String RENDERER = "sonia.blog.header.renderer";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public HeaderComponent()
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
    comments = (Boolean) state[1];
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
    state[1] = comments;

    return state;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Boolean getComments()
  {
    if (comments != null)
    {
      return comments;
    }

    ValueExpression ve = getValueExpression("comments");

    return (ve != null)
           ? (Boolean) ve.getValue(getFacesContext().getELContext())
           : Boolean.TRUE;
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
   * @param context
   *
   * @return
   */
  public List<WebResource> getResources(FacesContext context)
  {
    List<WebResource> resources = getMappingResources(context);

    if (resources != null)
    {
      resources.addAll(getServiceResources());
    }
    else
    {
      resources = getServiceResources();
    }

    Collections.sort(resources);

    return Util.unique(resources);
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param comments
   */
  public void setComments(Boolean comments)
  {
    this.comments = comments;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   *
   * @return
   */
  private List<WebResource> getMappingResources(FacesContext context)
  {
    List<WebResource> resources = null;
    BlogRequest request =
      BlogUtil.getBlogRequest(context.getExternalContext().getRequest());
    Mapping mapping = request.getMapping();

    if ((mapping != null) && (mapping instanceof FilterMapping))
    {
      resources = ((FilterMapping) mapping).getResources();
    }

    return resources;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  private List<WebResource> getServiceResources()
  {
    List<WebResource> result = null;
    ServiceReference<WebResource> reference =
      BlogContext.getInstance().getServiceRegistry().get(WebResource.class,
        Constants.SERVICE_WEBRESOURCE);

    if (reference != null)
    {
      result = reference.getAll();
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Boolean comments;

  /** Field description */
  private Object[] state;
}
