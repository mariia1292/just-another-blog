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



package sonia.blog.wui.resolver;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.entity.Blog;
import sonia.blog.util.BlogUtil;

//~--- JDK imports ------------------------------------------------------------

import com.sun.facelets.FaceletViewHandler;

import java.util.Locale;

import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class BlogViewHandler extends FaceletViewHandler
{

  /**
   * Constructs ...
   *
   *
   * @param orginal
   */
  public BlogViewHandler(ViewHandler orginal)
  {
    super(orginal);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   *
   * @return
   */
  @Override
  public Locale calculateLocale(FacesContext context)
  {
    Locale locale = null;
    Object object = context.getExternalContext().getRequest();
    BlogRequest request = BlogUtil.getBlogRequest(object);

    if (request != null)
    {
      Blog blog = request.getCurrentBlog();

      if (blog != null)
      {
        locale = blog.getLocale();
      }
    }

    if (locale == null)
    {
      locale = super.calculateLocale(context);
    }

    return locale;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   * @param viewId
   *
   * @return
   */
  @Override
  public String getActionURL(FacesContext context, String viewId)
  {
    String result = super.getActionURL(context, viewId);

    if (result.endsWith(viewId))
    {
      result = result.substring(0, result.length() - viewId.length());
    }

    return result;
  }
}
