/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro.def;

/**
 *
 * @author sdorra
 */
public class ComplexMacroParser
{

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
    int s = text.length();

    for (int i = 0; i < s; i++)
    {
      c = text.charAt(i);

      switch (c)
      {
        case '{' :
          parseMacroStart();

          break;

        case '}' :
          break;

        case '|' :
          break;

        case '/' :
          break;

        case '=' :
          break;

        case ';' :
          break;

        default :
      }

      l = c;
      append();
    }

    return result.toString();
  }

  /**
   * Method description
   *
   */
  private void append()
  {
    if (!skip)
    {
      if (name)
      {
        nameBuffer.append(c);
      }
      else if (endName)
      {
        endNameBuffer.append(c);
      }
      else if (parameterName)
      {
        parameterNameBuffer.append(c);
      }
      else if (parameterValue)
      {
        parameterValueBuffer.append(c);
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
  private void parseMacroStart()
  {
    if (l != '\\')
    {
      if (!body)
      {
        name = true;
        nameBuffer = new StringBuffer();
        skip = true;
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private StringBuffer bodyBuffer;

  /** Field description */
  private char c;

  /** Field description */
  private StringBuffer endNameBuffer;

  /** Field description */
  private char l;

  /** Field description */
  private boolean name = false;

  /** Field description */
  private boolean endName = false;

  /** Field description */
  private StringBuffer nameBuffer;

  /** Field description */
  private boolean parameterName = false;

  /** Field description */
  private StringBuffer parameterNameBuffer;

  /** Field description */
  private boolean parameterValue = false;

  /** Field description */
  private boolean body = false;

  /** Field description */
  private StringBuffer parameterValueBuffer;

  /** Field description */
  private StringBuffer result = new StringBuffer();

  /** Field description */
  private boolean skip;
}
