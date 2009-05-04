/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.validator;

//~--- JDK imports ------------------------------------------------------------

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author sdorra
 */
public class MailValidator implements Validator
{

  /** Field description */
  private static final String REGEX =
    "^[A-z0-9][\\w.-]*@[A-z0-9][\\w\\-\\.]+\\.[A-z0-9]{2,6}$";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public MailValidator()
  {
    p = Pattern.compile(REGEX);
  }

  //~--- methods --------------------------------------------------------------

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
    ResourceBundle bundle = context.getApplication().getResourceBundle(context,
                              "message");

    if ((value != null) && (value instanceof String)
        && ((String) value).length() > 0)
    {
      String text = (String) value;

      validate(context, bundle, text);
    }
    else
    {
      FacesMessage msg = new FacesMessage(bundle.getString("emailNotValid"));

      throw new ValidatorException(msg);
    }
  }

  /**
   * Method description
   *
   *
   * @param context
   * @param bundle
   * @param mail
   */
  protected void validate(FacesContext context, ResourceBundle bundle,
                          String mail)
  {
    Matcher m = p.matcher(mail);

    if (!m.matches())
    {
      String msgValue = bundle.getString("malformedMail");
      FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                           msgValue, msgValue);

      throw new ValidatorException(msg);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Pattern p;
}
