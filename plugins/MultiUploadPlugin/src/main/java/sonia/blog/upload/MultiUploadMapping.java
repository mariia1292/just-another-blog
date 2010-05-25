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



package sonia.blog.upload;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.editor.EditorUtil;
import sonia.blog.api.exception.BlogException;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.api.mapping.MappingConfig;
import sonia.blog.api.util.AttachmentFacade;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
@MappingConfig(regex = "^/editor/upload$")
public class MultiUploadMapping extends FinalMapping
{

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void handleFinalMapping(BlogRequest request, BlogResponse response,
                                    String[] param)
          throws IOException, ServletException
  {
    String name = request.getParameter("name");

    if (Util.isNotEmpty(name))
    {
      addAttachment(request, response, name);
    }
    else
    {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param name
   *
   * @throws IOException
   */
  private void addAttachment(BlogRequest request, BlogResponse response,
                             String name)
          throws IOException
  {
    ContentObject object = EditorUtil.getInstance().getCurrentObject(request);

    if (object != null)
    {
      if (object.getId() == null)
      {
        save(request, object);
      }

      AttachmentFacade attachmentFacade = new AttachmentFacade();
      Attachment attachement = attachmentFacade.createAttachment(object,
                                 request.getInputStream(), name);

      if (attachement != null)
      {
        BlogContext.getDAOFactory().getAttachmentDAO().add(
            request.getBlogSession(), attachement);
        EditorUtil.getInstance().setCurrentObject(request, object);
      }
      else
      {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
    }
    else
    {
      throw new BlogException("no contentobject found");
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param object
   */
  private void save(BlogRequest request, ContentObject object)
  {
    if (object instanceof Entry)
    {
      Entry entry = (Entry) object;

      if (entry.getAuthor() == null)
      {
        entry.setAuthor(request.getUser());
      }

      if (Util.isEmpty(entry.getTitle()))
      {
        entry.setTitle("Autosave");
      }

      BlogContext.getDAOFactory().getEntryDAO().add(request.getBlogSession(),
              entry);
    }
    else if (object instanceof Page)
    {
      Page page = (Page) object;

      if (page.getAuthor() == null)
      {
        page.setAuthor(request.getUser());
      }

      if (Util.isEmpty(page.getTitle()))
      {
        page.setTitle("Autosave");
      }

      BlogContext.getDAOFactory().getPageDAO().add(request.getBlogSession(),
              page);
    }
    else
    {
      throw new IllegalArgumentException("page or entry is required");
    }
  }
}
