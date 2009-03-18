/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.access.def;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.access.Action;
import sonia.jsf.access.Condition;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public class Rule
{

  /**
   * Constructs ...
   *
   */
  public Rule() {}

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Action> getActions()
  {
    return actions;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Condition getCondition()
  {
    return condition;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isLast()
  {
    return last;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param actions
   */
  public void setActions(List<Action> actions)
  {
    this.actions = actions;
  }

  /**
   * Method description
   *
   *
   * @param condition
   */
  public void setCondition(Condition condition)
  {
    this.condition = condition;
  }

  /**
   * Method description
   *
   *
   * @param last
   */
  public void setLast(boolean last)
  {
    this.last = last;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<Action> actions;

  /** Field description */
  private Condition condition;

  /** Field description */
  private boolean last;
}
