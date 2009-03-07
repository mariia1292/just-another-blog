/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
 */
public class InjectionProviderTest
{

  /**
   * Test of getInstance method, of class InjectionProvider.
   *
   * @throws IOException
   */
  @Test
  public void testGetInstance() throws IOException
  {
    byte[] buf = ".logger = FINEST\n".getBytes();
    ByteArrayInputStream bais = new ByteArrayInputStream(buf);

    LogManager.getLogManager().readConfiguration(bais);
    Logger.getLogger("sonia").setLevel(Level.FINEST);
    System.out.println("getInstance");

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
