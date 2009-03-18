/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.access;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public interface Action
{

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param context
   */
  public void doAction(HttpServletRequest request,
                       HttpServletResponse response, FacesContext context);

  /**
   * Method description
   *
   *
   * @param parameters
   */
  public void init(Map<String, String> parameters);
}
