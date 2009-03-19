/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.trackback;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.ContentObject;

import sonia.jsf.base.BaseComponent;

//~--- JDK imports ------------------------------------------------------------

import javax.el.ValueExpression;

/**
 *
 * @author sdorra
 */
public class TrackBackComponent extends BaseComponent
{

  /** Field description */
  public static final String FAMILY = "sonia.blog.trackback";

  /** Field description */
  public static final String RENDERER = "sonia.blog.trackback.renderer";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public TrackBackComponent()
  {
    setRendererType(RENDERER);
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

  //~--- set methods ----------------------------------------------------------

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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ContentObject object;
}
