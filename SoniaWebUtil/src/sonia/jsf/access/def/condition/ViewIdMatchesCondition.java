/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.access.def.condition;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.access.Condition;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sdorra
 */
public class ViewIdMatchesCondition implements Condition
{

  /**
   * Constructs ...
   *
   *
   * @param regex
   */
  public ViewIdMatchesCondition(String regex)
  {
    this.regex = regex;
  }

  //~--- methods --------------------------------------------------------------

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
    UIViewRoot viewRoot = context.getViewRoot();

    if (viewRoot != null)
    {
      String viewId = viewRoot.getViewId();

      result = viewId.matches(regex);
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
  private String regex;
}
