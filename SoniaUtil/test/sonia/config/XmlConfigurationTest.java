/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

/**
 *
 * @author sdorra
 */
public class XmlConfigurationTest extends ConfigurationTestBase
{

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected SecureConfiguration init()
  {
    return new XmlConfiguration();
  }
}
