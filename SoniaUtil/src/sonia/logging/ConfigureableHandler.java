/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.logging;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;
import java.util.logging.Handler;

/**
 *
 * @author sdorra
 */
public abstract class ConfigureableHandler extends Handler
{

  /**
   * Method description
   *
   *
   * @param paramters
   */
  public abstract void init(Map<String, String> paramters);
}
