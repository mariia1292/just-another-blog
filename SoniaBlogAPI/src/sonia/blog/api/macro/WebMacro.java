/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.macro.Macro;

//~--- JDK imports ------------------------------------------------------------

import java.util.Set;

/**
 *
 * @author sdorra
 */
public interface WebMacro extends Macro
{

  /**
   * Method description
   *
   *
   * @return
   */
  public String getJSInitCode();

  /**
   * Method description
   *
   *
   * @return
   */
  public Set<WebResource> getResources();
}
