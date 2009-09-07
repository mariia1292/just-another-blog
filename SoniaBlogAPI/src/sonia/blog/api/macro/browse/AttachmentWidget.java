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



package sonia.blog.api.macro.browse;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author Sebastian Sdorra
 */
public class AttachmentWidget extends SelectWidget
{

  /**
   * Method description
   *
   *
   * @param request
   * @param object
   * @param name
   * @param param
   *
   * @return
   */
  @Override
  public String getFormElement(BlogRequest request, ContentObject object,
                               String name, String param)
  {
    StringBuffer result = new StringBuffer();
    List<Attachment> attachments = null;
    AttachmentDAO attachmentDAO =
      BlogContext.getDAOFactory().getAttachmentDAO();

    if (object instanceof Entry)
    {
      attachments = attachmentDAO.findAllByEntry((Entry) object);
    }
    else if (object instanceof Page)
    {
      attachments = attachmentDAO.getAll((Page) object);
    }

    Map<String, String> params = buildParameterMap(param);
    String filter = params.get("filter");

    result.append("<select name=\"").append(name).append("\">");

    if ((params != null) && "true".equalsIgnoreCase(params.get("nullable")))
    {
      result.append("<option value=\"\">---</option>");
    }

    if (Util.hasContent(attachments))
    {
      for (Attachment attachment : attachments)
      {
        if (Util.isBlank(filter) || attachment.getName().matches(filter))
        {
          result.append("<option value=\"").append(attachment.getId());
          result.append("\">").append(attachment.getName());
          result.append("</option>");
        }
      }
    }

    result.append("</select>");

    return result.toString();
  }
}
