/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.validator;

//~--- JDK imports ------------------------------------------------------------

import java.text.SimpleDateFormat;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author sdorra
 */
public class DateFormatValidator implements Validator
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
    if (value instanceof String)
    {
      try
      {
        new SimpleDateFormat((String) value);
      }
      catch (IllegalArgumentException ex)
      {
        ResourceBundle bundle =
          context.getApplication().getResourceBundle(context, "message");
        String msgValue = bundle.getString("malformedDateFormat");
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                             msgValue, msgValue);

        throw new ValidatorException(msg);
      }
    }
  }
}
