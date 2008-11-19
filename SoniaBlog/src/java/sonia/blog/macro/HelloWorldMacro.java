/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.macro.Macro;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

/**
 *
 * @author sdorra
 */
public class HelloWorldMacro implements Macro
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
    return "Hello World";
  }
}
