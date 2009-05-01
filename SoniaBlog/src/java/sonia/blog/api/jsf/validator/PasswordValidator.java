/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.validator;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author sdorra
 */
public class PasswordValidator implements Validator
{

  /**
   * Method description
   *
   *
   * @param context
   * @param component
   * @param value
   *
   * @throws ValidatorException
   */
  public void validate(FacesContext context, UIComponent component,
                       Object value)
          throws ValidatorException
  {
    String passwordId = (String) component.getAttributes().get("passwordId");
    ResourceBundle msgs = context.getApplication().getResourceBundle(context,
                            "message");

    if (Util.hasContent(passwordId))
    {
      UIInput input = (UIInput) context.getViewRoot().findComponent(passwordId);

      if (input != null)
      {
        Object oValue = input.getValue();

        if ((oValue == null) ||!value.equals(oValue))
        {
          FacesMessage msg =
            new FacesMessage(msgs.getString("passwordsNotEqual"));

          throw new ValidatorException(msg);
        }
      }
      else
      {
        FacesMessage msg = new FacesMessage(passwordId + " not found");

        throw new ValidatorException(msg);
      }
    }
    else
    {
      FacesMessage msg = new FacesMessage("attribute equalsId is requiered");

      throw new ValidatorException(msg);
    }
  }
}
