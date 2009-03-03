/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro.def;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

import sonia.macro.MacroParser;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author sdorra
 */
public class RegexMacroParserTest
{

  /**
   * Test of parserText method, of class RegexMacroParser.
   *
   * @throws IOException
   */
  @Test
  public void testParserText() throws IOException
  {
    String text = readResource("/sonia/macro/def/code.0");
    MacroParser p = MacroParser.getInstance();

    p.putMacro("strong", StrongMacro.class);

    RegexMacroParser instance = new RegexMacroParser(p);

    text = instance.parserText(null, text);
    System.out.println(text);
  }

  /**
   * Method description
   *
   *
   * @param path
   *
   * @return
   *
   * @throws IOException
   */
  private String readResource(String path) throws IOException
  {
    StringBuffer result = new StringBuffer();
    InputStream in = RegexMacroParserTest.class.getResourceAsStream(path);
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    String line = reader.readLine();

    while (line != null)
    {
      result.append(line).append("\n");
      line = reader.readLine();
    }

    return result.toString();
  }
}
