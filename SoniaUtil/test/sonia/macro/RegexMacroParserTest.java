/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro;

/**
 *
 * @author sdorra
 */
public class RegexMacroParserTest extends MacroParserTestBase
{

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public MacroParser init()
  {
    return new RegexMacroParser();
  }
}
