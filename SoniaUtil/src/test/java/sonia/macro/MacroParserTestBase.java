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

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public abstract class MacroParserTestBase
{

  /**
   * Constructs ...
   *
   */
  public MacroParserTestBase()
  {
    Logger logger = Logger.getLogger("sonia");
    ConsoleHandler handler = new ConsoleHandler();

    handler.setLevel(Level.FINEST);
    logger.addHandler(handler);
    this.parser = init();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract MacroParser init();

  /**
   * Method description
   *
   */
  @Test
  public void bodyTest()
  {
    parser.putMacro("body", BodyMacro.class);

    String text = parser.parseText(env,
                                   "- {body}Hello World{/body} -").getText();

    assertEquals("- BODY: Hello World -", text);
  }

  /**
   * Method description
   *
   */
  @Test
  public void parameterTest()
  {
    parser.putMacro("parameter", ParameterMacro.class);

    String parseText = "- {parameter:p1=result;n1=3;n2=4}{/parameter} -";
    String text = parser.parseText(env, parseText).getText();

    assertEquals("- result=7 -", text);
  }

  /**
   * Method description
   *
   */
  @Test
  public void simpleTest()
  {
    parser.putMacro("simple", SimpleMacro.class);
    assertEquals(
        SimpleMacro.class,
        parser.getMacroFactory("simple").createMacro(
          new HashMap<String, String>()).getClass());

    String text = parser.parseText(env, "- {simple}{/simple} -").getText();

    assertEquals("- Hello from SimpleMacro -", text);
    parser.removeMacroFactory("simple");
    assertNull(parser.getMacroFactory("simple"));
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version    Enter version here..., 09/04/13
   * @author     Enter your name here...
   */
  protected static class BodyMacro implements Macro
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
      return "BODY: " + body;
    }
  }


  /**
   * Class description
   *
   *
   * @version    Enter version here..., 09/04/13
   * @author     Enter your name here...
   */
  protected static class ParameterMacro implements Macro
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
      return p1 + "=" + (n1 + n2);
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param n1
     */
    public void setN1(Integer n1)
    {
      this.n1 = n1;
    }

    /**
     * Method description
     *
     *
     * @param n2
     */
    public void setN2(Integer n2)
    {
      this.n2 = n2;
    }

    /**
     * Method description
     *
     *
     * @param p1
     */
    public void setP1(String p1)
    {
      this.p1 = p1;
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private Integer n1 = 0;

    /** Field description */
    private Integer n2 = 0;

    /** Field description */
    private String p1 = "nothing";
  }


  /**
   * Class description
   *
   *
   * @version    Enter version here..., 09/04/13
   * @author     Enter your name here...
   */
  protected static class SimpleMacro implements Macro
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
      return "Hello from SimpleMacro";
    }
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Map<String, Object> env = new HashMap<String, Object>();

  /** Field description */
  private MacroParser parser;
}
