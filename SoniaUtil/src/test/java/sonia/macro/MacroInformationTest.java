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



package sonia.macro;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

import sonia.macro.browse.MacroInfo;
import sonia.macro.browse.MacroInfoParameter;
import sonia.macro.browse.MacroInformation;
import sonia.macro.browse.MacroInformationParameter;
import sonia.macro.browse.MacroInformationProvider;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Sebastian Sdorra
 */
public class MacroInformationTest
{

  /**
   * Method description
   *
   */
  @Test
  public void getMetaInformationTest()
  {
    MacroInformationProvider provider = MacroInformationProvider.getInstance();

    assertNotNull(provider);

    MacroInformation info = provider.getInformation(InformationMacro.class,
                              Locale.GERMAN);

    assertEquals("info", info.getName());
    assertEquals("Information", info.getDisplayName());
    assertEquals("Gibt Infos", info.getDescription());
    assertNull(info.getIcon());

    List<MacroInformationParameter> params = info.getParameter();

    assertNotNull(params);
    assertTrue(params.size() == 2);

    boolean param1 = false;
    boolean param2 = false;

    for (MacroInformationParameter param : params)
    {
      if (param.getName().equals("test"))
      {
        param1 = true;
        assertEquals("Der Test", param.getLabel());
        assertNull(param.getDescription());
      }
      else if (param.getName().equals("test2"))
      {
        param2 = true;
        assertEquals("Der zweite Test", param.getLabel());
        assertEquals("Nicht so gut wie der erste", param.getDescription());
      }
    }

    assertTrue(param1);
    assertTrue(param2);
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/06/08
   * @author         Enter your name here...
   */
  @MacroInfo(
    name = "info",
    displayName = "Information",
    description = "Gibt Infos"
  )
  private static class InformationMacro implements Macro
  {

    /**
     * Method description
     *
     *
     * @param environment
     * @param body
     *
     * @return
     */
    public String doBody(Map<String, Object> environment, String body)
    {
      return "-- Hallo --";
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param notListed
     */
    public void setNotListed(int notListed)
    {
      this.notListed = notListed;
    }

    /**
     * Method description
     *
     *
     * @param test
     */
    @MacroInfoParameter(displayName = "Der Test")
    public void setTest(String test)
    {
      this.test = test;
    }

    /**
     * Method description
     *
     *
     * @param test2
     */
    @MacroInfoParameter(displayName = "Der zweite Test",
                        description = "Nicht so gut wie der erste")
    public void setTest2(String test2)
    {
      this.test2 = test2;
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private int notListed;

    /** Field description */
    private String test;

    /** Field description */
    private String test2;
  }
}
