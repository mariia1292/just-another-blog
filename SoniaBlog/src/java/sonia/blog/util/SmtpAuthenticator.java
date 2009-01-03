/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.util;

//~--- JDK imports ------------------------------------------------------------

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 *
 * @author sdorra
 */
public class SmtpAuthenticator extends Authenticator
{

  /**
   * Constructs ...
   *
   *
   * @param username
   * @param password
   */
  public SmtpAuthenticator(String username, String password)
  {
    this.username = username;
    this.password = password;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected PasswordAuthentication getPasswordAuthentication()
  {
    return new PasswordAuthentication(username, password);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String password;

  /** Field description */
  private String username;
}
