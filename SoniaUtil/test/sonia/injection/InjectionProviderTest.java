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


package sonia.injection;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

import sonia.config.Config;
import sonia.config.ConfigInjector;
import sonia.config.PropertiesConfiguration;

import sonia.plugin.service.Service;
import sonia.plugin.service.ServiceInjector;
import sonia.plugin.service.ServiceReference;
import sonia.plugin.service.ServiceRegistry;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class InjectionProviderTest
{

  /**
   * Test of getInstance method, of class InjectionProvider.
   *
   * @throws IOException
   */
  @Test
  public void testProvider() throws IOException
  {
    byte[] buf = ".logger = FINEST\n".getBytes();
    ByteArrayInputStream bais = new ByteArrayInputStream(buf);

    LogManager.getLogManager().readConfiguration(bais);
    Logger.getLogger("sonia").setLevel(Level.FINEST);
    System.out.println("testProvider");

    ServiceRegistry registry = new ServiceRegistry();
    ServiceReference<ServiceTest> reference =
      registry.register(ServiceTest.class, "/path/test");

    reference.add(new ServiceTest());
    reference.add(new ServiceTest());

    InjectionProvider injectionProvicer = new DefaultInjectionProvider();

    injectionProvicer.registerInjector(Service.class,
                                       new ServiceInjector(registry));

    PropertiesConfiguration configuration = new PropertiesConfiguration();

    configuration.set("test", "Hallo Test");
    configuration.set("test2", 3);
    configuration.setMulti("test3", new Double[] { 1.9, 1.0, 3.3 });
    injectionProvicer.registerInjector(Config.class,
                                       new ConfigInjector(configuration));

    TestObject test = new TestObject();

    assertNull(test.service);
    assertNull(test.service2);
    injectionProvicer.inject(test);
    assertNotNull(test.reference);
    assertNotNull(test.service);
    assertNotNull(test.service2);
    assertNotNull(test.services);
    assertTrue(test.services.size() == 2);
    assertEquals("Hallo Test", test.config);
    assertEquals(3, test.config2);
    assertEquals(1.9, test.config3[0]);
    assertEquals(1.0, test.config3[1]);
    assertEquals(3.3, test.config3[2]);
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version    Enter version here..., 09/02/21
   * @author     Enter your name here...
   */
  private class ServiceTest {}


  /**
   * Class description
   *
   *
   * @version    Enter version here..., 09/02/21
   * @author     Enter your name here...
   */
  private class TestObject
  {

    /** Field description */
    @Service("/path/test")
    public ServiceTest service;

    /** Field description */
    @Config("test")
    private String config;

    /** Field description */
    @Config("test2")
    private Integer config2;

    /** Field description */
    @Config("test3")
    private Double[] config3;

    /** Field description */
    @Service("/path/test")
    private ServiceReference<ServiceTest> reference;

    /** Field description */
    @Service("/path/test")
    private ServiceTest service2;

    /** Field description */
    @Service("/path/test")
    private List<ServiceTest> services;
  }
}