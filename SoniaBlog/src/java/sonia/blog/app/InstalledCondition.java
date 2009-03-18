/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;

import sonia.jsf.access.Condition;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sdorra
 */
public class InstalledCondition implements Condition
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
                                 FacesContext context)
  {
    return BlogContext.getInstance().isInstalled();
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
}
