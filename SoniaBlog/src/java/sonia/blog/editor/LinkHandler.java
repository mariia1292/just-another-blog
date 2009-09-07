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



package sonia.blog.editor;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.editor.AttachmentHandler;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.entity.Attachment;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.text.MessageFormat;

/**
 *
 * @author Sebastian Sdorra
 */
public class LinkHandler extends AttachmentHandler
{

  /** Field description */
  private static final String LABLE = "Link";

  /** Field description */
  private static final String OUTPUT = "<a href=\"{1}\">{0}</a>";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param attachment
   *
   * @return
   */
  public boolean appendHandler(Attachment attachment)
  {
    return true;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getLable()
  {
    return LABLE;
  }

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param selection
   * @param attachment
   *
   * @return
   */
  public String getOutput(BlogRequest request, String selection,
                          Attachment attachment)
  {
    String name = Util.isBlank(selection)
                  ? attachment.getName()
                  : selection;
    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    String url = linkBuilder.buildLink(request, attachment);

    return MessageFormat.format(OUTPUT, name, url);
  }
}
