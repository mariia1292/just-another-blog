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
import sonia.blog.api.macro.browse.StringInputWidget;
import sonia.blog.api.macro.browse.StringTextAreaWidget;

import sonia.macro.browse.MacroInformation;
import sonia.macro.browse.MacroInformationParameter;

import sonia.util.Util;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public class MacroScriptTest extends ScriptTest
{

  /**
   * Method description
   *
   *
   * @throws Exception
   */
  @Test
  public void extendedLoadAndStoreTest() throws Exception
  {
    MacroScript script = new MacroScript();

    script.setName("JUnit");
    script.setDescription("Junit Test Macro");
    script.setControllerContent(new ScriptContent("ECMAScript",
            "print('Hello');"));

    MacroInformation info = script.createMacroInformation();

    info.setDisplayName("JUnit Macro");
    info.setIcon("/junit.png");
    info.setPreview(false);
    info.setBodyWidget(StringTextAreaWidget.class);
    info.setWidgetParam("cols=50");

    List<MacroInformationParameter> parameters =
      new ArrayList<MacroInformationParameter>();
    MacroInformationParameter parameter = new MacroInformationParameter("p1",
                                            "Parameter 1", "First Param");

    parameter.setWidget(StringInputWidget.class);
    parameter.setWidgetParam("test");
    parameters.add(parameter);
    info.setParameter(parameters);

    File directory = TestUtil.createTempDirectory();

    try
    {
      BlogSession session = TestUtil.createGlobalAdminSession();

      script.store(session, directory);

      MacroScript loadScript = new MacroScript(session, directory);
      MacroInformation loadInfo = loadScript.getInformation();

      assertEquals(loadInfo.getName(), info.getName());
      assertEquals(loadInfo.getDescription(), info.getDescription());
      assertEquals(loadInfo.getDisplayName(), info.getDisplayName());
      assertEquals(loadInfo.getBodyWidget(), info.getBodyWidget());
      assertEquals(loadInfo.getWidgetParam(), info.getWidgetParam());

      List<MacroInformationParameter> loadParameters = loadInfo.getParameter();

      assertNotNull(loadParameters);
      assertTrue(loadParameters.size() == 1);

      MacroInformationParameter loadParameter = loadParameters.get(0);

      assertNotNull(loadParameter);
      assertEquals(loadParameter.getName(), parameter.getName());
      assertEquals(loadParameter.getLabel(), parameter.getLabel());
      assertEquals(loadParameter.getDescription(), parameter.getDescription());
      assertEquals(loadParameter.getWidget(), parameter.getWidget());
      assertEquals(loadParameter.getWidgetParam(), parameter.getWidgetParam());
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
  @Override
  protected Script createScript()
  {
    return new MacroScript();
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
  @Override
  protected Script createScript(BlogSession session, File directory)
          throws Exception
  {
    return new MacroScript(session, directory);
  }
}
