/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.access.def.action;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.access.Action;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class MessageAction implements Action
{

  /**
   * Constructs ...
   *
   *
   * @param message
   */
  public MessageAction(String message)
  {
    this.message = message;
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
    context.addMessage(null, new FacesMessage(message));
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
  private String message;
}
