/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.spam;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.spam.SpamInputProtection;

import sonia.jsf.base.BaseRenderer;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class SpamLabelRenderer extends BaseRenderer
{

  /** Field description */
  public static final String REQUESTKEY = "sonia.spam.answer";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   * @param component
   *
   * @throws IOException
   */
  @Override
  public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException
  {
    if (!(component instanceof SpamLabelComponent))
    {
      throw new IllegalArgumentException(
          "component is not an instance of SpamComponent");
    }

    SpamLabelComponent sc = (SpamLabelComponent) component;
    SpamInputProtection method = sc.getMethod();

    if (method == null)
    {
      throw new IllegalStateException("method is null");
    }
    else
    {
      BlogRequest request =
        (BlogRequest) context.getExternalContext().getRequest();
      String answer = method.renderInput(request, context.getResponseWriter());

      context.getExternalContext().getSessionMap().put(REQUESTKEY, answer);
    }
  }
}
