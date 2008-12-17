/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.access.def.action;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.access.Action;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

import javax.faces.context.FacesContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class ForwardAction implements Action
{

  /**
   * Constructs ...
   *
   *
   * @param target
   */
  public ForwardAction(String target)
  {
    this.target = target;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param context
   */
  public void doAction(HttpServletRequest request,
                       HttpServletResponse response, FacesContext context)
  {
    RequestDispatcher dispatcher = request.getRequestDispatcher(target);

    try
    {
      dispatcher.forward(request, response);
    }
    catch (Exception ex)
    {
      ex.printStackTrace(System.err);
    }
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
  private String target;
}