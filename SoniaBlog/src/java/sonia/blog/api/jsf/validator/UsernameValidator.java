/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.validator;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.entity.User;

//~--- JDK imports ------------------------------------------------------------

import java.text.MessageFormat;

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
public class UsernameValidator implements Validator
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
    ResourceBundle msgs = context.getApplication().getResourceBundle(context,
                            "message");

    if ((value != null) && (value instanceof String)
        && ((String) value).length() > 0)
    {
      String username = (String) value;
      User u = BlogContext.getDAOFactory().getUserDAO().get(username);

      if (u != null)
      {
        FacesMessage msg = new FacesMessage(
                               MessageFormat.format(
                                 msgs.getString("nameAllreadyExists"), value));

        throw new ValidatorException(msg);
      }
    }
    else
    {
      FacesMessage msg = new FacesMessage(msgs.getString("nameIsNotValid"));

      throw new ValidatorException(msg);
    }
  }
}
