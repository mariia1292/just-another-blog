/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sonia.blog.api.jsf.validator;

import java.net.MalformedURLException;
import java.net.URL;
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
public class URLValidator implements Validator {

  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
  {
    if ( value != null )
    {
      String url = value.toString();
      if ( url != null && url.length() > 0 )
      {
        try
        {
          new URL(url);
        }
        catch ( MalformedURLException ex )
        {
          ResourceBundle bundle = context.getApplication().getResourceBundle(context, "message");
          String msgValue = bundle.getString("malformedUrl");
          FacesMessage msg = new FacesMessage( FacesMessage.SEVERITY_ERROR, msgValue, msgValue );
          throw new ValidatorException(msg);
        }
      }
    }
    
  }

}
