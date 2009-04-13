/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;

import sonia.jsf.access.AccessHandler;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileInputStream;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class AccessPhaseListener implements PhaseListener
{

  /** Field description */
  private static final long serialVersionUID = -6955858435833487366L;

  /** Field description */
  private static Logger logger =
    Logger.getLogger(AccessPhaseListener.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param event
   */
  public void afterPhase(PhaseEvent event)
  {
    if (accessHandler == null)
    {
      System.out.println("ERROR");
    }
    else
    {
      FacesContext context = event.getFacesContext();
      HttpServletRequest request =
        (HttpServletRequest) context.getExternalContext().getRequest();
      HttpServletResponse response =
        (HttpServletResponse) context.getExternalContext().getResponse();

      accessHandler.handleAccess(request, response, context);
    }
  }

  /**
   * Method description
   *
   *
   * @param event
   */
  public void beforePhase(PhaseEvent event)
  {
    if (accessHandler == null)
    {
      accessHandler = AccessHandler.getInstance();

      String path = BlogContext.getInstance().getServletContext().getRealPath(
                        "/WEB-INF/config/access.xml");

      try
      {
        accessHandler.readConfig(new FileInputStream(path));
      }
      catch (Exception ex)
      {
        accessHandler = null;
        logger.log(Level.SEVERE, null, ex);
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public PhaseId getPhaseId()
  {
    return PhaseId.RENDER_RESPONSE;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private AccessHandler accessHandler;
}
