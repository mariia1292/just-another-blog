/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

/**
 *
 * @author sdorra
 */
public interface Macro
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
                         Map<String, String> parameters);
}
