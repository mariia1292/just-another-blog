/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.logging;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import sonia.util.Util;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class LogManagerTest
{

  /**
   * Constructs ...
   *
   */
  public LogManagerTest() {}

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws Exception
   */
  @AfterClass
  public static void tearDownClass() throws Exception {}

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws Exception
   */
  @BeforeClass
  public static void setUpClass() throws Exception {}

  //~--- methods --------------------------------------------------------------

  /**
   * Test of init method, of class ConfigureableHandler.
   *
   * @throws IOException
   */
  @Test
  public void testConfig1() throws IOException
  {
    LogManager logManager = LogManager.getInstance();
    File f = File.createTempFile("sonia.util.logging-", "");

    if (f.isFile())
    {
      assertTrue(f.delete());
    }

    assertTrue(f.mkdir());
    logManager.putVar("logdir", f.getPath());
    assertNotNull(logManager);

    InputStream in = Util.findResource("/sonia/logging/config-1.xml");

    assertNotNull(in);

    try
    {
      logManager.readConfiguration(in);
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
      fail(ex.getMessage());
    }

    in.close();

    Logger soniaLogger = Logger.getLogger("sonia");

    assertNotNull(soniaLogger);
    assertEquals(soniaLogger.getLevel(), Level.FINE);

    boolean file = false;
    boolean console = false;
    Handler[] handlers = soniaLogger.getHandlers();

    assertNotNull(handlers);
    assertTrue(handlers.length == 2);

    for (Handler handler : handlers)
    {
      if (handler.getClass().equals(FileHandler.class))
      {
        file = true;
      }
      else if (handler.getClass().equals(ConsoleHandler.class))
      {
        console = true;
      }
      else
      {
        fail(handler.getClass().getName() + " ???");
      }
    }

    assertTrue(console);
    assertTrue(file);
    assertNotNull(soniaLogger);
    soniaLogger.severe("test");

    for (Handler h : soniaLogger.getHandlers())
    {
      h.flush();
    }

    File[] files = f.listFiles();

    assertNotNull(files);
    assertTrue(files.length > 0);

    boolean logFile = false;

    for (File log : files)
    {
      if (log.getName().startsWith("test-") && log.getName().endsWith(".log"))
      {
        logFile = true;
        assertTrue(log.delete());
      }
    }

    for (Handler h : soniaLogger.getHandlers())
    {
      h.close();
    }

    assertTrue(logFile);
    assertTrue(f.delete());
  }
}
