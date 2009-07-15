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


package sonia.config;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

import sonia.security.cipher.DefaultCipher;

import static org.junit.Assert.*;

/**
 *
 * @author Sebastian Sdorra
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