/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro.def;

//~--- non-JDK imports --------------------------------------------------------

import sonia.macro.MacroParser;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

/**
 *
 * @author sdorra
 */
public class DefaultMacroParser extends MacroParser
{

  /**
   * Method description
   *
   *
   * @param environment
   * @param text
   *
   * @return
   */
  @Override
  public String parseText(Map<String, ?> environment, String text)
  {
    return new DefaultMacroParserDelegate(this, environment).parseText(text);
  }
}
