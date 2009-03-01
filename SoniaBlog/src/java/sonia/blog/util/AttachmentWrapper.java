/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.editor.AttachmentHandler;
import sonia.blog.entity.Attachment;

import sonia.plugin.service.ServiceReference;
import sonia.plugin.service.ServiceRegistry;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sdorra
 */
public class AttachmentWrapper
{

  /**
   * Constructs ...
   *
   *
   * @param attachment
   */
  public AttachmentWrapper(Attachment attachment)
  {
    this.attachment = attachment;
  }

  /**
   * Constructs ...
   *
   *
   * @param attachment
   * @param selection
   */
  public AttachmentWrapper(Attachment attachment, String selection)
  {
    this.attachment = attachment;
    this.selection = selection;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Attachment getAttachment()
  {
    return attachment;
  }

  /**
   * Method description
   *
   *
   *
   * @return
   */
  public List<AttachmentHandler> getAttachmentHandlers()
  {
    List<AttachmentHandler> result = new ArrayList<AttachmentHandler>();
    ServiceRegistry registry = BlogContext.getInstance().getServiceRegistry();
    ServiceReference<AttachmentHandler> reference =
      registry.get(AttachmentHandler.class,
                   Constants.SERVICE_ATTACHMENTHANDLER);

    if (reference != null)
    {
      List<AttachmentHandler> handlers = reference.getAll();

      if (handlers != null)
      {
        for (AttachmentHandler handler : handlers)
        {
          if (handler.appendHandler(attachment))
          {
            result.add(handler);
          }
        }
      }
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Attachment attachment;

  /** Field description */
  private String selection;
}
