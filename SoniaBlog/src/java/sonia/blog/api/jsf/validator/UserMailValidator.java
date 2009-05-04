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
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author sdorra
 */
public class UserMailValidator extends MailValidator
{

  /**
   * Method description
   *
   *
   * @param context
   * @param bundle
   * @param mail
   */
  @Override
  protected void validate(FacesContext context, ResourceBundle bundle,
                          String mail)
  {
    super.validate(context, bundle, mail);

    User u = BlogContext.getDAOFactory().getUserDAO().getByMail(mail);

    if (u != null)
    {
      FacesMessage msg = new FacesMessage(
                             MessageFormat.format(
                               bundle.getString("emailAllreadyExists"), mail));

      throw new ValidatorException(msg);
    }
  }
}
