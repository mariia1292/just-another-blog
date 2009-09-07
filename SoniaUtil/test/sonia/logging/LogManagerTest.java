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
 * @author Sebastian Sdorra
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