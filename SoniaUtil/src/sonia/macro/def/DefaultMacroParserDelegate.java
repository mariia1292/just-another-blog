/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro.def;

//~--- non-JDK imports --------------------------------------------------------

import sonia.macro.Macro;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author sdorra
 */
public class DefaultMacroParserDelegate
{

  /**
   * Constructs ...
   *
   *
   * @param parser
   * @param environment
   */
  public DefaultMacroParserDelegate(DefaultMacroParser parser,
                                    Map<String, ?> environment)
  {
    this.parser = parser;
    this.environment = environment;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param text
   *
   * @return
   */
  public String parseText(String text)
  {
    char[] chars = text.toCharArray();

    for (int i = 0; i < chars.length; i++)
    {
      c = chars[i];

      switch (c)
      {
        case '{' :
          parseMacroStart();

          break;

        case '}' :
          parseMacroEnd();

          break;

        case '/' :
          parseClosingChar();

          break;

        case '|' :
          parseParamChar();

          break;

        case ' ' :
          parseSpaceChar();

          break;
      }

      appendChar();
      l = c;
    }

    return resultBuffer.toString();
  }

  /**
   * Method description
   *
   */
  private void appendChar()
  {
    if (!skip)
    {
      if (macroName)
      {
        nameBuffer.append(c);
      }
      else if (macroParam)
      {
        paramBuffer.append(c);
      }
      else if (!macros.empty())
      {
        macros.peek().bodyBuffer.append(c);
      }
      else
      {
        resultBuffer.append(c);
      }
    }
    else
    {
      skip = false;
    }
  }

  /**
   * Method description
   *
   *
   * @param el
   */
  private void excecuteMacro(MacroElement el)
  {
    String result = "";
    Macro macro = parser.getMacro(el.name);
    Map<String, String> parameters = new HashMap<String, String>();

    if (el.param != null)
    {
      parseParameters(parameters, el.param);
    }

    if (macro != null)
    {
      result = macro.excecute(environment, el.bodyBuffer.toString(),
                              parameters);
    }
    else
    {
      result = " - no such macro - ";
    }

    if (macros.empty())
    {
      resultBuffer.append(result);
    }
    else
    {
      macros.peek().bodyBuffer.append(result);
    }
  }

  /**
   * Method description
   *
   */
  private void parseClosingChar()
  {
    if (l == '{')
    {
      macroClose = true;
      skip = true;
    }
    else if (macroName || macroParam)
    {
      skip = true;
    }
  }

  /**
   * Method description
   *
   */
  private void parseMacroEnd()
  {
    skip = true;

    if (l == '/')
    {
      String name = nameBuffer.toString();
      MacroElement macro = new MacroElement(name);

      if (macroParam)
      {
        macro.param = paramBuffer.toString();
      }

      skip = true;
      resetName();
      resetParam();
      excecuteMacro(macro);
    }
    else if (macroClose)
    {
      macroClose = false;

      MacroElement el = macros.pop();
      String name = nameBuffer.toString();

      resetName();

      while (!el.name.equals(name) &&!macros.empty())
      {
        el = macros.pop();
      }

      excecuteMacro(el);
    }
    else if (macroName || macroParam)
    {
      MacroElement macro = new MacroElement(nameBuffer.toString());

      if (macroParam)
      {
        macro.param = paramBuffer.toString();
      }

      macros.push(macro);
      resetName();
      resetParam();
    }
  }

  /**
   * Method description
   *
   */
  private void parseMacroStart()
  {
    if (!macroName)
    {
      macroName = true;
      skip = true;
    }
  }

  /**
   * Method description
   *
   */
  private void parseParamChar()
  {
    if (macroName)
    {
      macroParam = true;
      macroName = false;
      skip = true;
    }
  }

  /**
   * Method description
   *
   *
   * @param parameters
   * @param paramText
   */
  private void parseParameters(Map<String, String> parameters, String paramText)
  {
    String[] parts = paramText.split(";");

    for (String part : parts)
    {
      if (part.contains("="))
      {
        String[] entry = part.split("=");

        if (entry.length == 2)
        {
          parameters.put(entry[0], entry[1]);
        }
      }
    }
  }

  /**
   * Method description
   *
   */
  private void parseSpaceChar()
  {
    if (macroName)
    {
      skip = true;
    }
  }

  /**
   * Method description
   *
   */
  private void resetName()
  {
    macroName = false;
    nameBuffer = new StringBuffer();
  }

  /**
   * Method description
   *
   */
  private void resetParam()
  {
    macroParam = false;
    paramBuffer = new StringBuffer();
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version    Enter version here..., 08/09/18
   * @author     Enter your name here...
   */
  private static class MacroElement
  {

    /**
     * Constructs ...
     *
     *
     * @param name
     */
    public MacroElement(String name)
    {
      this.name = name;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public String toString()
    {
      return "<" + name + ">" + bodyBuffer + "</" + name + ">";
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private StringBuffer bodyBuffer = new StringBuffer();

    /** Field description */
    private String name;

    /** Field description */
    private String param;
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private char c;

  /** Field description */
  private Map<String, ?> environment;

  /** Field description */
  private char l;

  /** Field description */
  private boolean macroClose = false;

  /** Field description */
  private boolean macroName = false;

  /** Field description */
  private boolean macroParam = false;

  /** Field description */
  private StringBuffer paramBuffer = new StringBuffer();

  /** Field description */
  private DefaultMacroParser parser;

  /** Field description */
  private StringBuffer resultBuffer = new StringBuffer();

  /** Field description */
  private StringBuffer nameBuffer = new StringBuffer();

  /** Field description */
  private Stack<MacroElement> macros = new Stack<MacroElement>();

  /** Field description */
  private boolean skip = false;
}
