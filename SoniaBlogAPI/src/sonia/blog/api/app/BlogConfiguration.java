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

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Date;

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
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws IOException
   */
  public void load() throws IOException
  {
    File file = BlogContext.getInstance().getConfigFile();

    if (file.exists())
    {
      FileInputStream fis = new FileInputStream(file);

      try
      {
        load(fis);
      }
      finally
      {
        if (fis != null)
        {
          fis.close();
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @throws IOException
   */
  public void store() throws IOException
  {
    File file = BlogContext.getInstance().getConfigFile();
    File backupDir = new File(file.getParentFile(), "backup");

    if (!backupDir.exists())
    {
      backupDir.mkdirs();
    }

    File backupFile = new File(backupDir,
                               "config-" + new Date().getTime() + ".xml");

    file.renameTo(backupFile);

    FileOutputStream fos = new FileOutputStream(file);

    try
    {
      store(fos);
    }
    catch (IOException ex)
    {
      if (!file.exists() && backupFile.exists())
      {
        backupFile.renameTo(file);
      }
    }
    finally
    {
      if (fos != null)
      {
        fos.close();
      }
    }
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
      value = getCipher().decode(secureKey, value);
    }
    else
    {
      value = def;
    }

    return value;
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public String getEncString(String key)
  {
    return getEncString(key, null);
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
    if (value != null)
    {
      char[] secureKey = getKey();

      value = getCipher().encode(secureKey, value);
    }

    set(key, value);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private Cipher getCipher()
  {
    if (cipherReference == null)
    {
      cipherReference =
        BlogContext.getInstance().getServiceRegistry().get(Cipher.class,
          Constants.SERVCIE_CIPHER);
    }

    return cipherReference.get();
  }

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
