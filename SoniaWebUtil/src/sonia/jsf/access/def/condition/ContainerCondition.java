/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.access.def.condition;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.access.*;

/**
 *
 * @author sdorra
 */
public interface ContainerCondition extends Condition
{

  /**
   * Method description
   *
   *
   * @param condition
   */
  public void add(Condition condition);
}
