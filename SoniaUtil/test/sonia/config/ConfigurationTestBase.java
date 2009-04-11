/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

import sonia.security.cipher.DefaultCipher;

import static org.junit.Assert.*;

/**
 *
 * @author sdorra
 */
public abstract class ConfigurationTestBase
{

  /**
   * Constructs ...
   *
   */
  public ConfigurationTestBase()
  {
    config = init();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract SecureConfiguration init();

  /**
   * Method description
   *
   */
  @Test
  public void cipherTest()
  {
    config.setChipherKey("SoniaUtil".toCharArray());
    config.setCipher(new DefaultCipher());
    config.setSecureString("test6", "CipherHello");
    assertFalse("CipherHello".equals(config.getString("test6")));
    assertEquals("CipherHello", config.getSecureString("test6"));
  }

  /**
   * Method description
   *
   */
  @Test
  public void resolverTest()
  {
    System.setProperty("project-name", "SoniaUtil");
    config.setVariableResolver(new DefaultVariableResolver());
    config.set("test4", "this is the ${sys.project-name} project");
    assertEquals("this is the SoniaUtil project", config.getString("test4"));
    config.set("project-name", "SoniaUtil");
    config.set("test5", "${sys.project-name} = ${cnf.project-name}");
    assertEquals("SoniaUtil = SoniaUtil", config.getString("test5"));
  }

  /**
   * Method description
   *
   */
  @Test
  public void simpleTest()
  {
    config.set("test", "Hallo");
    config.set("test1", Long.valueOf(1l));
    config.set("test2", Boolean.TRUE);
    config.setMulti("test3", new String[] { "Hallo", "Hallo1" });
    assertEquals("Hallo", config.getString("test"));
    assertEquals(Long.valueOf(1), config.getLong("test1"));
    assertTrue(config.getBoolean("test2"));
    assertEquals(new String[] { "Hallo", "Hallo1" },
                 config.getStrings("test3"));
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private SecureConfiguration config;
}
