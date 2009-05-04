/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.app;

//~--- JDK imports ------------------------------------------------------------

import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author sdorra
 */
public class BlogContextPhaseListener implements PhaseListener
{

  /** Field description */
  private static final long serialVersionUID = 1195642361741262521L;

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param event
   */
  public void afterPhase(PhaseEvent event) {}

  /**
   * Method description
   *
   *
   * @param event
   */
  public void beforePhase(PhaseEvent event)
  {
    if (event.getPhaseId() == PhaseId.RENDER_RESPONSE)
    {
      FacesContext context = event.getFacesContext();

      applyMessages(context);
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

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   */
  @SuppressWarnings("unchecked")
  private void applyMessages(FacesContext context)
  {
    Iterator<FacesMessage> messages =
      (Iterator<FacesMessage>) context.getExternalContext().getSessionMap().get(
          "sonia.blog.messages");

    if (messages != null)
    {
      while (messages.hasNext())
      {
        FacesMessage msg = messages.next();

        context.addMessage(null, msg);
      }
    }
    else
    {
      context.getExternalContext().getSessionMap().remove(
          "sonia.blog.messages");
    }
  }
}
