/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.el.ValueExpression;

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
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

    Collections.sort( resources );

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
