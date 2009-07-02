/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.security.cipher;

/**
 *
 * @author sdorra
 */
public class DefaultCipherTest extends CipherTestBase
{

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected Cipher getCipher()
  {
    return new DefaultCipher();
  }
}
