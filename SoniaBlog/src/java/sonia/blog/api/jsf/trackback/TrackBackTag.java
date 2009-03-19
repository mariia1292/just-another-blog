/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sonia.blog.api.jsf.trackback;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import sonia.jsf.base.BaseTag;

/**
 *
 * @author sdorra
 */
public class TrackBackTag extends BaseTag {

  @Override
  public String getComponentType()
  {
    return TrackBackComponent.FAMILY;
  }

  @Override
  public String getRendererType()
  {
    return TrackBackComponent.RENDERER;
  }

  private ValueExpression object;

  public void setObject(ValueExpression object)
  {
    this.object = object;
  }

  @Override
  protected void setProperties(UIComponent component)
  {
    if ( object != null )
    {
      component.setValueExpression("object", object);
    }
    super.setProperties(component);
  }

  

}
