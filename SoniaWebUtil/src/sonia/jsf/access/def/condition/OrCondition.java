/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.access.def.condition;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.access.Condition;
import sonia.jsf.access.def.condition.ContainerCondition;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sdorra
 */
public class OrCondition implements ContainerCondition
{

  /**
   * Constructs ...
   *
   */
  public OrCondition()
  {
    this.conditions = new ArrayList<Condition>();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param condition
   */
  public void add(Condition condition)
  {
    conditions.add(condition);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param context
   *
   * @return
   */
  public boolean handleCondition(HttpServletRequest request,
                                 FacesContext context)
  {
    boolean result = false;

    for (Condition condition : conditions)
    {
      if (condition.handleCondition(request, context))
      {
        result = true;

        break;
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param parameters
   */
  public void init(Map<String, String> parameters)
  {

    // do nothing
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<Condition> conditions;
}
