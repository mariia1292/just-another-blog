/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.security.authentication;

/**
 *
 * @author sdorra
 */
public class SampleLoginModuleTest extends LoginModuleTestBase
{

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected Class<? extends LoginModule> getLoginModule()
  {
    return SampleLoginModule.class;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected char[] getPassword()
  {
    return SampleLoginModule.PASSWORD.toCharArray();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected String getRolename()
  {
    return SampleLoginModule.ROLENAME;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected String getUsername()
  {
    return SampleLoginModule.USERNAME;
  }
}
