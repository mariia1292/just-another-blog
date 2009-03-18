/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.access.def.action;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.access.Action;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.Map;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class RedirectAction implements Action
{

  /**
   * Constructs ...
   *
   *
   * @param target
   */
  public RedirectAction(String target)
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
    try
    {
      response.sendRedirect(target);
    }
    catch (IOException ex)
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
