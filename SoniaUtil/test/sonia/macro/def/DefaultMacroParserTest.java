/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro.def;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

import sonia.macro.Macro;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sdorra
 */
public class DefaultMacroParserTest
{

  /**
   * Test of parseText method, of class DefaultMacroParser.
   */
  @Test
  public void testParseText()
  {
    String sample =
      "Hello my name is {a|env=name/}.\n{d|s=<;e=>;b=Test/}"
      + "How {c|s=<;e=>}{b|s=<;e=>}are{/b} you? And {a|s=<;e=>}so{/a}{/c} on?";

    // String sample = "{b}a{/b}";
    DefaultMacroParser parser = new DefaultMacroParser();

    parser.putMacro("a", new SampleMacro());
    parser.putMacro("b", new SampleMacro());
    parser.putMacro("c", new SampleMacro());
    parser.putMacro("d", new SampleMacro());
    System.out.println("----");

    Map<String, Object> env = new HashMap<String, Object>();

    env.put("name", "Hans");
    System.out.println(parser.parseText(env, sample));
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version    Enter version here..., 08/09/18
   * @author     Enter your name here...    
   */
  private class SampleMacro implements Macro
  {

    /**
     * Method description
     *
     *
     * @param environment
     * @param body
     * @param parameters
     *
     * @return
     */
    public String excecute(Map<String, ?> environment, String body,
                           Map<String, String> parameters)
    {
      String s = parameters.get("s");

      if (s == null)
      {
        s = "!";
      }

      String e = parameters.get("e");

      if (e == null)
      {
        e = "!";
      }

      if (parameters.containsKey("b"))
      {
        body = parameters.get("b");
      }
      else if (parameters.containsKey("env"))
      {
        body = (String) environment.get(parameters.get("env"));
      }

      return s + body + e;
    }
  }
}
