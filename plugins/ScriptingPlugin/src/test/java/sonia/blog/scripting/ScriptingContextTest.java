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



package sonia.blog.scripting;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import sonia.blog.api.app.BlogSession;

import sonia.util.Util;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public class ScriptingContextTest
{

  /** Field description */
  private static File repository;

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws IOException
   */
  @AfterClass
  public static void close() throws IOException
  {
    Util.delete(repository);
  }

  /**
   * Method description
   *
   *
   * @throws IOException
   */
  @BeforeClass
  public static void init() throws IOException
  {
    repository = TestUtil.createTempDirectory();
  }

  /**
   * Method description
   *
   *
   * @throws ScriptingException
   */
  @Test
  public void crudTest() throws ScriptingException
  {
    ScriptingContext ctx = createContext();
    BlogSession session = TestUtil.createGlobalAdminSession();
    Script script = createTestScript();

    ctx.create(session, script);

    Script loadedScript = ctx.getScript(session, Script.class, "junit");

    assertNotNull(loadedScript);
    loadedScript.setDescription("JUnit Test description");
    ctx.update(session, loadedScript);
    loadedScript = ctx.getScript(session, Script.class, "junit");
    assertNotNull(loadedScript.getLastModified());
    assertEquals("JUnit Test description", loadedScript.getDescription());
    ctx.remove(session, script);
    loadedScript = ctx.getScript(session, Script.class, "junit");
    assertNull(loadedScript);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   */
  @Test
  public void getLanguagesTest()
  {
    ScriptingContext ctx = createContext();
    List<String> languages = ctx.getScriptLanguages();

    assertNotNull(languages);
    assertFalse(languages.isEmpty());
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private ScriptingContext createContext()
  {
    ScriptingContext ctx = new ScriptingContext(repository);

    ctx.setTestMode(true);

    return ctx;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  private Script createTestScript()
  {
    Script script = new Script();

    script.setName("junit");
    script.setControllerContent(new ScriptContent("ECMAScript",
            "print('Hello JUnit');"));

    return script;
  }
}
