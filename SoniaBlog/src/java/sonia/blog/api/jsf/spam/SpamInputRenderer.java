/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.spam;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseRenderer;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author sdorra
 */
public class SpamInputRenderer extends BaseRenderer
{

  /**
   * Method description
   *
   *
   * @param context
   * @param component
   */
  @Override
  public void decode(FacesContext context, UIComponent component)
  {
    if (!(component instanceof SpamInputComponent))
    {
      throw new IllegalArgumentException(
          "component is not an instance of SpamComponent");
    }

    SpamInputComponent sc = (SpamInputComponent) component;

    if (sc.isRendered())
    {
      String clientId = component.getClientId(context);
      String input =
        context.getExternalContext().getRequestParameterMap().get(clientId);
      String answer = (String) context.getExternalContext().getSessionMap().get(
                          SpamLabelRenderer.REQUESTKEY);

      context.getExternalContext().getSessionMap().remove(
          SpamLabelRenderer.REQUESTKEY);

      boolean valid = input.equals(answer);

      if (!valid)
      {
        String msg = context.getApplication().getResourceBundle(context,
                       "message").getString("spamInputFailure");

        context.addMessage(clientId,
                           new FacesMessage(FacesMessage.SEVERITY_WARN, msg,
                             null));
      }

      sc.setValid(valid);
    }
  }

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
    if (!(component instanceof SpamInputComponent))
    {
      throw new IllegalArgumentException(
          "component is not an instance of SpamComponent");
    }

    SpamInputComponent sc = (SpamInputComponent) component;

    if (sc.isRendered())
    {
      String clientId = sc.getClientId(context);
      ResponseWriter writer = context.getResponseWriter();

      writer.startElement("input", component);
      writer.writeAttribute("type", "text", null);
      writer.writeAttribute("id", clientId, null);
      writer.writeAttribute("name", clientId, null);
      writer.endElement("input");
    }
  }
}
