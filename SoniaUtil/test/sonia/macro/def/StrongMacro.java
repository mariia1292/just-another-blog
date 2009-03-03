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
public class StrongMacro implements Macro
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
    StringBuffer result = new StringBuffer();

    result.append("<strong param1='").append(param1).append("' param2='");
    result.append(param2).append("'>").append(body).append("</strong>");

    return result.toString();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String toString()
  {
    StringBuffer result = new StringBuffer();

    result.append("param1: ").append(param1).append("\n");
    result.append("param2: ").append(param2).append("\n");

    return result.toString();
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param param1
   */
  public void setParam1(String param1)
  {
    this.param1 = param1;
  }

  /**
   * Method description
   *
   *
   * @param param2
   */
  public void setParam2(String param2)
  {
    this.param2 = param2;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String param1;

  /** Field description */
  private String param2;
}
