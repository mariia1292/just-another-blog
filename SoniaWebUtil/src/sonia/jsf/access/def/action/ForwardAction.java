/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.access.def.action;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.access.Action;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

  /** Field description */
  private static Logger logger =
    Logger.getLogger(NavigationAction.class.getName());

  //~--- constructors ---------------------------------------------------------

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
    if (logger.isLoggable(Level.FINE))
    {
      StringBuffer log = new StringBuffer();

      log.append("forward to ").append(target);
      logger.fine(log.toString());
    }

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
