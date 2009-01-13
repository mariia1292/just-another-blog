/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.config.XmlConfiguration;

import sonia.plugin.service.ServiceReference;

import sonia.security.KeyGenerator;
import sonia.security.cipher.Cipher;

import sonia.util.Util;

/**
 *
 * @author sdorra
 */
public class BlogConfiguration extends XmlConfiguration
{

  /**
   * Constructs ...
   *
   */
  public BlogConfiguration()
  {
    super();
    init();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public String getEncString(String key, String def)
  {
    char[] secureKey = getKey();
    String value = getString(key);

    if (value != null)
    {
      value = cipherReference.get().decode(secureKey, value);
    }
    else
    {
      value = def;
    }

    return value;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   */
  public void setEncString(String key, String value)
  {
    char[] secureKey = getKey();

    value = cipherReference.get().decode(secureKey, value);
    set(key, value);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  private void init()
  {
    cipherReference =
      BlogContext.getInstance().getServiceRegistry().get(Cipher.class,
        Constants.SERVCIE_CIPHER);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private char[] getKey()
  {
    String key = getString(Constants.CONFIG_SECUREKEY);

    if (Util.isBlank(key))
    {
      key = KeyGenerator.generateKey(16);
      set(Constants.CONFIG_SECUREKEY, key);
    }

    return key.toCharArray();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference<Cipher> cipherReference;
}
