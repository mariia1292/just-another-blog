/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;

//~--- JDK imports ------------------------------------------------------------

import java.util.Iterator;
import java.util.logging.Logger;

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

  /** Field description */
  private static Logger logger =
    Logger.getLogger(BlogContextPhaseListener.class.getName());

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
    if (event.getPhaseId() == PhaseId.RESTORE_VIEW)
    {
      FacesContext context = event.getFacesContext();

      applyMessages(context);

      if (BlogContext.getInstance().isInstalled())
      {
        BlogRequest request =
          (BlogRequest) context.getExternalContext().getRequest();
        BlogResponse response =
          (BlogResponse) context.getExternalContext().getResponse();

        BlogContext.getInstance().getMappingHandler().handleMapping(request,
                response);
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
    return PhaseId.RESTORE_VIEW;
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
