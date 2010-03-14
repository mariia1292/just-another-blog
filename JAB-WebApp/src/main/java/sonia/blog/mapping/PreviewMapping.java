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



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.mapping.FilterMapping;
import sonia.blog.api.mapping.MappingNavigation;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;
import sonia.blog.util.BlogUtil;
import sonia.blog.wui.BlogBean;
import sonia.blog.wui.EntryBean;
import sonia.blog.wui.PageAuthorBean;
import sonia.blog.wui.PageBean;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.net.URLDecoder;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

/**
 *
 * @author Sebastian Sdorra
 */
public class PreviewMapping extends FilterMapping
{

  /** Field description */
  private static final String TYPE_ENTRY = "entry";

  /** Field description */
  private static final String TYPE_PAGE = "page";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(PreviewMapping.class.getName());

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public MappingNavigation getMappingNavigation()
  {
    return null;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected String handleFilterMapping(BlogRequest request,
          BlogResponse response, String[] param)
          throws IOException, ServletException
  {
    String result = null;

    if ((param != null) && (param.length == 1))
    {
      ContentObject object = getContentObject(request, param[0]);

      if (object != null)
      {
        setDisplayContent(request, object, false);

        if (object instanceof Entry)
        {
          BlogBean blogBean = BlogUtil.getSessionBean(request, BlogBean.class,
                                BlogBean.NAME);

          if (blogBean != null)
          {
            blogBean.setEntry(object);
            result = buildTemplateViewId(request, Constants.TEMPLATE_DETAIL);
          }
        }
        else if (object instanceof Page)
        {
          PageBean pageBean = new PageBean();

          pageBean.setPage((Page) object);
          request.setAttribute(PageBean.NAME, pageBean);
          result = buildTemplateViewId(request, Constants.TEMPLATE_PAGE);
        }
      }
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param type
   *
   * @return
   */
  private ContentObject getContentObject(BlogRequest request, String type)
  {
    ContentObject object = null;

    if (TYPE_ENTRY.equals(type))
    {
      object = getEntry(request);
    }
    else if (TYPE_PAGE.equals(type))
    {
      object = getPage(request);
    }
    else
    {
      throw new IllegalArgumentException("type is invalid");
    }

    return object;
  }

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  private Entry getEntry(BlogRequest request)
  {
    Entry entry = null;
    Long id = getId(request);

    if (id != null)
    {
      entry = BlogContext.getDAOFactory().getEntryDAO().get(id);
    }
    else
    {
      entry = new Entry();
    }

    String title = getParameter(request, "title");

    if (title != null)
    {
      entry.setTitle(title);
    }

    String content = getParameter(request, "content");

    if (content != null)
    {
      entry.setContent(content);
    }

    String teaser = getParameter(request, "teaser");

    if (teaser != null)
    {
      entry.setTeaser(teaser);
    }

    if (entry.getAuthor() == null)
    {
      entry.setAuthor(request.getUser());
    }

    if (entry.getBlog() == null)
    {
      entry.setBlog(request.getCurrentBlog());
    }

    return entry;
  }

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  private Long getId(BlogRequest request)
  {
    Long result = null;

    try
    {
      String idParam = request.getParameter("id");

      if (Util.isNotEmpty(idParam))
      {
        result = Long.getLong(idParam);
      }
    }
    catch (NumberFormatException ex)
    {

      // do nothing
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  private Page getPage(BlogRequest request)
  {
    Page page = null;
    Long id = getId(request);

    if (id != null)
    {
      page = BlogContext.getDAOFactory().getPageDAO().get(id);
    }
    else
    {
      page = new Page();
    }

    String title = getParameter(request, "title");

    if (title != null)
    {
      page.setTitle(title);
    }

    String content = getParameter(request, "content");

    if (content != null)
    {
      page.setContent(content);
    }

    if (page.getAuthor() == null)
    {
      page.setAuthor(request.getUser());
    }

    if (page.getBlog() == null)
    {
      page.setBlog(request.getCurrentBlog());
    }

    return page;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param name
   *
   * @return
   */
  private String getParameter(BlogRequest request, String name)
  {
    String param = request.getParameter(name);

    if (Util.isNotEmpty(param))
    {
      try
      {
        param = URLDecoder.decode(param, "UTF-8");
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }

    return param;
  }
}
