/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro.def;

//~--- non-JDK imports --------------------------------------------------------

import sonia.macro.Macro;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

/**
 *
 * @author sdorra
 */
public class CharBasedMacroParser
{

  /**
   * Constructs ...
   *
   *
   * @param macros
   */
  public CharBasedMacroParser(Map<String, Class<? extends Macro>> macros)
  {
    this.macros = macros;
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
    for (int i = 0; i < text.length(); i++)
    {
      c = text.charAt(i);

      switch (c)
      {
        case '{' :
          macroStart();

          break;

        case '|' :
          parameterStart();

          break;

        case '}' :
          macroEnd();

          break;

        case '/' :
          macroPreEnd();

          break;

        default :
          other();
      }

      l = c;
      appendChar();
    }

    end();

    return result.toString();
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
        macroNameBuffer.append(c);
      }
      else if (parameter)
      {
        parameterBuffer.append(c);
      }
      else if (body)
      {
        bodyBuffer.append(c);
      }
      else
      {
        result.append(c);
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
   */
  private void end()
  {

    // TODO: handle the end
  }

  /**
   * Method description
   *
   */
  private void handleMacroEnd()
  {
    String name = macroNameBuffer.toString();
    String body = null;

    if (bodyBuffer != null)
    {
      body = bodyBuffer.toString();
    }

    System.out.println("body: " + body);
    System.out.println("end: " + name);
  }

  /**
   * Method description
   *
   */
  private void handleMacroStart()
  {
    String name = macroNameBuffer.toString();
    String params = null;

    if ((parameterBuffer != null) && (parameterBuffer.length() > 0))
    {
      params = parameterBuffer.toString();
    }

    System.out.println("start: " + name + "-" + params);
  }

  /**
   * Method description
   *
   */
  private void macroEnd()
  {
    if (macroName || parameter)
    {
      skip = true;
      macroName = false;
      parameter = false;
      handleMacroStart();

      if (l == '/')
      {
        handleMacroEnd();
        reset();
      }
      else
      {
        body = true;
        bodyBuffer = new StringBuffer();
      }
    }
    else if (macroEndTag)
    {
      handleMacroEnd();
      reset();
    }
  }

  /**
   * Method description
   *
   */
  private void macroPreEnd()
  {
    if (body && (l == '{'))
    {
      macroEndTag = true;
    }
  }

  /**
   * Method description
   *
   */
  private void macroStart()
  {
    if ((l != '\\') &&!(body || parameter || macroName))
    {
      skip = true;
      macroName = true;
      macroNameBuffer = new StringBuffer();
    }
  }

  /**
   * Method description
   *
   */
  private void other()
  {
    if (macroName &&!(Character.isDigit(c) || Character.isLetter(c)))
    {
      result.append("{");
      result.append(macroNameBuffer.toString());
      reset();
    }
  }

  /**
   * Method description
   *
   */
  private void parameterStart()
  {
    if (macroName)
    {
      skip = true;
      macroName = false;
      parameter = true;
      parameterBuffer = new StringBuffer();
    }
  }

  /**
   * Method description
   *
   */
  private void reset()
  {
    macroNameBuffer = null;
    macroName = false;
    skip = false;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private boolean body;

  /** Field description */
  private StringBuffer bodyBuffer;

  /** Field description */
  private char c;

  /** Field description */
  private char l;

  /** Field description */
  private boolean macroEndTag = false;

  /** Field description */
  private StringBuffer macroNameBuffer;

  /** Field description */
  private Map<String, Class<? extends Macro>> macros;

  /** Field description */
  private boolean parameter;

  /** Field description */
  private StringBuffer parameterBuffer;

  /** Field description */
  private StringBuffer result = new StringBuffer();

  /** Field description */
  private boolean skip = false;

  /** Field description */
  private boolean macroName = false;
}
