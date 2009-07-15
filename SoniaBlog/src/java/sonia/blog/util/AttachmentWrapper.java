/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * http://kenai.com/projects/jab
 * 
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
 * @author Sebastian Sdorra
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