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

import org.junit.Test;

import sonia.blog.api.app.BlogSession;

import sonia.util.Util;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

/**
 *
 * @author Sebastian Sdorra
 */
public class ScriptTest
{

  /**
   * Method description
   *
   *
   * @throws Exception
   */
  @Test
  public void loadAndStoreTest() throws Exception
  {
    BlogSession session = TestUtil.createGlobalAdminSession();
    Script script = createScript();

    script.setName("junit");
    script.setDescription("Just a unit test");
    script.setControllerContent(new ScriptContent("ECMAScript",
            "print('Hello JUnit');"));
    script.setTemplateContent(new ScriptContent("Freemarker",
            "<h1>${var}</h1>"));
    assertTrue(script.isValid());

    File directory = TestUtil.createTempDirectory();

    try
    {
      script.store(session, directory);

      Script loadScript = createScript(session, directory);

      assertNotNull(loadScript.getCreationDate());
      assertNull(loadScript.getLastModified());
      assertEquals(script.getName(), loadScript.getName());
      assertEquals(script.getDescription(), loadScript.getDescription());
      assertEquals(script.getControllerContent().getLanguage(),
                   loadScript.getControllerContent().getLanguage());
      assertEquals(script.getControllerContent().getContent(),
                   loadScript.getControllerContent().getContent());
      assertEquals(script.getTemplateContent().getLanguage(),
                   loadScript.getTemplateContent().getLanguage());
      assertEquals(script.getTemplateContent().getContent(),
                   loadScript.getTemplateContent().getContent());
      loadScript.store(session, directory);
      assertNotNull(loadScript.getLastModified());
    }
    finally
    {
      Util.delete(directory);
    }
  }

  /**
   * Method description
   *
   *
   * @return
   */
  protected Script createScript()
  {
    return new Script();
  }

  /**
   * Method description
   *
   *
   *
   * @param session
   * @param directory
   *
   * @return
   *
   * @throws Exception
   */
  protected Script createScript(BlogSession session, File directory)
          throws Exception
  {
    return new Script(session, directory);
  }
}
