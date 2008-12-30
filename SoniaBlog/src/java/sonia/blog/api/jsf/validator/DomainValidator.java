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
public class DomainValidator implements Validator
{

  /** Field description */
  private static final String REGEX = "^[A-z0-9][\\w\\-\\.]+$";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public DomainValidator()
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
    if (value != null)
    {
      String text = value.toString();

      if ((text != null) && (text.length() > 0))
      {
        Matcher m = p.matcher(text);

        if (!m.matches())
        {
          ResourceBundle bundle =
            context.getApplication().getResourceBundle(context, "message");
          String msgValue = bundle.getString("malformedDomain");
          FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                               msgValue, msgValue);

          throw new ValidatorException(msg);
        }
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Pattern p;
}
