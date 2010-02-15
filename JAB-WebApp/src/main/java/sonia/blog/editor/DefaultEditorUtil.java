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

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.editor.EditorUtil;
import sonia.blog.entity.ContentObject;
import sonia.blog.wui.AbstractEditorBean;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

/**
 *
 * @author Sebastian Sdorra
 */
public class DefaultEditorUtil extends EditorUtil
{

  /** Field description */
  public static final String EDITOR_ENTRY = "entry";

  /** Field description */
  public static final String EDITOR_PAGE = "page";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultEditorUtil.class.getName());

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  @Override
  public ContentObject getCurrentObject(BlogRequest request)
  {
    ContentObject object = null;
    AbstractEditorBean editorBean = getEditorBean(request);

    if (editorBean != null)
    {
      object = editorBean.getObject();
    }
    else if (logger.isLoggable(Level.WARNING))
    {
      logger.warning("no editorbean found");
    }

    return object;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param object
   */
  @Override
  public void setCurrentObject(BlogRequest request, ContentObject object)
  {
    AbstractEditorBean editorBean = getEditorBean(request);

    if (editorBean != null)
    {
      editorBean.setObject(object);
    }
    else if (logger.isLoggable(Level.WARNING))
    {
      logger.warning("no editorbean found");
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  private AbstractEditorBean getEditorBean(BlogRequest request)
  {
    AbstractEditorBean editorBean = null;
    HttpSession session = request.getSession();

    if (session == null)
    {
      throw new IllegalStateException("session is null");
    }

    String editor = (String) session.getAttribute("editor");

    if (Util.isNotEmpty(editor))
    {
      if (EDITOR_ENTRY.equals(editor))
      {
        editorBean = (AbstractEditorBean) session.getAttribute("EntryBean");
      }
      else if (EDITOR_PAGE.equals(editor))
      {
        editorBean =
          (AbstractEditorBean) session.getAttribute("PageAuthorBean");
      }
      else
      {
        StringBuffer log = new StringBuffer("unknown editor variable: ");

        log.append(editor);
        logger.severe(log.toString());
      }
    }

    return editorBean;
  }
}
