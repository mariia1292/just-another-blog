/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

/**
 *
 * @author sdorra
 */
public interface MailService
{

  /**
   * Method description
   *
   *
   * @param to
   * @param from
   * @param subject
   * @param text
   *
   *
   * @throws Exception
   */
  public void sendHtmlMail(String to, String from, String subject, String text)
          throws Exception;

  /**
   * Method description
   *
   *
   * @param to
   * @param from
   * @param subject
   * @param text
   *
   *
   * @throws Exception
   */
  public void sendMail(String to, String from, String subject, String text)
          throws Exception;

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isConfigured();
}
