/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.access;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sdorra
 */
public interface Condition
{

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
                                 FacesContext context);

  /**
   * Method description
   *
   *
   * @param parameters
   */
  public void init(Map<String, String> parameters);
}