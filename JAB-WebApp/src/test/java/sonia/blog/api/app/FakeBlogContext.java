/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://kenai.com/projects/jab
 *
 */



package sonia.blog.api.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.dao.DAOFactory;
import sonia.blog.api.exception.BlogException;
import sonia.blog.dao.jpa.JpaDAOFactory;

import sonia.plugin.service.ServiceRegistry;

import sonia.security.cipher.Cipher;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Sebastian Sdorra
 */
public class FakeBlogContext extends BlogContext
{

  /** Field description */
  private static DAOFactory factory;

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static DAOFactory getDAOFactory()
  {
    if (factory == null)
    {
      factory = new JpaDAOFactory();
    }

    return factory;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public BlogConfiguration getConfiguration()
  {
    if (configuration == null)
    {
      configuration = new BlogConfiguration();
      configuration.setCipher(new DummyCipher());
      configuration.set(Constants.CONFIG_DB_DRIVER,
                        "org.apache.derby.jdbc.EmbeddedDriver");
      configuration.set(Constants.CONFIG_DB_EMBEDDED, Boolean.TRUE);
      configuration.set(Constants.CONFIG_DB_PROFILE, "derby-embedded");
      configuration.set(Constants.CONFIG_DB_USERNAME, "jab");
      configuration.set(Constants.CONFIG_DB_PASSWORD, "jab123");
      configuration.set(Constants.CONFIG_DB_URL,
                        "jdbc:derby:memory:jab;create=true");
    }

    return configuration;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public ResourceManager getResourceManager()
  {
    File file = null;

    try
    {
      file = File.createTempFile("jab-unit", "-test");
      file.delete();
      file.mkdirs();
    }
    catch (IOException ex)
    {
      throw new BlogException(ex);
    }

    if (resourceManager == null)
    {
      resourceManager = new ResourceManager(file);
    }

    return resourceManager;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public ServiceRegistry getServiceRegistry()
  {
    if (serviceRegistry == null)
    {
      try
      {
        serviceRegistry = new ServiceRegistry();
        serviceRegistry.register(String.class, Constants.SERVICE_DBPROFILE).add(
            "/sonia/blog/dao/jpa/profile/derby-embedded-profile.properties");
      }
      catch (Exception ex)
      {
        throw new BlogException(ex);
      }
    }

    return serviceRegistry;
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 10/02/20
   * @author         Enter your name here...
   */
  private static class DummyCipher extends Cipher
  {

    /**
     * Method description
     *
     *
     * @param key
     * @param value
     *
     * @return
     */
    @Override
    public String decode(char[] key, String value)
    {
      return value;
    }

    /**
     * Method description
     *
     *
     * @param key
     * @param value
     *
     * @return
     */
    @Override
    public String encode(char[] key, String value)
    {
      return value;
    }
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private BlogConfiguration configuration;

  /** Field description */
  private ResourceManager resourceManager;

  /** Field description */
  private ServiceRegistry serviceRegistry;
}
