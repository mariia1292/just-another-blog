/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.access;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.access.def.DefaultAccessHandler;

import sonia.util.ServiceLocator;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public abstract class AccessHandler
{

  /** Field description */
  private static AccessHandler instance;

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static AccessHandler getInstance()
  {
    if (instance == null)
    {
      instance = ServiceLocator.locateService(AccessHandler.class,
              new DefaultAccessHandler());
    }

    return instance;
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
  public abstract void handleAccess(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FacesContext context);

  /**
   * Method description
   *
   *
   * @param in
   *
   * @throws IOException
   */
  public abstract void readConfig(InputStream in) throws IOException;
}
