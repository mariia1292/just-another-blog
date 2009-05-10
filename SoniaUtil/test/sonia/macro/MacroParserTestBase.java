/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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

    String text = parser.parseText(env, "- {body}Hello World{/body} -").getText();

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
    assertEquals(SimpleMacro.class, parser.getMacro("simple"));

    String text = parser.parseText(env, "- {simple}{/simple} -").getText();

    assertEquals("- Hello from SimpleMacro -", text);
    parser.removeMacro("simple");
    assertNull(parser.getMacro("simple"));
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
    public String doBody(Map<String, ?> environment, String body)
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
    public String doBody(Map<String, ?> environment, String body)
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
    public String doBody(Map<String, ?> environment, String body)
    {
      return "Hello from SimpleMacro";
    }
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Map<String, String> env = new HashMap<String, String>();

  /** Field description */
  private MacroParser parser;
}
